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

package com.taotao.cloud.payment.biz.jeepay.core.exception;

import com.taotao.cloud.payment.biz.jeepay.core.constants.ApiCodeEnum;
import com.taotao.cloud.payment.biz.jeepay.core.model.ApiRes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/*
 * 异常信息自定义返回数据
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/8 16:30
 */
@Configuration
public class BizExceptionResolver implements HandlerExceptionResolver {

    private Logger logger = LogManager.getLogger(BizExceptionResolver.class);

    @Override
    public ModelAndView resolveException(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {

        // 是否包含ss框架
        boolean hasSpringSecurity = false;
        try {
            hasSpringSecurity =
                    Class.forName("org.springframework.security.access.AccessDeniedException")
                            != null;
        } catch (Exception e) {
        }

        String outPutJson;

        // 业务异常
        if (ex instanceof BizException) {
            logger.error("公共捕捉[Biz]异常：{}", ex.getMessage());
            outPutJson = ((BizException) ex).getApiRes().toJSONString();
        } else if (ex instanceof DataAccessException) {
            logger.error("公共捕捉[DataAccessException]异常：", ex);
            outPutJson = ApiRes.fail(ApiCodeEnum.DB_ERROR).toJSONString();
        } else if (hasSpringSecurity
                && ex instanceof org.springframework.security.access.AccessDeniedException) {
            logger.error("公共捕捉[AccessDeniedException]异常：", ex);
            outPutJson =
                    ApiRes.fail(ApiCodeEnum.SYS_PERMISSION_ERROR, ex.getMessage()).toJSONString();
        } else {
            logger.error("公共捕捉[Exception]异常：", ex);
            outPutJson = ApiRes.fail(ApiCodeEnum.SYSTEM_ERROR, ex.getMessage()).toJSONString();
        }

        try {
            this.outPutJson(response, outPutJson);
        } catch (IOException e) {
            logger.error("输出错误信息异常:", e);
        }

        return new ModelAndView();
    }

    public void outPutJson(HttpServletResponse res, String jsonStr) throws IOException {

        res.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        res.getWriter().write(jsonStr);
        res.getWriter().flush();
        res.getWriter().close();
    }
}
