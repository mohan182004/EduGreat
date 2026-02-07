package com.courseplatform.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("id")
    private List<Subtopic> subtopics = new ArrayList<>();

    public Topic() {}

    public Topic(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public List<Subtopic> getSubtopics() { return subtopics; }
    public void setSubtopics(List<Subtopic> subtopics) { this.subtopics = subtopics; }

    public void addSubtopic(Subtopic subtopic) {
        subtopics.add(subtopic);
        subtopic.setTopic(this);
    }
}
