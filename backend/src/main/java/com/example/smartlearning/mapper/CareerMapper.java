package com.example.smartlearning.mapper;

import com.example.smartlearning.entity.Career;
import com.example.smartlearning.vo.CareerAbilityVO;
import com.example.smartlearning.vo.CareerCourseVO;
import com.example.smartlearning.vo.CareerDetailVO;
import com.example.smartlearning.vo.CareerKnowledgePointVO;
import com.example.smartlearning.vo.CourseCareerVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CareerMapper {

    List<Career> selectAll();

    CareerDetailVO selectById(@Param("id") String id);

    List<CareerCourseVO> selectCoursesByCareerId(@Param("careerId") String careerId);

    List<CourseCareerVO> selectCareersByCourseId(@Param("courseId") String courseId);

    List<CareerAbilityVO> selectAbilitiesByCareerId(@Param("careerId") String careerId);

    List<CareerKnowledgePointVO> selectKnowledgePointsByCareerId(@Param("careerId") String careerId);
}
