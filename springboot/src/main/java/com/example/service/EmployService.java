package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Employ;
import com.example.entity.Position;
import com.example.exception.CustomException;
import com.example.mapper.EmployMapper;
import com.example.mapper.PositionMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业信息业务层方法
 */
@Service
public class EmployService {

    @Resource
    private EmployMapper employMapper;
    @Resource
    private PositionMapper positionMapper;

    public void add(Employ employ) {
        Employ dbEmploy = employMapper.selectByUsername(employ.getUsername());
        if (ObjectUtil.isNotNull(dbEmploy)) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        if (ObjectUtil.isEmpty(employ.getPassword())) {
            employ.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if (ObjectUtil.isEmpty(employ.getName())) {
            employ.setName(employ.getUsername());
        }
        employ.setRole(RoleEnum.EMPLOY.name());
        employMapper.insert(employ);
    }

    public void updateById(Employ employ) {
        employMapper.updateById(employ);
        if ("审核通过".equals(employ.getStatus())) {
            // 更新一下该企业岗位对应的新的行业
            List<Position> positions = positionMapper.selectByEmployId(employ.getId());
            for (Position position : positions) {
                if (ObjectUtil.isNotEmpty(position)) {
                    position.setIndustryId(employ.getIndustryId());
                    positionMapper.updateById(position);
                }
            }
        }
    }

    public void deleteById(Integer id) {
        employMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            employMapper.deleteById(id);
        }
    }

    public Employ selectById(Integer id) {
        Employ employ = employMapper.selectById(id);
        // 生成token
        String token = TokenUtils.createToken(employ.getId() + "-" + employ.getRole(), employ.getPassword());
        employ.setToken(token);
        return employ;
    }

    public List<Employ> selectAll(Employ employ) {
        return employMapper.selectAll(employ);
    }

    public PageInfo<Employ> selectPage(Employ employ, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Employ> list = employMapper.selectAll(employ);
        return PageInfo.of(list);
    }

    /**
     * 登录
     */
    public Employ login(Account account) {
        Employ dbEmploy = employMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbEmploy)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!dbEmploy.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        // 生成token
        String token = TokenUtils.createToken(dbEmploy.getId() + "-" + dbEmploy.getRole(), dbEmploy.getPassword());
        dbEmploy.setToken(token);
        return dbEmploy;
    }

    /**
     * 修改密码
     */
    public void updatePassword(Account account) {
        Employ dbEmploy = employMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbEmploy)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!account.getPassword().equals(dbEmploy.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbEmploy.setPassword(account.getNewPassword());
        employMapper.updateById(dbEmploy);
    }

    public void register(Account account) {
        Employ employ = new Employ();
        BeanUtils.copyProperties(account, employ);
        employ.setStatus("待审核");
        add(employ);
    }
}
