package com.ces.village.pojo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 微信内容安全审核api返回的数据
 */
@Setter
@Getter
public class WxMsgCheckResponse {
    private int errcode;// 错误码 ，正确返回0
    private String errmsg;
    private List<Detail> detail;
    private String trace_id;
    private Result result;

    /**
     * 	详细检测结果
     */
    @Setter
    @Getter
    public static class Detail {
        private String strategy;
        private int errcode;
        private String suggest;
        private int label;
        private int prob;
    }

    /**
     * 	综合检测结果
     */
    @Setter
    @Getter
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
}
