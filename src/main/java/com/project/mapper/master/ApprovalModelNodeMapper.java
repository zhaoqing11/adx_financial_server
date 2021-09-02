package com.project.mapper.master;

import com.project.entity.ApprovalModelNode;
import org.apache.ibatis.annotations.Param;

public interface ApprovalModelNodeMapper {

    ApprovalModelNode selectByNodeIndex(@Param("idApprovalModel") Integer idApprovalModel,
                                        @Param("nodeIndex") Integer nodeIndex);

}