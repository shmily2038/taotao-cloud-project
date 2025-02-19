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

package com.taotao.cloud.auth.biz.demo.authentication.provider;

import cn.herodotus.engine.oauth2.authentication.utils.OAuth2AuthenticationProviderUtils;
import cn.herodotus.engine.oauth2.authentication.utils.OAuth2EndpointUtils;
import cn.herodotus.engine.oauth2.core.constants.OAuth2ErrorCodes;
import cn.herodotus.engine.oauth2.core.definition.service.EnhanceUserDetailsService;
import cn.herodotus.engine.oauth2.core.exception.AccountEndpointLimitedException;
import cn.herodotus.engine.oauth2.core.properties.OAuth2ComplianceProperties;
import cn.herodotus.engine.oauth2.data.jpa.storage.JpaOAuth2AuthorizationService;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.util.Assert;

/**
 * Description: 抽象的用户认证Provider
 *
 * <p>提取公共的用户通用基类，方便涉及用户校验的自定义认证模式使用
 *
 * @author : gengwei.zheng
 * @date : 2022/7/6 16:07
 * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
 */
public abstract class AbstractUserDetailsAuthenticationProvider
        extends AbstractAuthenticationProvider {

    private static final Logger log =
            LoggerFactory.getLogger(AbstractUserDetailsAuthenticationProvider.class);

    private final MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();
    private final UserDetailsService userDetailsService;
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2ComplianceProperties complianceProperties;
    private PasswordEncoder passwordEncoder;

    public AbstractUserDetailsAuthenticationProvider(
            OAuth2AuthorizationService authorizationService,
            UserDetailsService userDetailsService,
            OAuth2ComplianceProperties complianceProperties) {
        this.userDetailsService = userDetailsService;
        this.authorizationService = authorizationService;
        this.complianceProperties = complianceProperties;
        setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
    }

    public EnhanceUserDetailsService getUserDetailsService() {
        return (EnhanceUserDetailsService) userDetailsService;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    protected abstract void additionalAuthenticationChecks(
            UserDetails userDetails, Map<String, Object> additionalParameters)
            throws AuthenticationException;

    protected abstract UserDetails retrieveUser(Map<String, Object> additionalParameters)
            throws AuthenticationException;

    private Authentication authenticateUserDetails(
            Map<String, Object> additionalParameters, String registeredClientId)
            throws AuthenticationException {
        UserDetails user = retrieveUser(additionalParameters);

        if (!user.isAccountNonLocked()) {
            log.debug("[Herodotus] |- Failed to authenticate since user account is locked");
            throw new LockedException(
                    messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.locked",
                            "User account is locked"));
        }
        if (!user.isEnabled()) {
            log.debug("[Herodotus] |- Failed to authenticate since user account is disabled");
            throw new DisabledException(
                    messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.disabled",
                            "User is disabled"));
        }
        if (!user.isAccountNonExpired()) {
            log.debug("[Herodotus] |- Failed to authenticate since user account has expired");
            throw new AccountExpiredException(
                    messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.expired",
                            "User account has expired"));
        }

        additionalAuthenticationChecks(user, additionalParameters);

        if (!user.isCredentialsNonExpired()) {
            log.debug(
                    "[Herodotus] |- Failed to authenticate since user account credentials have"
                            + " expired");
            throw new CredentialsExpiredException(
                    messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.credentialsExpired",
                            "User credentials have expired"));
        }

        if (complianceProperties.getSignInEndpointLimited().getEnabled()
                && !complianceProperties.getSignInKickOutLimited().getEnabled()) {
            if (authorizationService instanceof JpaOAuth2AuthorizationService) {
                JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService =
                        (JpaOAuth2AuthorizationService) authorizationService;
                int count =
                        jpaOAuth2AuthorizationService.findAuthorizationCount(
                                registeredClientId, user.getUsername());
                if (count >= complianceProperties.getSignInEndpointLimited().getMaximum()) {
                    throw new AccountEndpointLimitedException(
                            "Use same endpoint signIn exceed limit");
                }
            }
        }

        if (!complianceProperties.getSignInEndpointLimited().getEnabled()
                && complianceProperties.getSignInKickOutLimited().getEnabled()) {
            if (authorizationService instanceof JpaOAuth2AuthorizationService) {
                JpaOAuth2AuthorizationService jpaOAuth2AuthorizationService =
                        (JpaOAuth2AuthorizationService) authorizationService;
                List<OAuth2Authorization> authorizations =
                        jpaOAuth2AuthorizationService.findAvailableAuthorizations(
                                registeredClientId, user.getUsername());
                if (CollectionUtils.isNotEmpty(authorizations)) {
                    authorizations.forEach(
                            authorization -> {
                                OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                                        authorization.getToken(OAuth2RefreshToken.class);
                                if (ObjectUtils.isNotEmpty(refreshToken)) {
                                    authorization =
                                            OAuth2AuthenticationProviderUtils.invalidate(
                                                    authorization, refreshToken.getToken());
                                }
                                log.debug(
                                        "[Herodotus] |- Sign in user [{}] with token id [{}] will"
                                                + " be kicked out.",
                                        user.getUsername(),
                                        authorization.getId());
                                jpaOAuth2AuthorizationService.save(authorization);
                            });
                }
            }
        }

        return new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities());
    }

    protected Authentication getUsernamePasswordAuthentication(
            Map<String, Object> additionalParameters, String registeredClientId)
            throws AuthenticationException {
        Authentication authentication = null;
        try {
            authentication = authenticateUserDetails(additionalParameters, registeredClientId);
        } catch (AccountStatusException ase) {
            // covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
            String exceptionName = ase.getClass().getSimpleName();
            OAuth2EndpointUtils.throwError(
                    exceptionName,
                    ase.getMessage(),
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        } catch (BadCredentialsException bce) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.BAD_CREDENTIALS,
                    bce.getMessage(),
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        } catch (UsernameNotFoundException unfe) {
            OAuth2EndpointUtils.throwError(
                    OAuth2ErrorCodes.USERNAME_NOT_FOUND,
                    unfe.getMessage(),
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        return authentication;
    }
}
