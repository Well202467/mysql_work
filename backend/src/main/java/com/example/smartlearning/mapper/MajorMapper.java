package com.example.smartlearning.mapper;

import com.example.smartlearning.entity.Major;
import com.example.smartlearning.vo.MajorCourseVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MajorMapper {

    List<Major> selectAll();

    Major selectById(@Param("id") Long id);

    List<MajorCourseVO> selectCoursesByMajorId(@Param("majorId") Long majorId);
}
