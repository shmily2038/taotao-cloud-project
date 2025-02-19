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

package com.taotao.cloud.payment.biz.jeepay.mch.ctrl.division;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.payment.biz.jeepay.core.constants.ApiCodeEnum;
import com.taotao.cloud.payment.biz.jeepay.core.constants.CS;
import com.taotao.cloud.payment.biz.jeepay.core.entity.MchDivisionReceiver;
import com.taotao.cloud.payment.biz.jeepay.core.entity.MchDivisionReceiverGroup;
import com.taotao.cloud.payment.biz.jeepay.core.exception.BizException;
import com.taotao.cloud.payment.biz.jeepay.core.model.ApiRes;
import com.taotao.cloud.payment.biz.jeepay.mch.ctrl.CommonCtrl;
import com.taotao.cloud.payment.biz.jeepay.service.impl.MchDivisionReceiverGroupService;
import com.taotao.cloud.payment.biz.jeepay.service.impl.MchDivisionReceiverService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户分账接收者账号组
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021-08-23 11:50
 */
@RestController
@RequestMapping("api/divisionReceiverGroups")
public class MchDivisionReceiverGroupController extends CommonCtrl {

    @Autowired private MchDivisionReceiverGroupService mchDivisionReceiverGroupService;
    @Autowired private MchDivisionReceiverService mchDivisionReceiverService;

    /** list */
    @PreAuthorize("hasAnyAuthority( 'ENT_DIVISION_RECEIVER_GROUP_LIST' )")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ApiRes list() {

        MchDivisionReceiverGroup queryObject = getObject(MchDivisionReceiverGroup.class);

        LambdaQueryWrapper<MchDivisionReceiverGroup> condition = MchDivisionReceiverGroup.gw();
        condition.eq(MchDivisionReceiverGroup::getMchNo, getCurrentMchNo());

        if (StringUtils.isNotEmpty(queryObject.getReceiverGroupName())) {
            condition.like(
                    MchDivisionReceiverGroup::getReceiverGroupName,
                    queryObject.getReceiverGroupName());
        }

        if (queryObject.getReceiverGroupId() != null) {
            condition.eq(
                    MchDivisionReceiverGroup::getReceiverGroupId, queryObject.getReceiverGroupId());
        }

        condition.orderByDesc(MchDivisionReceiverGroup::getCreatedAt); // 时间倒序

        IPage<MchDivisionReceiverGroup> pages =
                mchDivisionReceiverGroupService.page(getIPage(true), condition);
        return ApiRes.page(pages);
    }

    /** detail */
    @PreAuthorize("hasAuthority( 'ENT_DIVISION_RECEIVER_GROUP_VIEW' )")
    @RequestMapping(value = "/{recordId}", method = RequestMethod.GET)
    public ApiRes detail(@PathVariable("recordId") Long recordId) {
        MchDivisionReceiverGroup record =
                mchDivisionReceiverGroupService.getOne(
                        MchDivisionReceiverGroup.gw()
                                .eq(MchDivisionReceiverGroup::getMchNo, getCurrentMchNo())
                                .eq(MchDivisionReceiverGroup::getReceiverGroupId, recordId));
        if (record == null) {
            throw new BizException(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        return ApiRes.ok(record);
    }

    /** add */
    @PreAuthorize("hasAuthority( 'ENT_DIVISION_RECEIVER_GROUP_ADD' )")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @MethodLog(remark = "新增分账账号组")
    public ApiRes add() {
        MchDivisionReceiverGroup record = getObject(MchDivisionReceiverGroup.class);
        record.setMchNo(getCurrentMchNo());
        record.setCreatedUid(getCurrentUser().getSysUser().getSysUserId());
        record.setCreatedBy(getCurrentUser().getSysUser().getRealname());
        if (mchDivisionReceiverGroupService.save(record)) {

            // 更新其他组为非默认分账组
            if (record.getAutoDivisionFlag() == CS.YES) {
                mchDivisionReceiverGroupService.update(
                        new LambdaUpdateWrapper<MchDivisionReceiverGroup>()
                                .set(MchDivisionReceiverGroup::getAutoDivisionFlag, CS.NO)
                                .eq(MchDivisionReceiverGroup::getMchNo, getCurrentMchNo())
                                .ne(
                                        MchDivisionReceiverGroup::getReceiverGroupId,
                                        record.getReceiverGroupId()));
            }
        }
        return ApiRes.ok();
    }

    /** update */
    @PreAuthorize("hasAuthority( 'ENT_DIVISION_RECEIVER_GROUP_EDIT' )")
    @RequestMapping(value = "/{recordId}", method = RequestMethod.PUT)
    @MethodLog(remark = "更新分账账号组")
    public ApiRes update(@PathVariable("recordId") Long recordId) {

        MchDivisionReceiverGroup reqRecord = getObject(MchDivisionReceiverGroup.class);

        MchDivisionReceiverGroup record = new MchDivisionReceiverGroup();
        record.setReceiverGroupName(reqRecord.getReceiverGroupName());
        record.setAutoDivisionFlag(reqRecord.getAutoDivisionFlag());

        LambdaUpdateWrapper<MchDivisionReceiverGroup> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(MchDivisionReceiverGroup::getReceiverGroupId, recordId);
        updateWrapper.eq(MchDivisionReceiverGroup::getMchNo, getCurrentMchNo());

        if (mchDivisionReceiverGroupService.update(record, updateWrapper)) {

            // 更新其他组为非默认分账组
            if (record.getAutoDivisionFlag() == CS.YES) {
                mchDivisionReceiverGroupService.update(
                        new LambdaUpdateWrapper<MchDivisionReceiverGroup>()
                                .set(MchDivisionReceiverGroup::getAutoDivisionFlag, CS.NO)
                                .eq(MchDivisionReceiverGroup::getMchNo, getCurrentMchNo())
                                .ne(MchDivisionReceiverGroup::getReceiverGroupId, recordId));
            }
        }

        return ApiRes.ok();
    }

    /** delete */
    @PreAuthorize("hasAuthority('ENT_DIVISION_RECEIVER_GROUP_DELETE')")
    @RequestMapping(value = "/{recordId}", method = RequestMethod.DELETE)
    @MethodLog(remark = "删除分账账号组")
    public ApiRes del(@PathVariable("recordId") Long recordId) {
        MchDivisionReceiverGroup record =
                mchDivisionReceiverGroupService.getOne(
                        MchDivisionReceiverGroup.gw()
                                .eq(MchDivisionReceiverGroup::getReceiverGroupId, recordId)
                                .eq(MchDivisionReceiverGroup::getMchNo, getCurrentMchNo()));
        if (record == null) {
            throw new BizException(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }

        if (mchDivisionReceiverService.count(
                        MchDivisionReceiver.gw()
                                .eq(MchDivisionReceiver::getReceiverGroupId, recordId))
                > 0) {
            throw new BizException("该组存在账号，无法删除");
        }

        mchDivisionReceiverGroupService.removeById(recordId);
        return ApiRes.ok();
    }
}
