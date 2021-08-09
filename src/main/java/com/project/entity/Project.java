package com.project.entity;

import lombok.Data;

@Data
public class Project {

    private Integer idProject;

    private String code;

    private Integer idProjectState;

    private String createTime;

    private String oldCode;

    private Integer sort;

    /** project_field_values */

    private Integer idProjectFieldValue;

    private String projectName;

    private String checkDate;

    /** inspect_plan */

    private Integer idInspectPlan;

}
