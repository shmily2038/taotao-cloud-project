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

package com.taotao.cloud.wechat.biz.module.cp.handler;

import java.util.Map;
import me.chanjar.weixin.common.api.WxConsts.MenuButtonType;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.message.WxCpXmlMessage;
import me.chanjar.weixin.cp.bean.message.WxCpXmlOutMessage;
import org.springframework.stereotype.Component;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Component
public class MenuHandler extends AbstractHandler {

    @Override
    public WxCpXmlOutMessage handle(
            WxCpXmlMessage wxMessage,
            Map<String, Object> context,
            WxCpService cpService,
            WxSessionManager sessionManager) {

        String msg =
                String.format(
                        "type:%s, event:%s, key:%s",
                        wxMessage.getMsgType(), wxMessage.getEvent(), wxMessage.getEventKey());
        if (MenuButtonType.VIEW.equals(wxMessage.getEvent())) {
            return null;
        }

        return WxCpXmlOutMessage.TEXT()
                .content(msg)
                .fromUser(wxMessage.getToUserName())
                .toUser(wxMessage.getFromUserName())
                .build();
    }
}
