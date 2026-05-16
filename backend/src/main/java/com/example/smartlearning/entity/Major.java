package com.example.smartlearning.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Major {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
