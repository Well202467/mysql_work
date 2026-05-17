package com.example.smartlearning.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminKnowledgePointVO {

    private Long id;
    private String courseId;
    private String courseName;
    private String name;
    private String description;
    private String difficulty;
    private String ability;
    private String source;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
