package com.example.classlog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "question")
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "question_id")
  private Long questionId;

  @ManyToOne
  @JoinColumn(name = "question_type_id", nullable = true)
  private QuestionType questionType;

  @Column(name = "edited_at", nullable = false)
  private LocalDateTime editedAt = LocalDateTime.now();

  @Column(name = "points")
  private Integer points;

  @Column(name = "content", nullable = false)
  private String content;

  @ManyToOne
  @JoinColumn(name = "file_id", nullable = true)
  private File file;

  @PrePersist
  protected void onCreate() {
    this.editedAt = LocalDateTime.now();
  }

}
