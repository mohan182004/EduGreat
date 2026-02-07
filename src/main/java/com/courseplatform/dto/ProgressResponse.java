package com.courseplatform.dto;

import java.time.Instant;

public class ProgressResponse {
    private String subtopicId;
    private boolean completed;
    private Instant completedAt;

    public ProgressResponse(String subtopicId, boolean completed, Instant completedAt) {
        this.subtopicId = subtopicId;
        this.completed = completed;
        this.completedAt = completedAt;
    }

    public String getSubtopicId() { return subtopicId; }
    public boolean isCompleted() { return completed; }
    public Instant getCompletedAt() { return completedAt; }
}
