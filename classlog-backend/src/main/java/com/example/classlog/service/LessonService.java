package com.example.classlog.service;

import com.example.classlog.repository.LessonRepository;
import com.example.classlog.dto.LessonDto;
import com.example.classlog.entity.Lesson;
import com.example.classlog.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    public List<LessonDto> getAllLessons() {
        return lessonRepository.findAll().stream()
                .map(lessonMapper::toLessonDto)
                .collect(Collectors.toList());
    }

    public LessonDto getLessonById(Long id) {
        System.out.println(lessonRepository.findByLessonId(id));
        return lessonRepository.findById(id)
                .map(lessonMapper::toLessonDto)
                .orElse(null);
    }

    public LessonDto createLesson(LessonDto lessonDto) {
        Lesson lesson = lessonMapper.toEntity(lessonDto);
        lesson = lessonRepository.save(lesson);
        return lessonMapper.toLessonDto(lesson);
    }

    @Transactional
    public void deleteLesson(Long id) {
        lessonRepository.deleteById(id);
    }

    public List<LessonDto> getRecentLessonsCreatedBy(Long userId, int numbOfLessons) {
        return lessonRepository.findByCreatedBy_IdOrderByLessonDateDesc(userId).stream()
                .limit(numbOfLessons)
                .map(lessonMapper::toLessonDto)
                .collect(Collectors.toList());
    }

    public List<LessonDto> getAllLessonsCreatedBy(Long userId) {
        return lessonRepository.findByCreatedBy_IdOrderByLessonDateDesc(userId).stream()
                .map(lessonMapper::toLessonDto)
                .collect(Collectors.toList());
    }

    public List<LessonDto> getAllLessonsForUser(Long userId) {
        return lessonRepository.findLessonsByUserId(userId).stream()
                .map(lessonMapper::toLessonDto)
                .collect(Collectors.toList());
    }

    public List<LessonDto> getRecentLessonsForUser(Long userId, int numbOfLessons) {
        return lessonRepository.findLessonsByUserId(userId).stream()
                .limit(numbOfLessons)
                .map(lessonMapper::toLessonDto)
                .collect(Collectors.toList());
    }

    public List<LessonDto> getLessonByClassId(Long classId) {
        return lessonRepository.findByClassEntity_Id(classId).stream()
                .map(lessonMapper::toLessonDto)
                .collect(Collectors.toList());
    }

    public List<LessonDto> getRecentLessonsByClassId(Long classId, int numbOfLessons) {
        return lessonRepository.findByClassEntity_Id(classId).stream()
                .limit(numbOfLessons)
                .map(lessonMapper::toLessonDto)
                .collect(Collectors.toList());
    }

    public LessonDto updateLesson(LessonDto lessonDto) {
        Lesson lesson = lessonMapper.toEntity(lessonDto);
        System.out.println(lesson);
        lesson = lessonRepository.save(lesson);
        return lessonMapper.toLessonDto(lesson);
    }
}
