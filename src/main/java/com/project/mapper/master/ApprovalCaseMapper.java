package com.project.mapper.master;

import com.project.entity.ApprovalCase;
import org.apache.ibatis.annotations.Param;

public interface ApprovalCaseMapper {

    ApprovalCase selectApprovalCaseById(@Param("idApprovalCase") Integer idApprovalCase);

    int selectApprovalCaseByDepartment(@Param("idApprovalModel") Integer idApprovalModel,
                                       @Param("idDepartment") Integer idDepartment);

}
