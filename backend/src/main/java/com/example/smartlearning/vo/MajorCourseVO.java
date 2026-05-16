package com.example.smartlearning.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MajorCourseVO {

    private String id;
    private String name;
    private String description;
    private String type;
    private BigDecimal credits;
}
