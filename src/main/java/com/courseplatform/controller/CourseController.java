package com.courseplatform.controller;

import com.courseplatform.dto.CourseDetailResponse;
import com.courseplatform.dto.CourseListResponse;
import com.courseplatform.dto.EnrollmentResponse;
import com.courseplatform.entity.User;
import com.courseplatform.service.CourseService;
import com.courseplatform.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Courses", description = "Browse courses and enroll")
public class CourseController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public CourseController(CourseService courseService, EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    @Operation(summary = "List all courses", description = "Public endpoint - returns all available courses with topic/subtopic counts")
    public ResponseEntity<CourseListResponse> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{courseId}")
    @Operation(summary = "Get course by ID", description = "Public endpoint - returns full course details with topics, subtopics, and markdown content")
    public ResponseEntity<CourseDetailResponse> getCourseById(@PathVariable String courseId) {
        return ResponseEntity.ok(courseService.getCourseById(courseId));
    }

    @PostMapping("/{courseId}/enroll")
    @Operation(summary = "Enroll in a course", description = "Authenticated endpoint - enroll the current user in the specified course")
    public ResponseEntity<EnrollmentResponse> enroll(
            @AuthenticationPrincipal User user,
            @PathVariable String courseId) {
        EnrollmentResponse response = enrollmentService.enroll(user, courseId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
