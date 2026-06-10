package com.example.smartlearning.mapper;

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
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminManageMapper {

    List<Major> selectMajors();

    Major selectMajorById(@Param("id") String id);

    int countMajorById(@Param("id") String id);

    int insertMajor(MajorDTO dto);

    int updateMajor(@Param("id") String id, @Param("dto") MajorDTO dto);

    int deleteMajor(@Param("id") String id);

    List<Course> selectCourses();

    Course selectCourseById(@Param("id") String id);

    int countCourseById(@Param("id") String id);

    int countCourseName(@Param("name") String name, @Param("excludeId") String excludeId);

    int insertCourse(CourseDTO dto);

    int updateCourse(@Param("id") String id, @Param("dto") CourseDTO dto);

    int deleteCourse(@Param("id") String id);

    List<AdminMajorCourseVO> selectMajorCourses();

    MajorCourse selectMajorCourseById(@Param("id") Long id);

    int countMajorCourseById(@Param("id") Long id);

    int countMajorCourseRelation(@Param("majorId") String majorId,
                                 @Param("courseId") String courseId,
                                 @Param("excludeId") Long excludeId);

    int countMajorCourseByMajorId(@Param("majorId") String majorId);

    int countMajorCourseByCourseId(@Param("courseId") String courseId);

    int insertMajorCourse(MajorCourseDTO dto);

    int updateMajorCourse(@Param("id") Long id, @Param("dto") MajorCourseDTO dto);

    int deleteMajorCourse(@Param("id") Long id);

    List<AdminKnowledgePointVO> selectKnowledgePoints();

    KnowledgePoint selectKnowledgePointById(@Param("id") Long id);

    int countKnowledgePointById(@Param("id") Long id);

    int countKnowledgePointByIdAndCourseId(@Param("id") Long id, @Param("courseId") String courseId);

    int countKnowledgePointByCourseId(@Param("courseId") String courseId);

    int insertKnowledgePoint(KnowledgePointDTO dto);

    int updateKnowledgePoint(@Param("id") Long id, @Param("dto") KnowledgePointDTO dto);

    int deleteKnowledgePoint(@Param("id") Long id);

    List<Career> selectCareers();

    Career selectCareerById(@Param("id") String id);

    int countCareerById(@Param("id") String id);

    int countCareerName(@Param("name") String name, @Param("excludeId") String excludeId);

    int insertCareer(CareerDTO dto);

    int updateCareer(@Param("id") String id, @Param("dto") CareerDTO dto);

    int deleteCareer(@Param("id") String id);

    List<AdminCareerProfileVO> selectCareerProfiles();

    CareerProfile selectCareerProfileById(@Param("id") Long id);

    int countCareerProfileById(@Param("id") Long id);

    int countCareerProfileByCareerId(@Param("careerId") String careerId, @Param("excludeId") Long excludeId);

    int insertCareerProfile(CareerProfileDTO dto);

    int updateCareerProfile(@Param("id") Long id, @Param("dto") CareerProfileDTO dto);

    int deleteCareerProfile(@Param("id") Long id);

    List<AdminCareerAbilityVO> selectCareerAbilities();

    CareerAbility selectCareerAbilityById(@Param("id") Long id);

    int countCareerAbilityById(@Param("id") Long id);

    int countCareerAbilityByCareerId(@Param("careerId") String careerId);

    int insertCareerAbility(CareerAbilityDTO dto);

    int updateCareerAbility(@Param("id") Long id, @Param("dto") CareerAbilityDTO dto);

    int deleteCareerAbility(@Param("id") Long id);

    List<AdminCareerCourseVO> selectCareerCourses();

    CareerCourse selectCareerCourseById(@Param("id") Long id);

    int countCareerCourseById(@Param("id") Long id);

    int countCareerCourseRelation(@Param("careerId") String careerId,
                                  @Param("courseId") String courseId,
                                  @Param("excludeId") Long excludeId);

    int countCareerCourseByCareerId(@Param("careerId") String careerId);

    int countCareerCourseByCourseId(@Param("courseId") String courseId);

    int insertCareerCourse(CareerCourseDTO dto);

    int updateCareerCourse(@Param("id") Long id, @Param("dto") CareerCourseDTO dto);

    int deleteCareerCourse(@Param("id") Long id);

    List<AdminCareerKnowledgePointVO> selectCareerKnowledgePoints();

    CareerKnowledgePoint selectCareerKnowledgePointById(@Param("id") Long id);

    int countCareerKnowledgePointById(@Param("id") Long id);

    int countCareerKnowledgePointRelation(@Param("careerId") String careerId,
                                          @Param("knowledgePointId") Long knowledgePointId,
                                          @Param("excludeId") Long excludeId);

    int countCareerKnowledgePointByCareerId(@Param("careerId") String careerId);

    int countCareerKnowledgePointByCourseId(@Param("courseId") String courseId);

    int countCareerKnowledgePointByKnowledgePointId(@Param("knowledgePointId") Long knowledgePointId);

    int insertCareerKnowledgePoint(CareerKnowledgePointDTO dto);

    int updateCareerKnowledgePoint(@Param("id") Long id, @Param("dto") CareerKnowledgePointDTO dto);

    int deleteCareerKnowledgePoint(@Param("id") Long id);

    List<OptionVO> selectMajorOptions();

    List<OptionVO> selectCourseOptions();

    List<OptionVO> selectCareerOptions();

    List<OptionVO> selectKnowledgePointOptions(@Param("courseId") String courseId);
}
