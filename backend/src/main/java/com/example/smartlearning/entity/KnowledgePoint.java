package com.example.smartlearning.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KnowledgePoint {

    private Long id;
    private String courseId;
    private String name;
    private String description;
    private String difficulty;
    private String ability;
    private String source;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
