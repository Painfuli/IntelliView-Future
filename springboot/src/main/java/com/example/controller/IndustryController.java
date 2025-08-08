package com.example.controller;

import com.example.common.Result;
import com.example.entity.Industry;
import com.example.service.IndustryService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 行业信息前端请求接口
 */
@RestController
@RequestMapping("/industry")
public class IndustryController {

    @Resource
    private IndustryService industryService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Industry industry) {
        industryService.add(industry);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Industry industry) {
        industryService.updateById(industry);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        industryService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        industryService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Industry industry = industryService.selectById(id);
        return Result.success(industry);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Industry industry) {
        List<Industry> list = industryService.selectAll(industry);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Industry industry,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Industry> pageInfo = industryService.selectPage(industry, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
