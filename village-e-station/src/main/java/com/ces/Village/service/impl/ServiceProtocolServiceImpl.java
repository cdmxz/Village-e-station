package com.ces.Village.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.Village.mapper.ServiceProtocolMapper;
import com.ces.Village.pojo.entity.ServiceProtocol;
import com.ces.Village.service.ServiceProtocolService;
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
