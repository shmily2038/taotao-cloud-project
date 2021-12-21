package com.taotao.cloud.order.api.enums.order;

/**
 * 订单状态枚举
 *
 * 
 * @since 2020/11/17 7:28 下午
 */
public enum PayStatusEnum {

    /**
     * 支付状态
     */
    UNPAID("待付款"),
    PAID("已付款"),
    CANCEL("已取消");

    private final String description;

    PayStatusEnum(String description) {
        this.description = description;
    }

    public String description() {
        return this.description;
    }


}
