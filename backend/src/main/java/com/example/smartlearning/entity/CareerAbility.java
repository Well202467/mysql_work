package com.example.smartlearning.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CareerAbility {

    private Long id;
    private String careerId;
    private String name;
    private String description;
    private String abilityType;
    private String importance;
    private String learningAdvice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
