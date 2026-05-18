package com.example.smartlearning.mapper;

import com.example.smartlearning.entity.VisitLog;
import com.example.smartlearning.vo.PopularPageVO;
import com.example.smartlearning.vo.VisitDailyStatsVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitMapper {

    int insertVisitLog(VisitLog visitLog);

    Long countPv(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    Long countUv(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    Long countSessions(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<PopularPageVO> selectPopularPages(@Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime,
                                           @Param("limit") int limit);

    List<VisitDailyStatsVO> selectDailyStats(@Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);
}
