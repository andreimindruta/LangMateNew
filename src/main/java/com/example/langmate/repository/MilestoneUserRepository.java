package com.example.langmate.repository;

import com.example.langmate.domain.entities.MilestoneUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MilestoneUserRepository extends JpaRepository<MilestoneUser, Long> {
    boolean existsByUserIdAndMilestoneId(Long userId, Long milestoneId);
    List<MilestoneUser> findByUserId(Long userId);
}

