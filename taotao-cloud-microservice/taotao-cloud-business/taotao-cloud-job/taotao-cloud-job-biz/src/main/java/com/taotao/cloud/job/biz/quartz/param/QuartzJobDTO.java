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

package com.taotao.cloud.job.biz.quartz.param;

import io.swagger.v3.oas.annotations.media.Schema;

/** 定时任务 */
@Schema(title = "定时任务")
public class QuartzJobDTO {

    @Schema(description = "主键 创建时不传，更新时传", example = "1")
    private Long id;

    @Schema(description = "任务名称 最好是bean名称", example = "demoJob")
    private String jobName;

    @Schema(description = "任务组名称 最好是bean名称", example = "demoJobGroup")
    private String groupName;

    @Schema(description = "Bean名称", example = "demoJob")
    private String beanName;

    @Schema(description = "任务类名 和 bean名称 互斥", example = "com.taotao.cloud.xx.job.demoJob")
    private String jobClassName;

    @Schema(description = "cron表达式", example = "* 0/5 * * * ?")
    private String cronExpression;

    @Schema(description = "方法名称", example = "handleMessage")
    private String methodName;

    @Schema(description = "参数", example = "demo")
    private String params;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否并发  0：禁止  1：允许")
    private Integer concurrent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(Integer concurrent) {
        this.concurrent = concurrent;
    }
}
