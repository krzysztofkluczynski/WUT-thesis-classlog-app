package com.example.classlog.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "presence")
public class Presence {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "presence_id")
  private Long presenceId;

  @ManyToOne
  @JoinColumn(name = "student_id", nullable = false)
  private User student;
  @ManyToOne
  @JoinColumn(name = "lesson_id", nullable = false)
  private Lesson lesson;
}