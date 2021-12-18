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
package com.taotao.cloud.sys.biz.controller.backend;

import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.sys.api.dto.dict_item.DictItemSaveDTO;
import com.taotao.cloud.sys.api.dto.dict_item.DictItemUpdateDTO;
import com.taotao.cloud.sys.api.vo.dict_item.DictItemQueryVO;
import com.taotao.cloud.sys.biz.entity.DictItem;
import com.taotao.cloud.sys.biz.service.ISysDictItemService;
import com.taotao.cloud.web.base.controller.SuperController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台管理-字典项管理API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 14:58:55
 */
@Validated
@RestController
@RequestMapping("/backend/dict/item")
@Tag(name = "后台管理-字典项管理API", description = "后台管理-字典项管理API")
public class BackendDictItemController extends
	SuperController<ISysDictItemService, DictItem, Long, BaseQuery, DictItemSaveDTO, DictItemUpdateDTO, DictItemQueryVO> {


}
