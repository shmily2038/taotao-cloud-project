package com.taotao.cloud.order.biz.service.cart.render;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.order.api.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.enums.cart.CartTypeEnum;
import com.taotao.cloud.order.api.enums.cart.RenderStepEnums;
import com.taotao.cloud.order.biz.entity.order.Trade;
import com.taotao.cloud.order.biz.service.cart.CartService;
import com.taotao.cloud.order.biz.service.order.TradeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 交易构造&&创建
 */
@Service
public class TradeBuilder {

	/**
	 * 购物车渲染步骤
	 */
	@Autowired
	private List<CartRenderStep> cartRenderSteps;
	/**
	 * 交易
	 */
	@Autowired
	private TradeService tradeService;
	/**
	 * 购物车业务
	 */
	@Autowired
	private CartService cartService;


	/**
	 * 构造购物车 购物车与结算信息不一致的地方主要是优惠券计算和运费计算，其他规则都是一致都
	 *
	 * @param checkedWay 购物车类型
	 * @return 购物车展示信息
	 */
	public TradeDTO buildCart(CartTypeEnum checkedWay) {
		//读取对应购物车的商品信息
		TradeDTO tradeDTO = cartService.readDTO(checkedWay);

		//购物车需要将交易中的优惠券取消掉
		if (checkedWay.equals(CartTypeEnum.CART)) {
			tradeDTO.setStoreCoupons(null);
			tradeDTO.setPlatformCoupon(null);
		}

		//按照计划进行渲染
		renderCartBySteps(tradeDTO, RenderStepStatement.cartRender);
		return tradeDTO;
	}

	/**
	 * 构造结算页面
	 */
	public TradeDTO buildChecked(CartTypeEnum checkedWay) {
		//读取对应购物车的商品信息
		TradeDTO tradeDTO = cartService.readDTO(checkedWay);
		//需要对购物车渲染
		if (isSingle(checkedWay)) {
			renderCartBySteps(tradeDTO, RenderStepStatement.checkedSingleRender);
		} else {
			renderCartBySteps(tradeDTO, RenderStepStatement.checkedRender);
		}

		return tradeDTO;
	}

	/**
	 * 创建一笔交易 1.构造交易 2.创建交易
	 *
	 * @param checkedWay 购物车类型
	 * @return 交易信息
	 */
	public Trade createTrade(CartTypeEnum checkedWay) {
		//读取对应购物车的商品信息
		TradeDTO tradeDTO = cartService.readDTO(checkedWay);

		//需要对购物车渲染
		if (isSingle(checkedWay)) {
			renderCartBySteps(tradeDTO, RenderStepStatement.singleTradeRender);
		} else {
			renderCartBySteps(tradeDTO, RenderStepStatement.tradeRender);
		}

		//添加order订单及order_item子订单并返回
		return tradeService.createTrade(tradeDTO);
	}

	/**
	 * 是否为单品渲染
	 *
	 * @param checkedWay 购物车类型
	 * @return 返回是否单品
	 */
	private boolean isSingle(CartTypeEnum checkedWay) {
		//拼团   积分   砍价商品

		return (checkedWay.equals(CartTypeEnum.PINTUAN) || checkedWay.equals(CartTypeEnum.POINTS)
			|| checkedWay.equals(CartTypeEnum.KANJIA));
	}

	/**
	 * 根据渲染步骤，渲染购物车信息
	 *
	 * @param tradeDTO      交易DTO
	 * @param defaultRender 渲染枚举
	 */
	private void renderCartBySteps(TradeDTO tradeDTO, RenderStepEnums[] defaultRender) {
		for (RenderStepEnums step : defaultRender) {
			for (CartRenderStep render : cartRenderSteps) {
				try {
					if (render.step().equals(step)) {
						render.render(tradeDTO);
					}
				} catch (ServiceException e) {
					throw e;
				} catch (Exception e) {
					LogUtil.error("购物车{}渲染异常：", render.getClass(), e);
				}
			}
		}
	}
}
