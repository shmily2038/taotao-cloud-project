/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taotao.cloud.sys.biz.entity.QuartzJob;
import com.taotao.cloud.sys.biz.entity.ScheduledLog;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledLogMapper extends BaseMapper<ScheduledLog> {

}
