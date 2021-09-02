package com.project.mapper.master;

import com.project.entity.ApprovalProcessNode;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApprovalProcessNodeMapper {

    List<ApprovalProcessNode> selectProcessNodeByIdApproval(@Param("idApproval")Integer ApprovalProcessNode);

    int updateSelective(ApprovalProcessNode approvalProcessNode);

    int addSelective(ApprovalProcessNode approvalProcessNode);

    ApprovalProcessNode selectByPrimaryKey(@Param("idApprovalProcessNode") Integer idApprovalProcessNode);

}
