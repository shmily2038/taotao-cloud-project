package com.taotao.cloud.workflow.biz.engine.model.flowtask.method;

import java.util.List;
import jnpf.base.UserInfo;
import jnpf.engine.entity.FlowTaskNodeEntity;
import jnpf.engine.model.flowengine.FlowModel;
import jnpf.engine.model.flowengine.shuntjson.nodejson.ChildNodeList;
import lombok.Data;

@Data
public class TaskHandleIdStatus {
   /**审批类型（0：拒绝，1：同意）**/
   private Integer status;
   /**当前节点属性**/
   private ChildNodeList nodeModel;
   /**用户**/
   private UserInfo userInfo;
   /**审批对象**/
   private FlowModel flowModel;
   /**节点list**/
   private List<FlowTaskNodeEntity> taskNodeList;

}
