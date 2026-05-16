package com.example.smartlearning.service.impl;

import com.example.smartlearning.entity.Major;
import com.example.smartlearning.mapper.MajorMapper;
import com.example.smartlearning.service.MajorService;
import com.example.smartlearning.vo.MajorCourseVO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MajorServiceImpl implements MajorService {

    private final MajorMapper majorMapper;

    public MajorServiceImpl(MajorMapper majorMapper) {
        this.majorMapper = majorMapper;
    }

    @Override
    public List<Major> getMajorList() {
        return majorMapper.selectAll();
    }

    @Override
    public Major getMajorById(String id) {
        return majorMapper.selectById(id);
    }

    @Override
    public List<MajorCourseVO> getCoursesByMajorId(String majorId) {
        return majorMapper.selectCoursesByMajorId(majorId);
    }
}
