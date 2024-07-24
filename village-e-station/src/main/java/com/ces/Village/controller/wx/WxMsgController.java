package com.ces.Village.controller.wx;

import com.ces.Village.pojo.dto.WxMediaCheckPushResponse;
import com.ces.Village.service.WxMsgService;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * 微信消息推送
 */
@Log4j2
@Api(tags = "微信消息推送")
@Controller
@RequestMapping("/api/wxmsg")
public class WxMsgController {
    @Autowired
    private WxMsgService wxMsgService;

    /**
     * 服务器有效性验证
     */
    @GetMapping("/")
    public void verifyToken(HttpServletRequest request, HttpServletResponse response) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        try {
//            response.getWriter().print(Long.valueOf(echostr));
            if (wxMsgService.checkSignature(signature, timestamp, nonce)) {
                log.info("微信消息推送有效性验证成功");
                response.getWriter().print(Long.valueOf(echostr));
            }
        } catch (Exception e) {
            log.error("微信消息推送有效性验证失败，" + e.getMessage());
        }
    }

    /**
     * 接收来自微信服务器 的推送消息
     */
    @ResponseBody
    @PostMapping("/")
    public String wxPush(@RequestBody WxMediaCheckPushResponse pushDTO) {
        try {
            if (pushDTO.getDetail() != null) {
                // 媒体检测接口回调
                wxMsgService.mediaCheckCallBack(pushDTO);
            }
        } catch (Exception e) {
            log.error("接收消息微信推送出现错误：" + e.getMessage());
        }
        return "success";
    }

    @GetMapping("/rid")
    public String rid(@RequestParam(value = "rid") String rid, HttpServletResponse response) {
        try {
            String reason = wxMsgService.queryRid(rid);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().print(reason);
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
