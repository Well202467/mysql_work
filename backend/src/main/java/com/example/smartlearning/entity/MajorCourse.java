package com.example.smartlearning.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MajorCourse {

    private Long id;
    private Long majorId;
    private Long courseId;
    private String type;
    private BigDecimal credits;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
