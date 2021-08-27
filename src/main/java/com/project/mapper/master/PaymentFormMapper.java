package com.project.mapper.master;

import com.project.entity.PaymentForm;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PaymentFormMapper {

    List<PaymentForm> selectPayFlowRecordDetails(@Param("idCardType") Integer idCardType, @Param("year") Integer year);

    List<PaymentForm> selectCollectionByIdCardType(@Param("idCardType") Integer idCardType, @Param("date") String date);

    List<PaymentForm> selectPaymentByIdCardType(@Param("idCardType") Integer idCardType, @Param("date") String date);

    List<PaymentForm> queryLastDayCollectionRecord(@Param("idCardType") Integer idCardType);

    List<PaymentForm> queryLastDayFlowRecord(@Param("idCardType") Integer idCardType);

    int queryIncomeFlowRecordDetailTotal(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<PaymentForm> queryIncomeFlowRecordDetail(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                                   @Param("startTime") String startTime, @Param("endTime") String endTime);

    int queryPayFlowRecordDetailTotal(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<PaymentForm> queryPayFlowRecordDetail(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
                                                @Param("startTime") String startTime, @Param("endTime") String endTime);

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
