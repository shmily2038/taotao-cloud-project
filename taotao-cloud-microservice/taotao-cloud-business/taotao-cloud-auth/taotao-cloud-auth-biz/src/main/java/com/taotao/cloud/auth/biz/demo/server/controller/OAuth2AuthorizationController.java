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

package com.taotao.cloud.auth.biz.demo.server.controller;

import cn.herodotus.engine.data.core.service.WriteableService;
import cn.herodotus.engine.oauth2.data.jpa.entity.HerodotusAuthorization;
import cn.herodotus.engine.oauth2.data.jpa.service.HerodotusAuthorizationService;
import cn.herodotus.engine.rest.core.controller.BaseWriteableRestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description: OAuth2 认证管理接口
 *
 * @author : gengwei.zheng
 * @date : 2022/3/1 18:52
 */
@RestController
@RequestMapping("/authorize/authorization")
@Tags({@Tag(name = "OAuth2 认证服务接口"), @Tag(name = "OAuth2 认证管理接口")})
public class OAuth2AuthorizationController
        extends BaseWriteableRestController<HerodotusAuthorization, String> {

    private final HerodotusAuthorizationService herodotusAuthorizationService;

    @Autowired
    public OAuth2AuthorizationController(
            HerodotusAuthorizationService herodotusAuthorizationService) {
        this.herodotusAuthorizationService = herodotusAuthorizationService;
    }

    @Override
    public WriteableService<HerodotusAuthorization, String> getWriteableService() {
        return this.herodotusAuthorizationService;
    }
}
