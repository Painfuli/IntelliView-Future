//package com.example.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.example.common.Result;
//import com.example.entity.Account;
//import com.example.service.AiInterviewDbService;
//import com.example.service.SparkApiService;
//import com.example.utils.TokenUtils;
//import jakarta.annotation.Resource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/ai/interview")
//public class AiInterviewController {
//
//    private static final Logger log = LoggerFactory.getLogger(AiInterviewController.class);
//
//    @Resource
//    private SparkApiService sparkApiService;
//
//    @Resource
//    private AiInterviewDbService aiInterviewDbService;
//
//    // --- DTOs (Data Transfer Objects) ---
//    // 确保每个DTO都有正确的 public getter 和 setter 方法，
//    // 以便Spring MVC (通常使用Jackson或FastJSON) 能够正确地进行请求体的反序列化和响应体的序列化。
//
//    static class StartRequestDto {
//        private String jobTitle;
//
//        public String getJobTitle() { // 公开的getter
//            return jobTitle;
//        }
//
//        public void setJobTitle(String jobTitle) { // 公开的setter
//            this.jobTitle = jobTitle;
//        }
//    }
//
//    static class StartResponseDto {
//        private String sessionId;
//        private String firstQuestion;
//
//        // 构造函数
//        public StartResponseDto(String sessionId, String firstQuestion) {
//            this.sessionId = sessionId;
//            this.firstQuestion = firstQuestion;
//        }
//
//        // Getters
//        public String getSessionId() {
//            return sessionId;
//        }
//        public String getFirstQuestion() {
//            return firstQuestion;
//        }
//        // Setters (可选，但对于某些序列化库或特定用法可能需要)
//        public void setSessionId(String sessionId) {
//            this.sessionId = sessionId;
//        }
//        public void setFirstQuestion(String firstQuestion) {
//            this.firstQuestion = firstQuestion;
//        }
//    }
//
//    static class ChatRequestDto {
//        private String sessionId;
//        private String userAnswer;
//
//        // Getters
//        public String getSessionId() { // 公开的getter
//            return sessionId;
//        }
//        public String getUserAnswer() { // 公开的getter
//            return userAnswer;
//        }
//        // Setters
//        public void setSessionId(String sessionId) { // 公开的setter
//            this.sessionId = sessionId;
//        }
//        public void setUserAnswer(String userAnswer) { // 公开的setter
//            this.userAnswer = userAnswer;
//        }
//    }
//
//    static class ChatResponseDto {
//        private String aiResponse;
//
//        // 构造函数
//        public ChatResponseDto(String aiResponse) {
//            this.aiResponse = aiResponse;
//        }
//        // Getter
//        public String getAiResponse() {
//            return aiResponse;
//        }
//        // Setter
//        public void setAiResponse(String aiResponse) {
//            this.aiResponse = aiResponse;
//        }
//    }
//
//    // --- Controller Endpoints ---
//    @PostMapping("/start")
//    public ResponseEntity<?> startInterview(@RequestBody StartRequestDto requestBody) {
//        Account currentUser = TokenUtils.getCurrentUser();
//        if (currentUser == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.error("401", "用户未登录，请先登录。"));
//        }
//
//        List<SparkApiService.Message> messagesForSparkApi = new ArrayList<>();
//        String systemPromptContent = String.format(
//                "你是一位专业的AI面试官。你接下来的任务是针对“%s”这个岗位进行提问。请确保你的问题专业、切题。",
//                requestBody.getJobTitle() // 调用 getJobTitle()
//        );
//        SparkApiService.Message systemMessageForSpark = SparkApiService.Message.builder().role("system").content(systemPromptContent).build();
//        SparkApiService.Message userTriggerMessage = SparkApiService.Message.builder().role("user").content("你好，请提出第一个面试问题。").build();
//
//        messagesForSparkApi.add(systemMessageForSpark);
//        messagesForSparkApi.add(userTriggerMessage);
//
//        try {
//            String firstAiQuestionContent = sparkApiService.getSparkResponse(currentUser.getId().toString(), messagesForSparkApi);
//            log.info("First question received from Spark API: [{}]", firstAiQuestionContent);
//
//            if (firstAiQuestionContent == null) {
//                log.error("CRITICAL: firstQuestion from Spark API is NULL!");
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(Result.error("500", "AI服务未能返回有效的初始问题。"));
//            }
//            SparkApiService.Message firstAiMessage = SparkApiService.Message.builder().role("assistant").content(firstAiQuestionContent).build();
//
//            String sessionId = aiInterviewDbService.createNewSessionAndSaveInitialMessages(
//                    currentUser.getId(),
//                    requestBody.getJobTitle(), // 调用 getJobTitle()
//                    systemMessageForSpark,
//                    firstAiMessage
//            );
//
//            log.info("AI Interview session started and persisted. SessionID: {}, UserID: {}, JobTitle: {}", sessionId, currentUser.getId(), requestBody.getJobTitle()); // 调用 getJobTitle()
//
//            // 返回简化的HashMap响应 (与之前保持一致，因为前端适应了这个)
//            Map<String, Object> simpleSuccessResponse = new HashMap<>();
//            simpleSuccessResponse.put("code", "200");
//            simpleSuccessResponse.put("msg", "面试成功开始");
//            Map<String, String> dataMap = new HashMap<>();
//            dataMap.put("sessionId", sessionId);
//            dataMap.put("firstQuestion", firstAiQuestionContent);
//            simpleSuccessResponse.put("data", dataMap);
//
//            log.info("Attempting to return simplified response for /start: {}", JSON.toJSONString(simpleSuccessResponse));
//            return ResponseEntity.ok(simpleSuccessResponse);
//
//        } catch (IOException e) {
//            log.error("IOException during AI interview start for UserID {}: {}", currentUser.getId(), e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Result.error("500", "AI服务通讯失败：" + e.getMessage()));
//        } catch (Exception e) {
//            log.error("Unexpected error starting AI interview for UserID {}: {}", currentUser.getId(), e.getMessage(), e);
//            log.error("Full stack trace for unexpected error in /start: ", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Result.error("500", "AI服务发生内部错误，请稍后再试。"));
//        }
//    }
//
//    @PostMapping("/chat")
//    public ResponseEntity<?> chat(@RequestBody ChatRequestDto requestBody) {
//        Account currentUser = TokenUtils.getCurrentUser();
//        if (currentUser == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Result.error("401", "用户未登录，请先登录。"));
//        }
//
//        String sessionId = requestBody.getSessionId(); // 调用 getSessionId()
//        log.info("[CHAT] Received request for session ID: {}", sessionId);
//        log.info("[CHAT] Current sessionHistories keys (from DB perspective, this log is less relevant now): keys will be fetched from DB");
//
//        List<SparkApiService.Message> history = aiInterviewDbService.loadMessageHistory(sessionId);
//
//        if (history.isEmpty()) {
//            log.warn("[CHAT] No history found for session ID: {}. UserID: {}", sessionId, currentUser.getId());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.error("400", "无效的面试会话或没有找到历史消息，请重新开始面试。"));
//        }
//
//        SparkApiService.Message userMessage = SparkApiService.Message.builder().role("user").content(requestBody.getUserAnswer()).build(); // 调用 getUserAnswer()
//        history.add(userMessage);
//
//        final int MAX_MESSAGES_TO_SPARK = 15;
//        List<SparkApiService.Message> messagesForSparkApi = history;
//        if (history.size() > MAX_MESSAGES_TO_SPARK) {
//            messagesForSparkApi = new ArrayList<>(history.subList(history.size() - MAX_MESSAGES_TO_SPARK, history.size()));
//            if (!messagesForSparkApi.get(0).getRole().equals("system") && history.get(0).getRole().equals("system")) {
//                messagesForSparkApi.add(0, history.get(0));
//            }
//            log.info("Chat history for Spark API (session {}) trimmed to {} messages.", sessionId, messagesForSparkApi.size());
//        }
//
//        try {
//            String aiResponseContent = sparkApiService.getSparkResponse(currentUser.getId().toString(), messagesForSparkApi);
//            SparkApiService.Message aiMessage = SparkApiService.Message.builder().role("assistant").content(aiResponseContent).build();
//
//            int currentMaxOrderInDb = history.size() - 2; // 用户的消息刚加入history，但还未入库
//
//            aiInterviewDbService.saveUserAndAiMessages(sessionId, userMessage, aiMessage, currentMaxOrderInDb);
//
//            log.debug("AI Interview chat processed. SessionID: {}, UserID: {}", sessionId, currentUser.getId());
//
//            Map<String, Object> simpleChatSuccessResponse = new HashMap<>();
//            simpleChatSuccessResponse.put("code", "200");
//            simpleChatSuccessResponse.put("msg", "聊天响应成功");
//            Map<String, String> chatDataMap = new HashMap<>();
//            chatDataMap.put("aiResponse", aiResponseContent);
//            simpleChatSuccessResponse.put("data", chatDataMap);
//
//            log.info("[CHAT] Attempting to return simplified chat response: {}", JSON.toJSONString(simpleChatSuccessResponse));
//            return ResponseEntity.ok(simpleChatSuccessResponse);
//
//        } catch (IOException e) {
//            log.error("IOException during AI interview chat for UserID {}: {}", currentUser.getId(), e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Result.error("500", "AI服务通讯失败：" + e.getMessage()));
//        } catch (Exception e) {
//            log.error("Unexpected error during AI interview chat for UserID {}: {}", currentUser.getId(), e.getMessage(), e);
//            log.error("Full stack trace for unexpected error in /chat: ", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Result.error("500", "AI服务发生内部错误，请稍后再试。"));
//        }
//    }
//}
