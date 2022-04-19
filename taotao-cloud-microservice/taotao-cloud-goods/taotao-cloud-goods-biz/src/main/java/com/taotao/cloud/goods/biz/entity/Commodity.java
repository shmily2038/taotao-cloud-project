package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 小程序直播商品表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = Commodity.TABLE_NAME)
@TableName(Commodity.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Commodity.TABLE_NAME, comment = "小程序直播商品表")
public class Commodity extends BaseSuperEntity<Commodity, Long> {

	public static final String TABLE_NAME = "tt_commodity";

	/**
	 * 图片
	 */
	@Column(name = "goods_image", columnDefinition = "varchar(255) not null comment '图片'")
	private String goodsImage;

	/**
	 * 商品名称
	 */
	@Column(name = "name", columnDefinition = "varchar(255) not null comment '商品名称'")
	private String name;

	/**
	 * 1：一口价（只需要传入price，price2不传）
	 * <p></p>
	 * 2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传）
	 * <p></p>
	 * 3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传
	 */
	@Column(name = "price_type", columnDefinition =
		"int not null comment '1：一口价（只需要传入price，price2不传）"
			+ "2：价格区间（price字段为左边界，price2字段为右边界，price和price2必传）"
			+ "3：显示折扣价（price字段为原价，price2字段为现价， price和price2必传'")
	private Integer priceType;

	/**
	 * 价格
	 */
	@Column(name = "price", columnDefinition = "decimal(10,2) not null comment '价格'")
	private BigDecimal price;

	/**
	 * 价格2
	 */
	@Column(name = "price2", columnDefinition = "decimal(10,2) not null comment '价格2'")
	private BigDecimal price2;

	/**
	 * 商品详情页的小程序路径
	 */
	@Column(name = "url", columnDefinition = "varchar(255) not null comment '商品详情页的小程序路径'")
	private String url;

	/**
	 * 微信程序直播商品ID
	 */
	@Column(name = "live_goods_id", columnDefinition = "bigint not null comment '微信程序直播商品ID'")
	private Long liveGoodsId;

	/**
	 * 审核单ID
	 */
	@Column(name = "audit_id", columnDefinition = "bigint not null comment '审核单ID'")
	private Long auditId;

	/**
	 * 审核状态
	 */
	@Column(name = "audit_status", columnDefinition = "varchar(255) not null comment '审核状态'")
	private String auditStatus;

	/**
	 * 店铺ID
	 */
	@Column(name = "store_id", columnDefinition = "bigint not null comment '店铺ID'")
	private Long storeId;

	/**
	 * 商品ID
	 */
	@Column(name = "goods_id", columnDefinition = "bigint not null comment '商品ID'")
	private Long goodsId;

	/**
	 * skuId
	 */
	@Column(name = "sku_id", columnDefinition = "bigint not null comment 'skuId'")
	private Long skuId;
}
