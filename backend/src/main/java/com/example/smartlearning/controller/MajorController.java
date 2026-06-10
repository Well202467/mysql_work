package com.example.smartlearning.controller;

import com.example.smartlearning.common.ApiResponse;
import com.example.smartlearning.entity.Major;
import com.example.smartlearning.service.MajorService;
import com.example.smartlearning.vo.MajorCourseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/majors")
@Tag(name = "专业查询接口")
public class MajorController {

    private final MajorService majorService;

    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    @GetMapping
    @Operation(summary = "查询专业列表")
    public ApiResponse<List<Major>> getMajorList() {
        List<Major> majors = majorService.getMajorList();
        return ApiResponse.success(majors == null ? Collections.emptyList() : majors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据专业ID查询专业详情")
    public ApiResponse<?> getMajorById(@PathVariable String id) {
        Major major = majorService.getMajorById(id);
        return ApiResponse.success(major == null ? Collections.emptyList() : major);
    }

    @GetMapping("/{id}/courses")
    @Operation(summary = "根据专业ID查询课程列表")
    public ApiResponse<List<MajorCourseVO>> getCoursesByMajorId(@PathVariable String id) {
        List<MajorCourseVO> courses = majorService.getCoursesByMajorId(id);
        return ApiResponse.success(courses == null ? Collections.emptyList() : courses);
    }
}
