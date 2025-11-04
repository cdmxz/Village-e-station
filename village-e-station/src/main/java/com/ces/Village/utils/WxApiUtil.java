package com.ces.village.utils;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ces.village.pojo.dto.*;
import com.ces.village.properties.WxApiProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信api工具类
 */
@Log4j2
@Component
public class WxApiUtil {
    private final WxApiProperties wxApiProperties;
    private WxTokenResponse tokenObject;

    public WxApiUtil(WxApiProperties wxApiProperties) {
        this.wxApiProperties = wxApiProperties;
        tokenObject = WxTokenResponse.builder()
                .expireTime(System.currentTimeMillis())
                .build();
    }


    /**
     * 通过code获取用户的openid
     *
     * @param code
     * @return
     */
    public WxOpenIdResponse code2OpenId(String code) {
        // 通过 code 获取 access_token
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", wxApiProperties.getAppid());
        paramMap.put("secret", wxApiProperties.getAppSecret());
        paramMap.put("js_code", code);
        paramMap.put("grant_type", "authorization_code");
        String json = HttpClientUtil.doGet(wxApiProperties.getUserOpenidUrl(), paramMap);
        WxOpenIdResponse parseObject = JSONObject.parseObject(json, WxOpenIdResponse.class);
        return parseObject;
    }


    /**
     * 获取微信api的接口调用凭据
     */
    private WxTokenResponse requestAccessToken() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("appid", wxApiProperties.getAppid());
        paramMap.put("secret", wxApiProperties.getAppSecret());
        paramMap.put("grant_type", "client_credential");
        String json = HttpClientUtil.doGet(wxApiProperties.getApiAccessTokenUrl(), paramMap);
        WxTokenResponse token = JSONObject.parseObject(json, WxTokenResponse.class);
        if (token.getErrMsg() != null && !token.getErrMsg().equals("ok")) {
            throw new RuntimeException("获取token失败，" + token.getErrMsg());
        }
        return token;
    }

    /**
     * 在需要时应调用此接口获取accessToken，
     * 不要直接使用全局变量，方便失效时更新accessToken
     *
     * @return
     */
    private String getAccessToken() {
        // 判断accesstoken是否失效
        long now = System.currentTimeMillis();
        if (tokenObject.getExpireTime() > now) {
            return tokenObject.getAccessToken();
        }
        // token失效，重新获取
        WxTokenResponse tokenDTO = requestAccessToken();
        // 更新全局变量tokenObject
        tokenDTO.setExpireTime(System.currentTimeMillis() + (tokenDTO.getExpiresIn() - 10) * 1000);
        tokenObject = tokenDTO;
        return tokenDTO.getAccessToken();
    }


    /**
     * 调用微信文本检测接口
     *
     * @return
     */
    public WxMsgCheckResponse msgSecCheck(String text, String openid) {
        String accessToken = getAccessToken();
        String url = wxApiProperties.getMsgSecCheckUrl() + "?access_token=" + accessToken;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("content", text);
        paramMap.put("version", 2);
        paramMap.put("scene", 2);
        paramMap.put("openid", openid);
        String json = HttpClientUtil.doPost4Json(url, paramMap);
        WxMsgCheckResponse checkDTO = JSON.parseObject(json, WxMsgCheckResponse.class);
        return checkDTO;
    }

    /**
     * 检测图片内容安全
     * 调用微信媒体检测接口
     * 异步返回
     *
     * @param imgUrl
     * @param openid
     * @return
     */
    public WxMediaCheckResponse ImageCheckAsync(String imgUrl, String openid) {
        String accessToken = getAccessToken();
        String url = wxApiProperties.getMediaCheckUrl() + "?access_token=" + accessToken;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("media_url", imgUrl);
        paramMap.put("version", 2);
        paramMap.put("media_type", 2);
        paramMap.put("scene", 2);
        paramMap.put("openid", openid);
        String json = HttpClientUtil.doPost4Json(url, paramMap);
        WxMediaCheckResponse checkDTO = JSON.parseObject(json, WxMediaCheckResponse.class);
        return checkDTO;
    }


    /**
     * 通过code获取手机号
     * 注意：该code与获取用户的openid的code不同
     * 返回json结果
     */
    public WxPhoneResponse getPhoneNumber(WxApiProperties wxApiProperties, String accessToken, String code) {
        String url = wxApiProperties.getPhoneNumberUrl() + "?access_token=" + accessToken;
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("code", code);
        String json = HttpClientUtil.doPost4Json(url, paramMap);
        WxPhoneResponse object = JSON.parseObject(json, WxPhoneResponse.class);
        return object;
    }


    /**
     * 推送微信消息给用户
     *
     * @param openid
     * @param templateId
     * @return
     */
    public WxMsgPushResponse msgPush(String openid, String templateId, String data, String page) {
        try {
            String accessToken = getAccessToken();
            String url = wxApiProperties.getMsgPushUrl() + "?access_token=" + accessToken;
            URL apiUrl =  new URI(url).toURL();
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setDoOutput(true);

            // 构建请求体
            String body = String.format(
                    "{\"touser\":\"%s\",\"template_id\":\"%s\",\"page\":\"%s\",\"data\":%s}",
                    openid, templateId, page, data);
            byte[] requestBodyBytes = body.getBytes(StandardCharsets.UTF_8);

            // 发送请求
            connection.getOutputStream().write(requestBodyBytes);

            // 读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();
            WxMsgPushResponse pushResponse = JSON.parseObject(response.toString(), WxMsgPushResponse.class);
            return pushResponse;
        } catch (Exception e) {
            // 异常处理逻辑
            log.error(e.getMessage());
        }
        return null;
    }

    public String queryRid(String rid) {
        String post = "https://api.weixin.qq.com/cgi-bin/openapi/rid/get?access_token=" + getAccessToken();
        JSONObject param = new JSONObject();
        param.put("rid", rid);
        String body = HttpClientUtil.doPost4Json(post, param);
        return body;
    }
}
