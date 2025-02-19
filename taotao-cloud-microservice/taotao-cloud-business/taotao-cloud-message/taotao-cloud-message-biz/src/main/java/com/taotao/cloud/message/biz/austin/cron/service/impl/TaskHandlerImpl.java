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

package com.taotao.cloud.message.biz.austin.cron.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.message.biz.austin.cron.csv.CountFileRowHandler;
import com.taotao.cloud.message.biz.austin.cron.pending.CrowdBatchTaskPending;
import com.taotao.cloud.message.biz.austin.cron.service.TaskHandler;
import com.taotao.cloud.message.biz.austin.cron.utils.ReadFileUtils;
import com.taotao.cloud.message.biz.austin.cron.vo.CrowdInfoVo;
import com.taotao.cloud.message.biz.austin.support.dao.MessageTemplateDao;
import com.taotao.cloud.message.biz.austin.support.domain.MessageTemplate;
import com.taotao.cloud.message.biz.austin.support.pending.AbstractLazyPending;
import java.util.HashMap;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author 3y
 * @date 2022/2/9
 */
@Service
@Slf4j
public class TaskHandlerImpl implements TaskHandler {

    @Autowired private MessageTemplateDao messageTemplateDao;

    @Autowired private ApplicationContext context;

    @Override
    public void handle(Long messageTemplateId) {

        MessageTemplate messageTemplate =
                messageTemplateDao.findById(messageTemplateId).orElse(null);
        if (Objects.isNull(messageTemplate)) {
            return;
        }
        if (StrUtil.isBlank(messageTemplate.getCronCrowdPath())) {
            log.error(
                    "TaskHandler#handle crowdPath empty! messageTemplateId:{}", messageTemplateId);
            return;
        }

        // 1. 获取文件行数大小
        long countCsvRow =
                ReadFileUtils.countCsvRow(
                        messageTemplate.getCronCrowdPath(), new CountFileRowHandler());

        // 2. 读取文件得到每一行记录给到队列做lazy batch处理
        CrowdBatchTaskPending crowdBatchTaskPending = context.getBean(CrowdBatchTaskPending.class);
        ReadFileUtils.getCsvRow(
                messageTemplate.getCronCrowdPath(),
                row -> {
                    if (CollUtil.isEmpty(row.getFieldMap())
                            || StrUtil.isBlank(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))) {
                        return;
                    }

                    // 3. 每一行处理交给LazyPending
                    HashMap<String, String> params =
                            ReadFileUtils.getParamFromLine(row.getFieldMap());
                    CrowdInfoVo crowdInfoVo =
                            CrowdInfoVo.builder()
                                    .receiver(row.getFieldMap().get(ReadFileUtils.RECEIVER_KEY))
                                    .params(params)
                                    .messageTemplateId(messageTemplateId)
                                    .build();
                    crowdBatchTaskPending.pending(crowdInfoVo);

                    // 4. 判断是否读取文件完成回收资源且更改状态
                    onComplete(row, countCsvRow, crowdBatchTaskPending, messageTemplateId);
                });
    }

    /**
     * 文件遍历结束时 1. 暂停单线程池消费(最后会回收线程池资源) 2. 更改消息模板的状态(暂未实现)
     *
     * @param row
     * @param countCsvRow
     * @param crowdBatchTaskPending
     * @param messageTemplateId
     */
    private void onComplete(
            CsvRow row,
            long countCsvRow,
            AbstractLazyPending crowdBatchTaskPending,
            Long messageTemplateId) {
        if (row.getOriginalLineNumber() == countCsvRow) {
            crowdBatchTaskPending.setStop(true);
            log.info("messageTemplate:[{}] read csv file complete!", messageTemplateId);
        }
    }
}
