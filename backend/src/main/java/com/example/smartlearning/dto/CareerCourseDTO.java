package com.example.smartlearning.dto;

import lombok.Data;

@Data
public class CareerCourseDTO {

    private Long id;
    private String careerId;
    private String courseId;
    private String courseRole;
    private String relatedAbility;
    private String relatedPoints;
    private String importance;
    private Integer isCore;
    private String learningStage;
}
