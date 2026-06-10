package com.example.smartlearning.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminCareerAbilityVO {

    private Long id;
    private String careerId;
    private String careerName;
    private String name;
    private String description;
    private String abilityType;
    private String importance;
    private String learningAdvice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
