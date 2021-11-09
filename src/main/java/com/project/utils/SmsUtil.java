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

import java.util.Map;

public class SmsUtil {

    static Logger log = LogManager.getLogger(SmsUtil.class);

    // 产品名称:云通信短信API产品
    static final String product = "Dysmsapi";
    // 产品域名
    static final String domain = "dysmsapi.aliyuncs.com";

    static final String accessKeyId = "LTAI4G9rXumF5gBhYSv5rtbK";
    static final String accessKeySecret = "mzAhsCj0zilRF1f24XhaEwhyupjTnZ";

    public static void sendSms(String telephone, Map<String,Object> datas) throws ClientException {
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
        request.setTemplateCode("SMS_226810740");

        MessageVO pubMsg = (MessageVO) datas.get("pubMsg");
        MessageVO priMsg = (MessageVO) datas.get("priMsg");
        MessageVO generalMsg = (MessageVO) datas.get("generalMsg");
        MessageVO pubGeneralMsg = (MessageVO) datas.get("pubGeneralMsg");

        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的用户,您的验证码为${code}"时,此处的值为
        String jsonText = "{\"DATE\":\"" + pubMsg.getDate() + "\",\"card_pub\":\"" + pubMsg.getCard() + "\",\"remainingSum_pub\":\""+ pubMsg.getRemainingSum() +"\"," +
                "\"collectionAmount_pub\":\""+ pubMsg.getCollectionAmount() +"\",\"payAmount_pub\":\""+ pubMsg.getPayAmount() +"\",\"serviceCharge_pub\":\""+ pubMsg.getServiceCharge()  +"\"," +
                "\"card_pri\":\""+ priMsg.getCard() + "\",\"remainingSum_pri\":\""+ priMsg.getRemainingSum() +"\",\"collectionAmount_pri\":\"" + priMsg.getCollectionAmount() +"\"," +
                "\"payAmount_pri\":\""+ priMsg.getPayAmount() +"\",\"serviceCharge_pri\":\"" + priMsg.getServiceCharge() +"\",\"card_pub2\":\"" + generalMsg.getCard() + "\"," +
                "\"remainingSum_pub2\":\""+ generalMsg.getRemainingSum() +"\",\"collectionAmount_pub2\":\""+ generalMsg.getCollectionAmount() +"\",\"payAmount_pub2\":\""+ generalMsg.getPayAmount() +"\"," +
                "\"serviceCharge_pub2\":\""+ generalMsg.getServiceCharge() +"\",\"card_pub3\":\""+ pubGeneralMsg.getCard() +"\",\"remainingSum_pub3\":\""+ pubGeneralMsg.getRemainingSum() +"\"," +
                "\"collectionAmount_pub3\":\""+ pubGeneralMsg.getCollectionAmount() +"\",\"payAmount_pub3\":\""+ pubGeneralMsg.getPayAmount() +"\",\"serviceCharge_pub3\":\""+ pubGeneralMsg.getServiceCharge() +"\"}";

        log.debug("json ===> " + jsonText);
        request.setTemplateParam(jsonText);

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
