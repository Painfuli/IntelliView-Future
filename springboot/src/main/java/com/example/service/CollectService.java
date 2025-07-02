package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.entity.Industry;
import com.example.entity.Position;
import com.example.exception.CustomException;
import com.example.mapper.CollectMapper;
import com.example.mapper.IndustryMapper;
import com.example.mapper.PositionMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 收藏信息业务层方法
 */
@Service
public class CollectService {

    @Resource
    private CollectMapper collectMapper;
    @Resource
    private PositionMapper positionMapper;
    @Resource
    private IndustryMapper industryMapper;

    public void add(Collect collect) {
        List<Collect> collects = collectMapper.selectAll(collect);
        if (CollectionUtil.isNotEmpty(collects)) {
            throw new CustomException("-1", "您已经收藏过该岗位了，请勿重复收藏");
        }
        collectMapper.insert(collect);
    }

    public void updateById(Collect collect) {
        collectMapper.updateById(collect);
    }

    public void deleteById(Integer id) {
        collectMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            collectMapper.deleteById(id);
        }
    }

    public Collect selectById(Integer id) {
        return collectMapper.selectById(id);
    }

    public List<Collect> selectAll(Collect collect) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            collect.setStudentId(currentUser.getId());
        }
        List<Collect> collects = collectMapper.selectAll(collect);
        for (Collect dbCollect : collects) {
            Position position = positionMapper.selectById(dbCollect.getPositionId());
            if (ObjectUtil.isNotEmpty(position)) {
                dbCollect.setPositionName(position.getName());
                dbCollect.setPositionSalary(position.getSalary());
                String tags = position.getTags();
                if (ObjectUtil.isNotEmpty(tags)) {
                    String[] split = tags.split(",");
                    List<String> list = Arrays.asList(split);
                    if (list.size() > 3) {
                        dbCollect.setTagList(list.subList(0, 3));
                    } else {
                        dbCollect.setTagList(list);
                    }
                }
                dbCollect.setEmployAvatar(position.getEmployAvatar());
                dbCollect.setEmployName(position.getEmployName());
                dbCollect.setEmployStage(position.getEmployStage());

                Industry industry = industryMapper.selectById(position.getIndustryId());
                if (ObjectUtil.isNotEmpty(industry)) {
                    dbCollect.setIndustryName(industry.getName());
                }
            }
        }
        return collects;
    }

    public PageInfo<Collect> selectPage(Collect collect, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Collect> list = collectMapper.selectAll(collect);
        return PageInfo.of(list);
    }

}
