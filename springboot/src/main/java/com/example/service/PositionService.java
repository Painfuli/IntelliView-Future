package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.RoleEnum;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.*;
import com.example.utils.TokenUtils;
import com.example.utils.UserCF;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 职位信息业务层方法
 */
@Service
public class PositionService {

    @Resource
    private PositionMapper positionMapper;
    @Resource
    private EmployMapper employMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CollectMapper collectMapper;
    @Resource
    private SubmitMapper submitMapper;

    public void add(Position position) {
        Account currentUser = TokenUtils.getCurrentUser();
        Employ employ = employMapper.selectById(currentUser.getId());
        if (!"审核通过".equals(employ.getStatus())) {
            throw new CustomException("500", "您的资质还未审核通过。不允许新增岗位");
        }
        position.setEmployId(employ.getId());
        position.setIndustryId(employ.getIndustryId());
        position.setStatus("待审核");
        positionMapper.insert(position);
    }

    public void updateById(Position position) {
        positionMapper.updateById(position);
    }

    public void deleteById(Integer id) {
        positionMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            positionMapper.deleteById(id);
        }
    }

    public Position selectById(Integer id) {
        Position position = positionMapper.selectById(id);
        String tags = position.getTags();
        if (ObjectUtil.isNotEmpty(tags)) {
            String[] split = tags.split(",");
            position.setTagList(Arrays.asList(split));
        }
        return position;
    }

    public List<Position> selectAll(Position position) {
        List<Position> positions = positionMapper.selectAll(position);
        extracted(positions);
        return positions;
    }

    private static void extracted(List<Position> positions) {
        for (Position dbPosition : positions) {
            String tags = dbPosition.getTags();
            if (ObjectUtil.isNotEmpty(tags)) {
                String[] split = tags.split(",");
                List<String> list = Arrays.asList(split);
                if (list.size() > 3) {
                    dbPosition.setTagList(list.subList(0, 3));
                } else {
                    dbPosition.setTagList(list);
                }
            }
        }
    }

    public PageInfo<Position> selectPage(Position position, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.EMPLOY.name().equals(currentUser.getRole())) {
            position.setEmployId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Position> list = positionMapper.selectAll(position);
        return PageInfo.of(list);
    }

    /**
     * 推荐岗位（基于协同过滤推荐算法）
     */
    public List<Position> recommend() {
        Account currentUser = TokenUtils.getCurrentUser();
        // 2. 获取所有的岗位信息
        List<Position> positions = positionMapper.selectAll(new Position());
        if (ObjectUtil.isEmpty(currentUser)) {
            // 说明这个时候是游客模式
            return getRandomPositions(3, positions, null);
        }
        // 1. 获取所有的用户信息
        List<User> users = userMapper.selectAll(new User());
        // 3. 获取所有的收藏信息
        List<Collect> collects = collectMapper.selectAll(new Collect());
        // 4. 获取所有的投递信息
        List<Submit> submits = submitMapper.selectAll(new Submit());
        // 存储所有的用户和所有的岗位之间的相关性指数的数据
        List<RelateDTO> data = new ArrayList<>();

        // 开始计算所有用户和所有岗位之间的相关性指数的数据
        for (Position position : positions) {
            Integer positionId = position.getId();
            for (User user : users) {
                Integer userId = user.getId();
                int index = 1;
                // 如果该用户收藏过该岗位，我们权重给他 1
                List<Collect> collectList = collects.stream()
                        .filter(x -> x.getPositionId().equals(positionId) && x.getStudentId().equals(userId))
                        .collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(collectList)) {
                    index += 1;
                }
                // 如果该用户投递过该岗位，我们权重给他 2
                List<Submit> submitList = submits.stream()
                        .filter(x -> x.getPositionId().equals(positionId) && x.getUserId().equals(userId))
                        .collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(submitList)) {
                    index += 2;
                }
                if (index > 1) {
                    RelateDTO relateDTO = new RelateDTO(userId, positionId, index);
                    data.add(relateDTO);
                }
            }
        }
        // 调用基于用户行为的UserCF的推荐方法获取到被推荐的岗位的id的list
        List<Integer> positionIds = UserCF.recommend(currentUser.getId(), data);
        // 把list里对应的岗位id变成岗位信息
        List<Position> result = positions.stream().filter(x -> positionIds.contains(x.getId())).collect(Collectors.toList());

        // 如果这个推荐出来的list是空的
        if (CollectionUtil.isEmpty(result)) {
            result = getRandomPositions(3, positions, null);
        }
        // 如果推荐的数据不够3个
        if (result.size() < 3) {
            result.addAll(getRandomPositions(3 - result.size(), positions, result));
        }
        extracted(result);
        return result;
    }

    public List<Position> getRandomPositions(int num, List<Position> positions, List<Position> result) {
        Collections.shuffle(positions);
        if (CollectionUtil.isNotEmpty(result)) {
            positions = positions.stream().filter(x -> !result.contains(x)).collect(Collectors.toList());
        }
        return positions.size() > num ? positions.subList(0, num) : positions;
    }
}
