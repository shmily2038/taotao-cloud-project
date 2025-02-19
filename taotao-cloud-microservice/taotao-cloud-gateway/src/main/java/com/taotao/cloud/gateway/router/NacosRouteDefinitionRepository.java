//package com.taotao.cloud.gateway.router;
//
//import cn.hutool.core.collection.CollectionUtil;
//import cn.hutool.json.JSONUtil;
//import com.alibaba.cloud.nacos.NacosConfigManager;
//import com.alibaba.cloud.nacos.NacosConfigProperties;
//import com.alibaba.nacos.api.config.ConfigService;
//import com.alibaba.nacos.api.config.listener.Listener;
//import com.alibaba.nacos.api.exception.NacosException;
//import com.github.benmanes.caffeine.cache.Cache;
//import jakarta.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//import lombok.RequiredArgsConstructor;
//import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
//import org.springframework.cloud.gateway.route.RouteDefinition;
//import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.context.ApplicationEventPublisherAware;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
///**
// * https://github.com/alibaba/spring-cloud-alibaba/blob/2.2.x/spring-cloud-alibaba-examples/nacos-example/nacos-config-example/src/main/java/com/alibaba/cloud/examples/example/ConfigListenerExample.java
// */
//@Component
//@RequiredArgsConstructor
//public class NacosRouteDefinitionRepository implements RouteDefinitionRepository,
//	ApplicationEventPublisherAware {
//
//	private static final String DATA_ID = "router.json";
//
//	private final NacosConfigManager nacosConfigManager;
//
//	private final NacosConfigProperties nacosConfigProperties;
//
//	private final Cache<String, RouteDefinition> caffeineCache;
//
//	private ApplicationEventPublisher applicationEventPublisher;
//
//	@PostConstruct
//	public void init() throws NacosException {
//		String group = nacosConfigProperties.getGroup();
//		ConfigService configService = nacosConfigManager.getConfigService();
//		configService.addListener(DATA_ID, group, new Listener() {
//			@Override
//			public Executor getExecutor() {
//				return Executors.newSingleThreadExecutor();
//			}
//
//			@Override
//			public void receiveConfigInfo(String configInfo) {
//				// 清除缓存
//				caffeineCache.invalidateAll();
//				// 刷新事件
//				applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
//			}
//		});
//	}
//
//
//	@Override
//	public Flux<RouteDefinition> getRouteDefinitions() {
//		Collection<RouteDefinition> definitions = caffeineCache.asMap().values();
//		if (CollectionUtil.isEmpty(definitions)) {
//			try {
//				// pull nacos config info
//				String group = nacosConfigProperties.getGroup();
//				ConfigService configService = nacosConfigManager.getConfigService();
//				String configInfo = configService.getConfig(DATA_ID, group, 3000);
//				definitions = JSONUtil.toList(configInfo, RouteDefinition.class);
//				return Flux.fromIterable(definitions)
//					.doOnNext(route -> caffeineCache.put(route.getId(), route));
//			} catch (Exception e) {
//				return Flux.fromIterable(new ArrayList<>(0));
//			}
//		}
//		return Flux.fromIterable(definitions);
//	}
//
//	@Override
//	public Mono<Void> save(Mono<RouteDefinition> route) {
//		return Mono.empty();
//	}
//
//	@Override
//	public Mono<Void> delete(Mono<String> routeId) {
//		return Mono.empty();
//	}
//
//	@Override
//	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
//		this.applicationEventPublisher = applicationEventPublisher;
//	}
//}
