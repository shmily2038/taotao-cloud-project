package com.taotao.cloud.java.javaee.s2.c11_distributed.item.java.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.qf.item.mapper.ItemMapper;
import com.qf.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Override
    @Transactional
    @LcnTransaction
    public void update() {
        itemMapper.update();
    }
}
