package com.example.smartlearning.controller;

import com.example.smartlearning.common.Result;
import com.example.smartlearning.service.VisitService;
import com.example.smartlearning.vo.VisitDailyStatsVO;
import com.example.smartlearning.vo.VisitTodayStatsVO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/stats")
public class AdminStatsController {

    private final VisitService visitService;

    public AdminStatsController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping("/today")
    public Result<VisitTodayStatsVO> getTodayStats() {
        return Result.success(visitService.getTodayStats());
    }

    @GetMapping("/history")
    public Result<List<VisitDailyStatsVO>> getHistoryStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(visitService.getHistoryStats(startDate, endDate));
    }
}
