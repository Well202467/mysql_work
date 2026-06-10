package com.example.smartlearning.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminCareerCourseVO {

    private Long id;
    private String careerId;
    private String careerName;
    private String courseId;
    private String courseName;
    private String courseRole;
    private String relatedAbility;
    private String relatedPoints;
    private String importance;
    private Integer isCore;
    private String learningStage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
