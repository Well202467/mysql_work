package com.example.smartlearning.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VisitLog {

    private Long id;
    private String visitorId;
    private String sessionId;
    private String path;
    private String ip;
    private String userAgent;
    private String referer;
    private LocalDateTime createdAt;
}
