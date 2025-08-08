package com.example.controller;

import com.example.common.Result;
import com.example.entity.Position;
import com.example.service.PositionService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 职位信息前端请求接口
 */
@RestController
@RequestMapping("/position")
public class PositionController {

    @Resource
    private PositionService positionService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Position position) {
        positionService.add(position);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Position position) {
        positionService.updateById(position);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        positionService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        positionService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Position position = positionService.selectById(id);
        return Result.success(position);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Position position) {
        List<Position> list = positionService.selectAll(position);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Position position,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Position> pageInfo = positionService.selectPage(position, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    @GetMapping("/recommend")
    public Result recommend() {
        List<Position> list = positionService.recommend();
        return Result.success(list);
    }

}
