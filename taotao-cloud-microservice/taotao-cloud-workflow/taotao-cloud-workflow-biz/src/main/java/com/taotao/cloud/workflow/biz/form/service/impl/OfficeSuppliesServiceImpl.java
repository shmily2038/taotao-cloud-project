package com.taotao.cloud.workflow.biz.form.service.impl;

import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.Map;
import jnpf.base.service.BillRuleService;
import jnpf.engine.service.FlowTaskService;
import jnpf.engine.util.ModelUtil;
import jnpf.exception.WorkFlowException;
import jnpf.form.entity.OfficeSuppliesEntity;
import jnpf.form.mapper.OfficeSuppliesMapper;
import jnpf.form.model.officesupplies.OfficeSuppliesForm;
import jnpf.form.service.OfficeSuppliesService;
import jnpf.util.JsonUtil;
import jnpf.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 领用办公用品申请表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2019年9月29日 上午9:18
 */
@Service
public class OfficeSuppliesServiceImpl extends ServiceImpl<OfficeSuppliesMapper, OfficeSuppliesEntity> implements OfficeSuppliesService {

    @Autowired
    private BillRuleService billRuleService;
    @Autowired
    private FlowTaskService flowTaskService;

    @Override
    public OfficeSuppliesEntity getInfo(String id) {
        QueryWrapper<OfficeSuppliesEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OfficeSuppliesEntity::getId, id);
        return this.getOne(queryWrapper);
    }

    @Override
    @DSTransactional
    public void save(String id, OfficeSuppliesEntity entity) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_OfficeSuppliesNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.save(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(),entity);
    }

    @Override
    @DSTransactional
    public void submit(String id, OfficeSuppliesEntity entity, Map<String, List<String>> candidateList) throws WorkFlowException {
        //表单信息
        if (id == null) {
            entity.setId(RandomUtil.uuId());
            this.save(entity);
            billRuleService.useBillNumber("WF_OfficeSuppliesNo");
        } else {
            entity.setId(id);
            this.updateById(entity);
        }
        //流程信息
        ModelUtil.submit(id, entity.getFlowId(), entity.getId(), entity.getFlowTitle(), entity.getFlowUrgent(), entity.getBillNo(), entity,null, candidateList);
    }

    @Override
    public void data(String id, String data) {
        OfficeSuppliesForm officeSuppliesForm = JsonUtil.getJsonToBean(data, OfficeSuppliesForm.class);
        OfficeSuppliesEntity entity = JsonUtil.getJsonToBean(officeSuppliesForm, OfficeSuppliesEntity.class);
        entity.setId(id);
        this.saveOrUpdate(entity);
    }
}
