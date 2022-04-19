package com.taotao.cloud.promotion.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import com.taotao.cloud.promotion.api.enums.MemberCouponStatusEnum;
import com.taotao.cloud.promotion.api.query.CouponSearchParams;
import com.taotao.cloud.promotion.biz.entity.MemberCoupon;
import java.math.BigDecimal;
import java.util.List;

/**
 * 会员优惠券业务层
 *
 *
 * @since 2020/11/18 9:45 上午
 */
public interface MemberCouponService extends IService<MemberCoupon> {

    /**
     * 检查该会员领取优惠券的可领取数量
     *
     * @param couponId 优惠券编号
     * @param memberId 会员
     */
    void checkCouponLimit(String couponId, String memberId);

    /**
     * 领取优惠券
     *
     * @param couponId   优惠券编号
     * @param memberId   会员
     * @param memberName 会员名称
     */
    void receiveBuyerCoupon(String couponId, String memberId, String memberName);

    /**
     * 领取优惠券
     *
     * @param couponId   优惠券编号
     * @param memberId   会员
     * @param memberName 会员名称
     */
    void receiveCoupon(String couponId, String memberId, String memberName);

    /**
     * 获取会员优惠券列表
     *
     * @param param  查询参数
     * @param pageVo 分页参数
     * @return 会员优惠券列表
     */
    IPage<MemberCoupon> getMemberCoupons(CouponSearchParams param, PageVO pageVo);

    /**
     * 获取会员所有优惠券
     *
     * @return 会员优惠券列表
     */
    List<MemberCoupon> getMemberCoupons();

    /**
     * 获取会员优惠券列表
     *
     * @param param      查询参数
     * @param pageVo     分页参数
     * @param totalPrice 当前商品总价
     * @return 会员优惠券列表
     */
    IPage<MemberCoupon> getMemberCouponsByCanUse(CouponSearchParams param, BigDecimal totalPrice, PageVO pageVo);

    /**
     * 获取当前会员当前商品可用的会员优惠券
     *
     * @param memberId   会员Id
     * @param couponIds  优惠券id列表
     * @param totalPrice 当前商品总价
     * @return 会员优惠券列表
     */
    List<MemberCoupon> getCurrentGoodsCanUse(String memberId, List<String> couponIds, BigDecimal totalPrice);

    /**
     * 获取当前会员全品类优惠券
     *
     * @param memberId 会员Id
     * @param storeId  店铺id
     * @return 会员优惠券列表
     */
    List<MemberCoupon> getAllScopeMemberCoupon(String memberId, List<String> storeId);

    /**
     * 获取会员优惠券数量
     *
     * @return 会员优惠券数量
     */
    long getMemberCouponsNum();

    /**
     * 更新会员优惠券状态
     *
     * @param status 要变更的状态
     * @param id     会员优惠券id
     */
    void updateMemberCouponStatus(MemberCouponStatusEnum status, String id);

    /**
     * 使用优惠券
     *
     * @param ids 会员优惠券id
     */
    void used(List<String> ids);

    /**
     * 作废当前会员优惠券
     *
     * @param id id
     */
    void cancellation(String id);

    /**
     * 关闭会员优惠券
     *
     * @param couponIds 优惠券id集合
     */
    void closeMemberCoupon(List<String> couponIds);

}
