package com.project.mapper.master;

import com.project.entity.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentMapper {

    int insertSelective(Department department);

    int updateSelective(Department department);

    int deleteSelective(@Param("idDepartment") Integer idDepartment);

    List<Department> selectAll();

}
