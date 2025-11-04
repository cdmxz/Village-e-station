package com.ces.village.controller.user;

import com.ces.village.annotation.LoginRequired;
import com.ces.village.common.R;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.utils.AliOssUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * 图片接口
 */
@RestController
@RequestMapping("/api/image")
@Tag(name = "图片接口")
@Log4j2
public class ImageController {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 图片上传
     *
     * @param file
     * @return
     */
    @LoginRequired
    @PostMapping("/upload")
    @Operation(summary = "图片上传")
    public R<String> upload(MultipartFile file) {
        log.info("图片上传：{}", file);
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀   dfdfdf.png/fsfsd.jpg等。。。。
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称
            String objectName = UUID.randomUUID() + extension;
            //文件的请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return R.success(filePath);
        } catch (Exception e) {
            log.error("图片上传失败：{}", e.getMessage());
        }
        return R.error(ErrorCodeEnum.UPLOAD_FAILED);
    }

    /**
     * 从oss删除文件
     *
     * @param objectName
     * @return
     */
    @LoginRequired
    @DeleteMapping("/delete")
    public R<?> delete(@RequestParam String objectName) {
        log.info("从oss删除文件:{}", objectName);
        // 调用aliOssUtil的deleteObject方法，传入文件名进行删除
        boolean result = aliOssUtil.delete(objectName);
        if (result)
            return R.success();
        else
            return R.error(ErrorCodeEnum.OSS_DELETE_FAILED);
    }

}

