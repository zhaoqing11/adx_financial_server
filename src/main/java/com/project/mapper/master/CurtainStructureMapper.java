package com.project.mapper.master;

import java.util.List;

public interface CurtainStructureMapper {

    List<CurtainStructure> getCurtainStructureList();

    List<CurtainStructure> selectById(Integer[] idCurtainStructures); // @Param("idCurtainStructure") Integer idCurtainStructure

}
