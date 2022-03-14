package com.taotao.cloud.promotion.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 优惠券活动的优惠券VO
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CouponActivityItemVO extends CouponActivityItem {

    @Schema(description =  "优惠券名称")
    private String couponName;

    @Schema(description =  "面额")
    private Double price;

    /**
     * POINT("打折"), PRICE("减免现金");
     *
     * @see CouponTypeEnum
     */
    @Schema(description =  "优惠券类型")
    private String couponType;

    @Schema(description =  "折扣")
    private Double couponDiscount;
}
