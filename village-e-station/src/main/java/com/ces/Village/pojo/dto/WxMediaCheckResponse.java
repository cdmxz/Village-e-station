package com.ces.Village.pojo.dto;

import lombok.Data;

/**
 * 微信音视频内容安全识别 返回参数
 */
@Data
public class WxMediaCheckResponse {
    private int errcode;// 错误码 ，正确返回0
    private String errmsg;
    // 唯一请求标识，标记单次请求，用于匹配异步推送结果
    private String trace_id;
}
