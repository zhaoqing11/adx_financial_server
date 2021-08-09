package com.project.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.project.entity.*;
import com.project.mapper.master.*;
import com.project.service.ProjectService;
import com.project.utils.ConvertImage;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.exception.ServiceException;
import com.project.utils.pdf.PdfUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("all")
public class ProjectServiceImpl implements ProjectService {

    private Logger logger = LogManager.getLogger(ProjectServiceImpl.class);

    @Autowired
    private ReturnEntity returnEntity;

    @Autowired
    private ProjectMapper projectMapper;

//    @Autowired
//    private ProjectTypeMapper projectTypeMapper;

//    @Autowired
//    private CurtainStructureMapper curtainStructureMapper;
//
//    @Autowired
//    private ProjectFieldMapper projectFieldMapper;
//
//    @Autowired
//    private ProjectFieldValuesMapper projectFieldValuesMapper;

//    @Autowired
//    private InspectPlanMapper inspectPlanMapper;

    @Value("${project.rootFolder}")
    private String rootFolder;

    @Value("${project.imagesPath}")
    private String imagesPath;

    @Override
    public ReturnEntity selectMaxSort() {
        try {
            Project project = projectMapper.selectMaxSort();
            int num = 0;
            if (project != null && project.getSort() != null) {
                num = project.getSort();
            }
            returnEntity = ReturnUtil.success(num);
        } catch (Exception e) {
            logger.error("获取当前序号失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectSortIsExits(Integer sort) {
        try {
            int count = projectMapper.selectSortIsExits(sort);
            returnEntity = ReturnUtil.success(count);
        } catch (Exception e) {
            logger.error("获取项目序号失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectIsExitsUnCommit(Integer idInspectPlan) {
        try {
//            List<ProjectFieldValues> projectFieldValuesList = projectFieldValuesMapper.selectIsExitsUnCommit(idInspectPlan);
//
//            Map<String, Object> map = new HashMap<String, Object>();
//            Project project = projectMapper.selectProjectByIdInspectPlan(idInspectPlan);
//            map.put("idProject", project.getIdProject());
//            map.put("projectFieldValuesList", projectFieldValuesList);

//            returnEntity = ReturnUtil.success(map);
        } catch (Exception e) {
            logger.error("获取项目草稿失败：" + e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity deleteProject(Integer idProject) {
        try {
//            projectMapper.deleteProject(idProject);
//            int count = projectFieldValuesMapper.deleteProjectValues(idProject);
//            if (count > 0) {
//                returnEntity = ReturnUtil.success("删除成功");
//            } else {
//                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "删除失败");
//            }
        } catch (Exception e) {
            logger.error("删除项目失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public void exportSheet(HttpServletResponse response, Integer idProject) {
        try {
//            Map<String, List<Map<String, Object>>> map = new LinkedHashMap<String, List<Map<String, Object>>>();
//            List<ProjectField> projectFields = projectFieldMapper.selectAll();
//            Project project = projectMapper.selectByIdProject(idProject);
//            List<ProjectFieldValues> projectFieldValues = projectFieldValuesMapper.selectByIdProject(idProject);
//            if (project == null || projectFieldValues.size() < 1) {
//                throw new ServiceException("项目信息不存在！");
//            }
//
//            // 处理动态新增数据
//            List<ProjectFieldValues> newProjectFields = new ArrayList<ProjectFieldValues>();
//            if (projectFieldValues.size() > 60) { // 59
//                newProjectFields = projectFieldValues.stream().filter(s ->
//                        s.getIdProjectField() > 71).collect(Collectors.toList()); // 71
//
//                // 移除动态新增字段
//                newProjectFields.forEach(item -> {
//                    projectFieldValues.remove(item);
//                });
//            }
//
//            List<Map<String, Object>> columnField1 = new ArrayList<Map<String, Object>>();
//            List<Map<String, Object>> columnValue1 = new ArrayList<Map<String, Object>>();
//
//            List<Map<String, Object>> columnField2 = new ArrayList<Map<String, Object>>();
//            List<Map<String, Object>> columnValue2 = new ArrayList<Map<String, Object>>();
//
//            List<Map<String, Object>> columnField3 = new ArrayList<Map<String, Object>>();
//            List<Map<String, Object>> columnValue3 = new ArrayList<Map<String, Object>>();
//            for (int i = 0; i < projectFields.size(); i++) {
//                Map<String, Object> mp = new LinkedHashMap<>();
//                ProjectField field = projectFields.get(i);
//
//                String values = "";
//                // 获取对应key的value值是否存在
//                List<ProjectFieldValues> pfvList = projectFieldValues.stream().filter(s ->
//                        field.getIdProjectField() == s.getIdProjectField()).collect(Collectors.toList());
//
//                values = pfvList.size() > 0 ? pfvList.get(0).getValue() : values;
//                String files = pfvList.size() > 0 &&
//                        !StringUtils.isEmpty(pfvList.get(0).getFiles()) ? pfvList.get(0).getFiles() : ""; // 获取上传图片
//
//                if (Tools.isEmpty(values) && (field.getIdProjectField() > 42 && field.getIdProjectField() <= 55)) {
//                    continue;
//                }
//
//                // 根据position定位
//                if (!StringUtils.isEmpty(field.getPosition())) {
//                    switch (field.getPosition()) {
//                        case 1:
//                            mp.put(field.getKey() + "-key", field.getName());
//                            columnField1.add(mp);
//
//                            mp = new LinkedHashMap<>();
//
//                            mp.put(field.getKey() + "-value", formatValue(field.getIdProjectField(), values, files));
//                            columnValue1.add(mp);
//                            break;
//                        case 2:
//                            mp.put(field.getKey() + "-key", field.getName());
//                            columnField2.add(mp);
//
//                            mp = new LinkedHashMap<>();
//
//                            mp.put(field.getKey() + "-value", formatValue(field.getIdProjectField(), values, files));
//                            columnValue2.add(mp);
//                            break;
//                        case 3:
//                            mp.put(field.getKey() + "-key", field.getName());
//                            columnField3.add(mp);
//
//                            mp = new LinkedHashMap<>();
//
//                            mp.put(field.getKey() + "-value", formatValue(field.getIdProjectField(), values, files));
//                            columnValue3.add(mp);
//                            break;
//                    }
//                }
//            }
//
//            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
//            for (int i = 0; i < columnField1.size(); i++) {
//                Map<String, Object> mp = new LinkedHashMap<>();
//
//                for (String key : columnField1.get(i).keySet()) {
//                    mp.put(key, columnField1.get(i).get(key));
//                }
//                for (String key : columnValue1.get(i).keySet()) {
//                    Object value = i >= columnValue1.size() ? "" : columnValue1.get(i).get(key);
//                    mp.put(key, value);
//                }
//
//                if (i < columnField2.size()) {
//                    for (String key : columnField2.get(i).keySet()) {
//                        mp.put(key, columnField2.get(i).get(key));
//                    }
//
//                    for (String key : columnValue2.get(i).keySet()) {
//                        Object value = i >= columnValue2.size() ? "" : columnValue2.get(i).get(key);
//                        mp.put(key, value);
//                    }
//                }
//
//                if (i < columnField3.size()) {
//                    for (String key : columnField3.get(i).keySet()) {
//                        mp.put(key, columnField3.get(i).get(key));
//                    }
//
//                    for (String key : columnValue3.get(i).keySet()) {
//                        Object value = i >= columnValue3.size() ? "" : columnValue3.get(i).get(key);
//                        mp.put(key, value);
//                    }
//                }
//                dataList.add(mp);
//            }
//
//            // 设置 要求及建议 合并到 检查结果 一列
//            String val_t6 = "";
//            String val_c14 = "";
//            int i = 0, k = 0;
//            for(Map<String, Object> mp: dataList) {
//                for (Map.Entry<String, Object> tmp: mp.entrySet()) {
//                    if (tmp.getKey().equals("c14-value")) {
//                        val_c14 = tmp.getValue().toString();
//                        continue;
//                    }
//                    if (tmp.getKey().equals("t6-value")) {
//                        val_t6 = tmp.getValue().toString();
//                        break;
//                    }
//                }
//                if (Tools.isEmpty(val_c14)) {
//                    i++;
//                }
//                if (Tools.isEmpty(val_t6)) {
//                    k++;
//                }
//            }
//
//            Map<String, Object> rMap = dataList.get(i);
//            for (Map.Entry<String, Object> tmp: rMap.entrySet()) {
//                if (tmp.getKey().equals("c14-value")) {
//                    String newStr = val_c14 + ";" +val_t6;
//                    tmp.setValue(newStr);
//                    break;
//                }
//            }
//            if (dataList.size() <= k) {
//                dataList.remove(k - 1);
//            } else {
//                dataList.remove(k);
//            }
//
//            map.put(projectFieldValues.get(0).getValue(), dataList);
//            // 生成pdf文件
//            PdfUtil.createPdfDocument(response, project, map, newProjectFields);

        } catch (Exception e) {
            logger.error("导出pdf失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 格式化值
     *
     * @param idField
     * @param val
     * @return
     */
    private String formatValue(int idField, String val, String files) {
        if (StringUtils.isEmpty(val)) {
            return "";
        }
        JSONObject jsonObject = null;
        String newVal = "";
        String values = null;
        Map<String, Object> mp = new LinkedHashMap<String, Object>();
        switch (idField) {
            case 1:
            case 2:
            case 9:
            case 8:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 71:
                values = val;
                break;
            case 3:
//                String[] strArray = val.split(",");
//                Integer[] newArray = new Integer[strArray.length];
//                int i = 0;
//                for (String tmp : strArray) {
//                    newArray[i] = Integer.parseInt(tmp);
//                    i++;
//                }
//                List<CurtainStructure> structureList = curtainStructureMapper.selectById(newArray);
//                String strName = "";
//                i = 0;
//                for (CurtainStructure tmp : structureList) {
//                    strName += tmp.getName();
//                    if (i < structureList.size() - 1) {
//                        strName += ",";
//                    }
//                }
//                values = strName != null ? strName : "";
                break;
            case 4:
//                String[] str4Array = val.split(",");
//                Integer[] new4Array = new Integer[str4Array.length];
//                int k = 0;
//                for (String tmp : str4Array) {
//                    new4Array[k] = Integer.parseInt(tmp);
//                    k++;
//                }
//                List<ProjectType> projectTypeList = projectTypeMapper.selectById(new4Array);
//                String str4Name = "";
//                i = 0;
//                for (ProjectType tmp : projectTypeList) {
//                    str4Name += tmp.getName();
//                    if (i < projectTypeList.size() - 1) {
//                        str4Name += ",";
//                    }
//                }
//                values = str4Name != null ? str4Name : "";
                break;
            case 18:
            case 38:
            case 42:
                String newVal18 = "";
                switch (Integer.parseInt(val)) {
                    case 1:
                        newVal18 = "是";
                        break;
                    case 2:
                        newVal18 = "否";
                        break;
                    default:
                        newVal18 = "无";
                        break;
                }
                mp.put(newVal18, files);
                jsonObject = new JSONObject();
                newVal = jsonObject.toJSONString(mp);
                values = newVal;
                break;
            case 19:
                String newVal19 = "";
                switch (Integer.parseInt(val)) {
                    case 1:
                        newVal19 = "是";
                        break;
                    case 2:
                        newVal19 = "否";
                        break;
                    case 3:
                        newVal19 = "无";
                        break;
                    default:
                        newVal19 = "未到年限";
                        break;
                }
                mp.put(newVal19, files);
                jsonObject = new JSONObject();
                newVal = jsonObject.toJSONString(mp);
                values = newVal;
                break;
            case 20:
            case 21:
                String newVal20 = "";
                switch (Integer.parseInt(val)) {
                    case 1:
                        newVal20 = "是";
                        break;
                    case 2:
                        newVal20 = "否";
                        break;
                    default:
                        newVal20 = "未到年限";
                        break;
                }
                mp.put(newVal20, files);
                jsonObject = new JSONObject();
                newVal = jsonObject.toJSONString(mp);
                values = newVal;
                break;
            case 33:
                String newVal33 = "";
                switch (Integer.parseInt(val)) {
                    case 1:
                        newVal33 = "是（结构胶）";
                        break;
                    case 2:
                        newVal33 = "是（密封胶）";
                        break;
                    default:
                        newVal33 = "否";
                        break;
                }
                mp.put(newVal33, files);
                jsonObject = new JSONObject();
                newVal = jsonObject.toJSONString(mp);
                values = newVal;
                break;
            case 43:
                String newVal43 = "";
                String[] ids43 = val.split(",");
                for (String id : ids43) {
                    switch (Integer.parseInt(id)) {
                        case 1:
                            newVal43 += "无建筑玻璃幕墙使用维护说明书、";
                            break;
                        case 2:
                            newVal43 += "无日常巡查和维护记录、";
                            break;
                        case 3:
                            newVal43 += "无定期全面检查报告、";
                            break;
                        case 4:
                            newVal43 += "无预拉力检查和调整报告、";
                            break;
                        case 5:
                            newVal43 += "无硅酮结构密封胶粘接性能抽样报告、";
                            break;
                        case 6:
                            newVal43 += "无安全性鉴定报告、";
                            break;
                        case 7:
                            newVal43 += "无维修更换施工资料、";
                            break;
                        default:
                            newVal43 += "无突发事件应急处置预案";
                            break;
                    }
                }
                mp.put(newVal43, files);
                jsonObject = new JSONObject();
                newVal = jsonObject.toJSONString(mp);
                values = newVal;
                break;
            case 44:
                String newVal44 = "";
                String[] ids44 = val.split(",");
                for (String id : ids44) {
                    switch (Integer.parseInt(id)) {
                        case 1:
                            newVal44 += "破裂、";
                            break;
                        case 2:
                            newVal44 += "破损、";
                            break;
                        default:
                            newVal44 += "明显变形";
                            break;
                    }
                }

                mp.put(newVal44, files);
                jsonObject = new JSONObject();
                newVal = jsonObject.toJSONString(mp);
                values = newVal;
                break;
            case 45:
                String newVal45 = "";
                String[] ids45 = val.split(",");
                for (String id : ids45) {
                    switch (Integer.parseInt(id)) {
                        case 1:
                            newVal45 += "老化、";
                            break;
                        case 2:
                            newVal45 += "开裂、";
                            break;
                        case 3:
                            newVal45 += "脱落、";
                            break;
                        default:
                            newVal45 += "孔洞";
                            break;
                    }
                }

                mp.put(newVal45, files);
                jsonObject = new JSONObject();
                newVal = jsonObject.toJSONString(mp);
                values = newVal;
                break;
            case 46:
                String newVal46 = "";
                String[] ids46 = val.split(",");
                for (String id : ids46) {
                    switch (Integer.parseInt(id)) {
                        case 1:
                            newVal46 += "脱落、";
                            break;
                        case 2:
                            newVal46 += "老化、";
                            break;
                        default:
                            newVal46 += "损坏";
                            break;
                    }
                }

                mp.put(newVal46, files);
                jsonObject = new JSONObject();
                newVal = jsonObject.toJSONString(mp);
                values = newVal;
                break;
            case 49:
                String newVal49 = "";
                String[] ids49 = val.split(",");
                for (String id : ids49) {
                    switch (Integer.parseInt(id)) {
                        case 1:
                            newVal49 += "松动、";
                            break;
                        case 2:
                            newVal49 += "变形、";
                            break;
                        default:
                            newVal49 += "错位";
                            break;
                    }
                }

                mp.put(newVal49, files);
                jsonObject = new JSONObject();
                newVal = jsonObject.toJSONString(mp);
                values = newVal;
                break;
            case 50:
                String newVal50 = "";
                String[] ids50 = val.split(",");
                for (String id : ids50) {
                    switch (Integer.parseInt(id)) {
                        case 1:
                            newVal50 += "松动、";
                            break;
                        case 2:
                            newVal50 += "变形、";
                            break;
                        case 3:
                            newVal50 += "错位、";
                            break;
                        default:
                            newVal50 += "锈蚀";
                            break;
                    }
                }

                mp.put(newVal50, files);
                jsonObject = new JSONObject();
                newVal = jsonObject.toJSONString(mp);
                values = newVal;
                break;
            case 51:
                String newVal51 = "";
                String[] ids51 = val.split(",");
                for (String id : ids51) {
                    switch (Integer.parseInt(id)) {
                        case 1:
                            newVal51 += "预埋件连接处、";
                            break;
                        default:
                            newVal51 += "钢结构构件";
                            break;
                    }
                }

                mp.put(newVal51, files);
                jsonObject = new JSONObject();
                newVal = jsonObject.toJSONString(mp);
                values = newVal;
                break;
            case 53:
                String newVal53 = "";
                String[] ids53 = val.split(",");
                for (String id : ids53) {
                    switch (Integer.parseInt(id)) {
                        case 1:
                            newVal53 += "松动、";
                            break;
                        case 2:
                            newVal53 += "脱落、";
                            break;
                        case 3:
                            newVal53 += "损坏、";
                            break;
                        default:
                            newVal53 += "功能障碍";
                            break;
                    }
                }

                mp.put(newVal53, files);
                jsonObject = new JSONObject();
                newVal = jsonObject.toJSONString(mp);
                values = newVal;
                break;
            case 54:
                String newVal54 = "";
                String[] ids54 = val.split(",");
                for (String id : ids54) {
                    switch (Integer.parseInt(id)) {
                        case 1:
                            newVal54 += "明显变形、";
                            break;
                        case 2:
                            newVal54 += "刮窗、";
                            break;
                        default:
                            newVal54 += "启闭不畅";
                            break;
                    }
                }

                mp.put(newVal54, files);
                jsonObject = new JSONObject();
                newVal = jsonObject.toJSONString(mp);
                values = newVal;
                break;
            case 56:
                String newVal56 = "";
                String[] ids56 = val.split(",");
                switch (Integer.parseInt(ids56[0])) {
                    case 1:
                        newVal56 += "管理档案资料齐全、";
                        break;
                    case 2:
                        newVal56 += "管理档案资料有缺失、";
                        break;
                    default:
                        newVal56 += "管理档案资料缺失严重、";
                        break;
                }
                switch (Integer.parseInt(ids56[1])) {
                    case 1:
                        newVal56 += "维护保养及时到位、";
                        break;
                    case 2:
                        newVal56 += "存在安全隐患、";
                        break;
                    default:
                        newVal56 += "缺乏必要维护保养，存在明显安全隐患、";
                        break;
                }
                switch (Integer.parseInt(ids56[2])) {
                    case 1:
                        newVal56 += "幕墙安全管理规范可控。";
                        break;
                    case 2:
                        newVal56 += "幕墙安全管理需加强改进。";
                        break;
                    default:
                        newVal56 += "幕墙安全缺乏管理应立即改进。";
                        break;
                }
                values = newVal56;
                break;
            default:
                values = Integer.parseInt(val) == 1 ? "是" : "否";
                mp.put(values, files);
                jsonObject = new JSONObject();
                newVal = jsonObject.toJSONString(mp);
                values = newVal;
                break;
        }
        return values;
    }

    @Override
    public ReturnEntity getProjectTypeList() {
        try {
//            List<ProjectType> projectTypeList = projectTypeMapper.getProjectTypeList();
//            returnEntity = ReturnUtil.success(projectTypeList);
        } catch (Exception e) {
            logger.error("获取玻璃类型列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity getCurtainStructureList() {
        try {
//            List<CurtainStructure> curtainStructureList = curtainStructureMapper.getCurtainStructureList();
//            returnEntity = ReturnUtil.success(curtainStructureList);
        } catch (Exception e) {
            logger.error("获取玻璃幕墙结构失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectAllProjects() {
        try {
            List<Project> projectList = projectMapper.selectAll();
            returnEntity = ReturnUtil.success(projectList);
        } catch (Exception e) {
            logger.error("获取项目列表失败：" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectProjectFieldAll() {
        try {
//            List<ProjectField> projectFieldList = projectFieldMapper.selectAll();
//            returnEntity = ReturnUtil.success(projectFieldList);
        } catch (Exception e) {
            logger.error("获取项目字典列表失败：" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

//    @Override
//    public ReturnEntity insertSelective(List<ProjectFieldValues> projectFieldValuesList, Project project) {
//        List<ProjectFieldValues> addFieldValues = projectFieldValuesList.stream().filter(s ->
//                s.getIdProjectField() == 72).collect(Collectors.toList()); // 71
//        projectFieldValuesList.remove(60); // 59
//
//        // 处理动态新增子项
//        if (addFieldValues.size() > 0 &&
//                !Tools.isEmpty(addFieldValues.get(0).getValue())) {
//            List<FieldValuesVO> fieldValuesVOS = JSONArray.parseArray(addFieldValues.get(0).getValue(), FieldValuesVO.class);
//            for (FieldValuesVO item : fieldValuesVOS) {
//                Project project1 = new Project();
//
//                ProjectField ptField = new ProjectField();
//                ptField.setName(item.getTitle());
//                ptField.setType("text");
//                ptField.setPosition(1);
//                projectFieldMapper.insertSeleative(ptField);
//
//                ProjectFieldValues fieldValues = new ProjectFieldValues();
//                fieldValues.setValue(item.getValue());
//                fieldValues.setIdProjectField(ptField.getIdProjectField());
//                projectFieldValuesList.add(fieldValues);
//            }
//        }
//
//        if (project.getIdProject() > -1) { // 修改
//            projectMapper.updateSelective(project);
//            for (ProjectFieldValues item : projectFieldValuesList) {
//                if (item.getIdProject() == null) {
//                    item.setIdProject(project.getIdProject());
//                }
//                if (project.getIdProjectState() == 2) {
//                    if (item.getIdProjectField() == 1 && Tools.isEmpty(item.getValue())) {
//                        throw new ServiceException("请填写项目名称");
//                    }
//                    if (item.getIdProjectField() == 65 && Tools.isEmpty(item.getValue())) {
//                        throw new ServiceException("请填写项目地址");
//                    }
//                    if (item.getIdProjectField() == 3 && Tools.isEmpty(item.getValue())) {
//                        throw new ServiceException("请选择玻璃幕墙结构");
//                    }
//                    if (item.getIdProjectField() == 4 && Tools.isEmpty(item.getValue())) {
//                        throw new ServiceException("请选择主要玻璃类型");
//                    }
//                }
//                // 过滤签名字段，将base64转换为图片
//                if (item.getIdProjectField() > 56 && item.getIdProjectField() <= 61) {
//                    if (Tools.notEmpty(item.getValue())) {
//                        String[] strArray = item.getValue().split("-");
//
//                        List<String> fileList = new ArrayList<String>();
//                        for (String img : strArray) {
//                            if (img.length() > 36) {
//                                String file = ConvertImage.GenerateImage(img, rootFolder + imagesPath);
//                                String sb = file.substring(2, file.length() - 2);
//                                fileList.add(sb);
//                            } else {
//                                fileList.add(img);
//                            }
//                        }
//                        if (fileList.size() > 0) {
//                            org.json.JSONArray jsonArray = new org.json.JSONArray(fileList);
//                            item.setValue(jsonArray.toString());
//                        } else {
//                            item.setValue(null);
//                        }
//                    }
//                }
//                if (item.getIdProjectFieldValue() != null) {
//                    projectFieldValuesMapper.updateSelective(item);
//                } else {
//                    projectFieldValuesMapper.insertSelective(item); // 防止修改信息后新增了某些属性
//                }
//            }
//
//            if (project.getIdProjectState() == 2) {
//                InspectPlan inspectPlan = new InspectPlan();
//                inspectPlan.setIdInspectPlan(project.getIdInspectPlan());
//                inspectPlan.setIdInspectState(2); // 设置为“已提交”
//                inspectPlanMapper.updateSelective(inspectPlan);
//
//                // 更新监管平台数据库对应项目
//                updateProject(projectFieldValuesList, project.getIdProject());
//            }
//            returnEntity = ReturnUtil.success(project.getIdProject());
//        } else { // 新增
//            try {
//                    projectFieldValuesList.forEach(item -> {
//                        item.setIdProject(project.getIdProject());
//                    });
//                    // 过滤多选题图片及签名字段，将base64转换为图片
//                    List<ProjectFieldValues> signtureProjectValues = projectFieldValuesList.stream().filter(s ->
//                            s.getIdProjectField() > 56 && s.getIdProjectField() <= 61).collect(Collectors.toList());
//                    signtureProjectValues.forEach(item -> {
//                        if (null != item.getValue() && item.getIdProjectField() != 56) {
//                            String file = ConvertImage.GenerateImage(item.getValue(), rootFolder + imagesPath);
//                            item.setValue(file);
//                        }
//                    });
//                    int fieldValCount = projectFieldValuesMapper.addProjectFieldValues(projectFieldValuesList);
//                    if (fieldValCount >= 1) {
//                        if (project.getIdProjectState() == 2) {
//                            // 更新监管平台数据库对应项目
//                            updateProject(projectFieldValuesList, project.getIdProject());
//                        }
//                        returnEntity = ReturnUtil.success(project.getIdProject());
//                    } else {
//                        returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "新增失败");
//                    }
//            } catch (Exception e) {
//                logger.error("新增项目失败，错误消息：--->" + e.getMessage());
//                throw new ServiceException(e.getMessage());
//            }
//        }
//        return returnEntity;
//    }

//    private void updateProject(List<ProjectFieldValues> projectFieldValuesList, Integer idProject) {
//        logger.info("开始更新广州市既有幕墙巡查平台数据...");
//        List<ProjectFieldValues> projectName = projectFieldValuesList.stream().filter(s ->
//                s.getIdProjectField() == 1).collect(Collectors.toList());
//
//        if (projectName.size() < 1 || Tools.isEmpty(projectName.get(0).getValue())) {
//            return;
//        }
//
//        List<ProjectFieldValues> curtainHeight = projectFieldValuesList.stream().filter(s ->
//                s.getIdProjectField() == 8).collect(Collectors.toList());
//
//        List<ProjectFieldValues> area = projectFieldValuesList.stream().filter(s ->
//                s.getIdProjectField() == 9).collect(Collectors.toList());
//
//        List<ProjectFieldValues> mainCompletion = projectFieldValuesList.stream().filter(s ->
//                s.getIdProjectField() == 10).collect(Collectors.toList());
//
//        List<ProjectFieldValues> owners = projectFieldValuesList.stream().filter(s ->
//                s.getIdProjectField() == 11).collect(Collectors.toList());
//
//        List<ProjectFieldValues> ownersPhone = projectFieldValuesList.stream().filter(s ->
//                s.getIdProjectField() == 63).collect(Collectors.toList());
//
//        List<ProjectFieldValues> personMaintenance = projectFieldValuesList.stream().filter(s ->
//                s.getIdProjectField() == 13).collect(Collectors.toList());
//
//        List<ProjectFieldValues> personMaintenancePhone = projectFieldValuesList.stream().filter(s ->
//                s.getIdProjectField() == 64).collect(Collectors.toList());
//
//        List<ProjectFieldValues> address = projectFieldValuesList.stream().filter(s ->
//                s.getIdProjectField() == 65).collect(Collectors.toList());
//
//        logger.info("数据更新成功...");
//    }

    @Override
    public ReturnEntity selectByIdProject(Integer idProject) {
        try {
//            Project project = projectMapper.selectByIdProject(idProject);
//            List<ProjectFieldValues> projectFieldValues = projectFieldValuesMapper.selectByIdProject(idProject);
//
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("project", project);
//            map.put("projectFieldValueList", projectFieldValues);
//            returnEntity = ReturnUtil.success(map);
        } catch (Exception e) {
            logger.error("根据id查询项目信息失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }
}
