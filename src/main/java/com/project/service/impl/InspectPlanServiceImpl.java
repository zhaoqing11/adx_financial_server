package com.project.service.impl;

import com.project.entity.Project;
import com.project.mapper.master.InspectPlanMapper;
import com.project.mapper.master.ProjectMapper;
import com.project.service.InspectPlanService;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("All")
public class InspectPlanServiceImpl implements InspectPlanService {

    private static Logger logger = LogManager.getLogger(InspectPlanServiceImpl.class);

    @Autowired
    private static ProjectMapper projectMapper;

    @Autowired
    private static InspectPlanMapper inspectPlanMapper;

    @Autowired
    private static ReturnEntity returnEntity;

//    public InspectPlanServiceImpl() {
//    }

//    @Autowired
//    public InspectPlanServiceImpl(ProjectMapper projectMapper) {
//        InspectPlanServiceImpl.projectMapper = projectMapper;
//    }
//
//    @Autowired
//    public InspectPlanServiceImpl(InspectPlanMapper inspectPlanMapper) {
//        InspectPlanServiceImpl.inspectPlanMapper = inspectPlanMapper;
//    }
//
//    @Autowired
//    public InspectPlanServiceImpl(InspectionGroupMapper inspectionGroupMapper) {
//        InspectPlanServiceImpl.inspectionGroupMapper = inspectionGroupMapper;
//    }

//    public static synchronized ReturnEntity addInspectPlan(InspectPlan inspectPlan) {
//        try {
//            Thread.sleep(300);
//        } catch (InterruptedException e) {
//            throw new ServiceException(e.getMessage());
//        }
//        return ReturnUtil.success("新增抽查检查计划成功");
//    }

    @Override
    public ReturnEntity insertSelective(InspectPlan inspectPlan) {
        try {
            inspectPlan.setIdInspectState(1); // 设置为“未提交”
            inspectPlanMapper.insertSelective(inspectPlan);

            Project project = new Project();
            String code = Tools.date2Str(new Date(), "yyyyMMddHHmmss");
            project.setCode(code);
            project.setIdInspectPlan(inspectPlan.getIdInspectPlan());
            project.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            projectMapper.addProject(project);

            returnEntity = ReturnUtil.success("新增抽查检查计划成功");
        } catch (Exception e) {
            logger.error("新增抽查检查计划失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }
}
