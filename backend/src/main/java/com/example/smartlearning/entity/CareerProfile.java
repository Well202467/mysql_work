package com.example.smartlearning.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CareerProfile {

    private Long id;
    private String careerId;
    private String intro;
    private String workContent;
    private String requirement;
    private String commonTech;
    private String suitableMajor;
    private String developmentPath;
    private String source;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
