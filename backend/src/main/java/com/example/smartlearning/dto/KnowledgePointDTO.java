package com.example.smartlearning.dto;

import lombok.Data;

@Data
public class KnowledgePointDTO {

    private Long id;
    private String courseId;
    private String name;
    private String description;
    private String difficulty;
    private String ability;
    private String source;
}
