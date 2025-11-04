package com.ces.village.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信api url
 */
@Component
@ConfigurationProperties(prefix = "wechat.api")
@Data
public class WxApiProperties {
    /**
     * 微信登录接口，通过code获取用户的openid
     */
    private String userOpenidUrl = "https://api.weixin.qq.com/sns/jscode2session";
    private String apiAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
    private String phoneNumberUrl = "https://api.weixin.qq.com/wxa/business/getuserphonenumber";
    private String msgSecCheckUrl = "https://api.weixin.qq.com/wxa/msg_sec_check";
    private String mediaCheckUrl = "https://api.weixin.qq.com/wxa/media_check_async";
    private String MsgPushUrl = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send";


    private String appid; //小程序的appid
    private String appSecret; //小程序的秘钥
}
