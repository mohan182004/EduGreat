package com.courseplatform.config;

import com.courseplatform.entity.Course;
import com.courseplatform.entity.Subtopic;
import com.courseplatform.entity.Topic;
import com.courseplatform.repository.CourseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final CourseRepository courseRepository;
    private final ObjectMapper objectMapper;

    public DataSeeder(CourseRepository courseRepository, ObjectMapper objectMapper) {
        this.courseRepository = courseRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (courseRepository.count() > 0) {
            logger.info("Database already contains courses. Skipping seed data loading.");
            return;
        }

        logger.info("Loading seed data from courses.json...");

        InputStream inputStream = new ClassPathResource("courses.json").getInputStream();
        Map<String, Object> root = objectMapper.readValue(inputStream,
                new TypeReference<Map<String, Object>>() {});

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> coursesData = (List<Map<String, Object>>) root.get("courses");

        for (Map<String, Object> courseData : coursesData) {
            Course course = new Course(
                    (String) courseData.get("id"),
                    (String) courseData.get("title"),
                    (String) courseData.get("description")
            );

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> topicsData = (List<Map<String, Object>>) courseData.get("topics");

            if (topicsData != null) {
                for (Map<String, Object> topicData : topicsData) {
                    Topic topic = new Topic(
                            (String) topicData.get("id"),
                            (String) topicData.get("title")
                    );

                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> subtopicsData =
                            (List<Map<String, Object>>) topicData.get("subtopics");

                    if (subtopicsData != null) {
                        for (Map<String, Object> subtopicData : subtopicsData) {
                            Subtopic subtopic = new Subtopic(
                                    (String) subtopicData.get("id"),
                                    (String) subtopicData.get("title"),
                                    (String) subtopicData.get("content")
                            );
                            topic.addSubtopic(subtopic);
                        }
                    }

                    course.addTopic(topic);
                }
            }

            courseRepository.save(course);
            logger.info("Loaded course: {} - {} ({} topics)",
                    course.getId(), course.getTitle(), course.getTopics().size());
        }

        logger.info("Seed data loading complete. {} courses loaded.", courseRepository.count());
    }
}
