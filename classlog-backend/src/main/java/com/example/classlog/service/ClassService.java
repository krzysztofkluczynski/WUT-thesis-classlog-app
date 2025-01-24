package com.example.classlog.service;

import com.example.classlog.config.exceptions.AppException;
import com.example.classlog.dto.ClassDto;
import com.example.classlog.dto.CreateClassDto;
import com.example.classlog.dto.UserDto;
import com.example.classlog.entity.Class;
import com.example.classlog.entity.User;
import com.example.classlog.mapper.ClassMapper;
import com.example.classlog.repository.ClassRepository;
import com.example.classlog.repository.UserClassRepository;
import com.example.classlog.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClassService {

  private final ClassRepository classRepository;
  private final UserRepository userRepository;
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

  public ClassDto findClassById(Long classId) {
    return classRepository.findById(classId)
        .map(classMapper::toClassDto)
        .orElse(null);
  }

  @Transactional
  public void addUserToClassByCode(String classCode, UserDto user) {
    Optional<Class> classEntity = classRepository.findByCode(classCode);

    if (classEntity.isEmpty()) {
      throw new AppException("Class with code " + classCode + " not found", HttpStatus.NOT_FOUND);
    }

    if (userClassRepository.existsByClassEntity_IdAndUser_Id(classEntity.get().getId(),
        user.getId())) {
      throw new AppException("User is already added to that class", HttpStatus.NOT_FOUND);
    }

    userClassRepository.insertUserIntoClass(classEntity.get().getId(), user.getId());
  }

  @Transactional
  public ClassDto addClass(CreateClassDto createClassDto) {
    Class classEntity = classMapper.toEntity(createClassDto.getClassDto());
    Class savedClass = classRepository.save(classEntity);

    User user = userRepository.findById(createClassDto.getCreatedBy().getId())
        .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

    userClassRepository.insertUserIntoClass(classEntity.getId(), user.getId());

    return classMapper.toClassDto(savedClass);
  }

  public void deleteClass(Long classId) {
    if (!classRepository.existsById(classId)) {
      throw new AppException("Class not found", HttpStatus.NOT_FOUND);
    }
    classRepository.deleteById(classId);
  }
}