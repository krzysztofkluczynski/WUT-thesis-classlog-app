package com.example.classlog.service;

import com.example.classlog.dto.GradeDto;
import com.example.classlog.mapper.GradeMapper;
import com.example.classlog.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final GradeMapper gradeMapper;

    public List<GradeDto> findGradesByUserId(Long userId) {
        return gradeRepository.findByStudent_Id(userId).stream()
                .map(gradeMapper::toGradeDto)
                .collect(Collectors.toList());

    }
}
