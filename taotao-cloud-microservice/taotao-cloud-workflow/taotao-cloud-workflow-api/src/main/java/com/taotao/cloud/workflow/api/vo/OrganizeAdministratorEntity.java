package com.taotao.cloud.workflow.api.vo;

import java.util.Date;
import lombok.Data;

@Data
public class OrganizeAdministratorEntity {

	private String id;

	private String userId;

	private String organizeId;

	private String organizeType;

	private Integer thisLayerAdd;

	private Integer thisLayerEdit;

	private Integer thisLayerDelete;

	private Integer subLayerAdd;

	private Integer subLayerEdit;

	private Integer subLayerDelete;

	private String description;

	private Long sortCode;

	private Integer enabledMark;

	private Date creatorTime;

	private String creatorUserId;

	private Date lastModifyTime;

	private String lastModifyUserId;

	private Integer deleteMark;

	private Date deleteTime;

	private String deleteUserId;


}
