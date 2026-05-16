package com.example.smartlearning.service.impl;

import com.example.smartlearning.entity.KnowledgePoint;
import com.example.smartlearning.mapper.CourseMapper;
import com.example.smartlearning.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;

    public CourseServiceImpl(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Override
    public List<KnowledgePoint> getKnowledgePointsByCourseId(String courseId) {
        return courseMapper.selectKnowledgePointsByCourseId(courseId);
    }
}
