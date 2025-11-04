package com.ces.village.controller.ali;


import com.ces.village.common.R;
import com.ces.village.pojo.vo.AliStsVO;
import com.ces.village.service.OssService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@Log4j2
@Tag(name = "oss接口")
@RestController
@RequestMapping("/api/oss")
public class OssController {
    @Autowired
    private OssService ossService;

    /**
     * 获取阿里云sts
     * 前端通过该凭证访问oss
     *
     * @return
     */
    @Operation(summary = "获取阿里云sts")
    @RequestMapping("/sts")
    public R<?> getSts() {
        log.info("获取STS临时访问凭证");
        R<AliStsVO> aliStsVOResult = ossService.getSts();
        return aliStsVOResult;
    }

    /**
     * oss报警回调函数 （保留）
     * 由阿里云服务器调用
     */
    @Operation(summary = "oss报警回调函数")
    @RequestMapping("/alarmcallback")
    public void alarmCallback() {
        log.error("oss报警回调...");
        // ossService.stopOss();
    }
}
