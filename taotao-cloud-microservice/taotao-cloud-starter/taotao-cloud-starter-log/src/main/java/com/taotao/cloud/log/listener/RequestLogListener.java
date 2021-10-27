/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.log.listener;

import com.taotao.cloud.common.utils.ContextUtil;
import com.taotao.cloud.log.event.RequestLogEvent;
import com.taotao.cloud.log.model.Log;
import com.taotao.cloud.log.service.impl.KafkaRequestLogServiceImpl;
import com.taotao.cloud.log.service.impl.LoggerRequestLogServiceImpl;
import com.taotao.cloud.log.service.impl.RedisRequestLogServiceImpl;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * 注解形式的监听 异步监听日志事件
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/6/3 13:33
 */
public class RequestLogListener {

	@Async
	@EventListener(RequestLogEvent.class)
	public void saveRequestLog(RequestLogEvent event) {
		Log log = (Log) event.getSource();
		KafkaRequestLogServiceImpl kafkaRequestLogService = ContextUtil
			.getBean(KafkaRequestLogServiceImpl.class, true);
		if (null != kafkaRequestLogService) {
			kafkaRequestLogService.save(log);
		}

		LoggerRequestLogServiceImpl loggerRequestLogService = ContextUtil
			.getBean(LoggerRequestLogServiceImpl.class, true);
		if (null != loggerRequestLogService) {
			loggerRequestLogService.save(log);
		}

		RedisRequestLogServiceImpl redisRequestLogService = ContextUtil
			.getBean(RedisRequestLogServiceImpl.class, true);
		if (null != redisRequestLogService) {
			redisRequestLogService.save(log);
		}

		//sysLogService.save(requestLog);
	}
}
