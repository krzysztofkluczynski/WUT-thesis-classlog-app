package com.example.classlog.service;


import com.example.classlog.dto.UserDto;
import com.example.classlog.repository.PresenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PresenceService {

    private final PresenceRepository presenceRepository;

    @Transactional
    public void addPresenceForListOfStudents(long lessonId, List<UserDto> users) {
        for (UserDto user : users) {
            if (!presenceRepository.existsByLesson_LessonIdAndStudent_Id(lessonId, user.getId())) {
                presenceRepository.insertPresence(lessonId, user.getId());
            }
        }
    }
}
