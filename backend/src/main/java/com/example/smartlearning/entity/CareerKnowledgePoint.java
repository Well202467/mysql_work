package com.example.smartlearning.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CareerKnowledgePoint {

    private Long id;
    private String careerId;
    private String courseId;
    private Long knowledgePointId;
    private String useScene;
    private String importance;
    private String learningAdvice;
    private String source;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
