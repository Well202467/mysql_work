package com.example.smartlearning.controller;

import com.example.smartlearning.common.ApiResponse;
import com.example.smartlearning.entity.KnowledgePoint;
import com.example.smartlearning.service.CourseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/{id}/knowledge-points")
    public ApiResponse<List<KnowledgePoint>> getKnowledgePointsByCourseId(@PathVariable Long id) {
        List<KnowledgePoint> points = courseService.getKnowledgePointsByCourseId(id);
        return ApiResponse.success(points == null ? Collections.emptyList() : points);
    }
}
