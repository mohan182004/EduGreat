package com.courseplatform.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "subtopic_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "subtopic_id"})
})
public class SubtopicProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subtopic_id", nullable = false)
    private Subtopic subtopic;

    @Column(nullable = false)
    private boolean completed = true;

    @Column(nullable = false)
    private Instant completedAt;

    public SubtopicProgress() {}

    public SubtopicProgress(User user, Subtopic subtopic) {
        this.user = user;
        this.subtopic = subtopic;
        this.completed = true;
        this.completedAt = Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Subtopic getSubtopic() { return subtopic; }
    public void setSubtopic(Subtopic subtopic) { this.subtopic = subtopic; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Instant getCompletedAt() { return completedAt; }
    public void setCompletedAt(Instant completedAt) { this.completedAt = completedAt; }
}
