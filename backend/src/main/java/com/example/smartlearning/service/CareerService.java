package com.example.smartlearning.service;

import com.example.smartlearning.entity.Career;
import com.example.smartlearning.vo.CareerAbilityVO;
import com.example.smartlearning.vo.CareerCourseVO;
import com.example.smartlearning.vo.CareerKnowledgePointVO;
import com.example.smartlearning.vo.CourseCareerVO;

import java.util.List;

public interface CareerService {

    List<Career> getCareerList();

    Career getCareerById(String id);

    List<CareerCourseVO> getCoursesByCareerId(String careerId);

    List<CourseCareerVO> getCareersByCourseId(String courseId);

    List<CareerAbilityVO> getAbilitiesByCareerId(String careerId);

    List<CareerKnowledgePointVO> getKnowledgePointsByCareerId(String careerId);
}
