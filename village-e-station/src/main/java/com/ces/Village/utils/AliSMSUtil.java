package com.ces.Village.utils;

import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.ces.Village.properties.AliSMSProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 阿里云短信服务助手
 */
@Component
@Log4j2
public class AliSMSUtil {
    private final AliSMSProperties aliSMS;
    private com.aliyun.dysmsapi20170525.Client acsClient;

    @Autowired
    public AliSMSUtil(AliSMSProperties aliSMSProperties) {
        aliSMS = aliSMSProperties;
        InitClient();
    }

    private void InitClient() {
        try {
            com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                    .setAccessKeyId(aliSMS.getKeys().getAccessKeyId())
                    .setAccessKeySecret(aliSMS.getKeys().getAccessKeySecret());
            // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
            config.endpoint = aliSMS.getENDPOINT();
            acsClient = new com.aliyun.dysmsapi20170525.Client(config);
        } catch (Exception e) {
            log.error("初始化阿里云短信（SMS）服务失败");
        }
    }


    /**
     * 发送短信验证码
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    public boolean sendSMS(String phone, String code) {
        try {
            if (acsClient == null) {
                // 初始化acsClient失败
                return false;
            }
            com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest()
                    .setSignName(aliSMS.getSignName())
                    .setTemplateCode(aliSMS.getTemplateCode())
                    .setPhoneNumbers(phone)
                    .setTemplateParam("{\"code\":\"" + code + "\"}");
            com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
            SendSmsResponse sendSmsResponse = acsClient.sendSmsWithOptions(sendSmsRequest, runtime);
            String respCode = sendSmsResponse.getBody().getCode();
            if (respCode != null && respCode.equals("OK")) {
                return true;
            } else {
                throw new Exception(sendSmsResponse.getBody().getMessage());
            }
        } catch (ClientException e) {
            log.error("阿里云短信发送失败！code={},msg={}", e.getErrCode(), e.getErrMsg());
            return false;
        } catch (Exception e) {
            log.error("阿里云短信发送失败！msg={}", e.getMessage());
            return false;
        }
    }
}
