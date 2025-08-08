<template>
  <div class="interview-setup">
    <div class="setup-container">
      <div class="setup-header">
        <h1 class="setup-title">AI模拟面试</h1>
        <p class="setup-subtitle">准备好开始您的专业面试体验了吗？</p>
      </div>

      <div class="setup-form">
        <!-- 面试类型选择 -->
        <div class="form-group">
          <label class="form-label">面试类型</label>
          <div class="option-grid">
            <div
                v-for="type in interviewTypes"
                :key="type.id"
                class="option-card"
                :class="{ active: selectedType === type.id }"
                @click="selectedType = type.id"
            >
              <div class="option-icon">
                <svg viewBox="0 0 24 24" class="icon">
                  <path :d="type.icon"/>
                </svg>
              </div>
              <div class="option-content">
                <h3 class="option-title">{{ type.name }}</h3>
                <p class="option-desc">{{ type.description }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 面试难度选择 -->
        <div class="form-group">
          <label class="form-label">面试难度</label>
          <div class="difficulty-selector">
            <div
                v-for="level in difficultyLevels"
                :key="level.id"
                class="difficulty-option"
                :class="{ active: selectedDifficulty === level.id }"
                @click="selectedDifficulty = level.id"
            >
              <div class="difficulty-indicator" :class="level.id"></div>
              <span class="difficulty-label">{{ level.name }}</span>
            </div>
          </div>
        </div>

        <!-- 预计时长 -->
        <div class="form-group">
          <label class="form-label">预计时长</label>
          <div class="duration-selector">
            <div
                v-for="duration in durations"
                :key="duration.value"
                class="duration-option"
                :class="{ active: selectedDuration === duration.value }"
                @click="selectedDuration = duration.value"
            >
              <span class="duration-value">{{ duration.label }}</span>
              <span class="duration-desc">{{ duration.description }}</span>
            </div>
          </div>
        </div>

        <!-- 开始面试按钮 -->
        <button class="start-interview-btn" @click="handleStartInterview" :disabled="isStarting">
          <div v-if="isStarting" class="loading-spinner"></div>
          <svg v-else viewBox="0 0 24 24" class="icon">
            <path d="M8 5v14l11-7z"/>
          </svg>
          <span>{{ isStarting ? '正在初始化...' : '开始面试' }}</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// Props
const props = defineProps({
  isStarting: {
    type: Boolean,
    default: false
  }
})

// Emits
const emit = defineEmits(['start-interview'])

// 面试设置状态
const selectedType = ref('technical')
const selectedDifficulty = ref('intermediate')
const selectedDuration = ref(30)

// 面试类型配置
const interviewTypes = ref([
  {
    id: 'technical',
    name: '技术面试',
    description: '考察编程与技术能力',
    icon: 'M9.4 16.6L4.8 12l4.6-4.6L8 6l-6 6 6 6 1.4-1.4zm5.2 0L19.2 12l-4.6-4.6L16 6l6 6-6 6-1.4-1.4z'
  },
  {
    id: 'behavioral',
    name: 'HR面试',
    description: '评估沟通与团队合作',
    icon: 'M16 4c0-1.11.89-2 2-2s2 .89 2 2-.89 2-2 2-2-.89-2-2zM4 18v-6h3v-2c0-2.21 1.79-4 4-4s4 1.79 4 4v2h3v6H4zm9-8c0-1.1-.9-2-2-2s-2 .9-2 2v2h4v-2z'
  },
  {
    id: 'comprehensive',
    name: '综合面试',
    description: '全面评估综合素质',
    icon: 'M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z'
  },
  {
    id: 'leadership',
    name: '管理面试',
    description: '考察领导与管理能力',
    icon: 'M12 6.5c1.38 0 2.5-1.12 2.5-2.5S13.38 1.5 12 1.5 9.5 2.62 9.5 4s1.12 2.5 2.5 2.5zM21 9v-2c0-1.1-.9-2-2-2H5c-1.1 0-2 .9-2 2v2c0 1.1.9 2 2 2h3.5v7h5v-7H19c1.1 0 2-.9 2-2z'
  }
])

const difficultyLevels = ref([
  { id: 'junior', name: '初级', description: '1-2年经验' },
  { id: 'intermediate', name: '中级', description: '3-5年经验' },
  { id: 'senior', name: '高级', description: '5+年经验' }
])

const durations = ref([
  { value: 15, label: '15分钟', description: '快速评估' },
  { value: 30, label: '30分钟', description: '标准面试' },
  { value: 45, label: '45分钟', description: '深度交流' },
  { value: 60, label: '60分钟', description: '全面评估' }
])

// 计算属性
const getCurrentTypeInfo = computed(() => {
  return interviewTypes.value.find(type => type.id === selectedType.value) || interviewTypes.value[0]
})

const getCurrentDifficultyInfo = computed(() => {
  return difficultyLevels.value.find(level => level.id === selectedDifficulty.value) || difficultyLevels.value[0]
})

const getInterviewFocus = computed(() => {
  const focusMap = {
    'technical': '技术深度、编程能力、系统设计思维',
    'behavioral': '沟通表达、团队协作、解决冲突能力',
    'comprehensive': '技术能力与软技能的平衡展现',
    'leadership': '领导风格、团队管理、战略思维'
  }
  return focusMap[selectedType.value] || '综合能力评估'
})

// 方法
const handleStartInterview = () => {
  const interviewConfig = {
    type: selectedType.value,
    difficulty: selectedDifficulty.value,
    duration: selectedDuration.value,
    typeInfo: getCurrentTypeInfo.value,
    difficultyInfo: getCurrentDifficultyInfo.value,
    focus: getInterviewFocus.value
  }

  emit('start-interview', interviewConfig)
}

// 暴露给父组件的方法和数据
defineExpose({
  selectedType,
  selectedDifficulty,
  selectedDuration,
  getCurrentTypeInfo,
  getCurrentDifficultyInfo,
  getInterviewFocus
})
</script>

<style scoped>
.interview-setup {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  width: 100%;
  min-height: 100vh;
  overflow-y: auto;
}

.setup-container {
  width: 100%;
  max-width: 900px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(30px);
  border-radius: 24px;
  padding: 32px 40px;
  box-shadow:
      0 20px 60px rgba(0, 0, 0, 0.08),
      0 8px 30px rgba(0, 0, 0, 0.04),
      0 0 0 1px rgba(255, 255, 255, 0.5) inset;
  border: 1px solid rgba(255, 255, 255, 0.2);
  max-height: calc(100vh - 40px);
  overflow-y: auto;
  position: relative;
}

.setup-header {
  text-align: center;
  margin-bottom: 24px;
}

.setup-title {
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 8px 0;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.setup-subtitle {
  font-size: 16px;
  color: #64748b;
  margin: 0;
  font-weight: 500;
}

.setup-form {
  space-y: 24px;
}

.form-group {
  margin-bottom: 24px;
}

.form-label {
  display: block;
  font-size: 15px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 12px;
}

/* 面试类型选择 */
.option-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.option-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  gap: 12px;
  padding: 16px;
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  min-height: 120px;
  box-shadow:
      0 1px 3px rgba(0, 0, 0, 0.05),
      0 0 0 1px rgba(255, 255, 255, 0.5) inset;
  position: relative;
  overflow: hidden;
}

.option-card::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, transparent, rgba(59, 130, 246, 0.03));
  opacity: 0;
  transition: opacity 0.3s ease;
}

.option-card:hover {
  border-color: rgba(59, 130, 246, 0.3);
  background: rgba(255, 255, 255, 0.9);
  transform: translateY(-3px);
  box-shadow:
      0 8px 30px rgba(59, 130, 246, 0.08),
      0 4px 12px rgba(0, 0, 0, 0.05),
      0 0 0 1px rgba(59, 130, 246, 0.1) inset;
}

.option-card:hover::before {
  opacity: 1;
}

.option-card.active {
  border-color: rgba(59, 130, 246, 0.5);
  background: rgba(255, 255, 255, 0.95);
  box-shadow:
      0 12px 40px rgba(59, 130, 246, 0.12),
      0 8px 20px rgba(139, 92, 246, 0.05),
      0 0 0 1px rgba(59, 130, 246, 0.2) inset,
      0 0 0 1px rgba(255, 255, 255, 0.8) inset;
  transform: translateY(-2px);
}

.option-card.active::before {
  opacity: 1;
  background: linear-gradient(135deg, rgba(59, 130, 246, 0.04), rgba(139, 92, 246, 0.04));
}

.option-card.active .option-icon {
  transform: scale(1.05);
  box-shadow:
      0 6px 20px rgba(59, 130, 246, 0.35),
      0 0 0 1px rgba(255, 255, 255, 0.3) inset;
}

.option-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  border-radius: 12px;
  flex-shrink: 0;
  box-shadow:
      0 4px 12px rgba(59, 130, 246, 0.25),
      0 0 0 1px rgba(255, 255, 255, 0.2) inset;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.option-icon .icon {
  width: 20px;
  height: 20px;
  fill: white;
}

.option-content {
  flex: 1;
}

.option-title {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
  margin: 0 0 4px 0;
}

.option-desc {
  font-size: 12px;
  color: #64748b;
  margin: 0;
  line-height: 1.3;
}

/* 难度选择器 */
.difficulty-selector {
  display: flex;
  gap: 16px;
  justify-content: center;
}

.difficulty-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  padding: 12px 20px;
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  flex: 1;
  max-width: 180px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  box-shadow:
      0 1px 3px rgba(0, 0, 0, 0.05),
      0 0 0 1px rgba(255, 255, 255, 0.5) inset;
  position: relative;
  overflow: hidden;
}

.difficulty-option::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, transparent, rgba(59, 130, 246, 0.02));
  opacity: 0;
  transition: opacity 0.3s ease;
}

.difficulty-option:hover {
  border-color: rgba(59, 130, 246, 0.3);
  background: rgba(255, 255, 255, 0.9);
  transform: translateY(-2px);
  box-shadow:
      0 6px 20px rgba(59, 130, 246, 0.08),
      0 3px 8px rgba(0, 0, 0, 0.05),
      0 0 0 1px rgba(59, 130, 246, 0.1) inset;
}

.difficulty-option:hover::before {
  opacity: 1;
}

.difficulty-option.active {
  border-color: rgba(59, 130, 246, 0.5);
  background: rgba(255, 255, 255, 0.95);
  box-shadow:
      0 8px 25px rgba(59, 130, 246, 0.1),
      0 4px 12px rgba(0, 0, 0, 0.05),
      0 0 0 1px rgba(59, 130, 246, 0.2) inset,
      0 0 0 1px rgba(255, 255, 255, 0.8) inset;
  transform: translateY(-1px);
}

.difficulty-option.active::before {
  opacity: 1;
}

.difficulty-indicator {
  width: 20px;
  height: 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.difficulty-indicator.junior {
  background: linear-gradient(135deg, #22c55e, #16a34a);
}

.difficulty-indicator.intermediate {
  background: linear-gradient(135deg, #f59e0b, #d97706);
}

.difficulty-indicator.senior {
  background: linear-gradient(135deg, #ef4444, #dc2626);
}

.difficulty-indicator::after {
  content: '';
  width: 6px;
  height: 6px;
  background: white;
  border-radius: 50%;
}

.difficulty-label {
  font-size: 13px;
  font-weight: 600;
  color: #374151;
}

/* 时长选择器 */
.duration-selector {
  display: flex;
  gap: 12px;
  justify-content: center;
}

.duration-option {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  padding: 12px 16px;
  border: 1px solid rgba(226, 232, 240, 0.8);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  text-align: center;
  flex: 1;
  max-width: 150px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  box-shadow:
      0 1px 3px rgba(0, 0, 0, 0.05),
      0 0 0 1px rgba(255, 255, 255, 0.5) inset;
  position: relative;
  overflow: hidden;
}

.duration-option::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, transparent, rgba(59, 130, 246, 0.02));
  opacity: 0;
  transition: opacity 0.3s ease;
}

.duration-option:hover {
  border-color: rgba(59, 130, 246, 0.3);
  background: rgba(255, 255, 255, 0.9);
  transform: translateY(-2px);
  box-shadow:
      0 6px 20px rgba(59, 130, 246, 0.08),
      0 3px 8px rgba(0, 0, 0, 0.05),
      0 0 0 1px rgba(59, 130, 246, 0.1) inset;
}

.duration-option:hover::before {
  opacity: 1;
}

.duration-option.active {
  border-color: rgba(59, 130, 246, 0.5);
  background: rgba(255, 255, 255, 0.95);
  box-shadow:
      0 8px 25px rgba(59, 130, 246, 0.1),
      0 4px 12px rgba(0, 0, 0, 0.05),
      0 0 0 1px rgba(59, 130, 246, 0.2) inset,
      0 0 0 1px rgba(255, 255, 255, 0.8) inset;
  transform: translateY(-1px);
}

.duration-option.active::before {
  opacity: 1;
}

.duration-value {
  font-size: 16px;
  font-weight: 700;
  color: #1e293b;
}

.duration-desc {
  font-size: 11px;
  color: #64748b;
}

/* 开始面试按钮 */
.start-interview-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  width: 100%;
  max-width: 300px;
  margin: 24px auto 0;
  padding: 14px 28px;
  background: linear-gradient(135deg, #3b82f6, #8b5cf6);
  color: white;
  border: none;
  border-radius: 16px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow:
      0 4px 15px rgba(59, 130, 246, 0.3),
      0 0 0 1px rgba(255, 255, 255, 0.1) inset;
  position: relative;
  overflow: hidden;
}

.start-interview-btn::before {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.1), transparent);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.start-interview-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, #2563eb, #7c3aed);
  transform: translateY(-2px);
  box-shadow:
      0 8px 30px rgba(59, 130, 246, 0.4),
      0 4px 12px rgba(139, 92, 246, 0.2),
      0 0 0 1px rgba(255, 255, 255, 0.2) inset;
}

.start-interview-btn:hover:not(:disabled)::before {
  opacity: 1;
}

.start-interview-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
  transform: none;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.2);
}

.start-interview-btn .icon {
  width: 20px;
  height: 20px;
  fill: currentColor;
}

/* 加载动画 */
.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 响应式设计 */
@media screen and (max-width: 968px) {
  .option-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media screen and (max-width: 768px) {
  .setup-container {
    padding: 24px;
    max-width: 100%;
    margin: 0;
  }

  .setup-title {
    font-size: 24px;
  }

  .setup-subtitle {
    font-size: 14px;
  }

  .option-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }

  .option-card {
    padding: 12px;
    min-height: 100px;
  }

  .difficulty-selector {
    flex-wrap: wrap;
    gap: 10px;
  }

  .difficulty-option {
    min-width: calc(33.333% - 10px);
  }

  .duration-selector {
    flex-wrap: wrap;
    gap: 10px;
  }

  .duration-option {
    min-width: calc(50% - 10px);
  }
}

@media screen and (max-width: 480px) {
  .setup-title {
    font-size: 20px;
  }

  .option-grid {
    grid-template-columns: 1fr;
  }

  .option-card {
    flex-direction: row;
    text-align: left;
    gap: 12px;
    min-height: auto;
    padding: 16px;
  }

  .option-card .option-content {
    flex: 1;
  }

  .difficulty-selector {
    flex-direction: column;
    gap: 8px;
  }

  .difficulty-option {
    width: 100%;
    max-width: none;
  }

  .duration-selector {
    flex-direction: column;
    gap: 8px;
  }

  .duration-option {
    width: 100%;
    max-width: none;
  }
}

/* 移动端横屏模式优化 */
@media screen and (max-height: 600px) and (orientation: landscape) {
  .interview-setup {
    padding: 10px;
    min-height: auto;
  }

  .setup-container {
    padding: 20px;
    max-height: calc(100vh - 20px);
  }

  .setup-header {
    margin-bottom: 16px;
  }

  .setup-title {
    font-size: 20px;
  }

  .form-group {
    margin-bottom: 16px;
  }

  .option-grid {
    grid-template-columns: repeat(4, 1fr);
    gap: 8px;
  }

  .option-card {
    padding: 8px;
    min-height: 80px;
  }

  .option-icon {
    width: 30px;
    height: 30px;
  }

  .start-interview-btn {
    margin-top: 16px;
    padding: 10px 20px;
  }
}
</style>