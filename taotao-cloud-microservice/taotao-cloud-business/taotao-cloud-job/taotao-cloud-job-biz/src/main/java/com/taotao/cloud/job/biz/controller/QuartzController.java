/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.job.biz.controller;
//
// import jakarta.validation.Valid;
// import jakarta.validation.constraints.NotNull;
// import java.io.IOException;
// import java.sql.SQLException;
// import java.util.List;
// import org.quartz.JobKey;
// import org.quartz.SchedulerException;
// import org.quartz.TriggerKey;
// import org.quartz.ee.jmx.jboss.QuartzService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// @RestController
// @RequestMapping("/quartz")
// @Validated
// public class QuartzController {
//
//	@Autowired
//	private QuartzService quartzService;
//	@Autowired
//	private QuartzServiceNew quartzServiceNew;
//
//	/**
//	 * 添加或修改一个 job
//	 *
//	 * @param connName     连接名
//	 * @param editJobParam 任务参数
//	 */
//	@PostMapping("/{connName}/editJob")
//	public void editJob(@PathVariable("connName") String connName,
//		@RequestBody @Valid EditJobParam editJobParam) throws Exception {
//		quartzService.editJob(connName, editJobParam.getNamespace(), editJobParam);
//	}
//
//
//	/**
//	 * 查询所有的任务列表
//	 *
//	 * @param connName 连接名
//	 * @param catalog  数据库  catalog
//	 * @param schema   数据库 schema
//	 * @return
//	 * @throws IOException
//	 * @throws SQLException
//	 */
//	@GetMapping("/triggers")
//	public List<TriggerTask> triggers(@NotNull String connName, Namespace namespace,
//		String tablePrefix) throws Exception {
//		return quartzServiceNew.triggerTasks(connName, namespace, tablePrefix);
//	}
//
//	/**
//	 * 触发任务
//	 *
//	 * @param connName 连接名
//	 * @param group    分组名
//	 * @param name     任务名
//	 * @throws SchedulerException
//	 */
//	@GetMapping("/trigger")
//	public void trigger(@NotNull String connName, Namespace namespace, String group, String name)
//		throws Exception {
//		JobKey jobKey = new JobKey(name, group);
//		quartzService.trigger(connName, namespace, jobKey);
//	}
//
//	/**
//	 * 暂停
//	 *
//	 * @param connName 连接名
//	 * @param name     任务名
//	 * @param group    任务分组
//	 * @throws SchedulerException
//	 */
//	@GetMapping("/pause")
//	public void pause(@NotNull String connName, Namespace namespace, String name, String group)
//		throws Exception {
//		JobKey jobKey = new JobKey(name, group);
//		quartzService.pause(connName, namespace, jobKey);
//	}
//
//	/**
//	 * 恢复
//	 *
//	 * @param connName 连接名
//	 * @param name     任务名
//	 * @param group    任务分组
//	 * @throws SchedulerException
//	 */
//	@GetMapping("/resume")
//	public void resume(@NotNull String connName, Namespace namespace, String name, String group)
//		throws Exception {
//		JobKey jobKey = new JobKey(name, group);
//		quartzService.resume(connName, namespace, jobKey);
//	}
//
//	/**
//	 * 移除
//	 *
//	 * @param connName     连接名称
//	 * @param triggerName  触发器名称
//	 * @param triggerGroup 触发器分组
//	 * @param jobName      任务名
//	 * @param jobGroup     任务分组
//	 * @throws SchedulerException
//	 */
//	@GetMapping("/remove")
//	public void remove(@NotNull String connName, Namespace namespace, @NotNull String triggerName,
//		@NotNull String triggerGroup, @NotNull String jobName, @NotNull String jobGroup)
//		throws Exception {
//		TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);
//		JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
//		quartzService.remove(connName, namespace, triggerKey, jobKey);
//	}
// }
