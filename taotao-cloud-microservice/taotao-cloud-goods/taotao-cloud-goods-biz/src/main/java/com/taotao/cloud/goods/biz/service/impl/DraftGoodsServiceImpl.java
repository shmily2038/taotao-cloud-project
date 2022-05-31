package com.taotao.cloud.goods.biz.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.goods.api.dto.DraftGoodsBaseDTO;
import com.taotao.cloud.goods.api.dto.DraftGoodsBaseDTOBuilder;
import com.taotao.cloud.goods.api.dto.DraftGoodsDTO;
import com.taotao.cloud.goods.api.dto.GoodsParamsDTO;
import com.taotao.cloud.goods.api.query.DraftGoodsPageQuery;
import com.taotao.cloud.goods.api.vo.DraftGoodsBaseVO;
import com.taotao.cloud.goods.api.vo.DraftGoodsVO;
import com.taotao.cloud.goods.api.vo.DraftGoodsVOBuilder;
import com.taotao.cloud.goods.biz.entity.Category;
import com.taotao.cloud.goods.biz.entity.DraftGoods;
import com.taotao.cloud.goods.biz.entity.GoodsGallery;
import com.taotao.cloud.goods.biz.entity.GoodsSku;
import com.taotao.cloud.goods.biz.mapper.IDraftGoodsMapper;
import com.taotao.cloud.goods.biz.mapstruct.IDraftGoodsMapStruct;
import com.taotao.cloud.goods.biz.service.ICategoryService;
import com.taotao.cloud.goods.biz.service.IDraftGoodsService;
import com.taotao.cloud.goods.biz.service.IGoodsGalleryService;
import com.taotao.cloud.goods.biz.service.IGoodsSkuService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 草稿商品业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:24
 */
@AllArgsConstructor
@Service
public class DraftGoodsServiceImpl extends ServiceImpl<IDraftGoodsMapper, DraftGoods> implements
	IDraftGoodsService {

	/**
	 * 分类
	 */
	private final ICategoryService categoryService;
	/**
	 * 商品相册
	 */
	private final IGoodsGalleryService goodsGalleryService;
	/**
	 * 规格商品
	 */
	private final IGoodsSkuService goodsSkuService;

	@Override
	public Boolean addGoodsDraft(DraftGoodsDTO draftGoods) {
		DraftGoodsBaseDTO draftGoodsBaseDTO = draftGoods.draftGoodsBase();
		DraftGoodsBaseDTOBuilder builder = DraftGoodsBaseDTOBuilder.builder(draftGoodsBaseDTO);

		builder.goodsGalleryListJson(JSONUtil.toJsonStr(draftGoods.goodsGalleryList()));
		builder.skuListJson(JSONUtil.toJsonStr(draftGoods.skuList()));
		builder.goodsParamsListJson(JSONUtil.toJsonStr(draftGoods.goodsParamsDTOList()));

		return this.save(IDraftGoodsMapStruct.INSTANCE.draftGoodsDTOToDraftGoods(builder.build()));
	}

	@Override
	public Boolean updateGoodsDraft(DraftGoodsDTO draftGoods) {
		DraftGoodsBaseDTO draftGoodsBaseDTO = draftGoods.draftGoodsBase();
		DraftGoodsBaseDTOBuilder builder = DraftGoodsBaseDTOBuilder.builder(draftGoodsBaseDTO);

		builder.goodsGalleryListJson(JSONUtil.toJsonStr(draftGoods.goodsGalleryList()));
		builder.skuListJson(JSONUtil.toJsonStr(draftGoods.skuList()));
		builder.goodsParamsListJson(JSONUtil.toJsonStr(draftGoods.goodsParamsDTOList()));

		DraftGoods draftGoods1 = IDraftGoodsMapStruct.INSTANCE.draftGoodsDTOToDraftGoods(
			builder.build());

		// todo 此处需要修改
		draftGoods1.setId(0L);

		return this.updateById(draftGoods1);
	}

	@Override
	public Boolean saveGoodsDraft(DraftGoodsDTO draftGoods) {
		DraftGoodsBaseDTO draftGoodsBaseDTO = draftGoods.draftGoodsBase();
		DraftGoodsBaseDTOBuilder builder = DraftGoodsBaseDTOBuilder.builder(draftGoodsBaseDTO);

		if (draftGoods.goodsGalleryList() != null && !draftGoods.goodsGalleryList()
			.isEmpty()) {
			GoodsGallery goodsGallery = goodsGalleryService.getGoodsGallery(
				draftGoods.goodsGalleryList().get(0));
			builder.original(goodsGallery.getOriginal());
			builder.small(goodsGallery.getSmall());
			builder.thumbnail(goodsGallery.getThumbnail());
		}

		builder.goodsGalleryListJson(JSONUtil.toJsonStr(draftGoods.goodsGalleryList()));
		builder.skuListJson(
			JSONUtil.toJsonStr(this.getGoodsSkuList(draftGoods.skuList())));
		builder.goodsParamsListJson(JSONUtil.toJsonStr(draftGoods.goodsParamsDTOList()));

		return this.saveOrUpdate(IDraftGoodsMapStruct.INSTANCE.draftGoodsDTOToDraftGoods(builder.build()));
	}

	@Override
	public Boolean deleteGoodsDraft(Long id) {
		return this.removeById(id);
	}

	@Override
	public DraftGoodsVO getDraftGoods(Long id) {
		DraftGoods draftGoods = this.getById(id);
		DraftGoodsBaseVO draftGoodsBaseVO = IDraftGoodsMapStruct.INSTANCE.draftGoodsDTOToDraftGoods(draftGoods);
		DraftGoodsVOBuilder draftGoodsVOBuilder = DraftGoodsVOBuilder.builder().draftGoodsBase(draftGoodsBaseVO);

		//商品分类名称赋值
		List<String> categoryName = new ArrayList<>();
		String[] strArray = draftGoods.getCategoryPath().split(",");
		List<Category> categories = categoryService.listByIds(Arrays.asList(strArray));
		for (Category category : categories) {
			categoryName.add(category.getName());
		}
		draftGoodsVOBuilder.categoryName(categoryName);
		draftGoodsVOBuilder.goodsParamsDTOList(
			JSONUtil.toList(JSONUtil.parseArray(draftGoods.getGoodsParamsListJson()),
				GoodsParamsDTO.class));
		draftGoodsVOBuilder.goodsGalleryList(
			JSONUtil.toList(JSONUtil.parseArray(draftGoods.getGoodsGalleryListJson()),
				String.class));
		JSONArray jsonArray = JSONUtil.parseArray(draftGoods.getSkuListJson());
		List<GoodsSku> list = JSONUtil.toList(jsonArray, GoodsSku.class);
		draftGoodsVOBuilder.skuList(goodsSkuService.getGoodsSkuVOList(list));
		return draftGoodsVOBuilder.build();
	}

	@Override
	public IPage<DraftGoods> getDraftGoods(DraftGoodsPageQuery searchParams) {
		return this.page(searchParams.buildMpPage(), searchParams.queryWrapper());
	}

	/**
	 * 获取sku集合
	 *
	 * @param skuList sku列表
	 * @return sku集合
	 */
	private List<GoodsSku> getGoodsSkuList(List<Map<String, Object>> skuList) {
		List<GoodsSku> skus = new ArrayList<>();
		for (Map<String, Object> skuVO : skuList) {
			GoodsSku add = this.add(skuVO);
			skus.add(add);
		}
		return skus;
	}

	private GoodsSku add(Map<String, Object> map) {
		Map<String, Object> specMap = new HashMap<>(2);
		GoodsSku sku = new GoodsSku();
		for (Map.Entry<String, Object> m : map.entrySet()) {
			switch (m.getKey()) {
				case "sn" -> sku.setSn(m.getValue() != null ? m.getValue().toString() : "");
				case "cost" -> sku.setCost(Convert.toBigDecimal(m.getValue()));
				case "price" -> sku.setPrice(Convert.toBigDecimal(m.getValue()));
				case "quantity" -> sku.setQuantity(Convert.toInt(m.getValue()));
				case "weight" -> sku.setWeight(Convert.toBigDecimal(m.getValue()));
				default -> specMap.put(m.getKey(), m.getValue());
			}
		}
		sku.setSpecs(JSONUtil.toJsonStr(specMap));
		return sku;
	}
}
