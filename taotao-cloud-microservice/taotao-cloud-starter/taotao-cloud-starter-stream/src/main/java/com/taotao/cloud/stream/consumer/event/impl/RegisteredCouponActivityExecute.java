//package com.taotao.cloud.stream.consumer.event.impl;
//
//import cn.lili.event.MemberRegisterEvent;
//import cn.lili.modules.member.entity.dos.Member;
//import cn.lili.modules.promotion.entity.dos.CouponActivity;
//import cn.lili.modules.promotion.entity.enums.CouponActivityTypeEnum;
//import cn.lili.modules.promotion.entity.enums.PromotionsStatusEnum;
//import cn.lili.modules.promotion.service.CouponActivityService;
//import cn.lili.modules.promotion.tools.PromotionTools;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 注册赠券活动
// *
// */
//@Component
//public class RegisteredCouponActivityExecute implements MemberRegisterEvent {
//
//    @Autowired
//    private CouponActivityService couponActivityService;
//
//    /**
//     * 获取进行中的注册赠券的优惠券活动
//     * 发送注册赠券
//     *
//     * @param member 会员
//     */
//    @Override
//    public void memberRegister(Member member) {
//        List<CouponActivity> couponActivities = couponActivityService.list(new QueryWrapper<CouponActivity>()
//                .eq("coupon_activity_type", CouponActivityTypeEnum.REGISTERED.name())
//                .and(PromotionTools.queryPromotionStatus(PromotionsStatusEnum.START)));
//        couponActivityService.registered(couponActivities, member);
//    }
//}
