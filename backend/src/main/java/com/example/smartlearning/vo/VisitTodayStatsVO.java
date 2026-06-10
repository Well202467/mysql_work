package com.example.smartlearning.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VisitTodayStatsVO {

    private Long todayUv = 0L;
    private Long todayPv = 0L;
    private Long todaySessions = 0L;
    private String todayDate;
    private List<PopularPageVO> popularPages = new ArrayList<>();
}
