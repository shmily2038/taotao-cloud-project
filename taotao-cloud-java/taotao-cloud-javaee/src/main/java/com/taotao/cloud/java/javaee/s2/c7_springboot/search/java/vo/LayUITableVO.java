package com.taotao.cloud.java.javaee.s2.c7_springboot.search.java.vo;


import lombok.Data;

import java.util.List;

@Data
public class LayUITableVO<T> {

    private Integer code = 0;

    private String msg = "";

    private Long count;

    private List<T> data;

}
