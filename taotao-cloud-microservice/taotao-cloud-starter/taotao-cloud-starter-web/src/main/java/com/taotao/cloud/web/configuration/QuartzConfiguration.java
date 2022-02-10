/**
 * Copyright (C) 2018-2020 All rights reserved, Designed By www.yixiang.co 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.web.configuration;

import com.taotao.cloud.web.quartz.QuartzJobListener;
import com.taotao.cloud.web.quartz.QuartzManager;
import org.quartz.Scheduler;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * 定时任务配置
 */
@Configuration
public class QuartzConfiguration {

	/**
	 * 解决Job中注入Spring Bean为null的问题
	 */
	@Component("quartzJobFactory")
	public static class QuartzJobFactory extends AdaptableJobFactory {

		private final AutowireCapableBeanFactory capableBeanFactory;

		public QuartzJobFactory(AutowireCapableBeanFactory capableBeanFactory) {
			this.capableBeanFactory = capableBeanFactory;
		}

		@Override
		protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
			//调用父类的方法
			Object jobInstance = super.createJobInstance(bundle);
			capableBeanFactory.autowireBean(jobInstance);
			return jobInstance;
		}
	}

	/**
	 * 注入scheduler到spring
	 */
	@Bean(name = "scheduler")
	public Scheduler scheduler(QuartzJobFactory quartzJobFactory) throws Exception {
		SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
		factoryBean.setJobFactory(quartzJobFactory);
		factoryBean.afterPropertiesSet();

		Scheduler scheduler = factoryBean.getScheduler();
		scheduler.getListenerManager().addJobListener(new QuartzJobListener());

		// 添加JobListener, 精确匹配JobKey
		// KeyMatcher<JobKey> keyMatcher = KeyMatcher.keyEquals(JobKey.jobKey("helloJob", "group1"));
		// scheduler.getListenerManager().addJobListener(new HelloJobListener(), keyMatcher);

		scheduler.start();
		return scheduler;
	}

	@Bean
	public QuartzManager quartzManage() {
		return new QuartzManager();
	}
}
