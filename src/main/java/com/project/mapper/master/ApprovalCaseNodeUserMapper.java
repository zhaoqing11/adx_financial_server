package com.project.mapper.master;

import com.project.entity.ApprovalCaseNodeCheckUser;
import org.apache.ibatis.annotations.Param;

public interface ApprovalCaseNodeUserMapper {

    ApprovalCaseNodeCheckUser selectByIdApprovalCase(@Param("idApprovalCase") Integer idApprovalCase,
                                                @Param("idApprovalModelNode") Integer idApprovalModelNode);

}
