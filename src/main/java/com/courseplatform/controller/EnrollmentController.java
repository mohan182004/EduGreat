package com.courseplatform.controller;

import com.courseplatform.dto.EnrollmentProgressResponse;
import com.courseplatform.entity.User;
import com.courseplatform.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
@Tag(name = "Enrollments", description = "View enrollment details and progress")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/{enrollmentId}/progress")
    @Operation(summary = "View enrollment progress", description = "Authenticated endpoint - view progress for a specific enrollment including completed subtopics and completion percentage")
    public ResponseEntity<EnrollmentProgressResponse> getProgress(
            @AuthenticationPrincipal User user,
            @PathVariable Long enrollmentId) {
        EnrollmentProgressResponse response = enrollmentService.getProgress(user, enrollmentId);
        return ResponseEntity.ok(response);
    }
}
