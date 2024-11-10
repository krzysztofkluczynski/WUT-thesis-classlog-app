package com.example.classlog.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "class_id", nullable = false)
    private Class classEntity;

    @Column(name = "lesson_date", nullable = false)
    private LocalDateTime lessonDate;

    @Column(name = "subject", length = 255)
    private String subject;

    @Column(name = "content")
    private String content;
}