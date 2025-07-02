package com.example.entity;

import java.util.List;

public class Collect {
    private Integer id;
    private Integer studentId;
    private Integer positionId;

    private String positionName;
    private String positionSalary;
    private List<String> tagList;
    private String employAvatar;
    private String employName;
    private String industryName;
    private String employStage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionSalary() {
        return positionSalary;
    }

    public void setPositionSalary(String positionSalary) {
        this.positionSalary = positionSalary;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public String getEmployAvatar() {
        return employAvatar;
    }

    public void setEmployAvatar(String employAvatar) {
        this.employAvatar = employAvatar;
    }

    public String getEmployName() {
        return employName;
    }

    public void setEmployName(String employName) {
        this.employName = employName;
    }

    public String getIndustryName() {
        return industryName;
    }

    public void setIndustryName(String industryName) {
        this.industryName = industryName;
    }

    public String getEmployStage() {
        return employStage;
    }

    public void setEmployStage(String employStage) {
        this.employStage = employStage;
    }
}
