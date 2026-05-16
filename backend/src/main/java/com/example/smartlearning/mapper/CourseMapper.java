package com.example.smartlearning.mapper;

import com.example.smartlearning.entity.KnowledgePoint;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseMapper {

    List<KnowledgePoint> selectKnowledgePointsByCourseId(@Param("courseId") String courseId);
}
