package com.courseplatform.dto;

import java.time.Instant;

public class EnrollmentResponse {
    private Long enrollmentId;
    private String courseId;
    private String courseTitle;
    private Instant enrolledAt;

    public EnrollmentResponse(Long enrollmentId, String courseId, String courseTitle, Instant enrolledAt) {
        this.enrollmentId = enrollmentId;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.enrolledAt = enrolledAt;
    }

    public Long getEnrollmentId() { return enrollmentId; }
    public String getCourseId() { return courseId; }
    public String getCourseTitle() { return courseTitle; }
    public Instant getEnrolledAt() { return enrolledAt; }
}
