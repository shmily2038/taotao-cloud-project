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

package com.taotao.cloud.tenant.biz.dao;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.tenant.api.model.dto.TenantDTO;
import com.taotao.cloud.tenant.api.model.dto.TenantPageDTO;
import com.taotao.cloud.tenant.biz.convert.TenantConvert;
import com.taotao.cloud.tenant.biz.entity.TenantDO;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author
 * @version 0.0.1
 * @date 2022/11/26 14:21
 */
@RequiredArgsConstructor
@Component
public class TenantManager {

    private final TenantMapper tenantMapper;

    public List<TenantDO> listTenant() {
        return tenantMapper.selectList(Wrappers.emptyWrapper());
    }

    public TenantDO getTenantById(Long id) {
        return tenantMapper.selectById(id);
    }

    public Long addTenant(TenantDTO tenant) {
        TenantDO tenantDO = TenantConvert.INSTANCE.convert(tenant);
        tenantMapper.insert(tenantDO);
        return tenantDO.getId();
    }

    public void updateTenantAdmin(Long tenantId, Long userId) {
        TenantDO tenantDO = new TenantDO();
        tenantDO.setId(tenantId);

        tenantMapper.update(
                tenantDO,
                Wrappers.<TenantDO>lambdaUpdate().set(TenantDO::getTenantAdminId, userId));
    }

    public Integer updateTenant(TenantDTO tenantDTO) {
        return tenantMapper.updateById(TenantConvert.INSTANCE.convert(tenantDTO));
    }

    public Integer deleteTenantById(Long id) {
        return tenantMapper.deleteById(id);
    }

    public TenantDO getTenantByName(String name) {
        return tenantMapper.selectOne(
                Wrappers.<TenantDO>lambdaQuery().eq(TenantDO::getName, name).last("limit 1"));
    }

    public Page<TenantDO> pageTenant(TenantPageDTO pageDTO) {
        return tenantMapper.selectPage(
                Page.of(pageDTO.getCurrentPage(), pageDTO.getPageSize()), Wrappers.emptyWrapper());
    }

    public List<TenantDO> getTenantListByPackageId(Long packageId) {
        return tenantMapper.selectList(
                Wrappers.<TenantDO>lambdaQuery().eq(TenantDO::getPackageId, packageId));
    }

    public Boolean validTenantPackageUsed(Long packageId) {
        TenantDO tenantDO =
                tenantMapper.selectOne(
                        Wrappers.<TenantDO>lambdaQuery()
                                .eq(TenantDO::getPackageId, packageId)
                                .last("limit 1"));
        return Objects.nonNull(tenantDO);
    }
}
