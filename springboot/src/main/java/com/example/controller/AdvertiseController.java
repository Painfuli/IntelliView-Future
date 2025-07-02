package com.example.controller;

import com.example.common.Result;
import com.example.entity.Advertise;
import com.example.service.AdvertiseService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 广告位信息前端请求接口
 */
@RestController
@RequestMapping("/advertise")
public class AdvertiseController {

    @Resource
    private AdvertiseService advertiseService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Advertise advertise) {
        advertiseService.add(advertise);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Advertise advertise) {
        advertiseService.updateById(advertise);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        advertiseService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        advertiseService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Advertise advertise = advertiseService.selectById(id);
        return Result.success(advertise);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Advertise advertise) {
        List<Advertise> list = advertiseService.selectAll(advertise);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Advertise advertise,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Advertise> pageInfo = advertiseService.selectPage(advertise, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
