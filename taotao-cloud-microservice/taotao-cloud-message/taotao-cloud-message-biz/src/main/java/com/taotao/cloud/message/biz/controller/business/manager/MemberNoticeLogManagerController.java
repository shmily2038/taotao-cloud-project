package com.taotao.cloud.message.biz.controller.business.manager;// package com.taotao.cloud.message.biz.controller.manager;
//
// import cn.hutool.core.util.PageUtil;
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.taotao.cloud.common.constant.CommonConstant;
// import com.taotao.cloud.common.model.Result;
// import com.taotao.cloud.logger.annotation.RequestLogger;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import java.util.List;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// /**
//  * 管理端,会员消息接口
//  */
// @Validated
// @RestController
// @Tag(name = "平台管理端-会员消息日志管理API", description = "平台管理端-会员消息日志管理API")
// @RequestMapping("/message/manager/memberNoticeLog")
// public class MemberNoticeLogManagerController {
//
// 	@Autowired
// 	private MemberNoticeLogService memberNoticeLogService;
//
// 	@Operation(summary = "通过id获取", description = "通过id获取")
// 	@RequestLogger("通过id获取")
// 	@PreAuthorize("hasAuthority('dept:tree:data')")
// 	@GetMapping(value = "/{id}")
// 	public Result<MemberNoticeLog> get(@PathVariable String id) {
// 		MemberNoticeLog memberNoticeLog = memberNoticeLogService.getById(id);
// 		return Result.success(memberNoticeLog);
// 	}
//
// 	@Operation(summary = "获取全部数据", description = "获取全部数据")
// 	@RequestLogger("获取全部数据")
// 	@PreAuthorize("hasAuthority('dept:tree:data')")
// 	@GetMapping(value = "/all")
// 	public Result<List<MemberNoticeLog>> getAll() {
// 		List<MemberNoticeLog> list = memberNoticeLogService.list();
// 		return Result.success(list);
// 	}
//
// 	@Operation(summary = "分页获取", description = "分页获取")
// 	@RequestLogger("分页获取")
// 	@PreAuthorize("hasAuthority('dept:tree:data')")
// 	@GetMapping(value = "/page")
// 	public Result<IPage<MemberNoticeLog>> getByPage(PageVO page) {
// 		IPage<MemberNoticeLog> data = memberNoticeLogService.page(PageUtil.initPage(page));
// 		return Result.success(data);
// 	}
//
// 	@Operation(summary = "编辑或更新数据", description = "编辑或更新数据")
// 	@RequestLogger("编辑或更新数据")
// 	@PreAuthorize("hasAuthority('dept:tree:data')")
// 	@PostMapping(value = "/insertOrUpdate")
// 	public Result<MemberNoticeLog> saveOrUpdate(MemberNoticeLog memberNoticeLog) {
// 		memberNoticeLogService.saveOrUpdate(memberNoticeLog);
// 		return Result.success(memberNoticeLog);
// 	}
//
// 	@Operation(summary = "批量删除", description = "批量删除")
// 	@RequestLogger("批量删除")
// 	@PreAuthorize("hasAuthority('dept:tree:data')")
// 	@DeleteMapping(value = "/delByIds/{ids}")
// 	public Result<Object> delAllByIds(@PathVariable List ids) {
// 		memberNoticeLogService.removeByIds(ids);
// 		return Result.success();
// 	}
// }
