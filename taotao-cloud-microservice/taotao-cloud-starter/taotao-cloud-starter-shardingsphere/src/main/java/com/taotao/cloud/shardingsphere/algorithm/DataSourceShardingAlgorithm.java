/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.shardingsphere.algorithm;

import java.util.Collection;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.sharding.api.sharding.hint.HintShardingValue;

/**
 * 数据库分源算法
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:45:49
 */
public class DataSourceShardingAlgorithm implements HintShardingAlgorithm {

	@Override
	public Collection<String> doSharding(Collection collection,
		HintShardingValue hintShardingValue) {
		return hintShardingValue.getValues();
	}

	@Override
	public void init() {

	}

	@Override
	public String getType() {
		return null;
	}
}
