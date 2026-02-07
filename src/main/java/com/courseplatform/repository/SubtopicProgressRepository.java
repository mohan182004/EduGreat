package com.courseplatform.repository;

import com.courseplatform.entity.SubtopicProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubtopicProgressRepository extends JpaRepository<SubtopicProgress, Long> {
    Optional<SubtopicProgress> findByUserIdAndSubtopicId(Long userId, String subtopicId);
    List<SubtopicProgress> findByUserIdAndSubtopicIdIn(Long userId, List<String> subtopicIds);
}
