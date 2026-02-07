package com.courseplatform.repository;

import com.courseplatform.entity.Subtopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubtopicRepository extends JpaRepository<Subtopic, String> {

    @Query("SELECT s FROM Subtopic s " +
           "JOIN s.topic t " +
           "JOIN t.course c " +
           "WHERE c.id = :courseId " +
           "AND (LOWER(s.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(s.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Subtopic> findMatchingSubtopics(@Param("courseId") String courseId, @Param("query") String query);

    @Query("SELECT s FROM Subtopic s JOIN s.topic t JOIN t.course c WHERE c.id = :courseId")
    List<Subtopic> findByCourseId(@Param("courseId") String courseId);
}
