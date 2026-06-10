package com.example.smartlearning.vo;

import lombok.Data;

@Data
public class CareerKnowledgePointVO {

    private Long id;
    private String careerId;
    private String courseId;
    private String courseName;
    private Long knowledgePointId;
    private String knowledgePointName;
    private String knowledgePointDescription;
    private String useScene;
    private String importance;
    private String learningAdvice;
    private String source;
}
