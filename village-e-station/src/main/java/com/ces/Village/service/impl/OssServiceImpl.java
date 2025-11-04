package com.ces.village.service.impl;

import com.aliyun.green20220302.models.ImageModerationResponseBody;
import com.ces.village.common.R;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.pojo.vo.AliStsVO;
import com.ces.village.service.ContentReviewService;
import com.ces.village.service.OssService;
import com.ces.village.utils.AliOssUtil;
import com.ces.village.utils.JsonConvertUtil;
import com.ces.village.utils.RichTextUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Future;

/**
 * oss服务
 */
@Service
@Log4j2
public class OssServiceImpl implements OssService {
    @Autowired
    private AliOssUtil aliOssUtil;

    @Autowired
    private ContentReviewService contentReviewService;

    /**
     * 获取STS临时访问凭证
     * 前端通过该凭证访问oss
     */
    @Override
    public R<AliStsVO> getSts() {
        AliStsVO aliStsVO = aliOssUtil.getSts();
        if (aliStsVO != null)
            return R.success(aliStsVO);
        else
            return R.error(ErrorCodeEnum.STS_FAILED);
    }

    /**
     * 从oss删除文件
     *
     * @param objectName
     * @return
     */
    @Override
    @Async
    public Future<Boolean> deleteAsync(String objectName) {
        boolean result = aliOssUtil.delete(objectName);
        return new AsyncResult<>(result);
    }

    /**
     * 从oss批量删除文件
     *
     * @param objects
     * @return
     */
    @Override
    @Async
    public Future<Boolean> deleteBatchAsync(List<String> objects) {
        boolean result = false;
        for (String obj : objects) {
            result = aliOssUtil.delete(obj);
        }
        return new AsyncResult<>(result);
    }

    /**
     * 从oss批量删除文件
     *
     * @param objJson
     * @return
     */
    @Override
    @Async
    public Future<Boolean> deleteByJsonAsync(String objJson) {
        List<String> imgList = JsonConvertUtil.toJavaObject(objJson, List.class);
        boolean result = false;
        for (String obj : imgList) {
            result = aliOssUtil.delete(obj);
        }
        return new AsyncResult<>(result);
    }

    /**
     * 从oss批量删除文件
     * 传入富文本，例如：< img src="xxx">< /img>
     *
     * @param richText
     * @return
     */
    @Override
    @Async
    public Future<Boolean> deleteByRichAsync(String richText) {
        List<String> imgList = RichTextUtils.getImgUrls(richText);
        boolean result = false;
        for (String obj : imgList) {
            result = aliOssUtil.delete(obj);
        }
        return new AsyncResult<>(result);
    }


    /**
     * 图片审核，审核不通过的图片将会从oss删除
     *
     * @param imgUrl
     * @return
     */
    @Override
    @Async
    public void imgReviewAsync(String imgUrl) {
        imgReview(imgUrl);
    }

    public void imgReview(String imgUrl) {
        try {
            List<ImageModerationResponseBody.ImageModerationResponseBodyDataResult> labelList = contentReviewService.aliImgReview(imgUrl);
            String firstLabel = labelList.get(0).getLabel();
            if (!firstLabel.equals("nonLabel")) {
                // 表示图片 内容 不正常
                StringBuilder sb = new StringBuilder();
                for (ImageModerationResponseBody.ImageModerationResponseBodyDataResult item : labelList) {
                    sb.append(item.getLabel()).append(",");
                }
                log.info("图片：{} 审核含有：{}，从oss删除...", imgUrl, sb.substring(0, sb.length() - 1));
                // 从oss删除违规图片
                aliOssUtil.delete(imgUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片审核，审核不通过的图片将会从oss删除
     *
     * @param objects
     * @return
     */
    @Override
    @Async
    public void imgReviewBatchAsync(List<String> objects) {
        for (String obj : objects) {
            imgReview(obj);
        }
    }

    /**
     * 图片审核，审核不通过的图片将会从oss删除
     *
     * @param objJson
     * @return
     */
    @Override
    @Async
    public void imgReviewByJsonAsync(String objJson) {
        List<String> imgList = JsonConvertUtil.toJavaObject(objJson, List.class);
        for (String obj : imgList) {
            imgReview(obj);
        }
    }

    /**
     * 图片审核，审核不通过的图片将会从oss删除
     * 传入富文本，例如：< img src="xxx">< /img>
     *
     * @param richText
     * @return
     */
    @Override
    @Async
    public void imgReviewByRichAsync(String richText) {
        List<String> imgList = RichTextUtils.getImgUrls(richText);
        for (String obj : imgList) {
            imgReview(obj);
        }
    }
}
