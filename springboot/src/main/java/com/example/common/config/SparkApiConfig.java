package com.example.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "xfyun.spark")
public class SparkApiConfig {
    private String appid;       // 可保留，以备他用
    private String apikey;      // 可保留，以备他用
    private String apisecret;   // 可保留，以备他用

    private String hostUrl;
    private String apiPassword; // 新增
    private String modelName;   // 新增
    // private String domain; // 旧的，可以移除或注释

    // Standard Getters and Setters
    public String getAppid() { return appid; }
    public void setAppid(String appid) { this.appid = appid; }
    public String getApikey() { return apikey; }
    public void setApikey(String apikey) { this.apikey = apikey; }
    public String getApisecret() { return apisecret; }
    public void setApisecret(String apisecret) { this.apisecret = apisecret; }

    public String getHostUrl() { return hostUrl; }
    public void setHostUrl(String hostUrl) { this.hostUrl = hostUrl; }

    public String getApiPassword() { return apiPassword; } // 新增
    public void setApiPassword(String apiPassword) { this.apiPassword = apiPassword; } // 新增

    public String getModelName() { return modelName; } // 新增
    public void setModelName(String modelName) { this.modelName = modelName; } // 新增

    // public String getDomain() { return domain; }
    // public void setDomain(String domain) { this.domain = domain; }
}