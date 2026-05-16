package com.example.smartlearning.service;

import com.example.smartlearning.entity.Major;
import com.example.smartlearning.vo.MajorCourseVO;

import java.util.List;

public interface MajorService {

    List<Major> getMajorList();

    Major getMajorById(String id);

    List<MajorCourseVO> getCoursesByMajorId(String majorId);
}
