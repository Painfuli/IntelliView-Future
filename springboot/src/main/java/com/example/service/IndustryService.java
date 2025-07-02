package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Industry;
import com.example.mapper.IndustryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 行业信息业务层方法
 */
@Service
public class IndustryService {

    @Resource
    private IndustryMapper industryMapper;

    public void add(Industry industry) {
        industryMapper.insert(industry);
    }

    public void updateById(Industry industry) {
        industryMapper.updateById(industry);
    }

    public void deleteById(Integer id) {
        industryMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            industryMapper.deleteById(id);
        }
    }

    public Industry selectById(Integer id) {
        return industryMapper.selectById(id);
    }

    public List<Industry> selectAll(Industry industry) {
        return industryMapper.selectAll(industry);
    }

    public PageInfo<Industry> selectPage(Industry industry, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Industry> list = industryMapper.selectAll(industry);
        return PageInfo.of(list);
    }

}
