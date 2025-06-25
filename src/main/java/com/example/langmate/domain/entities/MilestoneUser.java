package com.example.langmate.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MilestoneUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "milestone_id")
  private Milestone milestone;
}
