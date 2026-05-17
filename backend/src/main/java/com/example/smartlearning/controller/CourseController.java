package com.example.smartlearning.controller;

import com.example.smartlearning.common.ApiResponse;
import com.example.smartlearning.entity.KnowledgePoint;
import com.example.smartlearning.service.CareerService;
import com.example.smartlearning.service.CourseService;
import com.example.smartlearning.vo.CourseCareerVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "课程查询接口")
public class CourseController {

    private final CourseService courseService;
    private final CareerService careerService;

    public CourseController(CourseService courseService, CareerService careerService) {
        this.courseService = courseService;
        this.careerService = careerService;
    }

    @Operation(summary = "根据课程ID查询知识点列表")
    @GetMapping("/{id}/knowledge-points")
    public ApiResponse<List<KnowledgePoint>> getKnowledgePointsByCourseId(@PathVariable String id) {
        List<KnowledgePoint> points = courseService.getKnowledgePointsByCourseId(id);
        return ApiResponse.success(points == null ? Collections.emptyList() : points);
    }

    @Operation(summary = "根据课程ID反查关联职业列表")
    @GetMapping("/{courseId}/careers")
    public ApiResponse<List<CourseCareerVO>> getCareersByCourseId(@PathVariable String courseId) {
        List<CourseCareerVO> careers = careerService.getCareersByCourseId(courseId);
        return ApiResponse.success(careers == null ? Collections.emptyList() : careers);
    }
}
