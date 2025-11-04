package com.ces.village.service;

import com.ces.village.common.R;
import com.ces.village.pojo.vo.AliStsVO;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.Future;

/**
 * oss服务
 */
public interface OssService {
    /**
     * 获取STS临时访问凭证
     * 前端通过该凭证访问oss
     */
    R<AliStsVO> getSts();

    /**
     * 从oss删除文件
     *
     * @param objectName
     * @return
     */
    Future<Boolean> deleteAsync(String objectName);

    /**
     * 从oss批量删除文件
     *
     * @param objects
     * @return
     */
    Future<Boolean> deleteBatchAsync(List<String> objects);

    /**
     * 从oss批量删除文件
     * 传入json数组，例如：[1.png, 2.png]
     * @return
     */
    Future<Boolean> deleteByJsonAsync(String objJson);

    /**
     * 从oss批量删除文件
     * 传入富文本，例如：< img src="xxx">< /img>
     * @return
     */
    Future<Boolean> deleteByRichAsync(String richText);

    /**
     * 图片审核，审核不通过的图片将会从oss删除
     *
     * @param imgUrl
     * @return
     */
    void imgReviewAsync(String imgUrl);

    /**
     * 图片审核，审核不通过的图片将会从oss删除
     *
     * @param objects
     * @return
     */
    @Async
    void imgReviewBatchAsync(List<String> objects);

    /**
     * 图片审核，审核不通过的图片将会从oss删除
     *
     * @param objJson
     * @return
     */
    @Async
    void imgReviewByJsonAsync(String objJson);

    /**
     * 图片审核，审核不通过的图片将会从oss删除
     * 传入富文本，例如：< img src="xxx">< /img>
     *
     * @param richText
     * @return
     */
    @Async
    void imgReviewByRichAsync(String richText);
}
