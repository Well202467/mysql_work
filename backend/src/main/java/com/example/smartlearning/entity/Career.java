package com.example.smartlearning.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Career {

    private String id;
    private String name;
    private String category;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
