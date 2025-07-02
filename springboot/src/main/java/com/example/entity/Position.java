package com.example.entity;

import java.util.List;

public class Position {
    private Integer id;
    private String name;
    private Integer employId;
    private Integer industryId;
    private String type;
    private String experience;
    private String salary;
    private String education;
    private String tags;
    private String content;
    private String status;

    private String employName;
    private String employAvatar;
    private String employStage;
    private String employScale;
    private String employCity;
    private String employAddress;
    private String industryName;
    private List<String> tagList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEmployId() {
        return employId;
    }

    public void setEmployId(Integer employId) {
        this.employId = employId;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getEmployStage() {
        return employStage;
    }

    public void setEmployStage(String employStage) {
        this.employStage = employStage;
    }

    public String getEmployScale() {
        return employScale;
    }

    public void setEmployScale(String employScale) {
        this.employScale = employScale;
    }

    public String getEmployCity() {
        return employCity;
    }

    public void setEmployCity(String employCity) {
        this.employCity = employCity;
    }

    public String getEmployAddress() {
        return employAddress;
    }

    public void setEmployAddress(String employAddress) {
        this.employAddress = employAddress;
    }
}
