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

package com.taotao.cloud.message.biz.austin.api.impl.config;

import com.taotao.cloud.message.biz.austin.api.enums.BusinessCode;
import com.taotao.cloud.message.biz.austin.api.impl.action.AfterParamCheckAction;
import com.taotao.cloud.message.biz.austin.api.impl.action.AssembleAction;
import com.taotao.cloud.message.biz.austin.api.impl.action.PreParamCheckAction;
import com.taotao.cloud.message.biz.austin.api.impl.action.SendMqAction;
import com.taotao.cloud.message.biz.austin.support.pipeline.ProcessController;
import com.taotao.cloud.message.biz.austin.support.pipeline.ProcessTemplate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * api层的pipeline配置类
 *
 * @author 3y
 */
@Configuration
public class PipelineConfig {

    @Autowired private PreParamCheckAction preParamCheckAction;
    @Autowired private AssembleAction assembleAction;
    @Autowired private AfterParamCheckAction afterParamCheckAction;
    @Autowired private SendMqAction sendMqAction;

    /**
     * 普通发送执行流程 1. 前置参数校验 2. 组装参数 3. 后置参数校验 4. 发送消息至MQ
     *
     * @return
     */
    @Bean("commonSendTemplate")
    public ProcessTemplate commonSendTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        processTemplate.setProcessList(
                Arrays.asList(
                        preParamCheckAction, assembleAction, afterParamCheckAction, sendMqAction));
        return processTemplate;
    }

    /**
     * 消息撤回执行流程 1.组装参数 2.发送MQ
     *
     * @return
     */
    @Bean("recallMessageTemplate")
    public ProcessTemplate recallMessageTemplate() {
        ProcessTemplate processTemplate = new ProcessTemplate();
        processTemplate.setProcessList(Arrays.asList(assembleAction, sendMqAction));
        return processTemplate;
    }

    /**
     * pipeline流程控制器 后续扩展则加BusinessCode和ProcessTemplate
     *
     * @return
     */
    @Bean
    public ProcessController processController() {
        ProcessController processController = new ProcessController();
        Map<String, ProcessTemplate> templateConfig = new HashMap<>(4);
        templateConfig.put(BusinessCode.COMMON_SEND.getCode(), commonSendTemplate());
        templateConfig.put(BusinessCode.RECALL.getCode(), recallMessageTemplate());
        processController.setTemplateConfig(templateConfig);
        return processController;
    }
}
