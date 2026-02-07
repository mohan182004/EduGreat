package com.courseplatform.service;

import com.courseplatform.dto.*;
import com.courseplatform.entity.Course;
import com.courseplatform.entity.Subtopic;
import com.courseplatform.entity.Topic;
import com.courseplatform.exception.NotFoundException;
import com.courseplatform.repository.CourseRepository;
import com.courseplatform.repository.SubtopicRepository;
import com.courseplatform.repository.TopicRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final SubtopicRepository subtopicRepository;

    public CourseService(CourseRepository courseRepository, TopicRepository topicRepository,
                         SubtopicRepository subtopicRepository) {
        this.courseRepository = courseRepository;
        this.topicRepository = topicRepository;
        this.subtopicRepository = subtopicRepository;
    }

    @Transactional(readOnly = true)
    public CourseListResponse getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseListItem> items = courses.stream()
                .map(c -> new CourseListItem(c.getId(), c.getTitle(), c.getDescription(),
                        c.getTopicCount(), c.getSubtopicCount()))
                .collect(Collectors.toList());
        return new CourseListResponse(items);
    }

    @Transactional(readOnly = true)
    public CourseDetailResponse getCourseById(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException(
                        "Not Found", "Course with id '" + courseId + "' does not exist"));

        List<CourseDetailResponse.TopicDto> topicDtos = course.getTopics().stream()
                .map(topic -> new CourseDetailResponse.TopicDto(
                        topic.getId(),
                        topic.getTitle(),
                        topic.getSubtopics().stream()
                                .map(st -> new CourseDetailResponse.SubtopicDto(
                                        st.getId(), st.getTitle(), st.getContent()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new CourseDetailResponse(course.getId(), course.getTitle(),
                course.getDescription(), topicDtos);
    }

    @Transactional(readOnly = true)
    public SearchResponse search(String query) {
        List<Course> matchingCourses = courseRepository.searchCourses(query);

        List<SearchResponse.SearchResult> results = new ArrayList<>();

        for (Course course : matchingCourses) {
            List<SearchResponse.SearchMatch> matches = new ArrayList<>();
            String lowerQuery = query.toLowerCase();

            // Check course title match
            if (course.getTitle().toLowerCase().contains(lowerQuery)) {
                matches.add(new SearchResponse.SearchMatch(
                        "course", null, null, null,
                        course.getTitle() + " - " + course.getDescription()));
            }

            // Check course description match
            if (course.getDescription().toLowerCase().contains(lowerQuery)) {
                matches.add(new SearchResponse.SearchMatch(
                        "course", null, null, null,
                        course.getDescription()));
            }

            // Check topic titles
            for (Topic topic : course.getTopics()) {
                if (topic.getTitle().toLowerCase().contains(lowerQuery)) {
                    matches.add(new SearchResponse.SearchMatch(
                            "topic", topic.getTitle(), null, null,
                            topic.getTitle()));
                }

                // Check subtopics
                for (Subtopic st : topic.getSubtopics()) {
                    boolean titleMatch = st.getTitle().toLowerCase().contains(lowerQuery);
                    boolean contentMatch = st.getContent().toLowerCase().contains(lowerQuery);

                    if (titleMatch) {
                        matches.add(new SearchResponse.SearchMatch(
                                "subtopic", topic.getTitle(), st.getId(), st.getTitle(),
                                st.getTitle()));
                    }

                    if (contentMatch) {
                        String snippet = extractSnippet(st.getContent(), query);
                        matches.add(new SearchResponse.SearchMatch(
                                "content", topic.getTitle(), st.getId(), st.getTitle(),
                                snippet));
                    }
                }
            }

            if (!matches.isEmpty()) {
                results.add(new SearchResponse.SearchResult(course.getId(), course.getTitle(), matches));
            }
        }

        return new SearchResponse(query, results);
    }

    private String extractSnippet(String content, String query) {
        String lower = content.toLowerCase();
        String lowerQuery = query.toLowerCase();
        int index = lower.indexOf(lowerQuery);
        if (index == -1) return content.substring(0, Math.min(100, content.length())) + "...";

        int start = Math.max(0, index - 50);
        int end = Math.min(content.length(), index + query.length() + 50);

        String snippet = "";
        if (start > 0) snippet += "...";
        snippet += content.substring(start, end);
        if (end < content.length()) snippet += "...";

        return snippet;
    }
}
