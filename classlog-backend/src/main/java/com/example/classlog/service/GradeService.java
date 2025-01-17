package com.example.classlog.service;

import com.example.classlog.dto.GradeDto;
import com.example.classlog.entity.Class;
import com.example.classlog.entity.Grade;
import com.example.classlog.entity.User;
import com.example.classlog.mapper.GradeMapper;
import com.example.classlog.repository.ClassRepository;
import com.example.classlog.repository.GradeRepository;
import com.example.classlog.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GradeService {

  private final GradeRepository gradeRepository;

  private final ClassRepository classRepository;
  private final UserRepository userRepository;
  private final GradeMapper gradeMapper;

  public List<GradeDto> findGradesByUserId(Long userId) {
    return gradeRepository.findByStudent_Id(userId).stream()
        .map(gradeMapper::toGradeDto)
        .collect(Collectors.toList());

  }

  public GradeDto saveGrade(GradeDto gradeDto) {
    User student = userRepository.findById(gradeDto.getStudent().getId())
        .orElseThrow(
            () -> new IllegalArgumentException("Student with the given ID does not exist"));

    User teacher = userRepository.findById(gradeDto.getTeacher().getId())
        .orElseThrow(
            () -> new IllegalArgumentException("Teacher with the given ID does not exist"));

    Class assignedClass = classRepository.findById(gradeDto.getAssignedClass().getId())
        .orElseThrow(() -> new IllegalArgumentException("Class with the given ID does not exist"));

    Grade grade = gradeMapper.toEntity(gradeDto);
    grade.setStudent(student);
    grade.setTeacher(teacher);
    grade.setAssignedClass(assignedClass);

    return gradeMapper.toGradeDto(gradeRepository.save(grade));
  }

  public List<GradeDto> findGradesByClassId(Long classId) {
    return gradeRepository.findByAssignedClass_IdOrderByCreatedAtDesc(classId).stream()
        .map(gradeMapper::toGradeDto)
        .collect(Collectors.toList());
  }

  public void deleteGrade(Long id) {
    gradeRepository.deleteById(id);
  }
}
