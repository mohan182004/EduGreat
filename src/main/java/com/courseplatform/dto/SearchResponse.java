package com.courseplatform.dto;

import java.util.List;

public class SearchResponse {
    private String query;
    private List<SearchResult> results;

    public SearchResponse(String query, List<SearchResult> results) {
        this.query = query;
        this.results = results;
    }

    public String getQuery() { return query; }
    public List<SearchResult> getResults() { return results; }

    public static class SearchResult {
        private String courseId;
        private String courseTitle;
        private List<SearchMatch> matches;

        public SearchResult(String courseId, String courseTitle, List<SearchMatch> matches) {
            this.courseId = courseId;
            this.courseTitle = courseTitle;
            this.matches = matches;
        }

        public String getCourseId() { return courseId; }
        public String getCourseTitle() { return courseTitle; }
        public List<SearchMatch> getMatches() { return matches; }
    }

    public static class SearchMatch {
        private String type;
        private String topicTitle;
        private String subtopicId;
        private String subtopicTitle;
        private String snippet;

        public SearchMatch(String type, String topicTitle, String subtopicId, String subtopicTitle, String snippet) {
            this.type = type;
            this.topicTitle = topicTitle;
            this.subtopicId = subtopicId;
            this.subtopicTitle = subtopicTitle;
            this.snippet = snippet;
        }

        public String getType() { return type; }
        public String getTopicTitle() { return topicTitle; }
        public String getSubtopicId() { return subtopicId; }
        public String getSubtopicTitle() { return subtopicTitle; }
        public String getSnippet() { return snippet; }
    }
}
