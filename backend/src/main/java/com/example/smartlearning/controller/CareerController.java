package com.example.smartlearning.controller;

import com.example.smartlearning.common.ApiResponse;
import com.example.smartlearning.entity.Career;
import com.example.smartlearning.service.CareerService;
import com.example.smartlearning.vo.CareerAbilityVO;
import com.example.smartlearning.vo.CareerCourseVO;
import com.example.smartlearning.vo.CareerKnowledgePointVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/careers")
@Tag(name = "Careers")
public class CareerController {

    private final CareerService careerService;

    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @GetMapping
    @Operation(summary = "Get career list")
    public ApiResponse<List<Career>> getCareerList() {
        List<Career> careers = careerService.getCareerList();
        return ApiResponse.success(careers == null ? Collections.emptyList() : careers);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get career detail")
    public ApiResponse<?> getCareerById(@PathVariable String id) {
        Career career = careerService.getCareerById(id);
        return ApiResponse.success(career == null ? Collections.emptyList() : career);
    }

    @GetMapping("/{id}/courses")
    @Operation(summary = "Get courses by career id")
    public ApiResponse<List<CareerCourseVO>> getCoursesByCareerId(@PathVariable String id) {
        List<CareerCourseVO> courses = careerService.getCoursesByCareerId(id);
        return ApiResponse.success(courses == null ? Collections.emptyList() : courses);
    }

    @GetMapping("/{id}/abilities")
    @Operation(summary = "Get abilities by career id")
    public ApiResponse<List<CareerAbilityVO>> getAbilitiesByCareerId(@PathVariable String id) {
        List<CareerAbilityVO> abilities = careerService.getAbilitiesByCareerId(id);
        return ApiResponse.success(abilities == null ? Collections.emptyList() : abilities);
    }

    @GetMapping("/{id}/knowledge-points")
    @Operation(summary = "Get knowledge points by career id")
    public ApiResponse<List<CareerKnowledgePointVO>> getKnowledgePointsByCareerId(@PathVariable String id) {
        List<CareerKnowledgePointVO> points = careerService.getKnowledgePointsByCareerId(id);
        return ApiResponse.success(points == null ? Collections.emptyList() : points);
    }
}
