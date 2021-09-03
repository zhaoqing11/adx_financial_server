package com.project.mapper.master;

import com.project.entity.ApprovalModel;
import org.apache.ibatis.annotations.Param;

public interface ApprovalModelMapper {

    ApprovalModel selectByPrimaryKey(@Param("idApprovalModel") Integer idApprovalModel);

}
