package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Submit;
import com.example.exception.CustomException;
import com.example.mapper.SubmitMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 简历投递业务层方法
 */
@Service
public class SubmitService {

    @Resource
    private SubmitMapper submitMapper;

    public void add(Submit submit) {
        submit.setTime(DateUtil.now());
        submit.setStatus("已投递");
        List<Submit> list = submitMapper.selectByUserIdAndPositionId(submit.getUserId(), submit.getPositionId());
        if (CollectionUtil.isNotEmpty(list)) {
            throw new CustomException("-1", "您已经投递过该岗位，请勿重复投递！");
        }
        submitMapper.insert(submit);
    }

    public void updateById(Submit submit) {
        submitMapper.updateById(submit);
    }

    public void deleteById(Integer id) {
        submitMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            submitMapper.deleteById(id);
        }
    }

    public Submit selectById(Integer id) {
        return submitMapper.selectById(id);
    }

    public List<Submit> selectAll(Submit submit) {
        return submitMapper.selectAll(submit);
    }

    public PageInfo<Submit> selectPage(Submit submit, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.USER.name().equals(currentUser.getRole())) {
            submit.setUserId(currentUser.getId());
        }
        if (RoleEnum.EMPLOY.name().equals(currentUser.getRole())) {
            submit.setEmployId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Submit> list = submitMapper.selectAll(submit);
        return PageInfo.of(list);
    }

}
