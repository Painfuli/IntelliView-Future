package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.common.Result;
import com.example.entity.*;
import com.example.service.*;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Resource
    private EmployService employService;
    @Resource
    private UserService userService;
    @Resource
    private PositionService positionService;
    @Resource
    private SubmitService submitService;
    @Resource
    private IndustryService industryService;

    @GetMapping("/base")
    public Result base() {
        Map<String, Integer> map = new HashMap<>();
        // 查询出所有企业信息
        List<Employ> employs = employService.selectAll(new Employ());
        // 查询出所有的用户信息
        List<User> users = userService.selectAll(new User());
        // 查询出所有的岗位信息
        List<Position> positions = positionService.selectAll(new Position());
        // 查询出所有的投递信息
        List<Submit> submits = submitService.selectAll(new Submit());
        map.put("employNum", employs.size());
        map.put("employeeNum", users.size());
        map.put("positionNum", positions.size());
        map.put("submitNum", submits.size());
        return Result.success(map);
    }

    @GetMapping("/pie")
    public Result pie() {
        List<Map<String, Object>> resultList = new ArrayList<>();
        // 查询出所有的投递记录
        List<Submit> submits = submitService.selectAll(new Submit());
        Map<String, Long> map = submits.stream()
                .filter(x -> ObjectUtil.isNotEmpty(x.getPositionName()))
                .collect(Collectors.groupingBy(Submit::getPositionName, Collectors.counting()));
        for (String key : map.keySet()) {
            Map<String, Object> tmpMap = new HashMap<>();
            tmpMap.put("name", key);
            tmpMap.put("value", map.get(key));
            resultList.add(tmpMap);
        }
        return Result.success(resultList);
    }

    @GetMapping("/bar")
    public Result bar() {
        List<String> xAxis = new ArrayList<>();
        List<Long> yAxis = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();

        // 查询出所有的行业和岗位信息
        List<Industry> industries = industryService.selectAll(new Industry());
        List<Position> positions = positionService.selectAll(new Position());
        // 遍历行业信息
        for (Industry industry : industries) {
            xAxis.add(industry.getName());
            long count = positions.stream()
                    .filter(x -> ObjectUtil.isNotEmpty(x.getIndustryName()) && x.getIndustryId().equals(industry.getId()))
                    .count();
            yAxis.add(count);
        }

        resultMap.put("xAxis", xAxis);
        resultMap.put("yAxis", yAxis);
        return Result.success(resultMap);
    }

    @GetMapping("/line")
    public Result line() {
        List<String> xAxis = new ArrayList<>();
        List<Long> yAxis = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();

        Date today = new Date();
        DateTime start = DateUtil.offsetDay(today, -90);
        List<String> list = DateUtil.rangeToList(start, today, DateField.DAY_OF_YEAR).stream().map(DateUtil::formatDate).toList();

        List<Submit> submits = submitService.selectAll(new Submit());
        for (String xAix : list) {
            xAxis.add(xAix);
            long count = submits.stream().filter(x -> x.getTime().contains(xAix)).count();
            yAxis.add(count);
        }

        resultMap.put("xAxis", xAxis);
        resultMap.put("yAxis", yAxis);
        return Result.success(resultMap);
    }
}
