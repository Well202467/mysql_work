package com.example.smartlearning.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AdminMajorCourseVO {

    private Long id;
    private String majorId;
    private String majorName;
    private String courseId;
    private String courseName;
    private String type;
    private BigDecimal credits;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
