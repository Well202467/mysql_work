package com.example.smartlearning.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Course {

    private String id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
