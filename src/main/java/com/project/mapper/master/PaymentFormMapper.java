package com.project.mapper.master;

import com.project.entity.PaymentForm;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PaymentFormMapper {

    int queryPaymentRemittanceCount();

    int queryApprovalPaymentCount();

    int queryPaymentFormTotal(@Param("paymentForm") PaymentForm paymentForm);

    List<PaymentForm> queryAllPaymentForm(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                          @Param("paymentForm") PaymentForm paymentForm);

    int selectApprovalPaymentFormTotal(@Param("paymentForm") PaymentForm paymentForm);

    List<PaymentForm> selectApprovalPaymentFormByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                                      @Param("paymentForm") PaymentForm paymentForm);

    String queryMaxCode(@Param("date") Date date);

    int addSelective(PaymentForm paymentForm);

    int updateSelective(PaymentForm paymentForm);

    int deleteSelective(@Param("idPaymentForm") Integer idPaymentForm);

    PaymentForm selectByPrimaryKey(@Param("idPaymentForm") Integer idPaymentForm);

    int selectByPageTotal(@Param("paymentForm") PaymentForm paymentForm, @Param("idUser") Integer idUser);

    List<PaymentForm> selectByPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                   @Param("paymentForm") PaymentForm paymentForm, @Param("idUser") Integer idUser);
}
