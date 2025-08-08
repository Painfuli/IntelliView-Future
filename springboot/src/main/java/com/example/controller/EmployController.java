package com.example.controller;

import com.example.common.Result;
import com.example.entity.Employ;
import com.example.service.EmployService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业信息前端请求接口
 */
@RestController
@RequestMapping("/employ")
public class EmployController {

    @Resource
    private EmployService employService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Employ employ) {
        employService.add(employ);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Employ employ) {
        employService.updateById(employ);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        employService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        employService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Employ employ = employService.selectById(id);
        return Result.success(employ);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Employ employ) {
        List<Employ> list = employService.selectAll(employ);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Employ employ,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Employ> pageInfo = employService.selectPage(employ, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
