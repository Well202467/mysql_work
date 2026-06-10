package com.example.smartlearning.vo;

import lombok.Data;

@Data
public class CareerAbilityVO {

    private Long id;
    private String careerId;
    private String name;
    private String description;
    private String abilityType;
    private String importance;
    private String learningAdvice;
}
