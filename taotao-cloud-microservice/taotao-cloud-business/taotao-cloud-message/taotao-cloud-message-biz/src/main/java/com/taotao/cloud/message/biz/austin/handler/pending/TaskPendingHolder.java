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

package com.taotao.cloud.message.biz.austin.handler.pending;

import com.dtp.core.thread.DtpExecutor;
import com.taotao.cloud.message.biz.austin.handler.config.HandlerThreadPoolConfig;
import com.taotao.cloud.message.biz.austin.handler.utils.GroupIdMappingUtils;
import com.taotao.cloud.message.biz.austin.support.utils.ThreadPoolUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 存储 每种消息类型 与 TaskPending 的关系
 *
 * @author 3y
 */
@Component
public class TaskPendingHolder {

    @Autowired private ThreadPoolUtils threadPoolUtils;

    private Map<String, ExecutorService> taskPendingHolder = new HashMap<>(32);

    /** 获取得到所有的groupId */
    private static List<String> groupIds = GroupIdMappingUtils.getAllGroupIds();

    /** 给每个渠道，每种消息类型初始化一个线程池 */
    @PostConstruct
    public void init() {
        /**
         * example ThreadPoolName:austin.im.notice
         *
         * <p>可以通过apollo配置：dynamic-tp-apollo-dtp.yml 动态修改线程池的信息
         */
        for (String groupId : groupIds) {
            DtpExecutor executor = HandlerThreadPoolConfig.getExecutor(groupId);
            threadPoolUtils.register(executor);

            taskPendingHolder.put(groupId, executor);
        }
    }

    /**
     * 得到对应的线程池
     *
     * @param groupId
     * @return
     */
    public ExecutorService route(String groupId) {
        return taskPendingHolder.get(groupId);
    }
}
