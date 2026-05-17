package com.example.smartlearning.dto;

import lombok.Data;

@Data
public class CareerKnowledgePointDTO {

    private Long id;
    private String careerId;
    private String courseId;
    private Long knowledgePointId;
    private String useScene;
    private String importance;
    private String learningAdvice;
    private String source;
}
