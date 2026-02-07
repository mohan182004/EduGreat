package com.courseplatform.dto;

import java.util.List;

public class CourseDetailResponse {
    private String id;
    private String title;
    private String description;
    private List<TopicDto> topics;

    public CourseDetailResponse(String id, String title, String description, List<TopicDto> topics) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.topics = topics;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<TopicDto> getTopics() { return topics; }

    public static class TopicDto {
        private String id;
        private String title;
        private List<SubtopicDto> subtopics;

        public TopicDto(String id, String title, List<SubtopicDto> subtopics) {
            this.id = id;
            this.title = title;
            this.subtopics = subtopics;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public List<SubtopicDto> getSubtopics() { return subtopics; }
    }

    public static class SubtopicDto {
        private String id;
        private String title;
        private String content;

        public SubtopicDto(String id, String title, String content) {
            this.id = id;
            this.title = title;
            this.content = content;
        }

        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
    }
}
