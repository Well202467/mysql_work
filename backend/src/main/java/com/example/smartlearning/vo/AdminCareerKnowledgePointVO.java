package com.example.smartlearning.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminCareerKnowledgePointVO {

    private Long id;
    private String careerId;
    private String careerName;
    private String courseId;
    private String courseName;
    private Long knowledgePointId;
    private String knowledgePointName;
    private String useScene;
    private String importance;
    private String learningAdvice;
    private String source;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
