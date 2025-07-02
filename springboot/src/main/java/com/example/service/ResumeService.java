package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.example.entity.EduExp;
import com.example.entity.ProExp;
import com.example.entity.Resume;
import com.example.entity.WorkExp;
import com.example.mapper.ResumeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 简历信息业务层方法
 */
@Service
public class ResumeService {

    @Resource
    private ResumeMapper resumeMapper;

    public void add(Resume resume) {
        // 我们要把从前端接收到的结构化数据转成json字符串
        resume.setEduExps(JSONUtil.toJsonStr(resume.getEduExpList()));
        resume.setWorkExps(JSONUtil.toJsonStr(resume.getWorkExpList()));
        resumeMapper.insert(resume);
    }

    public void updateById(Resume resume) {
        // 我们要把从前端接收到的结构化数据转成json字符串
        resume.setEduExps(JSONUtil.toJsonStr(resume.getEduExpList()));
        resume.setWorkExps(JSONUtil.toJsonStr(resume.getWorkExpList()));
        resumeMapper.updateById(resume);
    }

    public void deleteById(Integer id) {
        resumeMapper.deleteById(id);
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            resumeMapper.deleteById(id);
        }
    }

    public Resume selectById(Integer id) {
        Resume resume = resumeMapper.selectById(id);
        // 我们要把从数据库里查出来的json字符串转成List
        resume.setEduExpList(JSONUtil.toList(resume.getEduExps(), EduExp.class));
        resume.setWorkExpList(JSONUtil.toList(resume.getWorkExps(), WorkExp.class));
        resume.setProExpList(JSONUtil.toList(resume.getProExps(), ProExp.class));
        return resume;
    }

    public List<Resume> selectAll(Resume resume) {
        return resumeMapper.selectAll(resume);
    }

    public PageInfo<Resume> selectPage(Resume resume, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Resume> list = resumeMapper.selectAll(resume);
        return PageInfo.of(list);
    }

}
