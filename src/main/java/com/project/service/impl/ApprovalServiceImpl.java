package com.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.project.entity.*;
import com.project.mapper.master.*;
import com.project.service.ApprovalService;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@SuppressWarnings("all")
public class ApprovalServiceImpl implements ApprovalService {

    private static Logger logger = LogManager.getLogger(ApprovalServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PaymentFormMapper paymentFormMapper;

    @Autowired
    private PaymentApprovalMapper paymentApprovalMapper;

    @Autowired
    private ApprovalModelMapper approvalModelMapper;

    @Autowired
    private ApprovalCaseNodeUserMapper approvalCaseNodeUserMapper;

    @Autowired
    private ApprovalModelNodeMapper approvalModelNodeMapper;

    @Autowired
    private ApprovalProcessNodeMapper approvalProcessNodeMapper;

    @Autowired
    private ApprovalCaseMapper approvalCaseMapper;

    @Autowired
    private ApprovalMapper approvalMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity selectApprovalInfo(Integer idApproval) {
        try {
            List<ApprovalProcessNode> processNodeList = approvalProcessNodeMapper.selectProcessNodeByIdApproval(idApproval);

            for (ApprovalProcessNode processNode : processNodeList) {
                String modelNodeName = "";
                if (processNode.getIdApprovalCaseNodeUser() > 0) {
                    ApprovalCaseNodeCheckUser approvalCaseNodeCheckUser = approvalCaseNodeUserMapper.selectByPrimaryKey(processNode.getIdApprovalCaseNodeUser());
                    modelNodeName = approvalModelNodeMapper.selectByPrimaryKey(approvalCaseNodeCheckUser.getIdApprovalModelNode()).getName();
                } else {
                    modelNodeName = "业务部门审核";
                }

                if (processNode.getIdCheckResult() != null && processNode.getIdCheckResult() == 2) {
                    processNode.setCheckCommon("不通过：" + processNode.getCheckCommon());
                }

                if (processNode.getIdApprovalCaseNodeUser() == 0) {
                    processNode.setCaseName("提交申请");
                } else {
                    processNode.setCaseName(modelNodeName); // 业务部门审核
                }

                String realName = userMapper.selectByPrimaryKey(processNode.getSubmitIdUser()).getRealName();
                processNode.setCaseUserName(realName);

                if (processNode.getApprovalIdUser() != null) {
                    if (processNode.getApprovalIdUser() == 0) { // 没有下一环节审批人，流程结束
                        processNode.setNextCaseName("业务部门汇款");
                        processNode.setNextCaseUserName("结束");
                    } else { // 有下一环节需要判断是审批还是退回
                        String nextCaseUserName = userMapper.selectByPrimaryKey(processNode.getApprovalIdUser()).getRealName();
                        processNode.setNextCaseUserName(nextCaseUserName);

                        if (processNode.getNextIdApprovalCaseNodeUser() == 0) { // 退回
                            processNode.setNextCaseName("提交申请");
                        } else {
                            processNode.setNextCaseName(modelNodeName); // 业务部门审核
                        }
                    }
                }
            }

            Approval approval = approvalMapper.selectByPrimaryKey(idApproval);
            ApprovalCase approvalCase = approvalCaseMapper.selectApprovalCaseById(approval.getIdApprovalCase());
            ApprovalModel approvalModel = approvalModelMapper.selectByPrimaryKey(approvalCase.getIdApprovalModel());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("list", processNodeList);
            jsonObject.put("name", approvalCase.getName() + approvalModel.getName() + "审核流程");

            returnEntity = ReturnUtil.success(jsonObject);
        } catch (Exception e) {
            logger.error("查询审批流失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity verify(ApprovalProcessNode approvalProcessNode, PaymentApproval paymentApproval) {
        try {
            Approval approval = new Approval();
            if (approvalProcessNode.getIdCheckResult() != null && approvalProcessNode.getIdCheckResult() != 4) {
                Integer idApprovalCase = approval.getIdApprovalCase();
                Integer idApproval = approvalProcessNode.getIdApproval();
                approval = approvalMapper.selectByPrimaryKey(idApproval);
                Integer idApprovalModel = approvalCaseMapper.selectApprovalCaseById(approval.getIdApprovalCase()).getIdApprovalModel();

                if (approvalProcessNode.getIdCheckResult() == 1) {
                    // 判断审批金额是否为空
                    if (Tools.isEmpty(paymentApproval.getAmount()) || paymentApproval.getIdPaymentForm() == null) {
                        throw new ServiceException("获取审批参数错误");
                    }

                    ApprovalModelNode reApprovalModelNode = approvalModelNodeMapper.selectByNodeIndex(idApprovalModel, 3);
                    ApprovalCaseNodeCheckUser reApprovalCaseNodeCheckUser = approvalCaseNodeUserMapper.selectByIdApprovalCase(idApprovalCase,
                            reApprovalModelNode.getIdApprovalModelNode());

                    approvalProcessNode.setNextIdApprovalCaseNodeUser(reApprovalCaseNodeCheckUser.getIdApprovalCaseNodeUser()); // 0
                    approvalProcessNode.setApprovalIdUser(reApprovalCaseNodeCheckUser.getIdUser()); // 0

                    if (Tools.isEmpty(approvalProcessNode.getCheckCommon())) {
                        approvalProcessNode.setCheckCommon("通过");
                    }
                    approvalProcessNode.setCreateTime(Tools.date2Str(new Date()));
                    approvalProcessNodeMapper.updateSelective(approvalProcessNode);

                    approval.setCheckState(2);
                    approval.setDone(true);
                    approvalMapper.updateSelective(approval);

                    // 创建审批金额
                    paymentApproval.setCreateTime(Tools.date2Str(new Date()));
                    int count = paymentApprovalMapper.addSelective(paymentApproval);
                    if (count > 0) {
                        PaymentForm paymentForm = new PaymentForm();
                        paymentForm.setIdPaymentForm(paymentApproval.getIdPaymentForm());
                        paymentForm.setIdPaymentFormState(2);
                        paymentForm.setState(3); // 审核通过

                        paymentFormMapper.updateSelective(paymentForm);
                    }

                } else if (approvalProcessNode.getIdCheckResult() == 2) {
                    approvalProcessNode.setNextIdApprovalCaseNodeUser(0);
                    approvalProcessNode.setApprovalIdUser(approval.getIdUser());
                    if (Tools.isEmpty(approvalProcessNode.getCheckCommon())) {
                        approvalProcessNode.setCheckCommon("不通过");
                    }
                    approvalProcessNode.setCreateTime(Tools.date2Str(new Date()));
                    approvalProcessNodeMapper.updateSelective(approvalProcessNode);

                    approval.setCheckState(3);
                    approval.setDone(true);
                    approvalMapper.updateSelective(approval);

                    PaymentForm paymentForm = new PaymentForm();
                    paymentForm.setIdPaymentForm(paymentApproval.getIdPaymentForm());
                    paymentForm.setState(4); // 退回

                    paymentFormMapper.updateSelective(paymentForm);
                }
            } else {
                throw new ServiceException("参数错误");
            }
            returnEntity = ReturnUtil.success("审批成功");
        } catch (Exception e) {
            logger.error("审批请款失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity updateSelective(Approval approval) {
        try {
            int count = approvalMapper.updateSelective(approval);
            if (count > 0) {
                returnEntity = ReturnUtil.success("修改成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "修改失败");
            }
        } catch (Exception e) {
            logger.error("修改审批记录失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectByPrimaryKey(Integer idApproval) {
        try {
            Approval approval = approvalMapper.selectByPrimaryKey(idApproval);
            returnEntity = ReturnUtil.success(approval);
        } catch (Exception e) {
            logger.error("根据id获取审核记录详情失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public int approvalComm(Integer idDepartment, Integer userId, Integer idApproval, Integer idApprovalModel, String checkCommon) {
        Approval approval = new Approval();
        try {
            Integer idApprovalCase = approvalCaseMapper.selectApprovalCaseByDepartment(idApprovalModel, idDepartment);

            if (idApproval != null) {
                approval = approvalMapper.selectByPrimaryKey(idApproval);
            } else {
                approval.setCheckState(1);
                approval.setIdUser(userId);
                approval.setDone(false);
                approval.setIdApprovalCase(idApprovalCase);
                approval.setGuid(Tools.createRandom(false, 32));
                approval.setCreateTime(Tools.date2Str(new Date()));
                approvalMapper.addSelective(approval);
            }
            ApprovalModelNode approvalModelNode = approvalModelNodeMapper.selectByNodeIndex(idApprovalModel, 2);
            ApprovalCaseNodeCheckUser approvalCaseNodeCheckUser = approvalCaseNodeUserMapper.selectByIdApprovalCase(idApprovalCase,
                    approvalModelNode.getIdApprovalModelNode());

            Integer nextIdApprovalCaseNodeUser = approvalCaseNodeCheckUser.getIdApprovalCaseNodeUser(); // 下一环节操作人 （审批人）
            Integer approvalIdUser = approvalCaseNodeCheckUser.getIdUser();

            // 自动生成申请记录
            ApprovalProcessNode approvalProcessNode = new ApprovalProcessNode();
            approvalProcessNode.setCreateTime(Tools.date2Str(new Date()));
            approvalProcessNode.setIdApproval(approval.getIdApproval());
            approvalProcessNode.setSubmitIdUser(userId);
            approvalProcessNode.setIdApprovalCaseNodeUser(0);
            approvalProcessNode.setIdCheckResult(1); // 通过
            approvalProcessNode.setCheckCommon(checkCommon);
            approvalProcessNode.setNextIdApprovalCaseNodeUser(nextIdApprovalCaseNodeUser);
            approvalProcessNode.setApprovalIdUser(approvalIdUser);
            approvalProcessNodeMapper.addSelective(approvalProcessNode);

            // 生成待审批记录
            ApprovalProcessNode processNode = new ApprovalProcessNode();
            processNode.setIdApproval(approval.getIdApproval());
            processNode.setIdApprovalCaseNodeUser(nextIdApprovalCaseNodeUser); // 上一环节指定审批人
            processNode.setSubmitIdUser(approvalIdUser);
            approvalProcessNodeMapper.addSelective(processNode);

            ApprovalModelNode remittanceApprovalModelNode = approvalModelNodeMapper.selectByNodeIndex(idApprovalModel, 3);
            ApprovalCaseNodeCheckUser remittanceApprovalCaseNodeCheckUser = approvalCaseNodeUserMapper.selectByIdApprovalCase(idApprovalCase,
                    remittanceApprovalModelNode.getIdApprovalModelNode());

            Integer reNextIdApprovalCaseNodeUser = remittanceApprovalCaseNodeCheckUser.getIdApprovalCaseNodeUser(); // 下一环节操作人 (汇款人)
            Integer reApprovalIdUser = remittanceApprovalCaseNodeCheckUser.getIdUser();

            // 生成待汇款记录
            ApprovalProcessNode processRemittance = new ApprovalProcessNode();
            processRemittance.setIdApproval(approval.getIdApproval());
            processRemittance.setIdApprovalCaseNodeUser(reNextIdApprovalCaseNodeUser);
            processRemittance.setSubmitIdUser(reApprovalIdUser);
            approvalProcessNodeMapper.addSelective(processRemittance);

        } catch (Exception e) {
            logger.error("创建审批记录失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return approval.getIdApproval();
    }
}
