package com.courseplatform.service;

import com.courseplatform.dto.*;
import com.courseplatform.entity.*;
import com.courseplatform.exception.ConflictException;
import com.courseplatform.exception.ForbiddenException;
import com.courseplatform.exception.NotFoundException;
import com.courseplatform.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final SubtopicRepository subtopicRepository;
    private final SubtopicProgressRepository progressRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                              CourseRepository courseRepository,
                              SubtopicRepository subtopicRepository,
                              SubtopicProgressRepository progressRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.subtopicRepository = subtopicRepository;
        this.progressRepository = progressRepository;
    }

    @Transactional
    public EnrollmentResponse enroll(User user, String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Not Found",
                        "Course with id '" + courseId + "' does not exist"));

        if (enrollmentRepository.existsByUserIdAndCourseId(user.getId(), courseId)) {
            throw new ConflictException("Already enrolled",
                    "You are already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment(user, course);
        enrollment = enrollmentRepository.save(enrollment);

        return new EnrollmentResponse(enrollment.getId(), course.getId(),
                course.getTitle(), enrollment.getEnrolledAt());
    }

    @Transactional
    public ProgressResponse markSubtopicComplete(User user, String subtopicId) {
        Subtopic subtopic = subtopicRepository.findById(subtopicId)
                .orElseThrow(() -> new NotFoundException("Not Found",
                        "Subtopic with id '" + subtopicId + "' does not exist"));

        String courseId = subtopic.getTopic().getCourse().getId();

        if (!enrollmentRepository.existsByUserIdAndCourseId(user.getId(), courseId)) {
            throw new ForbiddenException("Forbidden",
                    "You must be enrolled in this course to mark subtopics as complete");
        }

        // Idempotent: if already completed, return existing
        SubtopicProgress progress = progressRepository.findByUserIdAndSubtopicId(user.getId(), subtopicId)
                .orElseGet(() -> {
                    SubtopicProgress newProgress = new SubtopicProgress(user, subtopic);
                    return progressRepository.save(newProgress);
                });

        return new ProgressResponse(subtopicId, progress.isCompleted(), progress.getCompletedAt());
    }

    @Transactional(readOnly = true)
    public EnrollmentProgressResponse getProgress(User user, Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new NotFoundException("Not Found",
                        "Enrollment with id '" + enrollmentId + "' does not exist"));

        if (!enrollment.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Forbidden",
                    "You can only view your own enrollment progress");
        }

        Course course = enrollment.getCourse();
        List<Subtopic> allSubtopics = subtopicRepository.findByCourseId(course.getId());
        int totalSubtopics = allSubtopics.size();

        List<String> subtopicIds = allSubtopics.stream()
                .map(Subtopic::getId)
                .collect(Collectors.toList());

        List<SubtopicProgress> completedProgress =
                progressRepository.findByUserIdAndSubtopicIdIn(user.getId(), subtopicIds);

        int completedCount = completedProgress.size();
        double percentage = totalSubtopics > 0
                ? BigDecimal.valueOf((double) completedCount / totalSubtopics * 100)
                    .setScale(2, RoundingMode.HALF_UP).doubleValue()
                : 0.0;

        List<EnrollmentProgressResponse.CompletedItem> completedItems = completedProgress.stream()
                .map(p -> new EnrollmentProgressResponse.CompletedItem(
                        p.getSubtopic().getId(),
                        p.getSubtopic().getTitle(),
                        p.getCompletedAt()))
                .collect(Collectors.toList());

        return new EnrollmentProgressResponse(
                enrollment.getId(), course.getId(), course.getTitle(),
                totalSubtopics, completedCount, percentage, completedItems);
    }
}
