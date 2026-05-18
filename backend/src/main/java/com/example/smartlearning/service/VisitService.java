package com.example.smartlearning.service;

import com.example.smartlearning.dto.VisitRecordDTO;
import com.example.smartlearning.vo.VisitDailyStatsVO;
import com.example.smartlearning.vo.VisitTodayStatsVO;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

public interface VisitService {

    void recordVisit(VisitRecordDTO dto, HttpServletRequest request);

    VisitTodayStatsVO getTodayStats();

    List<VisitDailyStatsVO> getHistoryStats(LocalDate startDate, LocalDate endDate);
}
