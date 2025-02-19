/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.message.biz.austin.web.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.support.utils.AccountUtils;
import com.taotao.cloud.message.biz.austin.web.annotation.AustinResult;
import com.taotao.cloud.message.biz.austin.web.exception.CommonException;
import com.taotao.cloud.message.biz.austin.web.utils.Convert4Amis;
import com.taotao.cloud.message.biz.austin.web.utils.LoginUtils;
import com.taotao.cloud.message.biz.austin.web.vo.amis.CommonAmisVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信服务号
 *
 * @author 3y
 */
@Slf4j
@AustinResult
@RequestMapping("/officialAccount")
@RestController
@Api("微信服务号")
public class OfficialAccountController {

    @Autowired private AccountUtils accountUtils;

    @Autowired private LoginUtils loginUtils;

    @Autowired private StringRedisTemplate redisTemplate;

    /**
     * @param id 账号Id
     * @return
     */
    @GetMapping("/template/list")
    @ApiOperation("/根据账号Id获取模板列表")
    public List<CommonAmisVo> queryList(Integer id) {
        try {
            List<CommonAmisVo> result = new ArrayList<>();
            WxMpService wxMpService = accountUtils.getAccountById(id, WxMpService.class);

            List<WxMpTemplate> allPrivateTemplate =
                    wxMpService.getTemplateMsgService().getAllPrivateTemplate();
            for (WxMpTemplate wxMpTemplate : allPrivateTemplate) {
                CommonAmisVo commonAmisVo =
                        CommonAmisVo.builder()
                                .label(wxMpTemplate.getTitle())
                                .value(wxMpTemplate.getTemplateId())
                                .build();
                result.add(commonAmisVo);
            }
            return result;
        } catch (Exception e) {
            log.error(
                    "OfficialAccountController#queryList fail:{}",
                    Throwables.getStackTraceAsString(e));
            throw new CommonException(RespStatusEnum.SERVICE_ERROR);
        }
    }

    /**
     * 根据账号Id和模板ID获取模板列表
     *
     * @return
     */
    @PostMapping("/detailTemplate")
    @ApiOperation("/根据账号Id和模板ID获取模板列表")
    public CommonAmisVo queryDetailList(Integer id, String wxTemplateId) {
        if (Objects.isNull(id) || Objects.isNull(wxTemplateId)) {
            throw new CommonException(RespStatusEnum.CLIENT_BAD_PARAMETERS);
        }
        try {
            WxMpService wxMpService = accountUtils.getAccountById(id, WxMpService.class);
            List<WxMpTemplate> allPrivateTemplate =
                    wxMpService.getTemplateMsgService().getAllPrivateTemplate();
            return Convert4Amis.getWxMpTemplateParam(wxTemplateId, allPrivateTemplate);
        } catch (Exception e) {
            log.error(
                    "OfficialAccountController#queryDetailList fail:{}",
                    Throwables.getStackTraceAsString(e));
            throw new CommonException(RespStatusEnum.SERVICE_ERROR);
        }
    }

    /**
     * 接收微信的事件消息
     * https://developers.weixin.qq.com/doc/offiaccount/Basic_Information/Access_Overview.html
     * 临时给微信服务号登录使用，正常消息推送平台不会有此接口
     *
     * @return
     */
    @RequestMapping(
            value = "/receipt",
            produces = {CommonConstant.CONTENT_TYPE_XML})
    @ApiOperation("/接收微信的事件消息")
    public String receiptMessage(HttpServletRequest request) {
        try {
            WeChatLoginConfig configService = loginUtils.getLoginConfig();
            if (Objects.isNull(configService)) {
                return RespStatusEnum.DO_NOT_NEED_LOGIN.getMsg();
            }
            WxMpService wxMpService = configService.getOfficialAccountLoginService();

            String echoStr = request.getParameter(OfficialAccountParamConstant.ECHO_STR);
            String signature = request.getParameter(OfficialAccountParamConstant.SIGNATURE);
            String nonce = request.getParameter(OfficialAccountParamConstant.NONCE);
            String timestamp = request.getParameter(OfficialAccountParamConstant.TIMESTAMP);

            // echoStr!=null，说明只是微信调试的请求
            if (StrUtil.isNotBlank(echoStr)) {
                return echoStr;
            }

            if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
                return RespStatusEnum.CLIENT_BAD_PARAMETERS.getMsg();
            }

            String encryptType =
                    StrUtil.isBlank(request.getParameter(OfficialAccountParamConstant.ENCRYPT_TYPE))
                            ? OfficialAccountParamConstant.RAW
                            : request.getParameter(OfficialAccountParamConstant.ENCRYPT_TYPE);
            if (OfficialAccountParamConstant.RAW.equals(encryptType)) {
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
                log.info("raw inMessage:{}", JSON.toJSONString(inMessage));
                WxMpXmlOutMessage outMessage =
                        configService.getWxMpMessageRouter().route(inMessage);
                return outMessage.toXml();
            } else if (OfficialAccountParamConstant.AES.equals(encryptType)) {
                String msgSignature =
                        request.getParameter(OfficialAccountParamConstant.MSG_SIGNATURE);
                WxMpXmlMessage inMessage =
                        WxMpXmlMessage.fromEncryptedXml(
                                request.getInputStream(),
                                configService.getConfig(),
                                timestamp,
                                nonce,
                                msgSignature);
                log.info("aes inMessage:{}", JSON.toJSONString(inMessage));
                WxMpXmlOutMessage outMessage =
                        configService.getWxMpMessageRouter().route(inMessage);
                return outMessage.toEncryptedXml(configService.getConfig());
            }
            return RespStatusEnum.SUCCESS.getMsg();
        } catch (Exception e) {
            log.error(
                    "OfficialAccountController#receiptMessage fail:{}",
                    Throwables.getStackTraceAsString(e));
            return RespStatusEnum.SERVICE_ERROR.getMsg();
        }
    }

    /**
     * 临时给微信服务号登录使用（生成二维码），正常消息推送平台不会有此接口 返回二维码图片url 和 sceneId
     *
     * @return
     */
    @PostMapping("/qrCode")
    @ApiOperation("/生成 服务号 二维码")
    public CommonAmisVo getQrCode() {
        try {
            WeChatLoginConfig configService = loginUtils.getLoginConfig();
            if (Objects.isNull(configService)) {
                throw new CommonException(RespStatusEnum.DO_NOT_NEED_LOGIN);
            }
            String id = IdUtil.getSnowflake().nextIdStr();
            WxMpService wxMpService = configService.getOfficialAccountLoginService();
            WxMpQrCodeTicket ticket =
                    wxMpService.getQrcodeService().qrCodeCreateTmpTicket(id, 2592000);
            String url = wxMpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());
            return Convert4Amis.getWxMpQrCode(url, id);
        } catch (Exception e) {
            log.error(
                    "OfficialAccountController#getQrCode fail:{}",
                    Throwables.getStackTraceAsString(e));
            throw new CommonException(RespStatusEnum.SERVICE_ERROR);
        }
    }

    /**
     * 临时给微信服务号登录使用（给前端轮询检查是否已登录），正常消息推送平台不会有此接口
     *
     * @return
     */
    @RequestMapping("/check/login")
    @ApiOperation("/检查是否已经登录")
    public WxMpUser checkLogin(String sceneId) {
        try {
            String userInfo = redisTemplate.opsForValue().get(sceneId);
            if (StrUtil.isBlank(userInfo)) {
                throw new CommonException(RespStatusEnum.NO_LOGIN);
            }
            return JSON.parseObject(userInfo, (Type) WxMpUser.class);
        } catch (Exception e) {
            log.error(
                    "OfficialAccountController#checkLogin fail:{}",
                    Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    /**
     * 原因：微信测试号最多只能拥有100个测试用户
     *
     * <p>临时 删除测试号的用户，避免正常有问题
     *
     * <p>【正常消息推送平台不会有这个接口】
     *
     * @return
     */
    @RequestMapping("/delete/test/user")
    @ApiOperation("/删除测试号的测试用户")
    public void deleteTestUser(HttpServletRequest request) {
        try {

            // 删除粉丝
            String testUrl = "http://mp.weixin.qq.com/debug/cgi-bin/sandboxinfo";
            String action = "delfan";

            String cookie = request.getHeader(Header.COOKIE.getValue());
            List<String> openIds =
                    loginUtils
                            .getLoginConfig()
                            .getOfficialAccountLoginService()
                            .getUserService()
                            .userList(null)
                            .getOpenids();
            for (String openId : openIds) {
                Map<String, Object> params = new HashMap<>(4);
                params.put("openid", openId);
                params.put("random", RandomUtil.randomDouble());
                params.put("action", action);
                HttpUtil.createPost(testUrl).header(Header.COOKIE, cookie).form(params).execute();
            }
        } catch (Exception e) {
            log.error(
                    "OfficialAccountController#deleteTestUser fail:{}",
                    Throwables.getStackTraceAsString(e));
        }
    }
}
