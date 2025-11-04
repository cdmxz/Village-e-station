package com.ces.village.utils;

import com.aliyun.green20220302.models.ImageModerationRequest;
import com.aliyun.green20220302.models.ImageModerationResponse;
import com.aliyun.green20220302.models.TextModerationRequest;
import com.aliyun.green20220302.models.TextModerationResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import com.ces.village.properties.AliKeyProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 阿里云 内容安全 工具类
 */
@Component
@Log4j2
public class AliSecCheckUtil {

    private com.aliyun.green20220302.Client client;

    @Autowired
    public AliSecCheckUtil(AliKeyProperties keyProperties) {
        try {
            client = createClient(keyProperties.getAccessKeyId(), keyProperties.getAccessKeySecret());
        } catch (Exception e) {
            log.error("阿里云内容安全，初始化失败：" + e.getMessage());
        }
    }

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return Client
     * @throws Exception
     */
    public com.aliyun.green20220302.Client createClient(String accessKeyId, String accessKeySecret) throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        // Endpoint 请参考 https://api.aliyun.com/product/Green
        config.endpoint = "green-cip.cn-shenzhen.aliyuncs.com";
        return new com.aliyun.green20220302.Client(config);
    }


    /**
     * 图片审核服务
     *
     * @return 出错时返回null
     */
    public ImageModerationResponse imgSecCheck(String imgUrl) throws Exception {
        ImageModerationRequest imageModerationRequest = new ImageModerationRequest()
                .setService("baselineCheck") // 基线服务
                .setServiceParameters("{\"imageUrl\":\"" + imgUrl + "\"}");
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        TeaException err = null;
        try {
            return client.imageModerationWithOptions(imageModerationRequest, runtime);
        } catch (TeaException error) {
            err = error;
        } catch (Exception _error) {
            err = new TeaException(_error.getMessage(), _error);
        }
        String errStr = String.format("阿里云 图片审核返回错误：%s，错误建议链接：%s", err.getMessage(), err.getData().get("Recommend"));
        throw new Exception(errStr);
    }

    /**
     * 文本审核服务
     *
     * @param text
     * @return 出错时返回null
     */
    public TextModerationResponse msgSecCheck(String text, String type) throws Exception {
        TextModerationRequest textModerationRequest = new TextModerationRequest()
                .setService(type)
                .setServiceParameters("{\"content\":\"" + text + "\"}");
        RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        TeaException err = null;
        try {
            return client.textModerationWithOptions(textModerationRequest, runtime);
        } catch (TeaException error) {
            err = error;
        } catch (Exception _error) {
            err = new TeaException(_error.getMessage(), _error);
        }
        String errStr = String.format("阿里云 文本审核返回错误：%s，错误建议链接：%s", err.getMessage(), err.getData().get("Recommend"));
        throw new Exception(errStr);
    }
}
