package com.example.smartlearning.service.impl;

import com.example.smartlearning.dto.VisitRecordDTO;
import com.example.smartlearning.entity.VisitLog;
import com.example.smartlearning.mapper.VisitMapper;
import com.example.smartlearning.service.VisitService;
import com.example.smartlearning.vo.PopularPageVO;
import com.example.smartlearning.vo.VisitDailyStatsVO;
import com.example.smartlearning.vo.VisitTodayStatsVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class VisitServiceImpl implements VisitService {

    private static final int POPULAR_PAGE_LIMIT = 5;

    private final VisitMapper visitMapper;

    public VisitServiceImpl(VisitMapper visitMapper) {
        this.visitMapper = visitMapper;
    }

    @Override
    public void recordVisit(VisitRecordDTO dto, HttpServletRequest request) {
        if (dto == null || !StringUtils.hasText(dto.getVisitorId()) || !StringUtils.hasText(dto.getPath())) {
            return;
        }

        VisitLog visitLog = new VisitLog();
        visitLog.setVisitorId(limit(dto.getVisitorId(), 64));
        visitLog.setSessionId(limit(resolveSessionId(dto), 64));
        System.out.println("dto.sessionId = " + dto.getSessionId());
        System.out.println("visitLog.sessionId = " + visitLog.getSessionId());
        visitLog.setPath(limit(dto.getPath(), 255));
        visitLog.setIp(limit(getClientIp(request), 64));
        visitLog.setUserAgent(header(request, "User-Agent", null));
        visitLog.setReferer(limit(header(request, "Referer", null), 500));
        visitMapper.insertVisitLog(visitLog);
    }

    @Override
    public VisitTodayStatsVO getTodayStats() {
        LocalDate today = LocalDate.now();
        LocalDateTime startTime = today.atStartOfDay();
        LocalDateTime endTime = today.plusDays(1).atStartOfDay();

        VisitTodayStatsVO stats = new VisitTodayStatsVO();
        stats.setTodayDate(today.toString());
        stats.setTodayPv(defaultZero(visitMapper.countPv(startTime, endTime)));
        stats.setTodayUv(defaultZero(visitMapper.countUv(startTime, endTime)));
        stats.setTodaySessions(defaultZero(visitMapper.countSessions(startTime, endTime)));

        List<PopularPageVO> popularPages = visitMapper.selectPopularPages(startTime, endTime, POPULAR_PAGE_LIMIT);
        stats.setPopularPages(popularPages == null ? Collections.emptyList() : popularPages);
        return stats;
    }

    @Override
    public List<VisitDailyStatsVO> getHistoryStats(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }

        LocalDateTime startTime = startDate == null ? null : startDate.atStartOfDay();
        LocalDateTime endTime = endDate == null ? LocalDateTime.now() : endDate.plusDays(1).atStartOfDay();

        List<VisitDailyStatsVO> historyStats = visitMapper.selectDailyStats(startTime, endTime);
        return historyStats == null ? Collections.emptyList() : historyStats;
    }

    private String resolveSessionId(VisitRecordDTO dto) {
        if (validVisitId(dto.getSessionId())) {
            return dto.getSessionId();
        }
        return dto.getVisitorId();
    }

    private boolean validVisitId(String value) {
        return StringUtils.hasText(value) && !"0".equals(value.trim()) && !"null".equalsIgnoreCase(value.trim());
    }

    private String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String forwardedFor = header(request, "X-Forwarded-For", null);
        if (StringUtils.hasText(forwardedFor)) {
            return forwardedFor.split(",")[0].trim();
        }
        String realIp = header(request, "X-Real-IP", null);
        if (StringUtils.hasText(realIp)) {
            return realIp;
        }
        return request.getRemoteAddr();
    }

    private String header(HttpServletRequest request, String name, String defaultValue) {
        if (request == null) {
            return defaultValue;
        }
        String value = request.getHeader(name);
        return StringUtils.hasText(value) ? value : defaultValue;
    }

    private Long defaultZero(Long value) {
        return value == null ? 0L : value;
    }

    private String limit(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
