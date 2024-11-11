package com.example.classlog.service;

import com.example.classlog.dto.UserDto;
import com.example.classlog.repository.ClassRepository;
import com.example.classlog.dto.ClassDto;
import com.example.classlog.mapper.ClassMapper;
import com.example.classlog.repository.UserClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClassService {

    private final ClassRepository classRepository;
    private final ClassMapper classMapper;

    private final UserClassRepository userClassRepository;

    public List<ClassDto> findClassesByUserId(Long userId) {
        return classRepository.findByUserId(userId).stream()
                .map(classMapper::toClassDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addUsersToClass(Long classId, List<UserDto> users) {
        for (UserDto user : users) {
            if (!userClassRepository.existsByClassEntity_IdAndUser_Id(classId, user.getId())) {
                userClassRepository.insertUserIntoClass(classId, user.getId());
            }
        }
    }
    @Transactional
    public void removeUsersFromClass(Long classId, List<UserDto> users) {
        for (UserDto user : users) {
            if (userClassRepository.existsByClassEntity_IdAndUser_Id(classId, user.getId())) {
                userClassRepository.deleteUserFromClass(classId, user.getId());
            }
        }
    }
}