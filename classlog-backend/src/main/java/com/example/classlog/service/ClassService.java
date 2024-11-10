package com.example.classlog.service;

import com.example.classlog.repository.ClassRepository;
import com.example.classlog.dto.ClassDto;
import com.example.classlog.mapper.ClassMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClassService {

    private final ClassRepository classRepository;
    private final ClassMapper classMapper;

    public List<ClassDto> findClassesByUserId(Long userId) {
        return classRepository.findByUserId(userId).stream()
                .map(classMapper::toClassDto)
                .collect(Collectors.toList());
    }
}