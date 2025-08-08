<template>
  <div class="ai-interview-agent">
    <!-- 背景装饰 -->
    <div class="interview-background">
      <div class="gradient-orb orb-1"></div>
      <div class="gradient-orb orb-2"></div>
      <div class="gradient-orb orb-3"></div>
    </div>

    <!-- 面试设置阶段 -->
    <InterviewSetup
        v-if="!interviewStarted"
        :is-starting="isInitializing"
        @start-interview="handleInterviewStart"
    />

    <!-- 主要面试界面 -->
    <div v-else class="interview-main">
      <!-- 面试头部信息 -->
      <div class="interview-header">
        <div class="interview-info">
          <h2 class="interview-title">{{ interviewConfig.typeInfo?.name }}</h2>
          <div class="interview-meta">
            <span class="meta-item">
              <svg viewBox="0 0 24 24" class="meta-icon">
                <path d="M12 2C6.5 2 2 6.5 2 12s4.5 10 10 10 10-4.5 10-10S17.5 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67V7z"/>
              </svg>
              {{ formatTime(elapsedTime) }}
            </span>
            <span class="meta-item">
              <svg viewBox="0 0 24 24" class="meta-icon">
                <path d="M9 11H7v6h2v-6zm4 0h-2v6h2v-6zm4 0h-2v6h2v-6zm2-7h-3V2h-2v2H8V2H6v2H3c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 16H3V9h14v11z"/>
              </svg>
              第{{ Math.floor(conversationHistory.length / 2) + 1 }}轮
            </span>
            <span class="meta-item difficulty-badge" :class="interviewConfig.difficulty">
              {{ interviewConfig.difficultyInfo?.name }}
            </span>
          </div>
        </div>
        <button class="minimize-btn" @click="showSettings = !showSettings">
          <svg viewBox="0 0 24 24" class="icon">
            <path d="M12 8c1.1 0 2-.9 2-2s-.9-2-2-2-2 .9-2 2 .9 2 2 2zm0 2c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2zm0 6c-1.1 0-2 .9-2 2s.9 2 2 2 2-.9 2-2-.9-2-2-2z"/>
          </svg>
        </button>
      </div>

      <!-- AI面试官头像区域 -->
      <div class="interviewer-avatar">
        <div class="avatar-container">
          <div
              ref="centerOrbRef"
              class="avatar-orb"
              :class="{
                speaking: isSpeaking,
                listening: isMicEnabled && !isProcessing,
                thinking: isProcessing
              }"
          >
            <div class="avatar-core">
              <div class="ai-brain">
                <svg viewBox="0 0 24 24" class="brain-icon">
                  <path d="M12 2C13.1 2 14 2.9 14 4C14 5.1 13.1 6 12 6C10.9 6 10 5.1 10 4C10 2.9 10.9 2 12 2ZM21 9V7L15 1L13.5 2.5L16.17 5.17L10.5 10.5C10.1 10.9 9.6 11.1 9 11.1S7.9 10.9 7.5 10.5L1.83 4.83L0.41 6.24L6.41 12.24C6.2 12.64 6 13.1 6 13.6C6 14.6 6.4 15.5 7 16.2V19C7 20.1 7.9 21 9 21H15C16.1 21 17 20.1 17 19V16.2C17.6 15.5 18 14.6 18 13.6C18 13.1 17.8 12.64 17.59 12.24L21 9Z"/>
                </svg>
              </div>
              <div class="avatar-glow"></div>
            </div>
            <div class="pulse-ring"></div>
            <div class="status-indicator" :class="getStatusClass()"></div>
          </div>
        </div>

        <!-- AI面试官信息 -->
        <div class="interviewer-info">
          <h2 class="interviewer-name">AI面试官</h2>
          <p class="interviewer-title">{{ interviewConfig.typeInfo?.description }}</p>
          <div class="interview-status">
            <div class="status-dot" :class="getStatusClass()"></div>
            <span class="status-text">{{ getStatusText() }}</span>
          </div>
        </div>
      </div>

      <!-- 状态消息区域 -->
      <div v-if="processingMessage" class="status-message-card">
        <div class="message-content">
          <div class="loading-dots" v-if="isProcessing">
            <span></span><span></span><span></span>
          </div>
          <span class="message-text">{{ processingMessage }}</span>
        </div>
      </div>

      <!-- 面试进度指示 -->
      <div class="interview-progress">
        <div class="progress-item" :class="{ active: conversationHistory.length >= 0 }">
          <div class="progress-dot"></div>
          <span>开场</span>
        </div>
        <div class="progress-line" :class="{ active: conversationHistory.length >= 2 }"></div>
        <div class="progress-item" :class="{ active: conversationHistory.length >= 2 }">
          <div class="progress-dot"></div>
          <span>展开</span>
        </div>
        <div class="progress-line" :class="{ active: conversationHistory.length >= 6 }"></div>
        <div class="progress-item" :class="{ active: conversationHistory.length >= 6 }">
          <div class="progress-dot"></div>
          <span>深入</span>
        </div>
        <div class="progress-line" :class="{ active: conversationHistory.length >= 10 }"></div>
        <div class="progress-item" :class="{ active: conversationHistory.length >= 10 }">
          <div class="progress-dot"></div>
          <span>总结</span>
        </div>
      </div>
    </div>

    <!-- 隐藏的音频播放器 -->
    <audio
        ref="audioPlayerRef"
        :src="audioUrl"
        @ended="handlePlaybackEnd"
        style="display: none;"
    ></audio>

    <!-- 控制面板 -->
    <div v-if="interviewStarted" class="control-panel">
      <div class="control-section">
        <!-- 麦克风控制 -->
        <div class="mic-control">
          <input
              type="checkbox"
              id="micToggle"
              :checked="!isMicEnabled"
              @change="toggleMicrophone"
              style="display: none;"
          >
          <label
              class="mic-button"
              :class="{
                'active': isMicEnabled,
                'disabled': isProcessing,
                'speaking': isSpeaking
              }"
              for="micToggle"
          >
            <div class="mic-icon">
              <svg v-if="isMicEnabled" viewBox="0 0 24 24" class="icon">
                <path d="M12 14c1.66 0 3-1.34 3-3V5c0-1.66-1.34-3-3-3S9 3.34 9 5v6c0 1.66 1.34 3 3 3z"/>
                <path d="M17 11c0 2.76-2.24 5-5 5s-5-2.24-5-5H5c0 3.53 2.61 6.43 6 6.92V21h2v-3.08c3.39-.49 6-3.39 6-6.92h-2z"/>
              </svg>
              <svg v-else viewBox="0 0 24 24" class="icon">
                <path d="M19 11h-1.7c0 .74-.16 1.43-.43 2.05l1.23 1.23c.56-.98.9-2.09.9-3.28zm-4.02.17c0-.06.02-.11.02-.17V5c0-1.66-1.34-3-3-3S9 3.34 9 5v.18l5.98 5.99zM4.27 3L3 4.27l6.01 6.01V11c0 1.66 1.33 3 2.99 3 .22 0 .44-.03.65-.08l1.66 1.66c-.71.33-1.5.52-2.31.52-2.76 0-5-2.24-5-5H5c0 3.53 2.61 6.43 6 6.92V21h2v-3.08c.57-.08 1.12-.24 1.64-.46l2.36 2.36L21 15.73 4.27 3z"/>
              </svg>
            </div>
            <span class="mic-label">{{ isMicEnabled ? '点击静音' : '点击开始' }}</span>
            <div class="mic-pulse" v-if="isMicEnabled && isSpeaking"></div>
          </label>
        </div>

        <!-- 结束面试按钮 -->
        <button
            class="end-interview-btn"
            @click="endInterview"
            :disabled="isProcessing"
        >
          <svg viewBox="0 0 24 24" class="icon">
            <path d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/>
          </svg>
          <span>结束面试</span>
        </button>
      </div>

      <!-- 面试助手提示 -->
      <div class="interview-assistant">
        <div class="assistant-tips">
          <div class="tip-category">
            <h4 class="tip-title">面试技巧</h4>
            <div class="tips-grid">
              <div class="tip-item">
                <svg viewBox="0 0 24 24" class="tip-icon">
                  <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                </svg>
                <span>使用STAR法则回答</span>
              </div>
              <div class="tip-item">
                <svg viewBox="0 0 24 24" class="tip-icon">
                  <path d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"/>
                </svg>
                <span>提供具体的数据和例子</span>
              </div>
              <div class="tip-item">
                <svg viewBox="0 0 24 24" class="tip-icon">
                  <path d="M13 10V3L4 14h7v7l9-11h-7z"/>
                </svg>
                <span>主动提问显示兴趣</span>
              </div>
            </div>
          </div>

          <!-- 当前阶段建议 -->
          <div class="current-stage-tip">
            <h4 class="tip-title">当前阶段建议</h4>
            <p class="stage-advice">{{ getCurrentStageAdvice() }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 面试总结模态窗口 -->
    <div v-if="showSummary" class="interview-summary-modal">
      <div class="summary-container">
        <div class="summary-header">
          <h2 class="summary-title">面试总结</h2>
          <p class="summary-subtitle">本次模拟面试已完成</p>
        </div>

        <div class="summary-content">
          <div class="summary-stats">
            <div class="stat-item">
              <div class="stat-value">{{ formatTime(elapsedTime) }}</div>
              <div class="stat-label">面试时长</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ Math.floor(conversationHistory.length / 2) }}</div>
              <div class="stat-label">问答轮次</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ interviewConfig.typeInfo?.name }}</div>
              <div class="stat-label">面试类型</div>
            </div>
          </div>

          <div class="summary-feedback">
            <h3 class="feedback-title">AI面试官总结</h3>
            <div class="feedback-content">
              <p>{{ interviewFeedback }}</p>
            </div>
          </div>
        </div>

        <div class="summary-actions">
          <button class="action-btn secondary" @click="restartInterview">
            <svg viewBox="0 0 24 24" class="icon">
              <path d="M17.65 6.35C16.2 4.9 14.21 4 12 4c-4.42 0-7.99 3.58-7.99 8s3.57 8 7.99 8c3.73 0 6.84-2.55 7.73-6h-2.08c-.82 2.33-3.04 4-5.65 4-3.31 0-6-2.69-6-6s2.69-6 6-6c1.66 0 3.14.69 4.22 1.78L13 11h7V4l-2.35 2.35z"/>
            </svg>
            <span>重新面试</span>
          </button>
          <button class="action-btn primary" @click="closeVoiceChat">
            <svg viewBox="0 0 24 24" class="icon">
              <path d="M19 7v4H5.83l3.58-3.59L8 6l-6 6 6 6 1.41-1.41L5.83 13H21V7z"/>
            </svg>
            <span>返回主页</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick, computed } from 'vue'
import { onBeforeRouteLeave } from 'vue-router'
import InterviewSetup from '@/components/InterviewSetup.vue'

// Props and Emits
const emit = defineEmits(['close', 'conversation-update'])

// 讯飞配置
const XFYUN_CONFIG = {
  appId: '', // 你的App ID
  apiKey: '', // 你的API Key
  apiSecret: '', // 你的API Secret
  ttsWsUrl: 'wss://cbm01.cn-huabei-1.xf-yun.com/v1/private/mcd9m97e6', // TTS WebSocket URL
  sparkWsUrl: 'wss://spark-api.xf-yun.com/v3.5/chat', // Spark Max WebSocket URL
  sparkDomain: 'generalv3.5' // Spark Max 使用的domain参数
};

// Template refs
const centerOrbRef = ref(null)
const audioPlayerRef = ref(null)

// Reactive state
const isMicEnabled = ref(false)
const isProcessing = ref(false)
const audioUrl = ref(null)
const processingMessage = ref('')
const isPlaying = ref(false)
const isSpeaking = ref(false)
const currentText = ref('')
const isInitialized = ref(false)
const cryptoJSLoaded = ref(false)

// 面试状态
const interviewStarted = ref(false)
const isInitializing = ref(false)
const interviewConfig = ref({})
const showSettings = ref(false)
const showSummary = ref(false)
const interviewStartTime = ref(null)
const elapsedTime = ref(0)
const interviewFeedback = ref('')

// 定时器
let interviewTimer = null

// Non-reactive objects
let recognition = null
let silenceTimer = null
let audioContext = null
let analyser = null
let volumeData = null
let animationFrame = null
let mediaStream = null
let ttsWebSocket = null

// Conversation state
const conversationHistory = ref([])
const currentContext = ref('')
const maxConversationTurns = 10

// 工具函数
const formatTime = (seconds) => {
  const mins = Math.floor(seconds / 60)
  const secs = seconds % 60
  return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
}

const getCurrentStageAdvice = () => {
  const turns = conversationHistory.value.length
  if (turns === 0) return '保持自信，清晰地介绍自己的背景和经验'
  if (turns <= 4) return '展示您的核心技能，用具体例子支撑您的观点'
  if (turns <= 8) return '深入讨论技术细节，展现您的思考深度'
  if (turns <= 12) return '主动提问，展示您对职位和公司的兴趣'
  return '总结您的优势，表达对这个机会的热情'
}

// 计算属性
const getStatusClass = () => {
  if (isProcessing.value) return 'processing'
  if (isPlaying.value) return 'speaking'
  if (isMicEnabled.value && isSpeaking.value) return 'listening'
  if (isMicEnabled.value) return 'ready'
  return 'idle'
}

const getStatusText = () => {
  if (isProcessing.value) return '正在思考中...'
  if (isPlaying.value) return '面试官回答中'
  if (isMicEnabled.value && isSpeaking.value) return '正在聆听您的回答'
  if (isMicEnabled.value) return '请开始回答问题'
  return '面试已暂停'
}

// 面试控制函数
const handleInterviewStart = async (config) => {
  try {
    isInitializing.value = true
    interviewConfig.value = config

    // 等待 CryptoJS 加载完成
    if (!cryptoJSLoaded.value) {
      processingMessage.value = '正在加载必要组件...'
      // 等待 CryptoJS 加载
      await new Promise((resolve) => {
        const checkLoaded = () => {
          if (cryptoJSLoaded.value) {
            resolve()
          } else {
            setTimeout(checkLoaded, 100)
          }
        }
        checkLoaded()
      })
    }

    interviewStarted.value = true
    interviewStartTime.value = Date.now()
    elapsedTime.value = 0

    // 开始计时
    interviewTimer = setInterval(() => {
      elapsedTime.value = Math.floor((Date.now() - interviewStartTime.value) / 1000)
    }, 1000)

    // 初始化语音功能
    await initializeVoiceChat()

    // 发送开场白
    await sendWelcomeMessage()

  } catch (error) {
    console.error('面试初始化失败:', error)
    alert('面试初始化失败，请重试')
    // 重置状态
    interviewStarted.value = false
    isInitializing.value = false
  } finally {
    isInitializing.value = false
  }
}

const endInterview = async () => {
  if (interviewTimer) {
    clearInterval(interviewTimer)
    interviewTimer = null
  }

  // 生成面试反馈
  await generateInterviewFeedback()

  // 显示总结
  showSummary.value = true

  // 清理语音资源
  await cleanupResources()
}

const restartInterview = () => {
  showSummary.value = false
  interviewStarted.value = false
  conversationHistory.value = []
  currentContext.value = ''
  elapsedTime.value = 0
  interviewFeedback.value = ''
  processingMessage.value = ''
  interviewConfig.value = {}
}

const sendWelcomeMessage = async () => {
  const welcomeText = `您好！欢迎参加${interviewConfig.value.typeInfo.name}。我是您的AI面试官，今天我们将进行约${interviewConfig.value.duration}分钟的面试。首先，请您简单介绍一下自己。`

  try {
    processingMessage.value = '面试官正在准备开场...'
    isProcessing.value = true

    // 使用TTS播放欢迎词
    const audioBlob = await synthesizeWithXFYun(welcomeText)

    if (audioUrl.value) {
      URL.revokeObjectURL(audioUrl.value)
    }
    audioUrl.value = URL.createObjectURL(audioBlob)
    await nextTick()

    if (audioPlayerRef.value) {
      audioPlayerRef.value.load()
      await audioPlayerRef.value.play()
      isPlaying.value = true
    }

    // 更新对话历史
    conversationHistory.value.push({ role: 'assistant', content: welcomeText })

  } catch (error) {
    console.error('发送欢迎消息失败:', error)
    alert('初始化面试失败，请重试')
  } finally {
    isProcessing.value = false
    processingMessage.value = isMicEnabled.value ? '面试官正在聆听您的回答...' : '请开启麦克风开始面试'
  }
}

const generateInterviewFeedback = async () => {
  if (conversationHistory.value.length === 0) {
    interviewFeedback.value = '面试未开始，无法生成反馈。'
    return
  }

  try {
    isProcessing.value = true
    processingMessage.value = '正在生成面试反馈...'

    const messages = [
      {
        role: 'system',
        content: `你是一位专业的面试官，需要对刚刚完成的面试进行总结和反馈。请基于以下对话历史，提供建设性的反馈，包括：
1. 候选人的优势表现
2. 需要改进的地方
3. 具体的提升建议
请保持专业、友善的语气，字数控制在200字左右。`
      },
      ...conversationHistory.value,
      {
        role: 'user',
        content: '请对这次面试进行总结和反馈。'
      }
    ]

    const feedback = await getSparkMaxReply(messages)
    interviewFeedback.value = feedback

  } catch (error) {
    console.error('生成面试反馈失败:', error)
    interviewFeedback.value = '感谢您参加本次模拟面试。由于系统原因，无法生成详细反馈。建议您回顾面试过程，继续提升相关技能。'
  } finally {
    isProcessing.value = false
    processingMessage.value = ''
  }
}

// Authentication URL Generation
const createTTSAuthUrl = () => {
  if (typeof CryptoJS === 'undefined') {
    console.error("CryptoJS not loaded for TTS Auth!");
    throw new Error("CryptoJS not loaded");
  }
  const { apiKey, apiSecret } = XFYUN_CONFIG;
  const url = new URL(XFYUN_CONFIG.ttsWsUrl);
  const host = url.host;
  const path = url.pathname;
  const date = new Date().toUTCString();
  const signatureOrigin = `host: ${host}\ndate: ${date}\nGET ${path} HTTP/1.1`;
  const signatureSha = CryptoJS.HmacSHA256(signatureOrigin, apiSecret);
  const signature = CryptoJS.enc.Base64.stringify(signatureSha);
  const authorizationOrigin = `api_key="${apiKey}", algorithm="hmac-sha256", headers="host date request-line", signature="${signature}"`;
  const authorization = btoa(authorizationOrigin);
  return `${XFYUN_CONFIG.ttsWsUrl}?authorization=${authorization}&date=${encodeURIComponent(date)}&host=${host}`;
};

const createSparkAuthUrl = () => {
  if (typeof CryptoJS === 'undefined') {
    console.error("CryptoJS not loaded for Spark Auth!");
    throw new Error("CryptoJS not loaded");
  }
  const { apiKey, apiSecret } = XFYUN_CONFIG;
  const url = new URL(XFYUN_CONFIG.sparkWsUrl);
  const host = url.host;
  const path = url.pathname;
  const date = new Date().toUTCString();
  const signatureOrigin = `host: ${host}\ndate: ${date}\nGET ${path} HTTP/1.1`;
  const signatureSha = CryptoJS.HmacSHA256(signatureOrigin, apiSecret);
  const signature = CryptoJS.enc.Base64.stringify(signatureSha);
  const authorizationOrigin = `api_key="${apiKey}", algorithm="hmac-sha256", headers="host date request-line", signature="${signature}"`;
  const authorization = btoa(authorizationOrigin);
  return `${XFYUN_CONFIG.sparkWsUrl}?authorization=${authorization}&date=${encodeURIComponent(date)}&host=${host}`;
};

// Spark Max API Call
const getSparkMaxReply = (chatMessages) => {
  return new Promise((resolve, reject) => {
    const authUrl = createSparkAuthUrl();
    const sparkSocket = new WebSocket(authUrl);
    let fullResponseContent = "";

    sparkSocket.onopen = () => {
      console.log("讯飞Spark Max连接已建立");
      const requestData = {
        header: {
          app_id: XFYUN_CONFIG.appId,
        },
        parameter: {
          chat: {
            domain: XFYUN_CONFIG.sparkDomain,
            temperature: 0.7,
            max_tokens: 2048,
          }
        },
        payload: {
          message: {
            text: chatMessages
          }
        }
      };
      console.log("发送给Spark Max的请求数据:", JSON.stringify(requestData, null, 2));
      sparkSocket.send(JSON.stringify(requestData));
    };

    sparkSocket.onmessage = (event) => {
      const response = JSON.parse(event.data);

      if (response.header.code !== 0) {
        console.error(`Spark Max API错误 (${response.header.code}): ${response.header.message}`, response);
        const sid = response.header.sid || "N/A";
        reject(new Error(`Spark Max API错误: ${response.header.message} (SID: ${sid})`));
        if (sparkSocket.readyState === WebSocket.OPEN) sparkSocket.close();
        return;
      }

      if (response.payload && response.payload.choices && response.payload.choices.text) {
        response.payload.choices.text.forEach(choice => {
          fullResponseContent += choice.content;
        });
      }

      if (response.header.status === 2) {
        console.log("Spark Max Tokens usage:", response.payload.usage?.text);
        resolve(fullResponseContent);
        if (sparkSocket.readyState === WebSocket.OPEN) sparkSocket.close();
      }
    };

    sparkSocket.onerror = (error) => {
      console.error("讯飞Spark Max连接错误:", error);
      reject(new Error("讯飞Spark Max连接错误"));
    };

    sparkSocket.onclose = (event) => {
      console.log("讯飞Spark Max连接关闭:", event.code, event.reason);
      const wasHandled = fullResponseContent !== "" || (event.reason && event.reason.includes("Spark Max API错误"));
      if (!wasHandled && event.code !== 1000 ) {
        reject(new Error(`讯飞Spark Max连接意外关闭: ${event.code} ${event.reason}`));
      }
    };
  });
};

// TTS Synthesis
const synthesizeWithXFYun = (text) => {
  return new Promise((resolve, reject) => {
    try {
      const authUrl = createTTSAuthUrl();
      ttsWebSocket = new WebSocket(authUrl);
      let decodedAudioBinaryChunks = [];
      let isCompleted = false;

      ttsWebSocket.onopen = () => {
        console.log('讯飞TTS连接已建立');
        const requestData = {
          header: {
            app_id: XFYUN_CONFIG.appId,
            status: 2
          },
          parameter: {
            tts: {
              vcn: "x5_lingfeiyi_flow",
              speed: 50,
              volume: 50,
              pitch: 50,
              bgs: 0,
              reg: 0,
              rdn: 0,
              rhy: 0,
              audio: {
                encoding: "lame",
                sample_rate: 24000,
                channels: 1,
                bit_depth: 16,
                frame_size: 0
              }
            }
          },
          payload: {
            text: {
              encoding: "utf8",
              compress: "raw",
              format: "plain",
              status: 2,
              seq: 0,
              text: btoa(unescape(encodeURIComponent(text)))
            }
          }
        };
        console.log("发送给讯飞TTS的请求数据:", JSON.stringify(requestData, null, 2));
        ttsWebSocket.send(JSON.stringify(requestData));
      };

      ttsWebSocket.onmessage = (event) => {
        try {
          const response = JSON.parse(event.data);

          if (response.header.code !== 0) {
            console.error(`讯飞TTS错误(${response.header.code}): ${response.header.message}`, response);
            reject(new Error(`讯飞TTS错误(${response.header.code}): ${response.header.message}`));
            if (ttsWebSocket && ttsWebSocket.readyState === WebSocket.OPEN) ttsWebSocket.close();
            return;
          }

          if (response.payload && response.payload.audio && response.payload.audio.audio) {
            const audioBase64Chunk = response.payload.audio.audio;
            const audioStatus = response.payload.audio.status;

            if (audioBase64Chunk && audioBase64Chunk.length > 0) {
              try {
                const binaryChunk = atob(audioBase64Chunk);
                decodedAudioBinaryChunks.push(binaryChunk);
              } catch (e) {
                console.error("Base64解码单个音频块失败:", e);
              }
            }

            if (audioStatus === 2) {
              isCompleted = true;
              console.log('TTS合成完成，总解码后音频块数:', decodedAudioBinaryChunks.length);
              if (decodedAudioBinaryChunks.length === 0) {
                reject(new Error('未收到有效的音频数据'));
                if (ttsWebSocket && ttsWebSocket.readyState === WebSocket.OPEN) ttsWebSocket.close();
                return;
              }
              const combinedBinaryAudioData = decodedAudioBinaryChunks.join('');
              const audioArray = new Uint8Array(combinedBinaryAudioData.length);
              for (let i = 0; i < combinedBinaryAudioData.length; i++) {
                audioArray[i] = combinedBinaryAudioData.charCodeAt(i);
              }
              const audioBlob = new Blob([audioArray.buffer], { type: 'audio/mpeg' });
              console.log('TTS音频Blob大小:', audioBlob.size);
              resolve(audioBlob);
              if (ttsWebSocket && ttsWebSocket.readyState === WebSocket.OPEN) ttsWebSocket.close();
            }
          }
        } catch (error) {
          console.error('解析讯飞TTS响应失败:', error, '原始数据:', event.data);
          reject(new Error(`解析讯飞TTS响应失败: ${error.message}`));
          if (ttsWebSocket && ttsWebSocket.readyState === WebSocket.OPEN && !isCompleted) ttsWebSocket.close();
        }
      };

      ttsWebSocket.onerror = (error) => {
        console.error('讯飞TTS连接错误:', error);
        reject(new Error(`讯飞TTS连接错误`));
      };

      ttsWebSocket.onclose = (event) => {
        console.log('讯飞TTS连接关闭:', event.code, event.reason, '是否已完成:', isCompleted);
        if (!isCompleted && event.code !== 1000) {
          if (decodedAudioBinaryChunks.length > 0) {
            console.warn("TTS连接在合成完成前关闭，但已收到部分数据，尝试使用。");
            reject(new Error(`讯飞TTS连接意外关闭(有部分数据): ${event.code} ${event.reason}`));
          } else {
            reject(new Error(`讯飞TTS连接意外关闭: ${event.code} ${event.reason}`));
          }
        } else if (!isCompleted && decodedAudioBinaryChunks.length === 0 && event.code === 1000) {
          reject(new Error('讯飞TTS连接正常关闭但未收到音频数据'));
        }
        ttsWebSocket = null;
      };
    } catch (error) {
      console.error('讯飞TTS初始化失败:', error);
      reject(new Error(`讯飞TTS初始化失败: ${error.message}`));
    }
  });
};

// --- Lifecycle Hooks & Core Logic ---
onMounted(async () => {
  if (typeof CryptoJS === 'undefined') {
    console.log('CryptoJS not found, attempting to load...');
    const script = document.createElement('script');
    script.src = 'https://cdn.jsdelivr.net/npm/crypto-js@4.1.1/crypto-js.js';
    script.onload = async () => {
      console.log('CryptoJS loaded successfully.');
      cryptoJSLoaded.value = true
    };
    script.onerror = async () => {
      console.error('Failed to load CryptoJS from primary CDN, trying fallback...');
      const fallbackScript = document.createElement('script');
      fallbackScript.src = 'https://unpkg.com/crypto-js@4.1.1/crypto-js.js';
      fallbackScript.onload = async () => {
        console.log('CryptoJS loaded successfully from fallback CDN.');
        cryptoJSLoaded.value = true
      };
      fallbackScript.onerror = () => {
        console.error('Failed to load CryptoJS from all CDNs. Voice chat auth will fail.');
        alert('AI面试官组件初始化失败，请检查网络连接或刷新页面重试。');
      };
      document.head.appendChild(fallbackScript);
    };
    document.head.appendChild(script);
  } else {
    cryptoJSLoaded.value = true
  }

  const savedHistory = localStorage.getItem('ai_interview_history');
  if (savedHistory) {
    try {
      conversationHistory.value = JSON.parse(savedHistory);
      updateCurrentContext();
    } catch (error) {
      console.error('Failed to parse saved history:', error)
      localStorage.removeItem('ai_interview_history')
    }
  }
});

onUnmounted(() => {
  cleanupResources();
});

onBeforeRouteLeave((to, from, next) => {
  cleanupResources();
  next();
});

const initializeVoiceChat = async () => {
  if (isInitialized.value) return;
  if (!cryptoJSLoaded.value) {
    console.warn("initializeVoiceChat called before CryptoJS loaded. Aborting.");
    return;
  }

  try {
    await initSpeechRecognition();
    await setupAudioAnalysis();

    if (recognition) {
      recognition.start();
      isMicEnabled.value = true;
      isInitialized.value = true;
      startVolumeDetection();
      console.log('AI面试智能体已初始化并开始录音');
      processingMessage.value = '面试官正在聆听您的回答...'
    }
  } catch (error) {
    console.error('初始化语音聊天失败:', error);
    alert('启动录音失败，请检查麦克风权限');
    isMicEnabled.value = false;
  }
};

const initSpeechRecognition = async () => {
  if ('webkitSpeechRecognition' in window || 'SpeechRecognition' in window) {
    const SpeechRecognition = window.webkitSpeechRecognition || window.SpeechRecognition
    recognition = new SpeechRecognition()
    recognition.continuous = true
    recognition.interimResults = true
    recognition.lang = 'zh-CN'

    recognition.onstart = () => {
      console.log('语音识别已启动')
      processingMessage.value = '面试官正在聆听您的回答...'
    }

    recognition.onend = handleSpeechEnd
    recognition.onerror = handleSpeechError
    recognition.onresult = handleSpeechResult
  } else {
    throw new Error('您的浏览器不支持语音识别功能')
  }
};

const toggleMicrophone = async () => {
  if (isMicEnabled.value) {
    await disableMicrophone()
  } else {
    await enableMicrophone()
  }
};

const enableMicrophone = async () => {
  try {
    await setupAudioAnalysis()
    if (!recognition) await initSpeechRecognition();
    if (recognition) {
      recognition.start()
      isMicEnabled.value = true
      processingMessage.value = '面试官正在聆听您的回答...'
      startVolumeDetection()
    } else {
      alert('语音识别未能初始化。');
    }
  } catch (error) {
    console.error('启用麦克风失败:', error)
    alert('启用麦克风失败: ' + error.message)
  }
};

const disableMicrophone = async () => {
  try {
    if (recognition) recognition.stop()
    await cleanupAudioResources()
    isMicEnabled.value = false
    processingMessage.value = '面试已暂停，点击开始继续'
    stopVolumeDetection()
    isSpeaking.value = false
  } catch (error) {
    console.error('关闭麦克风失败:', error)
    alert('关闭麦克风失败: ' + error.message)
  }
};

const setupAudioAnalysis = async () => {
  if (mediaStream) {
    mediaStream.getTracks().forEach(track => track.stop());
    mediaStream = null;
  }
  if (audioContext && audioContext.state !== 'closed') {
    await audioContext.close();
    audioContext = null;
  }
  try {
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
    mediaStream = stream;
    audioContext = new (window.AudioContext || window.webkitAudioContext)();
    const source = audioContext.createMediaStreamSource(stream);
    analyser = audioContext.createAnalyser();
    analyser.fftSize = 256;
    volumeData = new Uint8Array(analyser.frequencyBinCount);
    source.connect(analyser);
  } catch (error) {
    console.error('音频分析器设置失败:', error);
    isMicEnabled.value = false;
    throw error;
  }
};

const startVolumeDetection = () => {
  if (!animationFrame) {
    updateVolume()
  }
};

const stopVolumeDetection = () => {
  if (animationFrame) {
    cancelAnimationFrame(animationFrame)
    animationFrame = null
  }
};

const updateVolume = () => {
  if (!analyser || !isMicEnabled.value || !audioContext || audioContext.state === 'closed') {
    isSpeaking.value = false;
    return;
  }

  analyser.getByteFrequencyData(volumeData);
  const average = volumeData.reduce((a, b) => a + b, 0) / volumeData.length;

  updateVisualFeedback(average);

  if (average > 20) {
    isSpeaking.value = true;
    if (silenceTimer) {
      clearTimeout(silenceTimer);
      silenceTimer = null;
    }
  } else if (isSpeaking.value) {
    if (!silenceTimer) {
      silenceTimer = setTimeout(() => {
        handleSilence();
      }, 1500);
    }
  }
  if (isMicEnabled.value) {
    animationFrame = requestAnimationFrame(updateVolume);
  }
};

const updateVisualFeedback = (volume) => {
  if (centerOrbRef.value) {
    const intensity = Math.min(volume / 128, 1);
    centerOrbRef.value.style.transform = `scale(${1 + intensity * 0.1})`
    centerOrbRef.value.style.filter = `brightness(${1 + intensity * 0.3})`
  }
};

const handleSilence = () => {
  isSpeaking.value = false;
  if (currentText.value && !isProcessing.value) {
    console.log("Silence detected, processing existing text:", currentText.value);
    processSpeech();
  } else {
    console.log("Silence detected, but no final text or already processing.");
  }
};

const handleSpeechResult = (event) => {
  const results = Array.from(event.results)
  let finalTranscript = ''
  let interimTranscript = ''

  results.forEach(result => {
    if (result.isFinal) {
      finalTranscript += result[0].transcript
    } else {
      interimTranscript += result[0].transcript
    }
  })

  if (interimTranscript) {
    processingMessage.value = `正在识别: ${interimTranscript}`
  }

  if (finalTranscript) {
    currentText.value = (currentText.value + " " + finalTranscript).trim();
    console.log("Final transcript received:", finalTranscript);
    if (silenceTimer) {
      clearTimeout(silenceTimer);
      silenceTimer = null;
    }
    if (isSpeaking.value) {
      silenceTimer = setTimeout(() => {
        handleSilence();
      }, 1500);
    }
  }
};

// Process speech logic (integrates Spark Max)
const processSpeech = async () => {
  if (!currentText.value.trim() || isProcessing.value) {
    if (!currentText.value.trim()) console.log("processSpeech called with empty text.");
    return;
  }

  try {
    isProcessing.value = true;
    processingMessage.value = '面试官正在分析您的回答...';

    const getInterviewStage = () => {
      const turns = conversationHistory.value.length;
      if (turns === 0) return '开场阶段 - 请进行简单的自我介绍和破冰交流';
      if (turns <= 4) return '基础了解阶段 - 了解候选人背景和基本能力';
      if (turns <= 8) return '深入交流阶段 - 详细探讨专业技能和项目经验';
      if (turns <= 12) return '能力评估阶段 - 考察解决问题的能力和思维方式';
      return '总结阶段 - 进行面试总结并给出建设性反馈';
    };

    const messages = [
      {
        role: 'system',
        content: `你是一位经验丰富的专业面试官，正在进行一场${interviewConfig.value.typeInfo.name}。请保持以下特点：

**面试背景：**
- 面试类型：${interviewConfig.value.typeInfo.name} - ${interviewConfig.value.typeInfo.description}
- 面试难度：${interviewConfig.value.difficultyInfo.name}（${interviewConfig.value.difficultyInfo.description}）
- 目标时长：${interviewConfig.value.duration}分钟
- 当前进度：已进行${Math.floor(conversationHistory.value.length / 2)}轮对话

**角色定位：**
- 专业、友善但不失严谨的面试官
- 具有丰富的行业经验和人才评估能力
- 能够深入挖掘候选人的潜力和能力

**面试风格：**
- 根据面试类型调整问题重点：${interviewConfig.value.focus}
- 提问专业且有针对性，结合候选人的回答进行深入追问
- 保持耐心和鼓励，营造轻松但专业的面试氛围
- 每次回应控制在80-120字，语言简洁明了
- 适时给予积极反馈，帮助候选人展现最佳状态

**当前面试阶段：** ${getInterviewStage()}
${currentContext.value ? `**讨论主题：** ${currentContext.value}` : ''}

请基于候选人的回答，给出专业的回应并提出下一个有价值的问题。记住保持面试官的专业形象。`
      },
      ...conversationHistory.value.slice(-(maxConversationTurns * 2 -1)).map(msg => ({
        role: msg.role,
        content: msg.content
      })),
      {
        role: 'user',
        content: currentText.value
      }
    ];

    const replyText = await getSparkMaxReply(messages);
    console.log('AI面试官回复 (Spark Max):', replyText);

    updateConversationHistory(currentText.value, replyText);
    processingMessage.value = '面试官正在组织语言...';

    const audioBlob = await synthesizeWithXFYun(replyText);

    if (audioUrl.value) {
      URL.revokeObjectURL(audioUrl.value);
    }
    audioUrl.value = URL.createObjectURL(audioBlob);
    await nextTick();
    if (audioPlayerRef.value) {
      audioPlayerRef.value.load();
      await audioPlayerRef.value.play();
      isPlaying.value = true;
    } else {
      throw new Error("Audio player ref not available");
    }

    emit('conversation-update', {
      userText: currentText.value,
      aiText: replyText,
      timestamp: new Date().toISOString()
    });

    currentText.value = '';

  } catch (error) {
    console.error('处理错误:', error);
    const errorMessage = error.message || '未知错误';
    const sidMatch = errorMessage.match(/SID: (\S+)/);
    const alertMessage = sidMatch ? `面试官系统异常: ${errorMessage}` : `面试官系统异常: ${errorMessage}. 请查看控制台。`;
    alert(alertMessage);
    currentText.value = '';
  } finally {
    isProcessing.value = false;
    processingMessage.value = isMicEnabled.value ? '面试官正在聆听您的回答...' : '面试已暂停，点击开始继续';
    if (isMicEnabled.value && !isPlaying.value && recognition) {
      console.log("ProcessSpeech finally: Attempting to restart recording");
      await restartRecording();
    }
  }
};

const updateConversationHistory = (userText, aiText) => {
  conversationHistory.value.push({ role: 'user', content: userText });
  conversationHistory.value.push({ role: 'assistant', content: aiText });

  if (conversationHistory.value.length > maxConversationTurns * 2) {
    conversationHistory.value = conversationHistory.value.slice(-maxConversationTurns * 2);
  }
  localStorage.setItem('ai_interview_history', JSON.stringify(conversationHistory.value));
  updateCurrentContext();
};

const updateCurrentContext = () => {
  if (conversationHistory.value.length >= 2) {
    const latestMessages = conversationHistory.value.slice(-2);
    const latestConversation = latestMessages.map(msg => msg.content).join(' ');
    const keywords = latestConversation.match(/[a-zA-Z\u4e00-\u9fa5]+/g) || [];
    if (keywords.length > 0) {
      currentContext.value = keywords.reduce((acc, curr) =>
          latestConversation.split(curr).length > latestConversation.split(acc).length ? curr : acc, keywords[0]
      );
    }
  } else {
    currentContext.value = '';
  }
};

const handleSpeechError = (event) => {
  console.error('语音识别错误:', event.error, event.message);
  let alertMsg = '语音识别出错: ' + event.error;
  if (event.error === 'no-speech') {
    alertMsg = '未检测到语音，请清晰表达您的想法';
  } else if (event.error === 'audio-capture') {
    alertMsg = '麦克风捕获失败，请检查设备或权限';
  } else if (event.error === 'not-allowed') {
    alertMsg = '麦克风权限未授予，请允许访问麦克风';
  } else if (event.error === 'aborted') {
    console.log('语音识别被中断');
    return;
  }
  processingMessage.value = alertMsg;
  isMicEnabled.value = false;
  isProcessing.value = false;
  isSpeaking.value = false;
  stopVolumeDetection();
  cleanupAudioResources().then(() => {
    // Optionally, try to re-initialize if it was a recoverable error
  });
};

const handleSpeechEnd = () => {
  console.log('语音识别结束');
  isSpeaking.value = false;
  if (isMicEnabled.value && !isProcessing.value && !isPlaying.value) {
    setTimeout(async () => {
      if (isMicEnabled.value && !isProcessing.value && !isPlaying.value && recognition) {
        try {
          recognition.start();
          console.log('语音识别已重新启动');
        } catch (error) {
          if (error.message && error.message.includes('already started')) {
            console.log('语音识别已在运行');
          } else {
            console.error('重新启动语音识别失败:', error);
            await initializeVoiceChat();
          }
        }
      }
    }, 100);
  }
};

const handlePlaybackEnd = async () => {
  console.log("Audio playback ended.");
  isPlaying.value = false;
  if (audioUrl.value) {
    URL.revokeObjectURL(audioUrl.value);
    audioUrl.value = null;
  }
  if (isMicEnabled.value && !isProcessing.value) {
    await restartRecording();
  } else {
    processingMessage.value = isMicEnabled.value ? '面试官正在聆听您的回答...' : '面试已暂停，点击开始继续';
  }
};

const restartRecording = async () => {
  if (!isMicEnabled.value) {
    console.log("Mic is disabled, not restarting recording.");
    return;
  }
  if (isProcessing.value) {
    console.log("Currently processing, deferring recording restart.");
    return;
  }
  if (!recognition) {
    console.warn("Speech recognition not initialized. Attempting to re-initialize.");
    await initializeVoiceChat();
    return;
  }

  try {
    if (!mediaStream || !audioContext || audioContext.state === 'closed') {
      console.log("Setting up audio analysis for restart...");
      await setupAudioAnalysis();
    }

    recognition.start();
    startVolumeDetection();
    processingMessage.value = '面试官正在聆听您的回答...';
    console.log("Recording restarted successfully.");
  } catch (error) {
    console.error('重新启动录音失败:', error);
    if (error.message && error.message.includes('already started')) {
      console.log("Recognition already running, continuing...");
      startVolumeDetection();
      processingMessage.value = '面试官正在聆听您的回答...';
    } else {
      processingMessage.value = '重新初始化中...';
      await cleanupAudioResources();
      await initializeVoiceChat();
    }
  }
};

const cleanupAudioResources = async () => {
  if (mediaStream) {
    mediaStream.getTracks().forEach(track => track.stop());
    mediaStream = null;
  }
  if (audioContext && audioContext.state !== 'closed') {
    await audioContext.close().catch(e => console.warn("Error closing audio context:", e));
    audioContext = null;
  }
  analyser = null;
  volumeData = null;
  stopVolumeDetection();
};

const cleanupResources = async () => {
  console.log("Cleaning up resources...");

  if (interviewTimer) {
    clearInterval(interviewTimer)
    interviewTimer = null
  }

  if (recognition) {
    recognition.onresult = null;
    recognition.onend = null;
    recognition.onerror = null;
    recognition.onstart = null;
    try {
      recognition.stop();
    } catch (error) {
      console.log('停止语音识别时出错:', error);
    }
    recognition = null;
  }

  if (ttsWebSocket && ttsWebSocket.readyState === WebSocket.OPEN) {
    console.log("Closing active TTS WebSocket.");
    ttsWebSocket.close(1000, "Component cleanup");
  }
  ttsWebSocket = null;

  await cleanupAudioResources();

  if (audioUrl.value) {
    URL.revokeObjectURL(audioUrl.value);
    audioUrl.value = null;
  }
  if (silenceTimer) {
    clearTimeout(silenceTimer);
    silenceTimer = null;
  }

  currentText.value = '';
  isMicEnabled.value = false;
  isProcessing.value = false;
  isSpeaking.value = false;
  isPlaying.value = false;
  processingMessage.value = '';
  isInitialized.value = false;

  console.log("Resources cleaned up.");
};

const closeVoiceChat = async () => {
  await cleanupResources();
  emit('close');
};

</script>

<style scoped>
.ai-interview-agent {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100vw;
  height: 100vh;
  height: 100dvh;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(135deg,
  #f8fafc 0%,
  #f1f5f9 25%,
  #e2e8f0 50%,
  #f1f5f9 75%,
  #f8fafc 100%);
  overflow: hidden;
  z-index: 9999;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.interview-background {
  position: absolute;
  inset: 0;
  overflow: hidden;
  pointer-events: none;
}

.gradient-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(40px);
  opacity: 0.4;
  animation: floatOrb 8s ease-in-out infinite;
}

.orb-1 {
  width: 200px;
  height: 200px;
  background: linear-gradient(45deg, #3b82f6, #8b5cf6);
  top: 10%;
  left: 15%;
  animation-delay: 0s;
}

.orb-2 {
  width: 150px;
  height: 150px;
  background: linear-gradient(45deg, #06b6d4, #3b82f6);
  top: 60%;
  right: 20%;
  animation-delay: -3s;
}

.orb-3 {
  width: 100px;
  height: 100px;
  background: linear-gradient(45deg, #8b5cf6, #06b6d4);
  bottom: 20%;
  left: 25%;
  animation-delay: -6s;
}

/* 主要内容区域 */
.interview-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  max-width: 600px;
  width: 100%;
}

/* 面试头部信息 */
.interview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  max-width: 600px;
  padding: 16px 24px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  margin-bottom: 24px;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.interview-info {
  flex: 1;
}

.interview-title {
  font-size: 18px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 8px 0;
}

.interview-meta {
  display: flex;
  gap: 16px;
  align-items: center;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #64748b;
  font-weight: 500;
}

.meta-icon {
  width: 14px;
  height: 14px;
  fill: currentColor;
}

.difficulty-badge {
  padding: 4px 8px;
  border-radius: 8px;
  font-size: 11px;
  font-weight: 600;
  color: white;
}

.difficulty-badge.junior {
  background: linear-gradient(135deg, #22c55e, #16a34a);
}

.difficulty-badge.intermediate {
  background: linear-gradient(135deg, #f59e0b, #d97706);
}

.difficulty-badge.senior {
  background: linear-gradient(135deg, #ef4444, #dc2626);
}

.minimize-btn {
  width: 32px;
  height: 32px;
  border: none;
  background: rgba(156, 163, 175, 0.2);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.minimize-btn:hover {
  background: rgba(156, 163, 175, 0.3);
}

.minimize-btn .icon {
  width: 16px;
  height: 16px;
  fill: #64748b;
}

/* AI面试官头像区域 */
.interviewer-avatar {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 40px;
}

.avatar-container {
  position: relative;
  margin-bottom: 24px;
}

.avatar-orb {
  position: relative;
  width: 140px;
  height: 140px;
  border-radius: 50%;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow:
      0 10px 40px rgba(59, 130, 246, 0.3),
      0 0 0 4px rgba(255, 255, 255, 0.9),
      inset 0 0 20px rgba(255, 255, 255, 0.2);
  transition: all 0.3s ease;
  overflow: visible;
}

.avatar-orb.listening {
  animation: gentlePulse 2s ease-in-out infinite;
  box-shadow:
      0 10px 40px rgba(59, 130, 246, 0.4),
      0 0 0 4px rgba(34, 197, 94, 0.6),
      inset 0 0 20px rgba(255, 255, 255, 0.2);
}

.avatar-orb.speaking {
  animation: activePulse 1s ease-in-out infinite;
  box-shadow:
      0 10px 40px rgba(59, 130, 246, 0.5),
      0 0 0 4px rgba(251, 191, 36, 0.8),
      inset 0 0 20px rgba(255, 255, 255, 0.2);
}

.avatar-core {
  position: relative;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.ai-brain {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.brain-icon {
  width: 100%;
  height: 100%;
  fill: white;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
}

.avatar-glow {
  position: absolute;
  inset: -10px;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.3) 0%, transparent 70%);
  border-radius: 50%;
  animation: rotateGlow 4s linear infinite;
}

.pulse-ring {
  position: absolute;
  inset: -20px;
  border: 2px solid rgba(59, 130, 246, 0.3);
  border-radius: 50%;
  animation: pulseFade 2s ease-in-out infinite;
}

.status-indicator {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  border: 2px solid white;
  transition: all 0.3s ease;
}

.status-indicator.idle { background: #94a3b8; }
.status-indicator.ready { background: #22c55e; }
.status-indicator.listening {
  background: #22c55e;
  animation: pulseIndicator 1s ease-in-out infinite;
}
.status-indicator.processing {
  background: #f59e0b;
  animation: pulseIndicator 0.8s ease-in-out infinite;
}
.status-indicator.speaking {
  background: #3b82f6;
  animation: pulseIndicator 0.6s ease-in-out infinite;
}

/* 面试官信息 */
.interviewer-info {
  text-align: center;
}

.interviewer-name {
  font-size: 24px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 8px 0;
  letter-spacing: -0.025em;
}

.interviewer-title {
  font-size: 16px;
  color: #64748b;
  margin: 0 0 16px 0;
  font-weight: 500;
}

.interview-status {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 20px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.status-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  transition: all 0.3s ease;
}

.status-text {
  font-size: 14px;
  font-weight: 500;
  color: #475569;
}

/* 状态消息卡片 */
.status-message-card {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  padding: 16px 24px;
  margin: 24px 0;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  max-width: 400px;
  width: 100%;
}

.message-content {
  display: flex;
  align-items: center;
  gap: 12px;
  justify-content: center;
}

.loading-dots {
  display: flex;
  gap: 4px;
}

.loading-dots span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #3b82f6;
  animation: loadingBounce 1.2s ease-in-out infinite;
}

.loading-dots span:nth-child(2) { animation-delay: 0.1s; }
.loading-dots span:nth-child(3) { animation-delay: 0.2s; }

.message-text {
  font-size: 14px;
  font-weight: 500;
  color: #374151;
  text-align: center;
}

/* 面试进度指示 */
.interview-progress {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 20px 0;
  padding: 16px 24px;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.progress-item {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 12px;
  font-weight: 500;
  color: #94a3b8;
  transition: all 0.3s ease;
}

.progress-item.active {
  color: #3b82f6;
}

.progress-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #e2e8f0;
  transition: all 0.3s ease;
}

.progress-item.active .progress-dot {
  background: #3b82f6;
  box-shadow: 0 0 12px rgba(59, 130, 246, 0.4);
}

.progress-line {
  flex: 1;
  height: 2px;
  background: #e2e8f0;
  border-radius: 1px;
  transition: all 0.3s ease;
}

.progress-line.active {
  background: linear-gradient(90deg, #3b82f6, #8b5cf6);
}

/* 控制面板 */
.control-panel {
  width: 100%;
  max-width: 600px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border-top: 1px solid rgba(255, 255, 255, 0.3);
}

.control-section {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-bottom: 20px;
}

/* 麦克风控制 */
.mic-control {
  position: relative;
}

.mic-button {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px;
  background: rgba(248, 250, 252, 0.9);
  border: 2px solid #e2e8f0;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  min-width: 120px;
  backdrop-filter: blur(10px);
}

.mic-button:hover {
  background: rgba(255, 255, 255, 1);
  border-color: #cbd5e1;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.mic-button.active {
  background: linear-gradient(135deg, #22c55e, #16a34a);
  border-color: #22c55e;
  color: white;
}

.mic-button.speaking {
  animation: micSpeaking 1s ease-in-out infinite;
}

.mic-button.disabled {
  opacity: 0.6;
  cursor: not-allowed;
  pointer-events: none;
}

.mic-icon {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.mic-icon .icon {
  width: 24px;
  height: 24px;
  fill: currentColor;
}

.mic-label {
  font-size: 12px;
  font-weight: 600;
  text-align: center;
  color: inherit;
}

.mic-pulse {
  position: absolute;
  inset: -4px;
  border: 2px solid #22c55e;
  border-radius: 20px;
  animation: pulseBorder 2s ease-in-out infinite;
}

/* 结束面试按钮 */
.end-interview-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 16px 24px;
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: white;
  border: none;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.end-interview-btn:hover {
  background: linear-gradient(135deg, #dc2626, #b91c1c);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(239, 68, 68, 0.3);
}

.end-interview-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.end-interview-btn .icon {
  width: 18px;
  height: 18px;
  fill: currentColor;
}

/* 面试助手 */
.interview-assistant {
  margin-top: 20px;
}

.assistant-tips {
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  padding: 20px;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.tip-title {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 12px 0;
}

.tips-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 12px;
  margin-bottom: 16px;
}

.tip-item {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
  color: #64748b;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.tip-icon {
  width: 14px;
  height: 14px;
  fill: #3b82f6;
}

.current-stage-tip {
  border-top: 1px solid rgba(226, 232, 240, 0.8);
  padding-top: 16px;
}

.stage-advice {
  font-size: 13px;
  color: #64748b;
  line-height: 1.5;
  margin: 0;
  font-style: italic;
}

/* 面试总结模态窗口 */
.interview-summary-modal {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 10000;
  padding: 20px;
}

.summary-container {
  background: white;
  border-radius: 24px;
  padding: 40px;
  max-width: 600px;
  width: 100%;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 25px 80px rgba(0, 0, 0, 0.3);
}

.summary-header {
  text-align: center;
  margin-bottom: 32px;
}

.summary-title {
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 8px 0;
}

.summary-subtitle {
  font-size: 16px;
  color: #64748b;
  margin: 0;
}

.summary-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 32px;
}

.stat-item {
  text-align: center;
  padding: 20px;
  background: linear-gradient(135deg, #f8fafc, #f1f5f9);
  border-radius: 16px;
}

.stat-value {
  font-size: 24px;
  font-weight: 700;
  color: #3b82f6;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 12px;
  color: #64748b;
  font-weight: 500;
}

.summary-feedback {
  margin-bottom: 32px;
}

.feedback-title {
  font-size: 18px;
  font-weight: 600;
  color: #374151;
  margin: 0 0 16px 0;
}

.feedback-content {
  padding: 20px;
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.1), rgba(139, 92, 246, 0.1));
  border-radius: 12px;
  border-left: 4px solid #3b82f6;
}

.feedback-content p {
  margin: 0;
  line-height: 1.6;
  color: #374151;
}

.summary-actions {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.action-btn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  border: none;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.action-btn.primary {
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  color: white;
}

.action-btn.primary:hover {
  background: linear-gradient(135deg, #2563eb, #7c3aed);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(59, 130, 246, 0.3);
}

.action-btn.secondary {
  background: #f8fafc;
  color: #374151;
  border: 2px solid #e5e7eb;
}

.action-btn.secondary:hover {
  background: #f1f5f9;
  border-color: #d1d5db;
  transform: translateY(-2px);
}

.action-btn .icon {
  width: 16px;
  height: 16px;
  fill: currentColor;
}

/* 动画 */
@keyframes floatOrb {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  33% { transform: translateY(-20px) rotate(120deg); }
  66% { transform: translateY(10px) rotate(240deg); }
}

@keyframes gentlePulse {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.02); opacity: 0.95; }
}

@keyframes activePulse {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.05); }
}

@keyframes rotateGlow {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@keyframes pulseFade {
  0% { opacity: 0.3; transform: scale(1); }
  50% { opacity: 0.1; transform: scale(1.1); }
  100% { opacity: 0.3; transform: scale(1); }
}

@keyframes pulseIndicator {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.2); opacity: 0.8; }
}

@keyframes loadingBounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}

@keyframes micSpeaking {
  0%, 100% { box-shadow: 0 0 0 0 rgba(34, 197, 94, 0.4); }
  50% { box-shadow: 0 0 0 8px rgba(34, 197, 94, 0); }
}

@keyframes pulseBorder {
  0%, 100% { opacity: 0.5; transform: scale(1); }
  50% { opacity: 0.2; transform: scale(1.05); }
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .ai-interview-agent {
    height: 100vh;
    height: -webkit-fill-available;
    height: 100dvh;
    padding: env(safe-area-inset-top) env(safe-area-inset-right) env(safe-area-inset-bottom) env(safe-area-inset-left);
  }

  .interview-header {
    flex-direction: column;
    gap: 12px;
    padding: 12px 16px;
  }

  .interview-meta {
    flex-wrap: wrap;
    gap: 8px;
    justify-content: center;
  }

  .interview-main {
    padding: 16px;
  }

  .avatar-orb {
    width: 100px;
    height: 100px;
  }

  .avatar-core {
    width: 60px;
    height: 60px;
  }

  .ai-brain {
    width: 36px;
    height: 36px;
  }

  .interviewer-name {
    font-size: 20px;
  }

  .interviewer-title {
    font-size: 14px;
  }

  .control-section {
    gap: 16px;
  }

  .mic-button {
    min-width: 100px;
    padding: 12px;
  }

  .end-interview-btn {
    padding: 12px 16px;
    font-size: 13px;
  }

  .tips-grid {
    grid-template-columns: 1fr;
    gap: 8px;
  }

  .interview-progress {
    padding: 12px 16px;
    gap: 6px;
  }

  .progress-item {
    font-size: 11px;
  }

  .summary-container {
    padding: 24px;
    margin: 16px;
  }

  .summary-stats {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .summary-actions {
    flex-direction: column;
  }

  .action-btn {
    width: 100%;
    justify-content: center;
  }
}
@media screen and (max-width: 480px) {
  .avatar-orb {
    width: 80px;
    height: 80px;
  }

  .interviewer-name {
    font-size: 18px;
  }

  .control-section {
    flex-direction: column;
    align-items: center;
    gap: 12px;
  }

  .mic-button,
  .end-interview-btn {
    width: 100%;
    max-width: 200px;
  }
}

/* iOS优化 */
@supports (-webkit-touch-callout: none) {
  .ai-interview-agent {
    position: fixed;
    overscroll-behavior: none;
    -webkit-overflow-scrolling: touch;
  }
}
</style>