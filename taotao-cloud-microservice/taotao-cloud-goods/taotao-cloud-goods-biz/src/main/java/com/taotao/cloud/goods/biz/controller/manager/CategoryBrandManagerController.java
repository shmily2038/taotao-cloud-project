package com.taotao.cloud.goods.biz.controller.manager;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.vo.CategoryBrandVO;
import com.taotao.cloud.goods.biz.service.CategoryBrandService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,分类品牌接口
 */
@Validated
@RestController
@Tag(name = "平台管理端-分类品牌管理API", description = "平台管理端-分类品牌管理API")
@RequestMapping("/goods/manager/categoryBrand")
public class CategoryBrandManagerController {

	/**
	 * 规格品牌管理
	 */
	@Autowired
	private CategoryBrandService categoryBrandService;

	@Operation(summary = "查询某分类下绑定的品牌信息", description = "查询某分类下绑定的品牌信息", method = CommonConstant.GET)
	@RequestLogger(description = "查询某分类下绑定的品牌信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{categoryId}")
	public Result<List<CategoryBrandVO>> getCategoryBrand(@PathVariable String categoryId) {
		return Result.success(categoryBrandService.getCategoryBrandList(categoryId));
	}

	@Operation(summary = "保存某分类下绑定的品牌信息", description = "保存某分类下绑定的品牌信息", method = CommonConstant.POST)
	@RequestLogger(description = "保存某分类下绑定的品牌信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping(value = "/{categoryId}")
	public Result<Boolean> saveCategoryBrand(@PathVariable String categoryId,
		@RequestParam List<String> categoryBrands) {
		return Result.success(categoryBrandService.saveCategoryBrandList(categoryId, categoryBrands));
	}

}
