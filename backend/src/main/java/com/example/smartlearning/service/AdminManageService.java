package com.example.smartlearning.service;

import com.example.smartlearning.common.Result;
import com.example.smartlearning.dto.CareerAbilityDTO;
import com.example.smartlearning.dto.CareerCourseDTO;
import com.example.smartlearning.dto.CareerDTO;
import com.example.smartlearning.dto.CareerKnowledgePointDTO;
import com.example.smartlearning.dto.CareerProfileDTO;
import com.example.smartlearning.dto.CourseDTO;
import com.example.smartlearning.dto.KnowledgePointDTO;
import com.example.smartlearning.dto.MajorCourseDTO;
import com.example.smartlearning.dto.MajorDTO;
import com.example.smartlearning.entity.Career;
import com.example.smartlearning.entity.CareerAbility;
import com.example.smartlearning.entity.CareerCourse;
import com.example.smartlearning.entity.CareerKnowledgePoint;
import com.example.smartlearning.entity.CareerProfile;
import com.example.smartlearning.entity.Course;
import com.example.smartlearning.entity.KnowledgePoint;
import com.example.smartlearning.entity.Major;
import com.example.smartlearning.entity.MajorCourse;
import com.example.smartlearning.vo.AdminCareerAbilityVO;
import com.example.smartlearning.vo.AdminCareerCourseVO;
import com.example.smartlearning.vo.AdminCareerKnowledgePointVO;
import com.example.smartlearning.vo.AdminCareerProfileVO;
import com.example.smartlearning.vo.AdminKnowledgePointVO;
import com.example.smartlearning.vo.AdminMajorCourseVO;
import com.example.smartlearning.vo.OptionVO;

import java.util.List;

public interface AdminManageService {

    Result<List<Major>> getMajors();

    Result<Major> createMajor(MajorDTO dto);

    Result<Major> updateMajor(String id, MajorDTO dto);

    Result<Void> deleteMajor(String id);

    Result<List<Course>> getCourses();

    Result<Course> createCourse(CourseDTO dto);

    Result<Course> updateCourse(String id, CourseDTO dto);

    Result<Void> deleteCourse(String id);

    Result<List<AdminMajorCourseVO>> getMajorCourses();

    Result<MajorCourse> createMajorCourse(MajorCourseDTO dto);

    Result<MajorCourse> updateMajorCourse(Long id, MajorCourseDTO dto);

    Result<Void> deleteMajorCourse(Long id);

    Result<List<AdminKnowledgePointVO>> getKnowledgePoints();

    Result<KnowledgePoint> createKnowledgePoint(KnowledgePointDTO dto);

    Result<KnowledgePoint> updateKnowledgePoint(Long id, KnowledgePointDTO dto);

    Result<Void> deleteKnowledgePoint(Long id);

    Result<List<Career>> getCareers();

    Result<Career> createCareer(CareerDTO dto);

    Result<Career> updateCareer(String id, CareerDTO dto);

    Result<Void> deleteCareer(String id);

    Result<List<AdminCareerProfileVO>> getCareerProfiles();

    Result<CareerProfile> createCareerProfile(CareerProfileDTO dto);

    Result<CareerProfile> updateCareerProfile(Long id, CareerProfileDTO dto);

    Result<Void> deleteCareerProfile(Long id);

    Result<List<AdminCareerAbilityVO>> getCareerAbilities();

    Result<CareerAbility> createCareerAbility(CareerAbilityDTO dto);

    Result<CareerAbility> updateCareerAbility(Long id, CareerAbilityDTO dto);

    Result<Void> deleteCareerAbility(Long id);

    Result<List<AdminCareerCourseVO>> getCareerCourses();

    Result<CareerCourse> createCareerCourse(CareerCourseDTO dto);

    Result<CareerCourse> updateCareerCourse(Long id, CareerCourseDTO dto);

    Result<Void> deleteCareerCourse(Long id);

    Result<List<AdminCareerKnowledgePointVO>> getCareerKnowledgePoints();

    Result<CareerKnowledgePoint> createCareerKnowledgePoint(CareerKnowledgePointDTO dto);

    Result<CareerKnowledgePoint> updateCareerKnowledgePoint(Long id, CareerKnowledgePointDTO dto);

    Result<Void> deleteCareerKnowledgePoint(Long id);

    Result<List<OptionVO>> getMajorOptions();

    Result<List<OptionVO>> getCourseOptions();

    Result<List<OptionVO>> getCareerOptions();

    Result<List<OptionVO>> getKnowledgePointOptions(String courseId);
}
