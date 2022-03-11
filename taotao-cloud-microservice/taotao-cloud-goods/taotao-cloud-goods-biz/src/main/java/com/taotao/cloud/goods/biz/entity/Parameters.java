package com.taotao.cloud.goods.biz.entity;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 商品参数
 *
 * 
 * @since 2020-02-23 9:14:33
 */
@Entity
@Table(name = Parameters.TABLE_NAME)
@TableName(Parameters.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Parameters.TABLE_NAME, comment = "商品参数")
public class Parameters extends BaseSuperEntity<Parameters, Long> {

	public static final String TABLE_NAME = "li_parameters";

    @ApiModelProperty(value = "参数名称", required = true)
    @NotEmpty(message = "参数名称必填")
    @Length(max = 5, message = "参数名称不能超过5字")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String paramName;


    @ApiModelProperty(value = "选择值")
    @NotEmpty(message = "参数选项值必填")
    @Length(max = 255, message = "参数选项过长，请简略")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String options;

    @ApiModelProperty(value = "是否可索引，0 不显示 1 显示", required = true)
    @NotNull(message = "是否可索引必选")
    @Min(value = 0, message = "是否可索引传值不正确")
    @Max(value = 1, message = "是否可索引传值不正确")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer isIndex;

    @ApiModelProperty(value = "是否必填是  1    否   0", required = true)
    @NotNull(message = "是否必填必选")
    @Min(value = 0, message = "是否必填传值不正确")
    @Max(value = 1, message = "是否必填传值不正确")

    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer required;

    @ApiModelProperty(value = "参数分组id", required = true)
    @NotNull(message = "所属参数组不能为空")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String groupId;

    @ApiModelProperty(value = "分类id", hidden = true)

    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String categoryId;

    @ApiModelProperty(value = "排序", hidden = true)
    @NotNull(message = "请输入排序值")
    @Max(value = 9999, message = "排序值不能大于9999")
    @Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private Integer sort;

}
