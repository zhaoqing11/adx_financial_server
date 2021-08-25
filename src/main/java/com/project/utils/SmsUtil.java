package com.project.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.project.entity.MessageVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SmsUtil {

    static Logger log = LogManager.getLogger(SmsUtil.class);

    // 产品名称:云通信短信API产品
    static final String product = "Dysmsapi";
    // 产品域名
    static final String domain = "dysmsapi.aliyuncs.com";

    static final String accessKeyId = "LTAI4G9rXumF5gBhYSv5rtbK";
    static final String accessKeySecret = "mzAhsCj0zilRF1f24XhaEwhyupjTnZ";

    public static void sendSms(String telephone, MessageVO messageVO) throws ClientException {
        // 超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        // 组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        // 必填:待发送手机号
        request.setPhoneNumbers(telephone);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName("安德信财务管理系统");
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_222860619");
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的用户,您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"date\":\"" + messageVO.getDate() + "\",\"card_pub\":\"" + messageVO.getCardPub() + "\",\"remainingSum_pub\":\""+ messageVO.getRemainingSumPub() +"\"," +
                "\"collectionAmount_pub\":\""+ messageVO.getCollectionAmountPub() +"\",\"payAmount_pub\":\""+ messageVO.getPayAmountPub() +"\",\"serviceCharge_pub\":\""+ messageVO.getServiceChargePub() +"\"," +
                "\"card_pri\":\""+ messageVO.getCardPri() +"\",\"remainingSum_pri\":\""+ messageVO.getRemainingSumPri() +"\",\"collectionAmount_pri\":\""+ messageVO.getCollectionAmountPri() +"\"," +
                "\"payAmount_pri\":\""+ messageVO.getPayAmountPri() +"\",\"serviceCharge_pri\":\""+ messageVO.getServiceChargePri() +"\"}");

        // 可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request.setOutId("yourOutId");
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            log.info("短信发送成功！");
        } else {
            log.info("短信发送失败！");
        }
    }

//    public static void main(String[] args) throws Exception {
//        int code = (int) (Math.random() * 9000) + 1000;
//        SendSmsResponse sendSms = sendSms("13662459025", String.valueOf(code));// 测试的手机号码
//        System.out.println("短信接口返回的数据----------------");
//        System.out.println("Code=" + sendSms.getCode()); // SMS_187944773
//        System.out.println("Message=" + sendSms.getMessage());
//        System.out.println("RequestId=" + sendSms.getRequestId());
//        System.out.println("BizId=" + sendSms.getBizId());
//    }

}
