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

package com.taotao.cloud.payment.biz.jeepay.mq.vender.rabbitmq;

import com.jeequan.jeepay.components.mq.constant.MQSendTypeEnum;
import com.jeequan.jeepay.components.mq.constant.MQVenderCS;
import com.jeequan.jeepay.components.mq.model.AbstractMQ;
import com.jeequan.jeepay.components.mq.vender.IMQSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * rabbitMQ 消息发送器的实现
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/7/23 16:52
 */
@Component
@ConditionalOnProperty(name = MQVenderCS.YML_VENDER_KEY, havingValue = MQVenderCS.RABBIT_MQ)
public class RabbitMQSender implements IMQSender {

    @Autowired private RabbitTemplate rabbitTemplate;

    @Override
    public void send(AbstractMQ mqModel) {

        if (mqModel.getMQType() == MQSendTypeEnum.QUEUE) {

            rabbitTemplate.convertAndSend(mqModel.getMQName(), mqModel.toMessage());
        } else {

            // fanout模式 的 routeKEY 没意义。
            this.rabbitTemplate.convertAndSend(
                    RabbitMQConfig.FANOUT_EXCHANGE_NAME_PREFIX + mqModel.getMQName(),
                    null,
                    mqModel.toMessage());
        }
    }

    @Override
    public void send(AbstractMQ mqModel, int delay) {

        if (mqModel.getMQType() == MQSendTypeEnum.QUEUE) {

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.DELAYED_EXCHANGE_NAME,
                    mqModel.getMQName(),
                    mqModel.toMessage(),
                    messagePostProcessor -> {
                        messagePostProcessor
                                .getMessageProperties()
                                .setDelay(Math.toIntExact(delay * 1000));
                        return messagePostProcessor;
                    });
        } else {

            // fanout模式 的 routeKEY 没意义。  没有延迟属性
            this.rabbitTemplate.convertAndSend(
                    RabbitMQConfig.FANOUT_EXCHANGE_NAME_PREFIX + mqModel.getMQName(),
                    null,
                    mqModel.toMessage());
        }
    }
}
