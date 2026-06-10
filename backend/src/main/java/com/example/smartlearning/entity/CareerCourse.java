package com.example.smartlearning.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CareerCourse {

    private Long id;
    private String careerId;
    private String courseId;
    private String courseRole;
    private String relatedAbility;
    private String relatedPoints;
    private String importance;
    private Integer isCore;
    private String learningStage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
