package com.project.utils.excel;

public enum FontEnum {

    /**  字体类型  */
    FONT_TYPE1("FONT_TYPE1", "宋体"),

    FONT_TYPE2("FONT_TYPE2", "黑体"),

    FONT_TYPE3("FONT_TYPE3", "楷体"),

    FONT_TYPE4("FONT_TYPE4", "等线"),

    FONT_TYPE5("FONT_TYPE5", "隶书"),

    FONT_TYPE6("FONT_TYPE6", "微软雅黑");

    /** 字体大小 */
    public static final int FONT_SIZE = 11;
    public static final int FONT_SIZE1 = 12;
    public static final int FONT_SIZE2 = 14;
    public static final int FONT_SIZE3 = 16;
    public static final int FONT_SIZE4 = 18;
    public static final int FONT_SIZE5 = 20;
    public static final int FONT_SIZE6 = 22;

    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    private FontEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

}
