package com.ces.village.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.village.pojo.entity.Reason;

/**
 * <p>
 * 审核失败的理由 服务类
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
public interface ReasonService extends IService<Reason> {

    /**
     * 查询理由表 更新招聘信息id和招聘信息类型
     * @param informationId
     * @param type
     * @return
     */
    Reason getReason(Long informationId, Integer type) ;
}
