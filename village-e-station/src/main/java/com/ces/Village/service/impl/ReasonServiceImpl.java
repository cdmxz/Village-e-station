package com.ces.Village.service.impl;

import com.ces.Village.mapper.ReasonMapper;
import com.ces.Village.pojo.entity.Reason;
import com.ces.Village.service.ReasonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
