package com.project.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceRecord {

    private Integer BalanceRecord;

    private BigDecimal lastBalance;

    private String createTime;
}
