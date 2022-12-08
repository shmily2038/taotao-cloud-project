package com.taotao.cloud.workflow.api.vo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

@Data
@TableName("base_moduledataauthorize")
public class ModuleDataAuthorizeEntity {

	/**
	 * 数据主键
	 */
	@TableId("F_ID")
	private String id;

	/**
	 * 字段名称
	 */
	@TableField("F_FULLNAME")
	private String fullName;

	/**
	 * 字段编码
	 */
	@TableField("F_ENCODE")
	private String enCode;

	/**
	 * 字段类型
	 */
	@TableField("F_TYPE")
	private String type;

	/**
	 * 条件符号
	 */
	@TableField("F_CONDITIONSYMBOL")
	private String conditionSymbol;

	/**
	 * 条件符号Json
	 */
	@TableField("F_CONDITIONSYMBOLJSON")
	private String conditionSymbolJson;

	/**
	 * 条件内容
	 */
	@TableField("F_CONDITIONTEXT")
	private String conditionText;

	/**
	 * 扩展属性
	 */
	@TableField("F_PROPERTYJSON")
	private String propertyJson;

	/**
	 * 描述
	 */
	@TableField("F_DESCRIPTION")
	private String description;

	/**
	 * 排序码
	 */
	@TableField("F_SORTCODE")
	private Long sortCode;

	/**
	 * 有效标志
	 */
	@TableField("F_ENABLEDMARK")
	private Integer enabledMark;

	/**
	 * 创建时间
	 */
	@TableField(value = "F_CREATORTIME", fill = FieldFill.INSERT)
	private Date creatorTime;

	/**
	 * 创建用户
	 */
	@TableField(value = "F_CREATORUSERID", fill = FieldFill.INSERT)
	private String creatorUserId;

	/**
	 * 修改时间
	 */
	@TableField(value = "F_LASTMODIFYTIME", fill = FieldFill.UPDATE)
	private Date lastModifyTime;

	/**
	 * 修改用户
	 */
	@TableField(value = "F_LASTMODIFYUSERID", fill = FieldFill.UPDATE)
	private String lastModifyUserId;

	/**
	 * 删除标志
	 */
	@TableField("F_DELETEMARK")
	private Integer deleteMark;

	/**
	 * 删除时间
	 */
	@TableField("F_DELETETIME")
	private Date deleteTime;

	/**
	 * 删除用户
	 */
	@TableField("F_DELETEUSERID")
	private String deleteUserId;

	/**
	 * 菜单主键
	 */
	@TableField("F_MODULEID")
	private String moduleId;

	/**
	 * 字段规则 主从
	 */
	@TableField("F_FIELDRULE")
	private Integer fieldRule;

	/**
	 * 绑定表格Id
	 */
	@TableField("F_BINDTABLE")
	private String bindTable;
}
