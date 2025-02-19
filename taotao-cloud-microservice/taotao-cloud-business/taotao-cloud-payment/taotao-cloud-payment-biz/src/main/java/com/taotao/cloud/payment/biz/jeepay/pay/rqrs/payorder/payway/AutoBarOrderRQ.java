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

package com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.payway;

import com.taotao.cloud.payment.biz.jeepay.pay.rqrs.payorder.UnifiedOrderRQ;
import lombok.Data;

/*
 * 支付方式： AUTO_BAR
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/8 17:34
 */
@Data
public class AutoBarOrderRQ extends UnifiedOrderRQ {

    /** 条码值 * */
    private String authCode;

    /** 构造函数 * */
    public AutoBarOrderRQ() {
        this.setWayCode(CS.PAY_WAY_CODE.AUTO_BAR);
    }
}
