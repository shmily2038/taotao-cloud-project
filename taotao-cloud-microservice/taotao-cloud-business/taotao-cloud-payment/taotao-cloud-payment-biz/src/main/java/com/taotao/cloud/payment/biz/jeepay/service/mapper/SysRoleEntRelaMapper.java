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

package com.taotao.cloud.payment.biz.jeepay.service.mapper;

import com.taotao.cloud.payment.biz.jeepay.core.entity.SysRoleEntRela;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 系统角色权限关联表 Mapper 接口
 *
 * @author [mybatis plus generator]
 * @since 2020-06-13
 */
public interface SysRoleEntRelaMapper extends BaseSuperMapper<SysRoleEntRela> {

    List<String> selectEntIdsByUserId(
            @Param("userId") Long userId, @Param("sysType") String sysType);
}
