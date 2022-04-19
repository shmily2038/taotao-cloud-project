package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
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
 * 直播间商品表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = StudioCommodity.TABLE_NAME)
@TableName(StudioCommodity.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = StudioCommodity.TABLE_NAME, comment = "直播间商品表")
public class StudioCommodity extends BaseSuperEntity<StudioCommodity, Long> {

	public static final String TABLE_NAME = "tt_studio_commodity";

	/**
	 * 房间ID
	 */
	@Column(name = "room_id", columnDefinition = "bigint not null comment '房间ID'")
	private Long roomId;

	/**
	 * 商品ID
	 */
	@Column(name = "goods_id", columnDefinition = "varchar(255) not null comment '商品ID'")
	private Long goodsId;
}
