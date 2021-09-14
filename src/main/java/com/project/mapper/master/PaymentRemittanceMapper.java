package com.project.mapper.master;

import com.project.entity.PaymentRemittance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PaymentRemittanceMapper {

    int selectRemittanceCount();

    int selectRemittanceByIdPamentForm(@Param("idPaymentForm") Integer idPaymentForm);

    List<PaymentRemittance> selectPaymentRemittanceByDepartment(@Param("idCardType") Integer idCardType, @Param("year") Integer year);

    PaymentRemittance selectRemittanceTotalByYear(@Param("idCardType") Integer idCardType, @Param("year") int year);

    int updateSelective(PaymentRemittance paymentRemittance);

    int addSelective(PaymentRemittance paymentRemittance);

}
