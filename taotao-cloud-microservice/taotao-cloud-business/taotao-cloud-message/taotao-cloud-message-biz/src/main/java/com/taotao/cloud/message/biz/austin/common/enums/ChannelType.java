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

package com.taotao.cloud.message.biz.austin.common.enums;

import com.taotao.cloud.message.biz.austin.common.dto.model.AlipayMiniProgramContentModel;
import com.taotao.cloud.message.biz.austin.common.dto.model.ContentModel;
import com.taotao.cloud.message.biz.austin.common.dto.model.DingDingRobotContentModel;
import com.taotao.cloud.message.biz.austin.common.dto.model.DingDingWorkContentModel;
import com.taotao.cloud.message.biz.austin.common.dto.model.EmailContentModel;
import com.taotao.cloud.message.biz.austin.common.dto.model.EnterpriseWeChatContentModel;
import com.taotao.cloud.message.biz.austin.common.dto.model.EnterpriseWeChatRobotContentModel;
import com.taotao.cloud.message.biz.austin.common.dto.model.FeiShuRobotContentModel;
import com.taotao.cloud.message.biz.austin.common.dto.model.ImContentModel;
import com.taotao.cloud.message.biz.austin.common.dto.model.MiniProgramContentModel;
import com.taotao.cloud.message.biz.austin.common.dto.model.OfficialAccountsContentModel;
import com.taotao.cloud.message.biz.austin.common.dto.model.PushContentModel;
import com.taotao.cloud.message.biz.austin.common.dto.model.SmsContentModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 发送渠道类型枚举
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum ChannelType {

    /** IM(站内信) -- 未实现该渠道 */
    IM(10, "IM(站内信)", ImContentModel.class, "im"),
    /** push(通知栏) --安卓 已接入 个推 */
    PUSH(20, "push(通知栏)", PushContentModel.class, "push"),
    /** sms(短信) -- 腾讯云、云片 */
    SMS(30, "sms(短信)", SmsContentModel.class, "sms"),
    /** email(邮件) -- QQ、163邮箱 */
    EMAIL(40, "email(邮件)", EmailContentModel.class, "email"),
    /** officialAccounts(微信服务号) -- 官方测试账号 */
    OFFICIAL_ACCOUNT(
            50, "officialAccounts(服务号)", OfficialAccountsContentModel.class, "official_accounts"),
    /** miniProgram(微信小程序) */
    MINI_PROGRAM(60, "miniProgram(小程序)", MiniProgramContentModel.class, "mini_program"),
    /** enterpriseWeChat(企业微信) */
    ENTERPRISE_WE_CHAT(
            70, "enterpriseWeChat(企业微信)", EnterpriseWeChatContentModel.class, "enterprise_we_chat"),
    /** dingDingRobot(钉钉机器人) */
    DING_DING_ROBOT(80, "dingDingRobot(钉钉机器人)", DingDingRobotContentModel.class, "ding_ding_robot"),
    /** dingDingWorkNotice(钉钉工作通知) */
    DING_DING_WORK_NOTICE(
            90,
            "dingDingWorkNotice(钉钉工作通知)",
            DingDingWorkContentModel.class,
            "ding_ding_work_notice"),
    /** enterpriseWeChat(企业微信机器人) */
    ENTERPRISE_WE_CHAT_ROBOT(
            100,
            "enterpriseWeChat(企业微信机器人)",
            EnterpriseWeChatRobotContentModel.class,
            "enterprise_we_chat_robot"),
    /** feiShuRoot(飞书机器人) */
    FEI_SHU_ROBOT(110, "feiShuRoot(飞书机器人)", FeiShuRobotContentModel.class, "fei_shu_robot"),
    /** alipayMiniProgram(支付宝小程序) */
    ALIPAY_MINI_PROGRAM(
            120,
            "alipayMiniProgram(支付宝小程序)",
            AlipayMiniProgramContentModel.class,
            "alipay_mini_program"),
    ;

    /** 编码值 */
    private final Integer code;

    /** 描述 */
    private final String description;

    /** 内容模型Class */
    private final Class<? extends ContentModel> contentModelClass;

    /** 英文标识 */
    private final String codeEn;

    /**
     * 通过code获取class
     *
     * @param code
     * @return
     */
    public static Class<? extends ContentModel> getChanelModelClassByCode(Integer code) {
        ChannelType[] values = values();
        for (ChannelType value : values) {
            if (value.getCode().equals(code)) {
                return value.getContentModelClass();
            }
        }
        return null;
    }

    /**
     * 通过code获取enum
     *
     * @param code
     * @return
     */
    public static ChannelType getEnumByCode(Integer code) {
        ChannelType[] values = values();
        for (ChannelType value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
