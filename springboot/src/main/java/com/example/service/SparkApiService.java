package com.example.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.common.config.SparkApiConfig;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SparkApiService {

    private static final Logger log = LoggerFactory.getLogger(SparkApiService.class);
    private final SparkApiConfig sparkApiConfig;
    private final OkHttpClient client;

    // 内部类 Message，用于构建对话历史
    public static class Message {
        private String role;
        private String content;

        // 私有构造函数，强制使用builder
        private Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() { return role; }
        public String getContent() { return content; }

        // Builder模式入口
        public static MessageBuilder builder() {
            return new MessageBuilder();
        }

        public static class MessageBuilder {
            private String role;
            private String content;

            public MessageBuilder role(String role) {
                this.role = role;
                return this;
            }

            public MessageBuilder content(String content) {
                this.content = content;
                return this;
            }

            public Message build() {
                if (this.role == null || this.content == null) {
                    // 或者根据你的业务逻辑，允许content为空（例如，某些特殊role）
                    throw new IllegalArgumentException("Role and Content cannot be null for a Message.");
                }
                // 确保 role 是小写且是有效值之一
                String lowerCaseRole = this.role.toLowerCase();
                if (!("system".equals(lowerCaseRole) || "user".equals(lowerCaseRole) || "assistant".equals(lowerCaseRole) || "tool".equals(lowerCaseRole))) {
                    log.warn("Attempting to build Message with potentially invalid role: '{}'. Standard roles are 'system', 'user', 'assistant', 'tool'.", this.role);
                    // 可以选择抛出异常，或者容错处理（例如，默认转为 "user" 或记录更详细日志）
                    // throw new IllegalArgumentException("Invalid role: " + this.role + ". Must be one of 'system', 'user', 'assistant', 'tool'.");
                }
                // 存储时统一使用小写，确保一致性
                return new Message(lowerCaseRole, this.content);
            }
        }
    }

    public SparkApiService(SparkApiConfig sparkApiConfig) {
        this.sparkApiConfig = sparkApiConfig;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    public String getSparkResponse(String platformUserId, List<Message> messageHistory) throws IOException {
        JSONObject requestJson = new JSONObject();
        requestJson.put("model", sparkApiConfig.getModelName());

        JSONArray messagesArray = new JSONArray();
        for (Message msg : messageHistory) {
            JSONObject m = new JSONObject();
            // 直接使用 Message 对象中已经处理过（例如转为小写）的 role
            m.put("role", msg.getRole());
            m.put("content", msg.getContent());
            messagesArray.add(m);
        }
        // **********************************************************************
        // ** 添加DEBUG日志，用于检查实际发送的messages数组内容 **
        // **********************************************************************
        log.debug("Constructed messages array for Spark API: {}", messagesArray.toJSONString());
        requestJson.put("messages", messagesArray);


        requestJson.put("temperature", 0.6);
        requestJson.put("max_tokens", 2048);
        if (platformUserId != null && !platformUserId.isEmpty()) {
            requestJson.put("user", platformUserId); // 对应API文档中的 user 字段
        }

        log.info("Sending request to Spark API (OpenAI compatible). Model: {}, URL: {}",
                sparkApiConfig.getModelName(), sparkApiConfig.getHostUrl());

        RequestBody body = RequestBody.create(requestJson.toJSONString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(sparkApiConfig.getHostUrl())
                .addHeader("Authorization", "Bearer " + sparkApiConfig.getApiPassword())
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBodyString = "";
            if (response.body() != null) {
                responseBodyString = response.body().string();
            }
            // log.debug("Spark API Response Body: {}", responseBodyString); // 可以取消注释以查看完整响应

            if (!response.isSuccessful()) {
                log.error("Spark API Request Failed: Status Code - {}, Body - {}", response.code(), responseBodyString);
                if (responseBodyString != null && !responseBodyString.isEmpty()) {
                    try {
                        JSONObject errorObject = JSON.parseObject(responseBodyString);
                        if (errorObject.containsKey("error") && errorObject.getJSONObject("error").containsKey("message")) {
                            throw new IOException("讯飞星火API请求失败: " + errorObject.getJSONObject("error").getString("message") + " (HTTP Status: " + response.code() + ")");
                        }
                    } catch (Exception ignored) {}
                }
                throw new IOException("讯飞星火API请求失败，状态码: " + response.code() + ". 详情: " + responseBodyString);
            }

            JSONObject responseObject = JSON.parseObject(responseBodyString);
            if (responseObject.containsKey("code") && responseObject.getInteger("code") != 0) {
                log.error("Spark API Error (from response body): Code - {}, Message - {}, SID - {}",
                        responseObject.getInteger("code"),
                        responseObject.getString("message"),
                        responseObject.getString("sid"));
                throw new IOException("讯飞星火API返回业务错误: " + responseObject.getString("message") +
                        " (Code: " + responseObject.getInteger("code") +
                        ", SID: " + responseObject.getString("sid") + ")");
            }

            JSONArray choicesArray = responseObject.getJSONArray("choices");
            if (choicesArray != null && !choicesArray.isEmpty()) {
                JSONObject firstChoice = choicesArray.getJSONObject(0);
                if (firstChoice.containsKey("message") && firstChoice.getJSONObject("message").containsKey("content")) {
                    return firstChoice.getJSONObject("message").getString("content");
                }
            }

            log.warn("Spark API did not return expected content structure. Full response: {}", responseBodyString);
            return "AI未能生成有效回复（响应结构不符），请稍后再试。";
        }
    }
}