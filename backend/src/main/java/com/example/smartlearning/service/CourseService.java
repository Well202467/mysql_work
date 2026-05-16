package com.example.smartlearning.service;

import com.example.smartlearning.entity.KnowledgePoint;

import java.util.List;

public interface CourseService {

    List<KnowledgePoint> getKnowledgePointsByCourseId(Long courseId);
}
