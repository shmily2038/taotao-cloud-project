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

package com.taotao.cloud.auth.biz.demo.starter.configuration;

import cn.herodotus.engine.assistant.core.utils.ResourceUtils;
import cn.herodotus.engine.oauth2.authentication.customizer.HerodotusJwtTokenCustomizer;
import cn.herodotus.engine.oauth2.authentication.customizer.HerodotusOpaqueTokenCustomizer;
import cn.herodotus.engine.oauth2.authentication.oidc.HerodotusOidcUserInfoMapper;
import cn.herodotus.engine.oauth2.authentication.provider.*;
import cn.herodotus.engine.oauth2.authentication.response.DefaultOAuth2AuthenticationEventPublisher;
import cn.herodotus.engine.oauth2.authentication.response.HerodotusAuthenticationFailureHandler;
import cn.herodotus.engine.oauth2.authentication.response.HerodotusAuthenticationSuccessHandler;
import cn.herodotus.engine.oauth2.authentication.utils.OAuth2ConfigurerUtils;
import cn.herodotus.engine.oauth2.authorization.customizer.HerodotusTokenStrategyConfigurer;
import cn.herodotus.engine.oauth2.core.definition.service.ClientDetailsService;
import cn.herodotus.engine.oauth2.core.enums.Certificate;
import cn.herodotus.engine.oauth2.core.properties.OAuth2ComplianceProperties;
import cn.herodotus.engine.oauth2.core.properties.OAuth2Properties;
import cn.herodotus.engine.rest.protect.crypto.processor.HttpCryptoProcessor;
import cn.herodotus.engine.rest.protect.tenant.MultiTenantFilter;
import cn.herodotus.engine.web.core.properties.EndpointProperties;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.UUID;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Description: 认证服务器配置
 *
 * <p>1. 权限核心处理 {@link org.springframework.security.web.access.intercept.FilterSecurityInterceptor}
 * 2. 默认的权限判断 {@link org.springframework.security.access.vote.AffirmativeBased} 3. 模式决策 {@link
 * org.springframework.security.authentication.ProviderManager}
 *
 * @author : gengwei.zheng
 * @date : 2022/2/12 20:57
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfiguration {

    private static final Logger log =
            LoggerFactory.getLogger(AuthorizationServerConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [OAuth2 Authorization Server] Auto Configure.");
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity httpSecurity,
            ClientDetailsService clientDetailsService,
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            HttpCryptoProcessor httpCryptoProcessor,
            OAuth2ComplianceProperties complianceProperties,
            HerodotusTokenStrategyConfigurer herodotusTokenStrategyConfigurer)
            throws Exception {

        log.debug(
                "[Herodotus] |- Core [Authorization Server Security Filter Chain] Auto Configure.");

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                new OAuth2AuthorizationServerConfigurer();
        httpSecurity.apply(authorizationServerConfigurer);

        HerodotusAuthenticationFailureHandler failureHandler =
                new HerodotusAuthenticationFailureHandler();
        authorizationServerConfigurer.tokenRevocationEndpoint(
                endpoint -> endpoint.errorResponseHandler(failureHandler));
        authorizationServerConfigurer.tokenIntrospectionEndpoint(
                endpoint -> endpoint.errorResponseHandler(failureHandler));
        authorizationServerConfigurer.clientAuthentication(
                endpoint -> endpoint.errorResponseHandler(failureHandler));
        authorizationServerConfigurer.authorizationEndpoint(
                endpoint -> {
                    endpoint.errorResponseHandler(failureHandler);
                    endpoint.consentPage("/oauth2/consent");
                });
        authorizationServerConfigurer.tokenEndpoint(
                endpoint -> {
                    AuthenticationConverter authenticationConverter =
                            new DelegatingAuthenticationConverter(
                                    Arrays.asList(
                                            new OAuth2AuthorizationCodeAuthenticationConverter(),
                                            new OAuth2RefreshTokenAuthenticationConverter(),
                                            new OAuth2ClientCredentialsAuthenticationConverter(),
                                            new OAuth2ResourceOwnerPasswordAuthenticationConverter(
                                                    httpCryptoProcessor),
                                            new OAuth2SocialCredentialsAuthenticationConverter(
                                                    httpCryptoProcessor)));
                    endpoint.accessTokenRequestConverter(authenticationConverter);
                    endpoint.errorResponseHandler(failureHandler);
                    endpoint.accessTokenResponseHandler(
                            new HerodotusAuthenticationSuccessHandler(httpCryptoProcessor));
                });

        // 使用自定义的 AuthenticationProvider 替换已有 AuthenticationProvider
        authorizationServerConfigurer.withObjectPostProcessor(
                new ObjectPostProcessor<AuthenticationProvider>() {
                    @Override
                    public <O extends AuthenticationProvider> O postProcess(O object) {
                        OAuth2AuthorizationService authorizationService =
                                OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity);

                        if (org.springframework.security.oauth2.server.authorization.authentication
                                .OAuth2AuthorizationCodeAuthenticationProvider.class
                                .isAssignableFrom(object.getClass())) {
                            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator =
                                    OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);
                            OAuth2AuthorizationCodeAuthenticationProvider provider =
                                    new OAuth2AuthorizationCodeAuthenticationProvider(
                                            authorizationService, tokenGenerator);
                            log.debug(
                                    "[Herodotus] |- Custom"
                                            + " OAuth2AuthorizationCodeAuthenticationProvider is in"
                                            + " effect!");
                            return (O) provider;
                        }

                        if (org.springframework.security.oauth2.server.authorization.authentication
                                .OAuth2ClientCredentialsAuthenticationProvider.class
                                .isAssignableFrom(object.getClass())) {
                            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator =
                                    OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);
                            OAuth2ClientCredentialsAuthenticationProvider provider =
                                    new OAuth2ClientCredentialsAuthenticationProvider(
                                            authorizationService,
                                            tokenGenerator,
                                            clientDetailsService);
                            log.debug(
                                    "[Herodotus] |- Custom"
                                            + " OAuth2ClientCredentialsAuthenticationProvider is in"
                                            + " effect!");
                            return (O) provider;
                        }
                        return object;
                    }
                });

        authorizationServerConfigurer.oidc(
                oidc -> {
                    oidc.clientRegistrationEndpoint(Customizer.withDefaults());
                    oidc.userInfoEndpoint(
                            userInfo -> userInfo.userInfoMapper(new HerodotusOidcUserInfoMapper()));
                });

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
        // 仅拦截 OAuth2 Authorization Server 的相关 endpoint
        httpSecurity
                .securityMatcher(endpointsMatcher)
                // 开启请求认证
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                // 禁用对 OAuth2 Authorization Server 相关 endpoint 的 CSRF 防御
                .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
                .oauth2ResourceServer(
                        configurer -> herodotusTokenStrategyConfigurer.from(configurer));

        // 这里增加 DefaultAuthenticationEventPublisher 配置，是为了解决 ProviderManager
        // 在初次使用时，外部定义DefaultAuthenticationEventPublisher 不会注入问题
        // 外部注入DefaultAuthenticationEventPublisher是标准配置方法，两处都保留是为了保险，还需要深入研究才能决定去掉哪个。
        AuthenticationManagerBuilder authenticationManagerBuilder =
                httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        ApplicationContext applicationContext =
                httpSecurity.getSharedObject(ApplicationContext.class);
        authenticationManagerBuilder.authenticationEventPublisher(
                new DefaultOAuth2AuthenticationEventPublisher(applicationContext));

        // build() 方法会让以上所有的配置生效
        SecurityFilterChain securityFilterChain =
                httpSecurity
                        .formLogin(Customizer.withDefaults())
                        .sessionManagement(Customizer.withDefaults())
                        .addFilterBefore(new MultiTenantFilter(), AuthorizationFilter.class)
                        .build();

        // 增加新的、自定义 OAuth2 Granter
        OAuth2AuthorizationService authorizationService =
                OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity);
        OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator =
                OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);

        OAuth2ResourceOwnerPasswordAuthenticationProvider
                resourceOwnerPasswordAuthenticationProvider =
                        new OAuth2ResourceOwnerPasswordAuthenticationProvider(
                                authorizationService,
                                tokenGenerator,
                                userDetailsService,
                                complianceProperties);
        resourceOwnerPasswordAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        httpSecurity.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);

        OAuth2SocialCredentialsAuthenticationProvider socialCredentialsAuthenticationProvider =
                new OAuth2SocialCredentialsAuthenticationProvider(
                        authorizationService,
                        tokenGenerator,
                        userDetailsService,
                        complianceProperties);
        httpSecurity.authenticationProvider(socialCredentialsAuthenticationProvider);

        return securityFilterChain;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(OAuth2Properties oAuth2Properties)
            throws NoSuchAlgorithmException {

        OAuth2Properties.Jwk jwk = oAuth2Properties.getJwk();

        KeyPair keyPair = null;
        if (jwk.getCertificate() == Certificate.CUSTOM) {
            try {
                Resource[] resource = ResourceUtils.getResources(jwk.getJksKeyStore());
                if (ArrayUtils.isNotEmpty(resource)) {
                    KeyStoreKeyFactory keyStoreKeyFactory =
                            new KeyStoreKeyFactory(
                                    resource[0], jwk.getJksStorePassword().toCharArray());
                    keyPair =
                            keyStoreKeyFactory.getKeyPair(
                                    jwk.getJksKeyAlias(), jwk.getJksKeyPassword().toCharArray());
                }
            } catch (IOException e) {
                log.error("[Herodotus] |- Read custom certificate under resource folder error!", e);
            }

        } else {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }

        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey =
                new RSAKey.Builder(publicKey)
                        .privateKey(privateKey)
                        .keyID(UUID.randomUUID().toString())
                        .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    /** jwt 解码 */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        HerodotusJwtTokenCustomizer herodotusJwtTokenCustomizer = new HerodotusJwtTokenCustomizer();
        log.trace("[Herodotus] |- Bean [OAuth2 Jwt Token Customizer] Auto Configure.");
        return herodotusJwtTokenCustomizer;
    }

    @Bean
    public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> opaqueTokenCustomizer() {
        HerodotusOpaqueTokenCustomizer herodotusOpaqueTokenCustomizer =
                new HerodotusOpaqueTokenCustomizer();
        log.trace("[Herodotus] |- Bean [OAuth2 Opaque Token Customizer] Auto Configure.");
        return herodotusOpaqueTokenCustomizer;
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(
            EndpointProperties endpointProperties) {
        return AuthorizationServerSettings.builder()
                .issuer(endpointProperties.getIssuerUri())
                .authorizationEndpoint(endpointProperties.getAuthorizationEndpoint())
                .tokenEndpoint(endpointProperties.getAccessTokenEndpoint())
                .tokenRevocationEndpoint(endpointProperties.getTokenRevocationEndpoint())
                .tokenIntrospectionEndpoint(endpointProperties.getTokenIntrospectionEndpoint())
                .jwkSetEndpoint(endpointProperties.getJwkSetEndpoint())
                .oidcUserInfoEndpoint(endpointProperties.getOidcUserInfoEndpoint())
                .oidcClientRegistrationEndpoint(
                        endpointProperties.getOidcClientRegistrationEndpoint())
                .build();
    }
}
