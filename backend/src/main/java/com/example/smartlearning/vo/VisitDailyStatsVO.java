package com.example.smartlearning.vo;

import lombok.Data;

@Data
public class VisitDailyStatsVO {

    private String date;
    private Long uv;
    private Long pv;
    private Long sessions;
}
