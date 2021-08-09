package com.project.utils.common.base;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.project.utils.Tools;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SuppressWarnings("all")
public class HttpServletRquestUtil {
    /**
     * 要放入request中的参数名称
     */
    private static final String OPERANDS_NAME="operandsName";
    private static final String LOG_PRJ_ID = "logPrjId";
    private static final String OPERAND_TARGET = "operandTarget";
    /**
     * 获取HttpServletRquest 对象
     */
    private static HttpServletRequest getHttpServletRequest(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder
                .getRequestAttributes())
                .getRequest();
        return request;
    }
    /**
     * 写值到HttpServletRquest对象中
     * @param operandsName 要放入的值
     */
    public static void setOperandsNameHttpServletRequest(String operandsName){
        getHttpServletRequest().setAttribute(OPERANDS_NAME, operandsName);
    }
    /**
     * 获取HttpServletRquest对象中对应的值
     * @return 对应值
     */
    public static String getOperandsNameByHttpServletRequest(){
        String name = getHttpServletRequest().getAttribute(OPERANDS_NAME )== null ? "" : getHttpServletRequest().getAttribute(OPERANDS_NAME ).toString();
        return name;
    }

    /**
     * 写值到HttpServletRquest对象中
     * @param logPrjId  项目Id
     */
    public static void setLogPrjIdHttpServletRequest(Long logPrjId){
        getHttpServletRequest().setAttribute(LOG_PRJ_ID, logPrjId);
    }

    /**
     * 获取HttpServletRquest对象中对应的值
     * @return 对应值
     */
    public static Long getLogPrjIdHttpServletRequest(){
        Long logPrjId = getHttpServletRequest().getAttribute(LOG_PRJ_ID) == null ? null : Long.parseLong(getHttpServletRequest().getAttribute(LOG_PRJ_ID).toString());
        return logPrjId;
    }

    /**
     * 写值到HttpServletRquest对象中
     * @param operandsName 要放入的值
     */
    public static void setOperandTargetHttpServletRequest(String operandTarget){
        getHttpServletRequest().setAttribute(OPERAND_TARGET, operandTarget);
    }
    /**
     * 获取HttpServletRquest对象中对应的值
     * @return 对应值
     */
    public static String getOperandTargetByHttpServletRequest(){
        String name = getHttpServletRequest().getAttribute(OPERAND_TARGET )== null ? "" : getHttpServletRequest().getAttribute(OPERAND_TARGET).toString();
        return name;
    }

    /**

     * <p>Title: getUserInfo</p>

     * <p>Description: 获取缓存用户信息</p>

     * @param getParam 要获取用户的那个信息
     * @return

     */
    public static String getCacheUserInfo(String key) {
        HttpServletRequest request = getHttpServletRequest();
        Map<String, String[]> paramMap = request.getParameterMap();
        if(null != paramMap) {
            String [] userIds = paramMap.get(key);
            if(null != userIds) {
                if(Tools.notEmpty(userIds[0])) {
                    return userIds[0];
                }
            }
        }
        return null;
    }
}