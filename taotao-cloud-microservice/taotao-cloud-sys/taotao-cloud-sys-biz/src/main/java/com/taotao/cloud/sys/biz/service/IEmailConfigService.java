/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.sys.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sys.api.vo.alipay.EmailVo;
import com.taotao.cloud.sys.biz.entity.config.EmailConfig;
import org.springframework.scheduling.annotation.Async;

public interface IEmailConfigService extends IService<EmailConfig> {

	/**
	 * 更新邮件配置
	 * @param emailConfig 邮件配置
	 * @param old 旧的配置
	 * @return 是否更新完成
	 */
	Boolean update(EmailConfig emailConfig, EmailConfig old);

	/**
	 * 查询配置
	 * @return EmailConfig 邮件配置
	 */
	EmailConfig find();

	/**
	 * 发送邮件
	 * @param emailVo 邮件发送的内容
	 * @param emailConfig 邮件配置
	 * @throws Exception
	 */
	@Async
	void send(EmailVo emailVo, EmailConfig emailConfig) throws Exception;
}
