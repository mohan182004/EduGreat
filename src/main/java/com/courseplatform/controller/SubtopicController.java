package com.courseplatform.controller;

import com.courseplatform.dto.ProgressResponse;
import com.courseplatform.entity.User;
import com.courseplatform.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subtopics")
@Tag(name = "Progress", description = "Track learning progress")
public class SubtopicController {

    private final EnrollmentService enrollmentService;

    public SubtopicController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/{subtopicId}/complete")
    @Operation(summary = "Mark subtopic as completed", description = "Authenticated endpoint - mark a subtopic as completed. User must be enrolled in the parent course. Operation is idempotent.")
    public ResponseEntity<ProgressResponse> markComplete(
            @AuthenticationPrincipal User user,
            @PathVariable String subtopicId) {
        ProgressResponse response = enrollmentService.markSubtopicComplete(user, subtopicId);
        return ResponseEntity.ok(response);
    }
}
