package com.ces.village.controller.user;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.green20220302.models.TextModerationResponse;
import com.ces.village.common.R;
import com.ces.village.constant.AliReviewTypeConstant;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.pojo.dto.ContentReviewDTO;
import com.ces.village.service.UsersService;
import com.ces.village.utils.AliSecCheckUtil;
import com.ces.village.utils.StringUtils;
import com.ces.village.utils.WxApiUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 内容审核
 */
@Log4j2
@Tag(name = "内容审核接口")
@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ContentReviewController {
    private final AliSecCheckUtil aliSecCheckUtil;

    private final WxApiUtil wxApiUtil;
    private final UsersService usersService;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 文本审核
     *
     * @param reviewDTO
     * @return
     */
    @PostMapping("/text")
    public R<?> textReview(@RequestBody ContentReviewDTO reviewDTO) {
        if (StringUtils.isEmptyOrNull(reviewDTO.getText())) {
            return R.error(ErrorCodeEnum.PARAM_IS_NULL);
        }
        if (!AliReviewTypeConstant.isValid(reviewDTO.getType())) {
            return R.error(ErrorCodeEnum.PARAM_IS_INVALID);
        }
        try {
            TextModerationResponse response = aliSecCheckUtil.msgSecCheck(reviewDTO.getText(), reviewDTO.getType());
            if (response.getBody().getCode() != 200) {
                throw new Exception("text=" + reviewDTO.getText() + "，errMsg=" + response.getBody().getMessage());
            }
            String reason = response.getBody().getData().getReason();
            HashMap<String, Object> map = new HashMap<>();
            String key = "risk_tips";
            map.put(key, "");
            if (!StringUtils.isEmptyOrNull(reason)) {
                // 解析参数
                reason = JSONObject.parse(reason).get("riskTips").toString();
                StringBuilder sb = new StringBuilder();
                String[] reasons = reason.split(",");
                for (int i = 0; i < reasons.length; i++) {
                    String item = reasons[i];
                    if (i != 0) {
                        sb.append(",");
                    }
                    item = item.split("_")[0];
                    sb.append(item);
                }
                map.put(key, sb.toString());
            }
            return R.success(map);
        } catch (Exception e) {
            log.error("文本检测接口出错：" + e.getMessage());
            return R.error(ErrorCodeEnum.CONTENT_REVIEW_FAILED);
        }
    }

// TODO 勿删，保留备用
//    /**
//     * 图片审核
//     *
//     * @param reviewDTO
//     * @return
//     */
//    @LoginRequired
//    @PostMapping("/image")
//    public R<?> imgReview(@RequestBody ContentReviewDTO reviewDTO) {
//        if (StringUtils.isEmptyOrNull(reviewDTO.getImgUrl())) {
//            return R.error(ErrorCodeEnum.PARAM_IS_NULL);
//        }
//        CurrentUser currentUser = BaseContext.getCurrentUser();
//        Users users = usersService.getById(currentUser.getId());
//        try {
//            String openId = users.getOpenId();
//            String imgUrl = reviewDTO.getImgUrl();
//            WxMediaCheckResponse wxMediaCheckResponse = wxApiUtil.ImageCheckAsync(imgUrl, openId);
//            // 将trace_id和图片url保存到redis
//            String key = wxMediaCheckResponse.getTrace_id();
//            redisTemplate.opsForValue().set(key, imgUrl, Duration.ofMinutes(40));
//            return R.success(wxMediaCheckResponse);
//        } catch (Exception e) {
//            log.error("图片检测接口出错：" + e.getMessage());
//            return R.error(ErrorCodeEnum.CONTENT_REVIEW_FAILED);
//        }
//    }
}
