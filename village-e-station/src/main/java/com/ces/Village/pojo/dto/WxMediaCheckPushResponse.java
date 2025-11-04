package com.ces.village.pojo.dto;

import lombok.Data;

import java.util.List;

/**
 * 微信音视频内容安全识别 接口
 * 通过 微信消息推送 返回的结果
 */
@Data
public class WxMediaCheckPushResponse {

    private String ToUserName;

    private String FromUserName;

    private Integer CreateTime;

    private String MsgType;

    private String Event;

    private String appid;

    private String trace_id;

    private Integer version;

    private List<Detail> detail;

    private Integer errcode;

    private String errmsg;

    private Result result;

    @Data
    public static class Result {
        /**
         * 建议，有risky、pass、review三种值
         */
        private String suggest;
        /**
         * 命中标签枚举值，100 正常；10001 广告；20001 时政；20002 色情；20003 辱骂；20006 违法犯罪；20008 欺诈；20012 低俗；20013 版权；21000 其他
         */
        private int label;
    }

    @Data
    public static class Detail {

        private String strategy;

        private Integer errcode;

        private String suggest;

        private Integer label;

        private Integer prob;
    }
}
