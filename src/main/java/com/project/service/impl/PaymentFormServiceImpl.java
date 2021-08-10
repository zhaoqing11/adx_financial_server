package com.project.service.impl;

import com.project.entity.PaymentForm;
import com.project.mapper.master.PaymentFormMapper;
import com.project.service.PaymentFormService;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.common.PageBean;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
@SuppressWarnings("all")
public class PaymentFormServiceImpl implements PaymentFormService {

    private static Logger logger = LogManager.getLogger(PaymentFormServiceImpl.class);

    @Autowired
    private PaymentFormMapper paymentFormMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity addSelective(PaymentForm paymentForm) {
        try {
            if (paymentForm != null) {
                String newCode = "";
                // 获取当日最大编号数
                String code = paymentFormMapper.queryMaxCode(new Date());
                if (Tools.notEmpty(code)) { // 编号不为空接上一个产生编号数+1
                    int num = Integer.parseInt(code.substring(10, 13));
                    newCode = code.substring(0,10) + formatNum(String.valueOf(num  +  1));

                } else { // 为空创建当日首个编号
                    String date = Tools.date2Str(new Date(), "yy-MM-dd");
                    String[] dataArrays = date.split("-");
                    String year = dataArrays[0];
                    String month = dataArrays[1];
                    String day = dataArrays[2];
                    newCode = "A" + year + "-" + month + "-" + day + "-" + "001";
                }
                paymentForm.setCode(newCode);
                paymentForm.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));

                int count = paymentFormMapper.addSelective(paymentForm);
                if (count > 0) {
                    returnEntity = ReturnUtil.success("创建成功");
                } else {
                    returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "创建失败");
                }
            } else {
                throw new ServiceException("获取参数错误！");
            }

        } catch (Exception e) {
            logger.error("创建请款单失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    private String formatNum(String code) {
        switch (code.length()) {
            case 1:
                code = "00" + code;
                break;
            case 2:
                code = "0" + code;
                break;
        }
        return code;
    }

    @Override
    public ReturnEntity updateSelective(PaymentForm paymentForm) {
        try {
            int count = paymentFormMapper.updateSelective(paymentForm);
            if (count > 0) {
                returnEntity = ReturnUtil.success("修改成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "修改失败");
            }
        } catch (Exception e) {
            logger.error("修改请款单失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity deleteSelective(Integer idPaymentForm) {
        try {
            int count = paymentFormMapper.deleteSelective(idPaymentForm);
            if (count > 0) {
                returnEntity = ReturnUtil.success("删除成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "删除失败");
            }
        } catch (Exception e) {
            logger.error("删除请款单失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectByPrimaryKey(Integer idPaymentForm) {
        try {
            PaymentForm paymentForm = paymentFormMapper.selectByPrimaryKey(idPaymentForm);
            returnEntity = ReturnUtil.success(paymentForm);
        } catch (Exception e) {
            logger.error("根据id查询请款单信息失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectByPage(Integer startIndex, Integer pageSize, PaymentForm paymentForm) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            Integer idUser = paymentForm.getIdUser();
            if (idUser == null) {
                throw new ServiceException("参数错误！");
            }

            int total = paymentFormMapper.selectByPageTotal(paymentForm, idUser);
            PageBean<PaymentForm> pageBean = new PageBean<PaymentForm>(startIndex, pageSize, total);
            List<PaymentForm> paymentFormList = paymentFormMapper.selectByPage(pageBean.getStartIndex(),
                    pageBean.getPageSize(), paymentForm, idUser);

            pageBean.setList(paymentFormList);
            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("分页（条件）查询请款单列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }
}
