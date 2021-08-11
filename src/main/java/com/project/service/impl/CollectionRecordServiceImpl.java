package com.project.service.impl;

import com.project.entity.CollectionRecord;
import com.project.mapper.master.CollectionRecordMapper;
import com.project.service.CollectionRecordService;
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

import java.util.Date;
import java.util.List;

@Service
@SuppressWarnings("all")
public class CollectionRecordServiceImpl implements CollectionRecordService {

    static private Logger logger = LogManager.getLogger(CollectionRecordServiceImpl.class);

    @Autowired
    private CollectionRecordMapper collectionRecordMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity addSelective(CollectionRecord collectionRecord) {
        try {
            collectionRecord.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            int count = collectionRecordMapper.addSelective(collectionRecord);
            if (count > 0) {
                returnEntity = ReturnUtil.success("新增成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "新增失败");
            }
        } catch (Exception e) {
            logger.error("新增收款记录失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity updateSelective(CollectionRecord collectionRecord) {
        try {
            int count = collectionRecordMapper.updateSelective(collectionRecord);
            if (count > 0) {
                returnEntity = ReturnUtil.success("编辑成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "编辑失败");
            }
        } catch (Exception e) {
            logger.error("修改收款记录失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity deleteSelective(Integer idCollectionRecord) {
        try {
            int count = collectionRecordMapper.deleteSelective(idCollectionRecord);
            if (count > 0) {
                returnEntity = ReturnUtil.success("删除成功");
            } else {
                returnEntity = ReturnUtil.validError(HttpCode.CODE_500, "删除失败");
            }
        } catch (Exception e) {
            logger.error("删除收款记录失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity selectByPage(Integer startIndex, Integer pageSize, String startTime, String endTime) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            int total = collectionRecordMapper.selectByPageTotal(startTime, endTime);
            PageBean<CollectionRecord> pageBean = new PageBean<CollectionRecord>(startIndex, pageSize, total);
            List<CollectionRecord> collectionRecordList = collectionRecordMapper.selectByPage(pageBean.getStartIndex(), pageBean.getPageSize(),
                    startTime, endTime);

            pageBean.setList(collectionRecordList);
            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("分页（条件）查询收款列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
