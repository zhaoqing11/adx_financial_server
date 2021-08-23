package com.project.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 格式化金额工具类
 *
 * @author admin
 */
public class DecimalFormatUtil {

    public static final String DEFAULT_FORMAT = "#,###.00";

    public static final String FORMAT_NUM_TWO = "#.00";

    private DecimalFormatUtil() {
    }

    /**
     * 格式化数字表示
     *
     * @param bigDecimal
     * @param format
     * @return
     */
    public static String formatString(BigDecimal bigDecimal, String format) {
        if (bigDecimal == null) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = DEFAULT_FORMAT;
        }
        DecimalFormat df = new DecimalFormat(format);
        return df.format(bigDecimal.doubleValue());
    }

    /**
     * 格式化数字表示
     *
     * @param bigDecimal
     * @param format
     * @return
     */
    public static String formatString2(BigDecimal bigDecimal, String format) {
        if (bigDecimal == null) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = FORMAT_NUM_TWO;
        }
        DecimalFormat df = new DecimalFormat(format);
        return df.format(bigDecimal.doubleValue());
    }

}
