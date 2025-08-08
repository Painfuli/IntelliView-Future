package com.example.entity;

import java.util.List;

public class Resume {
    private Integer id;
    private String name;
    private String username;
    private String sex;
    private String salary;
    private String education;
    private String experience;
    private String phone;
    private String email;
    private String eduExps;
    private String workExps;
    private String proExps;
    private Integer userId;

    private String userAvatar;
    private List<EduExp> eduExpList;
    private List<WorkExp> workExpList;
    private List<ProExp> proExpList;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEduExps() {
        return eduExps;
    }

    public void setEduExps(String eduExps) {
        this.eduExps = eduExps;
    }

    public String getWorkExps() {
        return workExps;
    }

    public void setWorkExps(String workExps) {
        this.workExps = workExps;
    }

    public String getProExps() {
        return proExps;
    }

    public void setProExps(String proExps) {
        this.proExps = proExps;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<EduExp> getEduExpList() {
        return eduExpList;
    }

    public void setEduExpList(List<EduExp> eduExpList) {
        this.eduExpList = eduExpList;
    }

    public List<WorkExp> getWorkExpList() {
        return workExpList;
    }

    public void setWorkExpList(List<WorkExp> workExpList) {
        this.workExpList = workExpList;
    }

    public List<ProExp> getProExpList() {
        return proExpList;
    }

    public void setProExpList(List<ProExp> proExpList) {
        this.proExpList = proExpList;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
}
