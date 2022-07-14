package com.taotao.cloud.workflow.biz.engine.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import jnpf.base.Pagination;
import jnpf.engine.entity.FlowDelegateEntity;

/**
 * 流程委托
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月27日 上午9:18
 */
public interface FlowDelegateService extends IService<FlowDelegateEntity> {

    /**
     * 列表
     *
     * @param pagination 请求参数
     * @return
     */
    List<FlowDelegateEntity> getList(Pagination pagination);

    /**
     * 列表
     *
     * @return
     */
    List<FlowDelegateEntity> getList();


    /**
     * 信息
     *
     * @param id 主键值
     * @return
     */
    FlowDelegateEntity getInfo(String id);

    /**
     * 删除
     *
     * @param entity 实体对象
     */
    void delete(FlowDelegateEntity entity);

    /**
     * 创建
     *
     * @param entity 实体对象
     */
    void create(FlowDelegateEntity entity);

    /**
     * 获取委托的表单
     *
     * @param userId 被委托人
     * @return
     */
    List<FlowDelegateEntity> getUser(String userId);

    /**
     * 获取委托的表单
     *
     * @param userId        被委托人
     * @param flowId        流程引擎
     * @param creatorUserId 创建人
     * @return
     */
    List<FlowDelegateEntity> getUser(String userId, String flowId, String creatorUserId);

    /**
     * 更新
     *
     * @param id     主键值
     * @param entity 实体对象
     * @return
     */
    boolean update(String id, FlowDelegateEntity entity);

}
