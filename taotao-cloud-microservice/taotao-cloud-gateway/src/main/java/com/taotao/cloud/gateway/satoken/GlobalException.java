package com.taotao.cloud.gateway.satoken;

import org.springframework.context.annotation.Configuration;

/**
 * @author YangBoss
 * @title: GlobalException
 * @projectName meta
 * @description: 拦截全局异常类
 * @date 2022/8/19 15:39
 */
@Configuration
public class GlobalException {

	// 全局异常拦截（拦截项目中的所有异常）
//	@ResponseBody
//	@ExceptionHandler
//	public ResultJsonUtil<Object> handlerException(Exception e) {
//
//		// 打印堆栈，以供调试
////        System.out.println("全局异常---------------");
//		e.printStackTrace();
//
//		// 不同异常返回不同状态码
//		ResultJsonUtil<Object> re = null;
//		if (e instanceof NotLoginException) {    // 如果是未登录异常
//			NotLoginException ee = (NotLoginException) e;
//			re = new ResultJsonUtil().customized(
//				ResponseCodeConstant.OAUTH_TOKEN_FAILURE,
//				ResponseMessageConstant.OAUTH_TOKEN_MISSING,
//				null
//			);
//		} else if (e instanceof NotRoleException) {        // 如果是角色异常
//			NotRoleException ee = (NotRoleException) e;
//			re = new ResultJsonUtil().customized(
//				ResponseCodeConstant.OAUTH_TOKEN_DENIED,
//				"无此角色：" + ee.getRole(),
//				null
//			);
//		} else if (e instanceof NotPermissionException) {    // 如果是权限异常
//			NotPermissionException ee = (NotPermissionException) e;
//			re = new ResultJsonUtil().customized(
//				ResponseCodeConstant.OAUTH_TOKEN_DENIED,
//				"无此角色：" + ee.getCode(),
//				null
//			);
//		} else if (e instanceof DisableLoginException) {    // 如果是被封禁异常
//			DisableLoginException ee = (DisableLoginException) e;
//			re = new ResultJsonUtil().customized(
//				ResponseCodeConstant.USER_LOCK,
//				"账号被封禁：" + ee.getDisableTime() + "秒后解封",
//				null
//			);
//		} else {    // 普通异常, 输出：500 + 异常信息
//			re = new ResultJsonUtil().fail(e.getMessage());
//		}
//
//		// 返回给前端
//		return re;
//	}
}
