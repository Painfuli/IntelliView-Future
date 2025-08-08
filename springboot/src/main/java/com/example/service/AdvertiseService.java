package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.example.entity.Advertise;
import com.example.exception.CustomException;
import com.example.mapper.AdvertiseMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 广告位信息业务层方法
 */
@Service
public class AdvertiseService {

    @Resource
    private AdvertiseMapper advertiseMapper;

    public void add(Advertise advertise) {
        List<Advertise> list = advertiseMapper.selectByLocation(advertise.getLocation());
        if (CollectionUtil.isNotEmpty(list)) {
            throw new CustomException("-1", "您选择的广告位已经被占用了，请选择其他广告位");
        }
        advertiseMapper.insert(advertise);
    }

    public void updateById(Advertise advertise) {
        List<Advertise> list = advertiseMapper.selectByLocation(advertise.getLocation());
        if (CollectionUtil.isNotEmpty(list)) {
            if (!advertise.getId().equals(list.get(0).getId())) {
                throw new CustomException("-1", "您选择的广告位已经被占用了，请选择其他广告位");
            }
        }
        advertiseMapper.updateById(advertise);
    }

    public void deleteById(Integer id) {
        advertiseMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            advertiseMapper.deleteById(id);
        }
    }

    public Advertise selectById(Integer id) {
        return advertiseMapper.selectById(id);
    }

    public List<Advertise> selectAll(Advertise advertise) {
        return advertiseMapper.selectAll(advertise);
    }

    public PageInfo<Advertise> selectPage(Advertise advertise, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Advertise> list = advertiseMapper.selectAll(advertise);
        return PageInfo.of(list);
    }

}
