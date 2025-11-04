package com.ces.village.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.village.mapper.ReasonMapper;
import com.ces.village.pojo.entity.Reason;
import com.ces.village.service.ReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审核失败的理由 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Service
public class ReasonServiceImpl extends ServiceImpl<ReasonMapper, Reason> implements ReasonService {

    @Autowired
    private ReasonMapper reasonMapper;

    /**
     * 查询理由表 更新招聘信息id和招聘信息类型
     *
     * @param informationId
     * @param type
     * @return
     */
    @Override
    public Reason getReason(Long informationId, Integer type) {
        return reasonMapper.getReason(informationId, type);
    }
}
