package com.ces.village.controller.user;

import com.ces.village.common.R;
import com.ces.village.pojo.entity.PrivacyProtocol;
import com.ces.village.pojo.entity.ServiceProtocol;
import com.ces.village.service.PrivacyProtocolService;
import com.ces.village.service.ServiceProtocolService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 隐私政策、服务协议 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-12-11
 */
@Log4j2
@Tag(name = "服务协议、隐私协议接口")
@RestController
@RequestMapping("/api/protocol")
public class ProtocolController {
    /**
     * 服务协议
     */
    @Autowired
    private ServiceProtocolService serviceProtocolService;

    /**
     * 隐私协议
     */
    @Autowired
    private PrivacyProtocolService privacyProtocolService;

    /**
     * 获取服务协议
     */
    @RequestMapping("/service")
    public R<ServiceProtocol> getServiceProtocol() {
        log.info("获取服务协议...");
        List<ServiceProtocol> serviceProtocols = serviceProtocolService.list();
        ServiceProtocol serviceProtocol = serviceProtocols.get(0);
        serviceProtocol.setTitle("服务协议");
        return R.success(serviceProtocol);
    }

    /**
     * 获取隐私政策
     */
    @RequestMapping("privacy")
    public R<PrivacyProtocol> getPrivacyProtocol() {
        log.info("获取隐私政策...");
        List<PrivacyProtocol> privacyProtocols = privacyProtocolService.list();
        PrivacyProtocol privacyProtocol = privacyProtocols.get(0);
        privacyProtocol.setTitle("隐私政策");
        return R.success(privacyProtocol);
    }

}
