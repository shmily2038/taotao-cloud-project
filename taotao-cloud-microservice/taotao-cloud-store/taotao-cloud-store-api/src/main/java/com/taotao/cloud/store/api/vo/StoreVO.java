package com.taotao.cloud.store.api.vo;

import com.taotao.cloud.store.api.enums.StoreStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 店铺VO
 */
@Data
@Schema(description = "店铺VO")
//public class StoreVO extends Store {
public class StoreVO {

	private String memberId;

	private String memberName;

	private String storeName;

	private LocalDateTime storeEndTime;

	/**
	 * @see StoreStatusEnum
	 */
	private String storeDisable;

	private Boolean selfOperated;

	private String storeLogo;

	private String storeCenter;

	private String storeDesc;

	private String storeAddressPath;

	private String storeAddressIdPath;

	private String storeAddressDetail;

	private BigDecimal descriptionScore;

	private BigDecimal serviceScore;

	private BigDecimal deliveryScore;

	private Integer goodsNum;

	private Integer collectionNum;

	private String yzfSign;

	private String yzfMpSign;

	private String merchantEuid;

	@Schema(description = "库存预警数量")
	private Integer stockWarning;

	@Schema(description = "登录用户的昵称")
	private String nickName;
}
