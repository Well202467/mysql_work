package com.example.smartlearning.vo;

import lombok.Data;

@Data
public class CareerCourseVO {

    private String courseId;
    private String courseName;
    private String courseDescription;
    private String courseRole;
    private String relatedAbility;
    private String relatedPoints;
    private String importance;
    private Integer isCore;
    private String learningStage;
}
