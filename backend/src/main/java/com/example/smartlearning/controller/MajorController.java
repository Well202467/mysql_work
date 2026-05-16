package com.example.smartlearning.controller;

import com.example.smartlearning.common.ApiResponse;
import com.example.smartlearning.entity.Major;
import com.example.smartlearning.service.MajorService;
import com.example.smartlearning.vo.MajorCourseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/majors")
public class MajorController {

    private final MajorService majorService;

    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    @GetMapping
    public ApiResponse<List<Major>> getMajorList() {
        List<Major> majors = majorService.getMajorList();
        return ApiResponse.success(majors == null ? Collections.emptyList() : majors);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getMajorById(@PathVariable String id) {
        Major major = majorService.getMajorById(id);
        return ApiResponse.success(major == null ? Collections.emptyList() : major);
    }

    @GetMapping("/{id}/courses")
    public ApiResponse<List<MajorCourseVO>> getCoursesByMajorId(@PathVariable String id) {
        List<MajorCourseVO> courses = majorService.getCoursesByMajorId(id);
        return ApiResponse.success(courses == null ? Collections.emptyList() : courses);
    }
}
