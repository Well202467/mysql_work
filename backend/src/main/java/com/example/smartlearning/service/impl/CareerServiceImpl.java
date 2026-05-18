package com.example.smartlearning.service.impl;

import com.example.smartlearning.entity.Career;
import com.example.smartlearning.mapper.CareerMapper;
import com.example.smartlearning.service.CareerService;
import com.example.smartlearning.vo.CareerAbilityVO;
import com.example.smartlearning.vo.CareerCourseVO;
import com.example.smartlearning.vo.CareerDetailVO;
import com.example.smartlearning.vo.CareerKnowledgePointVO;
import com.example.smartlearning.vo.CourseCareerVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareerServiceImpl implements CareerService {

    private final CareerMapper careerMapper;

    public CareerServiceImpl(CareerMapper careerMapper) {
        this.careerMapper = careerMapper;
    }

    @Override
    public List<Career> getCareerList() {
        return careerMapper.selectAll();
    }

    @Override
    public CareerDetailVO getCareerById(String id) {
        return careerMapper.selectById(id);
    }

    @Override
    public List<CareerCourseVO> getCoursesByCareerId(String careerId) {
        return careerMapper.selectCoursesByCareerId(careerId);
    }

    @Override
    public List<CourseCareerVO> getCareersByCourseId(String courseId) {
        return careerMapper.selectCareersByCourseId(courseId);
    }

    @Override
    public List<CareerAbilityVO> getAbilitiesByCareerId(String careerId) {
        return careerMapper.selectAbilitiesByCareerId(careerId);
    }

    @Override
    public List<CareerKnowledgePointVO> getKnowledgePointsByCareerId(String careerId) {
        return careerMapper.selectKnowledgePointsByCareerId(careerId);
    }
}
