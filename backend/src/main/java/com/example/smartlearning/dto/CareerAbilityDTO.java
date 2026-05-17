package com.example.smartlearning.dto;

import lombok.Data;

@Data
public class CareerAbilityDTO {

    private Long id;
    private String careerId;
    private String name;
    private String description;
    private String abilityType;
    private String importance;
    private String learningAdvice;
}
