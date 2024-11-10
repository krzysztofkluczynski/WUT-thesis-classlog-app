package com.example.classlog.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_class")
public class UserClass {

    @EmbeddedId
    private UserClassId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @MapsId("classId")
    @JoinColumn(name = "class_id", nullable = false)
    private Class classEntity;

}
