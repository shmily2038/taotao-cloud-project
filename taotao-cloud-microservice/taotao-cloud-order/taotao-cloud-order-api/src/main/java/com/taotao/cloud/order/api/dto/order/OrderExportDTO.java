package com.taotao.cloud.order.api.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.order.api.enums.order.DeliverStatusEnum;
import com.taotao.cloud.order.api.enums.order.OrderStatusEnum;
import com.taotao.cloud.order.api.enums.order.PayStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 订单导出DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单导出DTO")
public class OrderExportDTO {

	@Schema(description = "订单编号")
	private String sn;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Schema(description = "创建时间")
	private Date createTime;

	@Schema(description = "用户名")
	private String memberName;

	@Schema(description = "收件人姓名")
	private String consigneeName;

	@Schema(description = "收件人手机")
	private String consigneeMobile;

	@Schema(description = "收件人地址")
	private String consigneeAddressPath;

	@Schema(description = "详细地址")
	private String consigneeDetail;

	@Schema(description = "支付方式")
	private String paymentMethod;

	@Schema(description = "物流公司名称")
	private String logisticsName;

	@Schema(description = "运费")
	private BigDecimal freightPrice;

	@Schema(description = "商品价格")
	private BigDecimal goodsPrice;

	@Schema(description = "优惠的金额")
	private BigDecimal discountPrice;

	@Schema(description = "总价格")
	private BigDecimal flowPrice;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "商品数量")
	private Integer num;

	@Schema(description = "买家订单备注")
	private String remark;

	/**
	 * @see OrderStatusEnum
	 */
	@Schema(description = "订单状态")
	private String orderStatus;

	/**
	 * @see PayStatusEnum
	 */
	@Schema(description = "付款状态")
	private String payStatus;

	/**
	 * @see DeliverStatusEnum
	 */
	@Schema(description = "货运状态")
	private String deliverStatus;

	@Schema(description = "是否需要发票")
	private Boolean needReceipt;

	@Schema(description = "店铺名称")
	private String storeName;
}
