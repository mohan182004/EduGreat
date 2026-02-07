package com.courseplatform.dto;

import java.util.List;

public class CourseListResponse {
    private List<CourseListItem> courses;

    public CourseListResponse(List<CourseListItem> courses) {
        this.courses = courses;
    }

    public List<CourseListItem> getCourses() { return courses; }
}
