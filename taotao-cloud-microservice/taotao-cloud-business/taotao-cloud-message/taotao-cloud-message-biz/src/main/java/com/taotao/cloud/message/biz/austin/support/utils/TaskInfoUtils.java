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

package com.taotao.cloud.message.biz.austin.support.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.taotao.cloud.message.biz.austin.common.constant.CommonConstant;
import java.util.Date;

/**
 * 生成 消息推送的URL 工具类
 *
 * @author 3y
 */
public class TaskInfoUtils {

    private static final int TYPE_FLAG = 1000000;
    private static final String CODE = "track_code_bid";

    /** 生成BusinessId 模板类型+模板ID+当天日期 (固定16位) */
    public static Long generateBusinessId(Long templateId, Integer templateType) {
        Integer today = Integer.valueOf(DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN));
        return Long.valueOf(String.format("%d%s", templateType * TYPE_FLAG + templateId, today));
    }

    /** 第二到8位为MessageTemplateId 切割出模板ID */
    public static Long getMessageTemplateIdFromBusinessId(Long businessId) {
        return Long.valueOf(String.valueOf(businessId).substring(1, 8));
    }

    /** 从businessId切割出日期 */
    public static Long getDateFromBusinessId(Long businessId) {
        return Long.valueOf(String.valueOf(businessId).substring(8));
    }

    /** 对url添加平台参数（用于追踪数据) */
    public static String generateUrl(String url, Long templateId, Integer templateType) {
        url = url.trim();
        Long businessId = generateBusinessId(templateId, templateType);
        if (url.indexOf(CommonConstant.QM) == -1) {
            return url + CommonConstant.QM_STRING + CODE + CommonConstant.EQUAL_STRING + businessId;
        } else {
            return url
                    + CommonConstant.AND_STRING
                    + CODE
                    + CommonConstant.EQUAL_STRING
                    + businessId;
        }
    }
}
