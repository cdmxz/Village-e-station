package com.ces.village.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.village.mapper.ServiceProtocolMapper;
import com.ces.village.pojo.entity.ServiceProtocol;
import com.ces.village.service.ServiceProtocolService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务协议表 服务类
 * </p>
 *
 * @author author
 * @since 2023-12-11
 */
@Service
public class ServiceProtocolServiceImpl extends ServiceImpl<ServiceProtocolMapper, ServiceProtocol> implements ServiceProtocolService {
}
