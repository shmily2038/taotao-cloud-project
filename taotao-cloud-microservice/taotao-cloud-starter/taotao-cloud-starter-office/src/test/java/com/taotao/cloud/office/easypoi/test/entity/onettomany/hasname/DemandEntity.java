package com.taotao.cloud.office.easypoi.test.entity.onettomany.hasname;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 需求梳理表
 *
 * @author Walt
 */
public class DemandEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 需求部门名称
     */
    @Excel(name = "部门", needMerge = true, width = 20)
    private String deptName = "";

    /**
     * 编码
     */
    @Excel(name = "供需编码", needMerge = true, width = 20)
    private String code;

    /**
     * 名称
     */
    @Excel(name = "供需名称", needMerge = true, width = 20)
    private String name;

    /**
     * 类别
     */
    @Excel(name = "类别(数据字典)", needMerge = true, dict = "category")
    private Integer category;

    /**
     * 年平均办件量
     */
    @Excel(name = "年办件量", needMerge = true)
    private Long handleTotal;
    @ExcelCollection(name = "需求材料表")
    private List<SupMaterialEntity> supMaterialList2 = Lists.newArrayList();
    /**
     * 需求材料表
     */
    @ExcelCollection(name = "需求材料表")
    private List<SupMaterialEntity> supMaterialList = Lists.newArrayList();

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Long getHandleTotal() {
        return handleTotal;
    }

    public void setHandleTotal(Long handleTotal) {
        this.handleTotal = handleTotal;
    }

    public List<SupMaterialEntity> getSupMaterialList2() {
        return supMaterialList2;
    }

    public void setSupMaterialList2(List<SupMaterialEntity> supMaterialList2) {
        this.supMaterialList2 = supMaterialList2;
    }

    public List<SupMaterialEntity> getSupMaterialList() {
        return supMaterialList;
    }

    public void setSupMaterialList(List<SupMaterialEntity> supMaterialList) {
        this.supMaterialList = supMaterialList;
    }
}
