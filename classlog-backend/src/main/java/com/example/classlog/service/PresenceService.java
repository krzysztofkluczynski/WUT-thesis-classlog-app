package com.example.classlog.service;


import com.example.classlog.dto.UserDto;
import com.example.classlog.mapper.UserMapper;
import com.example.classlog.repository.PresenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PresenceService {

    private final PresenceRepository presenceRepository;
    private final UserMapper userMapper;

    @Transactional
    public void addPresenceForListOfStudents(long lessonId, List<UserDto> users) {
        for (UserDto user : users) {
            if (!presenceRepository.existsByLesson_LessonIdAndStudent_Id(lessonId, user.getId())) {
                presenceRepository.insertPresence(lessonId, user.getId());
            }
        }
    }

    public List<UserDto> getPresentStudents(Long lessonId) {
        return presenceRepository.findUsersByLessonId(lessonId).stream()
                .map(userMapper::toUserDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public void updatePresenceForLesson(Long lessonId, List<Long> presentStudents) {
        List<Long> currentStudentIds = presenceRepository.findStudentIdsByLesson_LessonId(lessonId);

        for (Long studentId : currentStudentIds) {
            if (!presentStudents.contains(studentId)) {
                presenceRepository.deleteByLesson_LessonIdAndStudent_Id(lessonId, studentId);
            }
        }

        for (Long studentId : presentStudents) {
            if (!currentStudentIds.contains(studentId)) {
                presenceRepository.insertPresence(lessonId, studentId);
            }
        }
    }
}
