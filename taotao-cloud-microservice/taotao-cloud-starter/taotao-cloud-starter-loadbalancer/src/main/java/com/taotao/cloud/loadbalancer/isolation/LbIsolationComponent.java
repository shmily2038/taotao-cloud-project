 /*
  * Copyright 2017-2020 original authors
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  * https://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
 package com.taotao.cloud.loadbalancer.isolation;

 import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
 import org.springframework.cloud.client.ServiceInstance;
 import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
 import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
 import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
 import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
 import org.springframework.context.annotation.Bean;
 import org.springframework.core.env.Environment;

 /**
  * Ribbon扩展配置类
  *
  * @author shuigedeng
  * @version 1.0.0
  * @since 2020/6/15 11:31
  */
 @ConditionalOnProperty(value = "taotao.cloud.ribbon.isolation.enabled", havingValue = "true")
 @LoadBalancerClient
 public class LbIsolationComponent {

	 @Bean
	 public ReactorLoadBalancer<ServiceInstance> reactorServiceInstanceLoadBalancer(
		 Environment environment,
		 LoadBalancerClientFactory loadBalancerClientFactory) {
		 String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		 return new CustomLoadBalancer(
			 loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class));
	 }

 }
