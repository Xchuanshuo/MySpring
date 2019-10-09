package com.legend.demo.service.impl;

import com.legend.annotation.Autowired;
import com.legend.annotation.Service;
import com.legend.demo.model.Boy;
import com.legend.demo.repository.BoyMapper;
import com.legend.demo.service.BoyService;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-10-15.
 * @description
 */
@Service
public class BoyServiceImpl implements BoyService {

    @Autowired
    private BoyMapper boyMapper;

    @Override
    public List<Boy> getAllBoy() {
        return boyMapper.queryAllBoy();
    }
}
