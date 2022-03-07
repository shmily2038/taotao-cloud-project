package com.taotao.cloud.core.heaven.support.sort.impl;

import com.taotao.cloud.core.heaven.support.sort.ISort;
import java.util.List;

/**
 * 不进行任何排序
 */
public class NoSort<T> implements ISort<T> {

    @Override
    public List<T> sort(List<T> list) {
        return list;
    }

}
