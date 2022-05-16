package com.taotao.cloud.order.biz.controller.manager;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.order.biz.entity.trade.OrderLog;
import com.taotao.cloud.order.biz.service.trade.IOrderLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,订单日志管理API
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:57:19
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-订单日志管理API", description = "平台管理端-订单日志管理API")
@RequestMapping("/order/manager/order/log")
public class OrderLogController {

	private final IOrderLogService orderLogService;

	@Operation(summary = "通过id获取", description = "通过id获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<OrderLog> get(@PathVariable String id) {
		return Result.success(orderLogService.getById(id));
	}

	@Operation(summary = "分页获取", description = "分页获取")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<IPage<OrderLog>> getByPage(OrderLog entity,
		SearchVO searchVo, PageVO page) {
		return Result.success(orderLogService.page(
			PageUtil.initPage(page), PageUtil.initWrapper(entity, searchVo)));
	}

}
