package com.example.smartlearning.vo;

import lombok.Data;

@Data
public class CourseCareerVO {

    private String careerId;
    private String careerName;
    private String category;
    private String courseRole;
    private String relatedAbility;
    private String relatedPoints;
    private String importance;
    private Integer isCore;
    private String learningStage;
}
