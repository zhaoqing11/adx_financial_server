package com.project.mapper.master;

import com.project.entity.Approval;
import org.apache.ibatis.annotations.Param;

public interface ApprovalMapper {

    int updateSelective(Approval approval);

    int addSelective(Approval approval);

    Approval selectByPrimaryKey(@Param("idApproval") Integer idApproval);

}
