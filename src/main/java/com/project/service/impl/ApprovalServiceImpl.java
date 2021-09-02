package com.project.service.impl;

import com.project.entity.Approval;
import com.project.entity.ApprovalCaseNodeCheckUser;
import com.project.entity.ApprovalModelNode;
import com.project.entity.ApprovalProcessNode;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("all")
public class ApprovalServiceImpl implements ApprovalService {

    private static Logger logger = LogManager.getLogger(ApprovalServiceImpl.class);

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
            // Todo: test
            List<ApprovalProcessNode> processNodeList = approvalProcessNodeMapper.selectProcessNodeByIdApproval(idApproval);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("processNodeList", processNodeList);

            returnEntity = ReturnUtil.success(map);
        } catch (Exception e) {
            logger.error("" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity verify(ApprovalProcessNode approvalProcessNode) {
        try {
            Approval approval = new Approval();
            if (approvalProcessNode.getIdCheckResult() != null && approvalProcessNode.getIdCheckResult() != 4) {
                Integer idApprovalCase = approval.getIdApprovalCase();
                Integer idApproval = approvalProcessNode.getIdApproval();
                approval = approvalMapper.selectByPrimaryKey(idApproval);
                Integer idApprovalModel = approvalCaseMapper.selectApprovalCaseById(idApprovalCase).getIdApprovalModel();

                if (approvalProcessNode.getIdCheckResult() == 1) {
                    approvalProcessNode.setNextIdApprovalCaseNodeUser(0);
                    approvalProcessNode.setApprovalIdUser(0);
                    if (Tools.isEmpty(approvalProcessNode.getCheckCommon())) {
                        approvalProcessNode.setCheckCommon("通过");
                    }
                    approvalProcessNode.setCreateTime(Tools.date2Str(new Date()));
                    approvalProcessNodeMapper.updateSelective(approvalProcessNode);

                    approval.setCheckState(2);
                    approval.setDone(true);
                    approvalMapper.updateSelective(approval);

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
                }
            } else {
                throw new ServiceException("参数错误");
            }
        } catch (Exception e) {
            logger.error("" + e.getMessage());
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

            Integer nextIdApprovalCaseNodeUser = approvalCaseNodeCheckUser.getIdApprovalCaseNodeUser(); // 下一环节操作人
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

        } catch (Exception e) {
            logger.error("" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return approval.getIdApproval();
    }
}
