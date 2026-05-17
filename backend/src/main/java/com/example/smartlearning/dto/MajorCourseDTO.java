package com.example.smartlearning.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MajorCourseDTO {

    private Long id;
    private String majorId;
    private String courseId;
    private String type;
    private BigDecimal credits;
}
