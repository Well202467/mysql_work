package com.example.smartlearning.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CareerDetailVO {

    private String id;
    private String name;
    private String category;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long profileId;
    private String intro;
    private String workContent;
    private String requirement;
    private String commonTech;
    private String suitableMajor;
    private String developmentPath;
    private String source;
}
