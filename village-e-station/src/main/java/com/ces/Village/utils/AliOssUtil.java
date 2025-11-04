package com.ces.village.utils;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.ces.village.pojo.vo.AliStsVO;
import com.ces.village.properties.AliOssProperties;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

/**
 * 阿里云oss工具类
 */
@Data
@Log4j2
@Component
public class AliOssUtil {

    private String stsEndPoint;
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String roleSessionName;
    private String roleArn;
    private OSS ossClient;

    public AliOssUtil(AliOssProperties aliOssProperties) {
        this.stsEndPoint = aliOssProperties.getStsEndPoint();
        this.endpoint = aliOssProperties.getEndPoint();
        this.accessKeyId = aliOssProperties.getKeys().getAccessKeyId();
        this.accessKeySecret = aliOssProperties.getKeys().getAccessKeySecret();
        this.bucketName = aliOssProperties.getBucketName();
        this.roleSessionName = aliOssProperties.getRoleSessionName();
        this.roleArn = aliOssProperties.getRoleArn();
        ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }


    /**
     * oss链接转objectName
     *
     * @param url
     * @return
     */
    public String urlToObjectName(String url) {
        if (url.startsWith("http")) {
            int index = url.lastIndexOf("com");
            return url.substring(index + 4);
        } else {
            return url;
        }
    }

    /**
     * 获取STS临时访问凭证
     * 前端通过该凭证访问oss
     *
     * @return 失败返回null
     */
    public AliStsVO getSts() {
        try {
            String regionId = "";
            DefaultProfile.addEndpoint(regionId, "Sts", stsEndPoint);
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setSysMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
//            request.setPolicy("");
            request.setDurationSeconds(3600L);
            final AssumeRoleResponse response = client.getAcsResponse(request);
            AliStsVO aliStsVO = ConvertUtil.entityToVo(response.getCredentials(), AliStsVO.class);
            return aliStsVO;
        } catch (ClientException e) {
            log.error("获取sts失败：Error code={}，Error message={}，RequestId={}",
                    e.getErrCode(), e.getErrMsg(), e.getRequestId());
        }
        return null;
    }

    /**
     * 上传文件到oss
     *
     * @param bytes
     * @param objectName
     * @return
     */
    public String upload(byte[] bytes, String objectName) {
        try {
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, new ByteArrayInputStream(bytes));
        } catch (OSSException e) {
            log.error("oss上传文件失败：Error code={}，Error message={}，RequestId={}", e.getErrorCode(), e.getErrorMessage(), e.getRequestId());
      }
//        finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//        }

        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);

        log.info("文件上传到:{}", stringBuilder);
        return stringBuilder.toString();
    }


    /**
     * 删除文件
     *
     * @param urlOrObjectName oss下载url，或者oss文件对象名
     * @return
     */
    public boolean delete(String urlOrObjectName) {
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            String objectName = urlToObjectName(urlOrObjectName);
            ossClient.deleteObject(bucketName, objectName);
            return true;
        } catch (OSSException e) {
            log.error("oss删除失败：object={}，errCode={}，errMsg={}", urlOrObjectName, e.getErrorCode(), e.getErrorMessage());
        } catch (Exception e) {
            log.error("oss删除失败：object={}，msg={}", urlOrObjectName, e.getMessage());
        }
        return false;
    }
}
