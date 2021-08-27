package com.project.mapper.master;

import com.project.entity.PayFlowRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PayFlowRecordMapper {

    int updateSelective(@Param("amount") String amount, @Param("remainingSum") String remainingSum,
                        @Param("idPaymentRemittance") Integer idPaymentRemittance);

    int addSelective(PayFlowRecord payFlowRecord);

    List<PayFlowRecord> selectAllFlowRecord();

}
