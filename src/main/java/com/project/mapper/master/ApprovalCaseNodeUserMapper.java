package com.project.mapper.master;

import com.project.entity.ApprovalCaseNodeCheckUser;
import org.apache.ibatis.annotations.Param;

public interface ApprovalCaseNodeUserMapper {

    ApprovalCaseNodeCheckUser selectByPrimaryKey(@Param("idApprovalCaseNodeUser") Integer idApprovalCaseNodeUser);

    ApprovalCaseNodeCheckUser selectByIdApprovalCase(@Param("idApprovalCase") Integer idApprovalCase,
                                                @Param("idApprovalModelNode") Integer idApprovalModelNode);

}
