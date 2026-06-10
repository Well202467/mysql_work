package com.example.smartlearning.dto;

import lombok.Data;

@Data
public class CareerProfileDTO {

    private Long id;
    private String careerId;
    private String intro;
    private String workContent;
    private String requirement;
    private String commonTech;
    private String suitableMajor;
    private String developmentPath;
    private String source;
}
