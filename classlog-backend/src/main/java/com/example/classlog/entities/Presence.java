package com.example.classlog.entities;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
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