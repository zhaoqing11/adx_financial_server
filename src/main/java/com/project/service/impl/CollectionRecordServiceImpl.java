package com.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.project.entity.*;
import com.project.mapper.master.*;
import com.project.service.CollectionRecordService;
import com.project.utils.ReturnUtil;
import com.project.utils.Tools;
import com.project.utils.common.PageBean;
import com.project.utils.common.base.HttpCode;
import com.project.utils.common.base.ReturnEntity;
import com.project.utils.common.base.enums.CardType;
import com.project.utils.common.exception.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@SuppressWarnings("all")
public class CollectionRecordServiceImpl implements CollectionRecordService {

    static private Logger logger = LogManager.getLogger(CollectionRecordServiceImpl.class);

    @Autowired
    private PublicDailyMapper publicDailyMapper;

    @Autowired
    private PrivateDailyMapper privateDailyMapper;

    @Autowired
    private GeneralAccountDailyMapper generalAccountDailyMapper;

    @Autowired
    private SecondGeneralAccountDailyMapper secondGeneralAccountDailyMapper;

    @Autowired
    private PubGeneralDailyMapper pubGeneralDailyMapper;

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private IncomeFlowRecordMapper incomeFlowRecordMapper;

    @Autowired
    private CollectionRecordMapper collectionRecordMapper;

    @Autowired
    private ReturnEntity returnEntity;

    @Override
    public ReturnEntity updateCollectionRecord(Integer idCardType, Integer idDaily) {
        try {
            if (idCardType == CardType.ACCOUNT_TYPE_1) {
                PublicDaily publicDaily = new PublicDaily();
                publicDaily.setIdPublicDaily(idDaily);
                publicDaily.setState(0); // 状态置为“待审核”
                publicDailyMapper.updateSelective(publicDaily);
            } else if (idCardType == CardType.ACCOUNT_TYPE_2) {
                PrivateDaily privateDaily = new PrivateDaily();
                privateDaily.setIdPrivateDaily(idDaily);
                privateDaily.setState(0); // 状态置为“待审核”
                privateDailyMapper.updateSelective(privateDaily);
            } else if (idCardType == CardType.ACCOUNT_TYPE_3) {
                GeneralAccountDaily generalAccountDaily = new GeneralAccountDaily();
                generalAccountDaily.setIdGeneralAccountDaily(idDaily);
                generalAccountDaily.setState(0); // 状态置为“待审核”
                generalAccountDailyMapper.updateSelective(generalAccountDaily);
            } else if (idCardType == CardType.ACCOUNT_TYPE_4) {
                SecondGeneralAccountDaily generalAccountDaily = new SecondGeneralAccountDaily();
                generalAccountDaily.setIdSecondGeneralAccountDaily(idDaily);
                generalAccountDaily.setState(0); // 状态置为“待审核”
                secondGeneralAccountDailyMapper.updateSelective(generalAccountDaily);
            } else if (idCardType == CardType.ACCOUNT_TYPE_5) {
                PubGeneralDaily daily = new PubGeneralDaily();
                daily.setIdPubGeneralDaily(idDaily);
                daily.setState(0); // 状态置为“待审核”
                pubGeneralDailyMapper.updateSelective(daily);
            }
            returnEntity = ReturnUtil.success("编辑成功");
        } catch (Exception e) {
            logger.error("修改审核状态失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity addCollectionRecord(CollectionRecord collectionRecord, Integer idCardType, Integer idDaily) {
        try {
            collectionRecord.setIdCardType(idCardType);
            boolean flag = addCollectionRcord(collectionRecord);
            if (flag) {
                if (idCardType == CardType.ACCOUNT_TYPE_1) {
                    PublicDaily publicDaily = publicDailyMapper.selectByPrimaryKey(idDaily);

                    BigDecimal oldAmount = new BigDecimal(publicDaily.getCollectionAmount());
                    BigDecimal newAmount = new BigDecimal(collectionRecord.getAmount());
                    BigDecimal amountTotal = oldAmount.add(newAmount);

                    BigDecimal oldRemaingSum = new BigDecimal(publicDaily.getRemainingSum());
                    BigDecimal remainingTotal = oldRemaingSum.add(newAmount);

                    publicDaily.setCollectionAmount(String.valueOf(amountTotal));
                    publicDaily.setRemainingSum(String.valueOf(remainingTotal));

                    publicDailyMapper.updateSelective(publicDaily);
                } else if (idCardType == CardType.ACCOUNT_TYPE_2) {
                    PrivateDaily privateDaily = privateDailyMapper.selectByPrimaryKey(idDaily);

                    BigDecimal oldAmount = new BigDecimal(privateDaily.getCollectionAmount());
                    BigDecimal newAmount = new BigDecimal(collectionRecord.getAmount());
                    BigDecimal amountTotal = oldAmount.add(newAmount);

                    BigDecimal oldRemaingSum = new BigDecimal(privateDaily.getRemainingSum());
                    BigDecimal remainingTotal = oldRemaingSum.add(newAmount);

                    privateDaily.setCollectionAmount(String.valueOf(amountTotal));
                    privateDaily.setRemainingSum(String.valueOf(remainingTotal));

                    privateDailyMapper.updateSelective(privateDaily);
                } else if (idCardType == CardType.ACCOUNT_TYPE_3) {
                    GeneralAccountDaily generalAccountDaily = generalAccountDailyMapper.selectByPrimaryKey(idDaily);

                    BigDecimal oldAmount = new BigDecimal(generalAccountDaily.getCollectionAmount());
                    BigDecimal newAmount = new BigDecimal(collectionRecord.getAmount());
                    BigDecimal amountTotal = oldAmount.add(newAmount);

                    BigDecimal oldRemaingSum = new BigDecimal(generalAccountDaily.getRemainingSum());
                    BigDecimal remainingTotal = oldRemaingSum.add(newAmount);

                    generalAccountDaily.setCollectionAmount(String.valueOf(amountTotal));
                    generalAccountDaily.setRemainingSum(String.valueOf(remainingTotal));

                    generalAccountDailyMapper.updateSelective(generalAccountDaily);
                } else if (idCardType == CardType.ACCOUNT_TYPE_4) {
                    SecondGeneralAccountDaily secondGeneralAccountDaily = secondGeneralAccountDailyMapper.selectByPrimaryKey(idDaily);

                    BigDecimal oldAmount = new BigDecimal(secondGeneralAccountDaily.getCollectionAmount());
                    BigDecimal newAmount = new BigDecimal(collectionRecord.getAmount());
                    BigDecimal amountTotal = oldAmount.add(newAmount);

                    BigDecimal oldRemaingSum = new BigDecimal(secondGeneralAccountDaily.getRemainingSum());
                    BigDecimal remainingTotal = oldRemaingSum.add(newAmount);

                    secondGeneralAccountDaily.setCollectionAmount(String.valueOf(amountTotal));
                    secondGeneralAccountDaily.setRemainingSum(String.valueOf(remainingTotal));

                    secondGeneralAccountDailyMapper.updateSelective(secondGeneralAccountDaily);
                } else if (idCardType == CardType.ACCOUNT_TYPE_5) {
                    PubGeneralDaily daily = pubGeneralDailyMapper.selectByPrimaryKey(idDaily);

                    BigDecimal oldAmount = new BigDecimal(daily.getCollectionAmount());
                    BigDecimal newAmount = new BigDecimal(collectionRecord.getAmount());
                    BigDecimal amountTotal = oldAmount.add(newAmount);

                    BigDecimal oldRemaingSum = new BigDecimal(daily.getRemainingSum());
                    BigDecimal remainingTotal = oldRemaingSum.add(newAmount);

                    daily.setCollectionAmount(String.valueOf(amountTotal));
                    daily.setRemainingSum(String.valueOf(remainingTotal));

                    pubGeneralDailyMapper.updateSelective(daily);
                }
                returnEntity = ReturnUtil.success("编辑成功");
            } else {
                returnEntity = ReturnUtil.success("创建失败");
            }
        } catch (Exception e) {
            logger.error("添加收款失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

    @Override
    public ReturnEntity addSelective(CollectionRecord collectionRecord) {
        try {
            boolean flag = addCollectionRcord(collectionRecord);
            if (flag) {
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

    private boolean addCollectionRcord(CollectionRecord collectionRecord) {
        collectionRecord.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
        int count = collectionRecordMapper.addSelective(collectionRecord);
        if (count > 0) {
            // 创建支出流水记录
            Integer idCollectionRecord = collectionRecord.getIdCollectionRecord();
            Config config = configMapper.selectConfigInfo(collectionRecord.getIdCardType());
            ConfigVO configVO = JSONObject.parseObject(config.getConfig(), ConfigVO.class);

            BigDecimal remainingSum = new BigDecimal(configVO.getRemainingSum()); // 余额
            BigDecimal amount = new BigDecimal(collectionRecord.getAmount()); // 收款金额

            BigDecimal money = remainingSum.add(amount); // 剩余余额

            IncomeFlowRecord flowRecord = new IncomeFlowRecord();
            flowRecord.setIdCollectionRecord(idCollectionRecord);
            flowRecord.setAmount(collectionRecord.getAmount());
            flowRecord.setRemainingSum(String.valueOf(money));
            flowRecord.setCreateTime(Tools.date2Str(new Date(), "yyyy-MM-dd HH:mm:ss"));
            incomeFlowRecordMapper.addSelective(flowRecord);

            // 修改config文件余额变量
            configVO.setRemainingSum(String.valueOf(money));
            config.setConfig(JSONObject.toJSONString(configVO));

            configMapper.updateConfig(config);

            return true;
        } else {
            return false;
        }
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
    public ReturnEntity selectByPage(Integer startIndex, Integer pageSize, CollectionRecord collectionRecord) {
        try {
            startIndex = startIndex == null ? 0 : startIndex;
            pageSize = pageSize == null ? 0 : pageSize;

            String startTime = collectionRecord.getStartTime();
            String endTime = collectionRecord.getEndTime();

            startTime = Tools.notEmpty(startTime) ? Tools.date2Str(Tools.str2Date(startTime), "yyyy-MM-dd") : startTime;
            endTime = Tools.notEmpty(endTime) ? Tools.date2Str(Tools.str2Date(endTime), "yyyy-MM-dd") : endTime;

            int total = collectionRecordMapper.selectByPageTotal(startTime, endTime, collectionRecord.getIdCardType());
            PageBean<CollectionRecord> pageBean = new PageBean<CollectionRecord>(startIndex, pageSize, total);
            List<CollectionRecord> collectionRecordList = collectionRecordMapper.selectByPage(pageBean.getStartIndex(),
                    pageBean.getPageSize(), collectionRecord);

            pageBean.setList(collectionRecordList);
            returnEntity = ReturnUtil.success(pageBean);
        } catch (Exception e) {
            logger.error("分页（条件）查询收款列表失败，错误消息：--->" + e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return returnEntity;
    }

}
