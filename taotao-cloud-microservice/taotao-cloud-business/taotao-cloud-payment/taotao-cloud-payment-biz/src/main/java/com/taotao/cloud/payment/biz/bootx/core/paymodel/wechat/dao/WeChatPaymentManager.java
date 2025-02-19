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

package com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.dao;

import cn.bootx.common.mybatisplus.impl.BaseManager;
import cn.bootx.payment.core.paymodel.wechat.entity.WeChatPayment;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 微信支付记录
 *
 * @author xxm
 * @date 2021/6/21
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class WeChatPaymentManager extends BaseManager<WeChatPaymentMapper, WeChatPayment> {

    public Optional<WeChatPayment> findByPaymentId(Long paymentId) {
        return findByField(WeChatPayment::getPaymentId, paymentId);
    }
}
