//package com.example.service;
//
//import com.example.entity.AiInterviewMessage;
//import com.example.entity.AiInterviewSession;
//import com.example.mapper.AiInterviewMessageMapper;
//import com.example.mapper.AiInterviewSessionMapper;
//import jakarta.annotation.Resource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class AiInterviewDbService {
//
//    private static final Logger log = LoggerFactory.getLogger(AiInterviewDbService.class);
//
//    @Resource
//    private AiInterviewSessionMapper sessionMapper;
//
//    @Resource
//    private AiInterviewMessageMapper messageMapper;
//
//    /**
//     * 创建一个新的AI面试会话，并保存初始消息。
//     *
//     * @param userId   用户ID
//     * @param jobTitle 面试岗位
//     * @param initialSystemMessage 系统初始提示
//     * @param firstAiQuestion AI的第一个问题
//     * @return 创建的会话ID (UUID)
//     */
//    @Transactional // 确保会话和初始消息的原子性保存
//    public String createNewSessionAndSaveInitialMessages(Integer userId, String jobTitle,
//                                                         SparkApiService.Message initialSystemMessage,
//                                                         SparkApiService.Message firstAiQuestion) {
//        String sessionId = UUID.randomUUID().toString();
//
//        AiInterviewSession session = new AiInterviewSession();
//        session.setId(sessionId);
//        session.setUserId(userId);
//        session.setJobTitle(jobTitle);
//        session.setStatus("STARTED"); // 初始状态
//        // createdAt 和 updatedAt 会由数据库自动填充或在mapper中用NOW()
//
//        sessionMapper.insert(session);
//        log.info("Created new AI interview session in DB: {}", sessionId);
//
//        // 保存系统消息
//        AiInterviewMessage systemDbMessage = new AiInterviewMessage();
//        systemDbMessage.setSessionId(sessionId);
//        systemDbMessage.setMessageOrder(0); // 第一条消息
//        systemDbMessage.setRole(initialSystemMessage.getRole());
//        systemDbMessage.setContent(initialSystemMessage.getContent());
//        messageMapper.insert(systemDbMessage);
//
//        // 保存AI的第一个问题
//        AiInterviewMessage aiDbMessage = new AiInterviewMessage();
//        aiDbMessage.setSessionId(sessionId);
//        aiDbMessage.setMessageOrder(1); // 第二条消息
//        aiDbMessage.setRole(firstAiQuestion.getRole());
//        aiDbMessage.setContent(firstAiQuestion.getContent());
//        messageMapper.insert(aiDbMessage);
//
//        log.info("Saved initial system and AI messages for session: {}", sessionId);
//        return sessionId;
//    }
//
//    /**
//     * 保存一条用户消息和一条AI回复消息到指定会话。
//     *
//     * @param sessionId    会话ID
//     * @param userMessage  用户的消息 (SparkApiService.Message 类型)
//     * @param aiMessage    AI的回复消息 (SparkApiService.Message 类型)
//     * @param currentMaxOrder 当前会话中已有的最大消息顺序号
//     */
//    @Transactional
//    public void saveUserAndAiMessages(String sessionId, SparkApiService.Message userMessage, SparkApiService.Message aiMessage, int currentMaxOrder) {
//        AiInterviewMessage userDbMessage = new AiInterviewMessage();
//        userDbMessage.setSessionId(sessionId);
//        userDbMessage.setMessageOrder(currentMaxOrder + 1);
//        userDbMessage.setRole(userMessage.getRole());
//        userDbMessage.setContent(userMessage.getContent());
//        messageMapper.insert(userDbMessage);
//
//        AiInterviewMessage aiDbMessage = new AiInterviewMessage();
//        aiDbMessage.setSessionId(sessionId);
//        aiDbMessage.setMessageOrder(currentMaxOrder + 2);
//        aiDbMessage.setRole(aiMessage.getRole());
//        aiDbMessage.setContent(aiMessage.getContent());
//        messageMapper.insert(aiDbMessage);
//
//        log.info("Saved user and AI messages for session: {}", sessionId);
//    }
//
//
//    /**
//     * 从数据库加载指定会话ID的完整消息历史。
//     *
//     * @param sessionId 会话ID
//     * @return List<SparkApiService.Message> 消息历史，按顺序排列
//     */
//    public List<SparkApiService.Message> loadMessageHistory(String sessionId) {
//        List<AiInterviewMessage> dbMessages = messageMapper.selectBySessionIdOrdered(sessionId);
//        List<SparkApiService.Message> history = new ArrayList<>();
//        if (dbMessages != null) {
//            for (AiInterviewMessage dbMsg : dbMessages) {
//                history.add(SparkApiService.Message.builder()
//                        .role(dbMsg.getRole())
//                        .content(dbMsg.getContent())
//                        .build());
//            }
//        }
//        log.info("Loaded {} messages for session: {}", history.size(), sessionId);
//        return history;
//    }
//}
