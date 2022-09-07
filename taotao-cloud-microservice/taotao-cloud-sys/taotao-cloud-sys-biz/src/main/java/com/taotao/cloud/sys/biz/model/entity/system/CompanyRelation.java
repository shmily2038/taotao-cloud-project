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
package com.taotao.cloud.sys.biz.model.entity.system;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 用户-角色第三方表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:04:45
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = CompanyRelation.TABLE_NAME)
@TableName(CompanyRelation.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = CompanyRelation.TABLE_NAME, comment = "用户-关系第三方表")
public class CompanyRelation extends SuperEntity<CompanyRelation, Long> {

	public static final String TABLE_NAME = "tt_company_relation";

	/**
	 * 公司id
	 */
	@Column(name = "company_id", columnDefinition = "bigint not null comment '公司id'")
	private Long companyId;

	/**
	 * 对象类型 dept position
	 */
	@Column(name = "object_type", columnDefinition = "varchar(255) not null comment '对象类型'")
	private String objectType;

	/**
	 * 对象id deptId positionId
	 */
	@Column(name = "object_id", columnDefinition = "bigint not null comment '对象id'")
	private Long objectId;

	/**
	 * 排序值
	 */
	@Column(name = "sort_code", columnDefinition = "int null comment '排序值'")
	private Integer sortCode;

	@Builder
	public CompanyRelation(Long id, Long companyId, String objectType, Long objectId, Integer sortCode) {
		super(id);
		this.companyId = companyId;
		this.objectId = objectId;
		this.sortCode = sortCode;
		this.objectType = objectType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		CompanyRelation userRole = (CompanyRelation) o;
		return getId() != null && Objects.equals(getId(), userRole.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
