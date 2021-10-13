package com.project.mapper.master;

import com.project.entity.CardType;

import java.util.List;

public interface CardTypeMapper {

    int insertSelective(CardType cardType);

    List<CardType> selectAll();

}
