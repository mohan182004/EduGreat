package com.courseplatform.controller;

import com.courseplatform.dto.SearchResponse;
import com.courseplatform.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@Tag(name = "Search", description = "Search courses and content")
public class SearchController {

    private final CourseService courseService;

    public SearchController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @Operation(summary = "Search courses", description = "Public endpoint - search across course titles, descriptions, topics, subtopics, and content. Supports case-insensitive partial matching.")
    public ResponseEntity<SearchResponse> search(@RequestParam("q") String query) {
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(courseService.search(query.trim()));
    }
}
