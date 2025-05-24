package com.example.langmate.repository;

import com.example.langmate.domain.entities.Milestone;
import org.springframework.data.jpa.repository.JpaRepository;

    public interface MilestoneRepository extends JpaRepository<Milestone, Long> {
    }

