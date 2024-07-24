package com.ces.Village.pojo.dto;

import lombok.Data;

/**
 * 通过微信api获取手机号
 */
@Data
public class WxPhoneResponse {
    private int errcode;

    private String errmsg;

    private PhoneInfo phone_info;

    @Data
    public static class PhoneInfo {
        private String phoneNumber;

        private String purePhoneNumber;

        private int countryCode;

        private Watermark watermark;

        @Data
        public static class Watermark
        {
            private int timestamp;
            private String appid;

        }
    }

}
