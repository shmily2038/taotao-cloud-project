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

package com.taotao.cloud.auth.biz.demo.authentication.customizer;

import com.taotao.cloud.auth.biz.demo.core.definition.domain.HerodotusUser;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;

/**
 * Description: TokenCustomizer 通用处理
 *
 * @author : gengwei.zheng
 * @date : 2022/10/12 10:20
 */
public abstract class AbstractTokenCustomizer {

    protected void appendAll(
            Map<String, Object> attributes,
            Authentication authentication,
            Set<String> authorizedScopes) {

        appendAuthorities(attributes, authentication);
        appendCommons(attributes, authentication, authorizedScopes);
    }

    protected void appendAuthorities(
            Map<String, Object> attributes, Authentication authentication) {

        if (CollectionUtils.isNotEmpty(authentication.getAuthorities())) {
            Set<String> authorities =
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toSet());
            attributes.put(BaseConstants.AUTHORITIES, authorities);
        }
    }

    protected void appendCommons(
            Map<String, Object> attributes,
            Authentication authentication,
            Set<String> authorizedScopes) {

        if (CollectionUtils.isNotEmpty(authorizedScopes)) {
            attributes.put(OAuth2ParameterNames.SCOPE, authorizedScopes);
        }

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            HerodotusUser principal = (HerodotusUser) authentication.getPrincipal();
            putUserInfo(attributes, principal);
        }

        if (authentication instanceof OAuth2AccessTokenAuthenticationToken) {
            Object details = authentication.getDetails();
            if (ObjectUtils.isNotEmpty(details) && details instanceof HerodotusUser) {
                HerodotusUser principal = (HerodotusUser) details;
                putUserInfo(attributes, principal);
            }
        }

        attributes.put("license", "Apache-2.0 Licensed | Copyright © 2020-2023 码 匠 君");
    }

    private void putUserInfo(Map<String, Object> attributes, HerodotusUser principal) {
        attributes.put(BaseConstants.OPEN_ID, principal.getUserId());
        attributes.put(BaseConstants.ROLES, principal.getRoles());
        attributes.put(BaseConstants.AVATAR, principal.getAvatar());
        attributes.put(BaseConstants.EMPLOYEE_ID, principal.getEmployeeId());
    }
}
