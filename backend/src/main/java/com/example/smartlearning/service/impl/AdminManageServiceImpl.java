package com.example.smartlearning.service.impl;

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
import com.example.smartlearning.mapper.AdminManageMapper;
import com.example.smartlearning.service.AdminManageService;
import com.example.smartlearning.vo.AdminCareerAbilityVO;
import com.example.smartlearning.vo.AdminCareerCourseVO;
import com.example.smartlearning.vo.AdminCareerKnowledgePointVO;
import com.example.smartlearning.vo.AdminCareerProfileVO;
import com.example.smartlearning.vo.AdminKnowledgePointVO;
import com.example.smartlearning.vo.AdminMajorCourseVO;
import com.example.smartlearning.vo.OptionVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Service
public class AdminManageServiceImpl implements AdminManageService {

    private final AdminManageMapper adminManageMapper;

    public AdminManageServiceImpl(AdminManageMapper adminManageMapper) {
        this.adminManageMapper = adminManageMapper;
    }

    @Override
    public Result<List<Major>> getMajors() {
        return Result.success(safeList(adminManageMapper.selectMajors()));
    }

    @Override
    public Result<Major> createMajor(MajorDTO dto) {
        if (dto == null) {
            return fail("请求体不能为空");
        }
        if (missing(dto.getId())) {
            return fail("专业 id 不能为空");
        }
        if (missing(dto.getName())) {
            return fail("专业名称不能为空");
        }
        if (adminManageMapper.countMajorById(dto.getId()) > 0) {
            return fail("专业 id 已存在，不能重复新增");
        }

        adminManageMapper.insertMajor(dto);
        return Result.success(adminManageMapper.selectMajorById(dto.getId()));
    }

    @Override
    public Result<Major> updateMajor(String id, MajorDTO dto) {
        if (missing(id)) {
            return fail("专业 id 不能为空");
        }
        if (dto == null) {
            return fail("请求体不能为空");
        }
        if (changedId(id, dto.getId())) {
            return fail("不允许修改专业主键 id");
        }
        if (missing(dto.getName())) {
            return fail("专业名称不能为空");
        }
        if (adminManageMapper.countMajorById(id) == 0) {
            return fail("专业不存在，无法修改");
        }

        adminManageMapper.updateMajor(id, dto);
        return Result.success(adminManageMapper.selectMajorById(id));
    }

    @Override
    public Result<Void> deleteMajor(String id) {
        if (missing(id)) {
            return fail("专业 id 不能为空");
        }
        if (adminManageMapper.countMajorById(id) == 0) {
            return fail("专业不存在，无法删除");
        }
        int majorCourseCount = adminManageMapper.countMajorCourseByMajorId(id);
        if (majorCourseCount > 0) {
            return fail("该专业已关联 " + majorCourseCount + " 条专业课程记录，请先删除 major_course 中的关联记录");
        }

        adminManageMapper.deleteMajor(id);
        return Result.success();
    }

    @Override
    public Result<List<Course>> getCourses() {
        return Result.success(safeList(adminManageMapper.selectCourses()));
    }

    @Override
    public Result<Course> createCourse(CourseDTO dto) {
        if (dto == null) {
            return fail("请求体不能为空");
        }
        if (missing(dto.getId())) {
            return fail("课程 id 不能为空");
        }
        if (missing(dto.getName())) {
            return fail("课程名称不能为空");
        }
        if (adminManageMapper.countCourseById(dto.getId()) > 0) {
            return fail("课程 id 已存在，不能重复新增");
        }
        if (adminManageMapper.countCourseName(dto.getName(), null) > 0) {
            return fail("课程名称已存在，不能重复新增");
        }

        adminManageMapper.insertCourse(dto);
        return Result.success(adminManageMapper.selectCourseById(dto.getId()));
    }

    @Override
    public Result<Course> updateCourse(String id, CourseDTO dto) {
        if (missing(id)) {
            return fail("课程 id 不能为空");
        }
        if (dto == null) {
            return fail("请求体不能为空");
        }
        if (changedId(id, dto.getId())) {
            return fail("不允许修改课程主键 id");
        }
        if (missing(dto.getName())) {
            return fail("课程名称不能为空");
        }
        if (adminManageMapper.countCourseById(id) == 0) {
            return fail("课程不存在，无法修改");
        }
        if (adminManageMapper.countCourseName(dto.getName(), id) > 0) {
            return fail("课程名称已存在，不能重复修改");
        }

        adminManageMapper.updateCourse(id, dto);
        return Result.success(adminManageMapper.selectCourseById(id));
    }

    @Override
    public Result<Void> deleteCourse(String id) {
        if (missing(id)) {
            return fail("课程 id 不能为空");
        }
        if (adminManageMapper.countCourseById(id) == 0) {
            return fail("课程不存在，无法删除");
        }

        int majorCourseCount = adminManageMapper.countMajorCourseByCourseId(id);
        if (majorCourseCount > 0) {
            return fail("该课程已关联 " + majorCourseCount + " 条专业课程记录，请先删除 major_course 中的关联记录");
        }
        int knowledgePointCount = adminManageMapper.countKnowledgePointByCourseId(id);
        if (knowledgePointCount > 0) {
            return fail("该课程下还有 " + knowledgePointCount + " 条知识点记录，请先删除 knowledge_point 中的记录");
        }
        int careerCourseCount = adminManageMapper.countCareerCourseByCourseId(id);
        if (careerCourseCount > 0) {
            return fail("该课程已关联 " + careerCourseCount + " 条职业课程记录，请先删除 career_course 中的关联记录");
        }
        int careerKnowledgePointCount = adminManageMapper.countCareerKnowledgePointByCourseId(id);
        if (careerKnowledgePointCount > 0) {
            return fail("该课程已关联 " + careerKnowledgePointCount + " 条职业知识点记录，请先删除 career_knowledge_point 中的关联记录");
        }

        adminManageMapper.deleteCourse(id);
        return Result.success();
    }

    @Override
    public Result<List<AdminMajorCourseVO>> getMajorCourses() {
        return Result.success(safeList(adminManageMapper.selectMajorCourses()));
    }

    @Override
    public Result<MajorCourse> createMajorCourse(MajorCourseDTO dto) {
        Result<MajorCourse> validation = validateMajorCourseDTO(dto, null);
        if (validation != null) {
            return validation;
        }

        adminManageMapper.insertMajorCourse(dto);
        return Result.success(adminManageMapper.selectMajorCourseById(dto.getId()));
    }

    @Override
    public Result<MajorCourse> updateMajorCourse(Long id, MajorCourseDTO dto) {
        if (invalidId(id)) {
            return fail("专业课程关联 id 不能为空");
        }
        if (dto != null && changedId(id, dto.getId())) {
            return fail("不允许修改专业课程关联主键 id");
        }
        if (adminManageMapper.countMajorCourseById(id) == 0) {
            return fail("专业课程关联不存在，无法修改");
        }
        Result<MajorCourse> validation = validateMajorCourseDTO(dto, id);
        if (validation != null) {
            return validation;
        }

        adminManageMapper.updateMajorCourse(id, dto);
        return Result.success(adminManageMapper.selectMajorCourseById(id));
    }

    @Override
    public Result<Void> deleteMajorCourse(Long id) {
        if (invalidId(id)) {
            return fail("专业课程关联 id 不能为空");
        }
        if (adminManageMapper.countMajorCourseById(id) == 0) {
            return fail("专业课程关联不存在，无法删除");
        }

        adminManageMapper.deleteMajorCourse(id);
        return Result.success();
    }

    @Override
    public Result<List<AdminKnowledgePointVO>> getKnowledgePoints() {
        return Result.success(safeList(adminManageMapper.selectKnowledgePoints()));
    }

    @Override
    public Result<KnowledgePoint> createKnowledgePoint(KnowledgePointDTO dto) {
        Result<KnowledgePoint> validation = validateKnowledgePointDTO(dto, null);
        if (validation != null) {
            return validation;
        }

        adminManageMapper.insertKnowledgePoint(dto);
        return Result.success(adminManageMapper.selectKnowledgePointById(dto.getId()));
    }

    @Override
    public Result<KnowledgePoint> updateKnowledgePoint(Long id, KnowledgePointDTO dto) {
        if (invalidId(id)) {
            return fail("知识点 id 不能为空");
        }
        if (dto != null && changedId(id, dto.getId())) {
            return fail("不允许修改知识点主键 id");
        }
        KnowledgePoint oldPoint = adminManageMapper.selectKnowledgePointById(id);
        if (oldPoint == null) {
            return fail("知识点不存在，无法修改");
        }
        Result<KnowledgePoint> validation = validateKnowledgePointDTO(dto, id);
        if (validation != null) {
            return validation;
        }
        if (!oldPoint.getCourseId().equals(dto.getCourseId())
                && adminManageMapper.countCareerKnowledgePointByKnowledgePointId(id) > 0) {
            return fail("该知识点已被职业知识点关联使用，不能修改所属课程");
        }

        adminManageMapper.updateKnowledgePoint(id, dto);
        return Result.success(adminManageMapper.selectKnowledgePointById(id));
    }

    @Override
    public Result<Void> deleteKnowledgePoint(Long id) {
        if (invalidId(id)) {
            return fail("知识点 id 不能为空");
        }
        if (adminManageMapper.countKnowledgePointById(id) == 0) {
            return fail("知识点不存在，无法删除");
        }
        int careerKnowledgePointCount = adminManageMapper.countCareerKnowledgePointByKnowledgePointId(id);
        if (careerKnowledgePointCount > 0) {
            return fail("该知识点已关联 " + careerKnowledgePointCount + " 条职业知识点记录，请先删除 career_knowledge_point 中的关联记录");
        }

        adminManageMapper.deleteKnowledgePoint(id);
        return Result.success();
    }

    @Override
    public Result<List<Career>> getCareers() {
        return Result.success(safeList(adminManageMapper.selectCareers()));
    }

    @Override
    public Result<Career> createCareer(CareerDTO dto) {
        if (dto == null) {
            return fail("请求体不能为空");
        }
        if (missing(dto.getId())) {
            return fail("职业 id 不能为空");
        }
        if (missing(dto.getName())) {
            return fail("职业名称不能为空");
        }
        if (adminManageMapper.countCareerById(dto.getId()) > 0) {
            return fail("职业 id 已存在，不能重复新增");
        }
        if (adminManageMapper.countCareerName(dto.getName(), null) > 0) {
            return fail("职业名称已存在，不能重复新增");
        }
        fillCareerDefault(dto);

        adminManageMapper.insertCareer(dto);
        return Result.success(adminManageMapper.selectCareerById(dto.getId()));
    }

    @Override
    public Result<Career> updateCareer(String id, CareerDTO dto) {
        if (missing(id)) {
            return fail("职业 id 不能为空");
        }
        if (dto == null) {
            return fail("请求体不能为空");
        }
        if (changedId(id, dto.getId())) {
            return fail("不允许修改职业主键 id");
        }
        if (missing(dto.getName())) {
            return fail("职业名称不能为空");
        }
        if (adminManageMapper.countCareerById(id) == 0) {
            return fail("职业不存在，无法修改");
        }
        if (adminManageMapper.countCareerName(dto.getName(), id) > 0) {
            return fail("职业名称已存在，不能重复修改");
        }
        fillCareerDefault(dto);

        adminManageMapper.updateCareer(id, dto);
        return Result.success(adminManageMapper.selectCareerById(id));
    }

    @Override
    public Result<Void> deleteCareer(String id) {
        if (missing(id)) {
            return fail("职业 id 不能为空");
        }
        if (adminManageMapper.countCareerById(id) == 0) {
            return fail("职业不存在，无法删除");
        }
        int profileCount = adminManageMapper.countCareerProfileByCareerId(id, null);
        if (profileCount > 0) {
            return fail("该职业已关联 " + profileCount + " 条职业画像记录，请先删除 career_profile 中的记录");
        }
        int abilityCount = adminManageMapper.countCareerAbilityByCareerId(id);
        if (abilityCount > 0) {
            return fail("该职业已关联 " + abilityCount + " 条职业能力记录，请先删除 career_ability 中的记录");
        }
        int courseCount = adminManageMapper.countCareerCourseByCareerId(id);
        if (courseCount > 0) {
            return fail("该职业已关联 " + courseCount + " 条职业课程记录，请先删除 career_course 中的关联记录");
        }
        int knowledgePointCount = adminManageMapper.countCareerKnowledgePointByCareerId(id);
        if (knowledgePointCount > 0) {
            return fail("该职业已关联 " + knowledgePointCount + " 条职业知识点记录，请先删除 career_knowledge_point 中的关联记录");
        }

        adminManageMapper.deleteCareer(id);
        return Result.success();
    }

    @Override
    public Result<List<AdminCareerProfileVO>> getCareerProfiles() {
        return Result.success(safeList(adminManageMapper.selectCareerProfiles()));
    }

    @Override
    public Result<CareerProfile> createCareerProfile(CareerProfileDTO dto) {
        Result<CareerProfile> validation = validateCareerProfileDTO(dto, null);
        if (validation != null) {
            return validation;
        }

        adminManageMapper.insertCareerProfile(dto);
        return Result.success(adminManageMapper.selectCareerProfileById(dto.getId()));
    }

    @Override
    public Result<CareerProfile> updateCareerProfile(Long id, CareerProfileDTO dto) {
        if (invalidId(id)) {
            return fail("职业画像 id 不能为空");
        }
        if (dto != null && changedId(id, dto.getId())) {
            return fail("不允许修改职业画像主键 id");
        }
        if (adminManageMapper.countCareerProfileById(id) == 0) {
            return fail("职业画像不存在，无法修改");
        }
        Result<CareerProfile> validation = validateCareerProfileDTO(dto, id);
        if (validation != null) {
            return validation;
        }

        adminManageMapper.updateCareerProfile(id, dto);
        return Result.success(adminManageMapper.selectCareerProfileById(id));
    }

    @Override
    public Result<Void> deleteCareerProfile(Long id) {
        if (invalidId(id)) {
            return fail("职业画像 id 不能为空");
        }
        if (adminManageMapper.countCareerProfileById(id) == 0) {
            return fail("职业画像不存在，无法删除");
        }

        adminManageMapper.deleteCareerProfile(id);
        return Result.success();
    }

    @Override
    public Result<List<AdminCareerAbilityVO>> getCareerAbilities() {
        return Result.success(safeList(adminManageMapper.selectCareerAbilities()));
    }

    @Override
    public Result<CareerAbility> createCareerAbility(CareerAbilityDTO dto) {
        Result<CareerAbility> validation = validateCareerAbilityDTO(dto);
        if (validation != null) {
            return validation;
        }

        adminManageMapper.insertCareerAbility(dto);
        return Result.success(adminManageMapper.selectCareerAbilityById(dto.getId()));
    }

    @Override
    public Result<CareerAbility> updateCareerAbility(Long id, CareerAbilityDTO dto) {
        if (invalidId(id)) {
            return fail("职业能力 id 不能为空");
        }
        if (dto != null && changedId(id, dto.getId())) {
            return fail("不允许修改职业能力主键 id");
        }
        if (adminManageMapper.countCareerAbilityById(id) == 0) {
            return fail("职业能力不存在，无法修改");
        }
        Result<CareerAbility> validation = validateCareerAbilityDTO(dto);
        if (validation != null) {
            return validation;
        }

        adminManageMapper.updateCareerAbility(id, dto);
        return Result.success(adminManageMapper.selectCareerAbilityById(id));
    }

    @Override
    public Result<Void> deleteCareerAbility(Long id) {
        if (invalidId(id)) {
            return fail("职业能力 id 不能为空");
        }
        if (adminManageMapper.countCareerAbilityById(id) == 0) {
            return fail("职业能力不存在，无法删除");
        }

        adminManageMapper.deleteCareerAbility(id);
        return Result.success();
    }

    @Override
    public Result<List<AdminCareerCourseVO>> getCareerCourses() {
        return Result.success(safeList(adminManageMapper.selectCareerCourses()));
    }

    @Override
    public Result<CareerCourse> createCareerCourse(CareerCourseDTO dto) {
        Result<CareerCourse> validation = validateCareerCourseDTO(dto, null);
        if (validation != null) {
            return validation;
        }
        fillCareerCourseDefault(dto);

        adminManageMapper.insertCareerCourse(dto);
        return Result.success(adminManageMapper.selectCareerCourseById(dto.getId()));
    }

    @Override
    public Result<CareerCourse> updateCareerCourse(Long id, CareerCourseDTO dto) {
        if (invalidId(id)) {
            return fail("职业课程关联 id 不能为空");
        }
        if (dto != null && changedId(id, dto.getId())) {
            return fail("不允许修改职业课程关联主键 id");
        }
        if (adminManageMapper.countCareerCourseById(id) == 0) {
            return fail("职业课程关联不存在，无法修改");
        }
        Result<CareerCourse> validation = validateCareerCourseDTO(dto, id);
        if (validation != null) {
            return validation;
        }
        fillCareerCourseDefault(dto);

        adminManageMapper.updateCareerCourse(id, dto);
        return Result.success(adminManageMapper.selectCareerCourseById(id));
    }

    @Override
    public Result<Void> deleteCareerCourse(Long id) {
        if (invalidId(id)) {
            return fail("职业课程关联 id 不能为空");
        }
        if (adminManageMapper.countCareerCourseById(id) == 0) {
            return fail("职业课程关联不存在，无法删除");
        }

        adminManageMapper.deleteCareerCourse(id);
        return Result.success();
    }

    @Override
    public Result<List<AdminCareerKnowledgePointVO>> getCareerKnowledgePoints() {
        return Result.success(safeList(adminManageMapper.selectCareerKnowledgePoints()));
    }

    @Override
    public Result<CareerKnowledgePoint> createCareerKnowledgePoint(CareerKnowledgePointDTO dto) {
        Result<CareerKnowledgePoint> validation = validateCareerKnowledgePointDTO(dto, null);
        if (validation != null) {
            return validation;
        }

        adminManageMapper.insertCareerKnowledgePoint(dto);
        return Result.success(adminManageMapper.selectCareerKnowledgePointById(dto.getId()));
    }

    @Override
    public Result<CareerKnowledgePoint> updateCareerKnowledgePoint(Long id, CareerKnowledgePointDTO dto) {
        if (invalidId(id)) {
            return fail("职业知识点关联 id 不能为空");
        }
        if (dto != null && changedId(id, dto.getId())) {
            return fail("不允许修改职业知识点关联主键 id");
        }
        if (adminManageMapper.countCareerKnowledgePointById(id) == 0) {
            return fail("职业知识点关联不存在，无法修改");
        }
        Result<CareerKnowledgePoint> validation = validateCareerKnowledgePointDTO(dto, id);
        if (validation != null) {
            return validation;
        }

        adminManageMapper.updateCareerKnowledgePoint(id, dto);
        return Result.success(adminManageMapper.selectCareerKnowledgePointById(id));
    }

    @Override
    public Result<Void> deleteCareerKnowledgePoint(Long id) {
        if (invalidId(id)) {
            return fail("职业知识点关联 id 不能为空");
        }
        if (adminManageMapper.countCareerKnowledgePointById(id) == 0) {
            return fail("职业知识点关联不存在，无法删除");
        }

        adminManageMapper.deleteCareerKnowledgePoint(id);
        return Result.success();
    }

    @Override
    public Result<List<OptionVO>> getMajorOptions() {
        return Result.success(safeList(adminManageMapper.selectMajorOptions()));
    }

    @Override
    public Result<List<OptionVO>> getCourseOptions() {
        return Result.success(safeList(adminManageMapper.selectCourseOptions()));
    }

    @Override
    public Result<List<OptionVO>> getCareerOptions() {
        return Result.success(safeList(adminManageMapper.selectCareerOptions()));
    }

    @Override
    public Result<List<OptionVO>> getKnowledgePointOptions(String courseId) {
        return Result.success(safeList(adminManageMapper.selectKnowledgePointOptions(courseId)));
    }

    private Result<MajorCourse> validateMajorCourseDTO(MajorCourseDTO dto, Long excludeId) {
        if (dto == null) {
            return fail("请求体不能为空");
        }
        if (missing(dto.getMajorId())) {
            return fail("专业 id 不能为空");
        }
        if (missing(dto.getCourseId())) {
            return fail("课程 id 不能为空");
        }
        if (adminManageMapper.countMajorById(dto.getMajorId()) == 0) {
            return fail("专业不存在，majorId=" + dto.getMajorId());
        }
        if (adminManageMapper.countCourseById(dto.getCourseId()) == 0) {
            return fail("课程不存在，courseId=" + dto.getCourseId());
        }
        if (adminManageMapper.countMajorCourseRelation(dto.getMajorId(), dto.getCourseId(), excludeId) > 0) {
            return fail("该专业和课程的关联已存在，不能重复保存");
        }
        return null;
    }

    private Result<KnowledgePoint> validateKnowledgePointDTO(KnowledgePointDTO dto, Long ignoredId) {
        if (dto == null) {
            return fail("请求体不能为空");
        }
        if (missing(dto.getCourseId())) {
            return fail("课程 id 不能为空");
        }
        if (missing(dto.getName())) {
            return fail("知识点名称不能为空");
        }
        if (adminManageMapper.countCourseById(dto.getCourseId()) == 0) {
            return fail("课程不存在，courseId=" + dto.getCourseId());
        }
        return null;
    }

    private Result<CareerProfile> validateCareerProfileDTO(CareerProfileDTO dto, Long excludeId) {
        if (dto == null) {
            return fail("请求体不能为空");
        }
        if (missing(dto.getCareerId())) {
            return fail("职业 id 不能为空");
        }
        if (missing(dto.getIntro())) {
            return fail("职业画像 intro 不能为空");
        }
        if (missing(dto.getWorkContent())) {
            return fail("职业画像 workContent 不能为空");
        }
        if (missing(dto.getRequirement())) {
            return fail("职业画像 requirement 不能为空");
        }
        if (adminManageMapper.countCareerById(dto.getCareerId()) == 0) {
            return fail("职业不存在，careerId=" + dto.getCareerId());
        }
        if (adminManageMapper.countCareerProfileByCareerId(dto.getCareerId(), excludeId) > 0) {
            return fail("该职业已存在画像记录，一个 careerId 只允许一条 career_profile");
        }
        return null;
    }

    private Result<CareerAbility> validateCareerAbilityDTO(CareerAbilityDTO dto) {
        if (dto == null) {
            return fail("请求体不能为空");
        }
        if (missing(dto.getCareerId())) {
            return fail("职业 id 不能为空");
        }
        if (missing(dto.getName())) {
            return fail("职业能力名称不能为空");
        }
        if (adminManageMapper.countCareerById(dto.getCareerId()) == 0) {
            return fail("职业不存在，careerId=" + dto.getCareerId());
        }
        return null;
    }

    private Result<CareerCourse> validateCareerCourseDTO(CareerCourseDTO dto, Long excludeId) {
        if (dto == null) {
            return fail("请求体不能为空");
        }
        if (missing(dto.getCareerId())) {
            return fail("职业 id 不能为空");
        }
        if (missing(dto.getCourseId())) {
            return fail("课程 id 不能为空");
        }
        if (adminManageMapper.countCareerById(dto.getCareerId()) == 0) {
            return fail("职业不存在，careerId=" + dto.getCareerId());
        }
        if (adminManageMapper.countCourseById(dto.getCourseId()) == 0) {
            return fail("课程不存在，courseId=" + dto.getCourseId());
        }
        if (adminManageMapper.countCareerCourseRelation(dto.getCareerId(), dto.getCourseId(), excludeId) > 0) {
            return fail("该职业和课程的关联已存在，不能重复保存");
        }
        return null;
    }

    private Result<CareerKnowledgePoint> validateCareerKnowledgePointDTO(CareerKnowledgePointDTO dto, Long excludeId) {
        if (dto == null) {
            return fail("请求体不能为空");
        }
        if (missing(dto.getCareerId())) {
            return fail("职业 id 不能为空");
        }
        if (missing(dto.getCourseId())) {
            return fail("课程 id 不能为空");
        }
        if (dto.getKnowledgePointId() == null) {
            return fail("知识点 id 不能为空");
        }
        if (adminManageMapper.countCareerById(dto.getCareerId()) == 0) {
            return fail("职业不存在，careerId=" + dto.getCareerId());
        }
        if (adminManageMapper.countCourseById(dto.getCourseId()) == 0) {
            return fail("课程不存在，courseId=" + dto.getCourseId());
        }
        if (adminManageMapper.countKnowledgePointById(dto.getKnowledgePointId()) == 0) {
            return fail("知识点不存在，knowledgePointId=" + dto.getKnowledgePointId());
        }
        if (adminManageMapper.countKnowledgePointByIdAndCourseId(dto.getKnowledgePointId(), dto.getCourseId()) == 0) {
            return fail("知识点不属于当前课程，请检查 courseId 和 knowledgePointId");
        }
        if (adminManageMapper.countCareerKnowledgePointRelation(dto.getCareerId(), dto.getKnowledgePointId(), excludeId) > 0) {
            return fail("该职业和知识点的关联已存在，不能重复保存");
        }
        return null;
    }

    private void fillCareerDefault(CareerDTO dto) {
        if (dto.getSortOrder() == null) {
            dto.setSortOrder(0);
        }
    }

    private void fillCareerCourseDefault(CareerCourseDTO dto) {
        if (dto.getIsCore() == null) {
            dto.setIsCore(0);
        }
    }

    private boolean missing(String value) {
        return !StringUtils.hasText(value);
    }

    private boolean invalidId(Long id) {
        return id == null || id <= 0;
    }

    private boolean changedId(String pathId, String bodyId) {
        return StringUtils.hasText(bodyId) && !pathId.equals(bodyId);
    }

    private boolean changedId(Long pathId, Long bodyId) {
        return bodyId != null && !pathId.equals(bodyId);
    }

    private <T> List<T> safeList(List<T> list) {
        return list == null ? Collections.emptyList() : list;
    }

    private <T> Result<T> fail(String message) {
        return Result.error(message);
    }
}
