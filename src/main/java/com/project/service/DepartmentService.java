package com.project.service;

import com.project.entity.Department;
import com.project.utils.common.base.ReturnEntity;

public interface DepartmentService {

    ReturnEntity insertSelective(Department department);

    ReturnEntity updateSelective(Department department);

    ReturnEntity deleteSelective(Integer idDepartment);

    ReturnEntity selectAll();

}
