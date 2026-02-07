package com.courseplatform.dto;

import java.time.Instant;
import java.util.List;

public class EnrollmentProgressResponse {
    private Long enrollmentId;
    private String courseId;
    private String courseTitle;
    private int totalSubtopics;
    private int completedSubtopics;
    private double completionPercentage;
    private List<CompletedItem> completedItems;

    public EnrollmentProgressResponse(Long enrollmentId, String courseId, String courseTitle,
                                       int totalSubtopics, int completedSubtopics,
                                       double completionPercentage, List<CompletedItem> completedItems) {
        this.enrollmentId = enrollmentId;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.totalSubtopics = totalSubtopics;
        this.completedSubtopics = completedSubtopics;
        this.completionPercentage = completionPercentage;
        this.completedItems = completedItems;
    }

    public Long getEnrollmentId() { return enrollmentId; }
    public String getCourseId() { return courseId; }
    public String getCourseTitle() { return courseTitle; }
    public int getTotalSubtopics() { return totalSubtopics; }
    public int getCompletedSubtopics() { return completedSubtopics; }
    public double getCompletionPercentage() { return completionPercentage; }
    public List<CompletedItem> getCompletedItems() { return completedItems; }

    public static class CompletedItem {
        private String subtopicId;
        private String subtopicTitle;
        private Instant completedAt;

        public CompletedItem(String subtopicId, String subtopicTitle, Instant completedAt) {
            this.subtopicId = subtopicId;
            this.subtopicTitle = subtopicTitle;
            this.completedAt = completedAt;
        }

        public String getSubtopicId() { return subtopicId; }
        public String getSubtopicTitle() { return subtopicTitle; }
        public Instant getCompletedAt() { return completedAt; }
    }
}
