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

package com.taotao.cloud.auth.biz.demo.authentication.oidc;

import cn.herodotus.engine.assistant.core.definition.constants.BaseConstants;
import java.util.*;
import java.util.function.Function;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Description: TODO
 *
 * @author : gengwei.zheng
 * @date : 2022/10/15 11:58
 */
public class HerodotusOidcUserInfoMapper
        implements Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {

    private static final List<String> EMAIL_CLAIMS =
            Arrays.asList(StandardClaimNames.EMAIL, StandardClaimNames.EMAIL_VERIFIED);
    private static final List<String> PHONE_CLAIMS =
            Arrays.asList(
                    StandardClaimNames.PHONE_NUMBER, StandardClaimNames.PHONE_NUMBER_VERIFIED);
    private static final List<String> PROFILE_CLAIMS =
            Arrays.asList(
                    StandardClaimNames.NAME,
                    StandardClaimNames.FAMILY_NAME,
                    StandardClaimNames.GIVEN_NAME,
                    StandardClaimNames.MIDDLE_NAME,
                    StandardClaimNames.NICKNAME,
                    StandardClaimNames.PREFERRED_USERNAME,
                    StandardClaimNames.PROFILE,
                    StandardClaimNames.PICTURE,
                    StandardClaimNames.WEBSITE,
                    StandardClaimNames.GENDER,
                    StandardClaimNames.BIRTHDATE,
                    StandardClaimNames.ZONEINFO,
                    StandardClaimNames.LOCALE,
                    StandardClaimNames.UPDATED_AT);

    @Override
    public OidcUserInfo apply(OidcUserInfoAuthenticationContext authenticationContext) {
        OidcUserInfoAuthenticationToken authentication = authenticationContext.getAuthentication();
        if (authentication.getPrincipal() instanceof BearerTokenAuthentication) {
            BearerTokenAuthentication principal =
                    (BearerTokenAuthentication) authentication.getPrincipal();
            return new OidcUserInfo(getClaims(principal.getTokenAttributes()));
        } else {
            JwtAuthenticationToken principal =
                    (JwtAuthenticationToken) authentication.getPrincipal();
            return new OidcUserInfo(getClaims(principal.getToken().getClaims()));
        }
    }

    private static Map<String, Object> getClaims(Map<String, Object> claims) {
        Set<String> needRemovedClaims = new HashSet<>(32);
        needRemovedClaims.add(BaseConstants.AUTHORITIES);

        Map<String, Object> requestedClaims = new HashMap<>(claims);
        requestedClaims.keySet().removeIf(needRemovedClaims::contains);

        return requestedClaims;
    }
}
