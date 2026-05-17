package com.example.smartlearning.controller;

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
import com.example.smartlearning.service.AdminManageService;
import com.example.smartlearning.vo.AdminCareerAbilityVO;
import com.example.smartlearning.vo.AdminCareerCourseVO;
import com.example.smartlearning.vo.AdminCareerKnowledgePointVO;
import com.example.smartlearning.vo.AdminCareerProfileVO;
import com.example.smartlearning.vo.AdminKnowledgePointVO;
import com.example.smartlearning.vo.AdminMajorCourseVO;
import com.example.smartlearning.vo.OptionVO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminManageController {

    private final AdminManageService adminManageService;

    public AdminManageController(AdminManageService adminManageService) {
        this.adminManageService = adminManageService;
    }

    @GetMapping("/majors")
    public Result<List<Major>> getMajors() {
        return adminManageService.getMajors();
    }

    @PostMapping("/majors")
    public Result<Major> createMajor(@RequestBody MajorDTO dto) {
        return adminManageService.createMajor(dto);
    }

    @PutMapping("/majors/{id}")
    public Result<Major> updateMajor(@PathVariable String id, @RequestBody MajorDTO dto) {
        return adminManageService.updateMajor(id, dto);
    }

    @DeleteMapping("/majors/{id}")
    public Result<Void> deleteMajor(@PathVariable String id) {
        return adminManageService.deleteMajor(id);
    }

    @GetMapping("/courses")
    public Result<List<Course>> getCourses() {
        return adminManageService.getCourses();
    }

    @PostMapping("/courses")
    public Result<Course> createCourse(@RequestBody CourseDTO dto) {
        return adminManageService.createCourse(dto);
    }

    @PutMapping("/courses/{id}")
    public Result<Course> updateCourse(@PathVariable String id, @RequestBody CourseDTO dto) {
        return adminManageService.updateCourse(id, dto);
    }

    @DeleteMapping("/courses/{id}")
    public Result<Void> deleteCourse(@PathVariable String id) {
        return adminManageService.deleteCourse(id);
    }

    @GetMapping("/major-courses")
    public Result<List<AdminMajorCourseVO>> getMajorCourses() {
        return adminManageService.getMajorCourses();
    }

    @PostMapping("/major-courses")
    public Result<MajorCourse> createMajorCourse(@RequestBody MajorCourseDTO dto) {
        return adminManageService.createMajorCourse(dto);
    }

    @PutMapping("/major-courses/{id}")
    public Result<MajorCourse> updateMajorCourse(@PathVariable Long id, @RequestBody MajorCourseDTO dto) {
        return adminManageService.updateMajorCourse(id, dto);
    }

    @DeleteMapping("/major-courses/{id}")
    public Result<Void> deleteMajorCourse(@PathVariable Long id) {
        return adminManageService.deleteMajorCourse(id);
    }

    @GetMapping("/knowledge-points")
    public Result<List<AdminKnowledgePointVO>> getKnowledgePoints() {
        return adminManageService.getKnowledgePoints();
    }

    @PostMapping("/knowledge-points")
    public Result<KnowledgePoint> createKnowledgePoint(@RequestBody KnowledgePointDTO dto) {
        return adminManageService.createKnowledgePoint(dto);
    }

    @PutMapping("/knowledge-points/{id}")
    public Result<KnowledgePoint> updateKnowledgePoint(@PathVariable Long id, @RequestBody KnowledgePointDTO dto) {
        return adminManageService.updateKnowledgePoint(id, dto);
    }

    @DeleteMapping("/knowledge-points/{id}")
    public Result<Void> deleteKnowledgePoint(@PathVariable Long id) {
        return adminManageService.deleteKnowledgePoint(id);
    }

    @GetMapping("/careers")
    public Result<List<Career>> getCareers() {
        return adminManageService.getCareers();
    }

    @PostMapping("/careers")
    public Result<Career> createCareer(@RequestBody CareerDTO dto) {
        return adminManageService.createCareer(dto);
    }

    @PutMapping("/careers/{id}")
    public Result<Career> updateCareer(@PathVariable String id, @RequestBody CareerDTO dto) {
        return adminManageService.updateCareer(id, dto);
    }

    @DeleteMapping("/careers/{id}")
    public Result<Void> deleteCareer(@PathVariable String id) {
        return adminManageService.deleteCareer(id);
    }

    @GetMapping("/career-profiles")
    public Result<List<AdminCareerProfileVO>> getCareerProfiles() {
        return adminManageService.getCareerProfiles();
    }

    @PostMapping("/career-profiles")
    public Result<CareerProfile> createCareerProfile(@RequestBody CareerProfileDTO dto) {
        return adminManageService.createCareerProfile(dto);
    }

    @PutMapping("/career-profiles/{id}")
    public Result<CareerProfile> updateCareerProfile(@PathVariable Long id, @RequestBody CareerProfileDTO dto) {
        return adminManageService.updateCareerProfile(id, dto);
    }

    @DeleteMapping("/career-profiles/{id}")
    public Result<Void> deleteCareerProfile(@PathVariable Long id) {
        return adminManageService.deleteCareerProfile(id);
    }

    @GetMapping("/career-abilities")
    public Result<List<AdminCareerAbilityVO>> getCareerAbilities() {
        return adminManageService.getCareerAbilities();
    }

    @PostMapping("/career-abilities")
    public Result<CareerAbility> createCareerAbility(@RequestBody CareerAbilityDTO dto) {
        return adminManageService.createCareerAbility(dto);
    }

    @PutMapping("/career-abilities/{id}")
    public Result<CareerAbility> updateCareerAbility(@PathVariable Long id, @RequestBody CareerAbilityDTO dto) {
        return adminManageService.updateCareerAbility(id, dto);
    }

    @DeleteMapping("/career-abilities/{id}")
    public Result<Void> deleteCareerAbility(@PathVariable Long id) {
        return adminManageService.deleteCareerAbility(id);
    }

    @GetMapping("/career-courses")
    public Result<List<AdminCareerCourseVO>> getCareerCourses() {
        return adminManageService.getCareerCourses();
    }

    @PostMapping("/career-courses")
    public Result<CareerCourse> createCareerCourse(@RequestBody CareerCourseDTO dto) {
        return adminManageService.createCareerCourse(dto);
    }

    @PutMapping("/career-courses/{id}")
    public Result<CareerCourse> updateCareerCourse(@PathVariable Long id, @RequestBody CareerCourseDTO dto) {
        return adminManageService.updateCareerCourse(id, dto);
    }

    @DeleteMapping("/career-courses/{id}")
    public Result<Void> deleteCareerCourse(@PathVariable Long id) {
        return adminManageService.deleteCareerCourse(id);
    }

    @GetMapping("/career-knowledge-points")
    public Result<List<AdminCareerKnowledgePointVO>> getCareerKnowledgePoints() {
        return adminManageService.getCareerKnowledgePoints();
    }

    @PostMapping("/career-knowledge-points")
    public Result<CareerKnowledgePoint> createCareerKnowledgePoint(@RequestBody CareerKnowledgePointDTO dto) {
        return adminManageService.createCareerKnowledgePoint(dto);
    }

    @PutMapping("/career-knowledge-points/{id}")
    public Result<CareerKnowledgePoint> updateCareerKnowledgePoint(@PathVariable Long id,
                                                                   @RequestBody CareerKnowledgePointDTO dto) {
        return adminManageService.updateCareerKnowledgePoint(id, dto);
    }

    @DeleteMapping("/career-knowledge-points/{id}")
    public Result<Void> deleteCareerKnowledgePoint(@PathVariable Long id) {
        return adminManageService.deleteCareerKnowledgePoint(id);
    }

    @GetMapping("/options/majors")
    public Result<List<OptionVO>> getMajorOptions() {
        return adminManageService.getMajorOptions();
    }

    @GetMapping("/options/courses")
    public Result<List<OptionVO>> getCourseOptions() {
        return adminManageService.getCourseOptions();
    }

    @GetMapping("/options/careers")
    public Result<List<OptionVO>> getCareerOptions() {
        return adminManageService.getCareerOptions();
    }

    @GetMapping("/options/knowledge-points")
    public Result<List<OptionVO>> getKnowledgePointOptions(@RequestParam(required = false) String courseId) {
        return adminManageService.getKnowledgePointOptions(courseId);
    }
}
