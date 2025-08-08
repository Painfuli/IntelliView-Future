<template>
  <div class="login-container">
    <div class="login-card">
      <!-- 左侧 -->
      <div class="login-sidebar">
        <div class="icon-box boss-icon">
          <div class="icon">
            <span class="boss-badge">IVF</span>
          </div>
          <div class="icon-text">
            <div class="primary-text">找工作</div>
            <div class="secondary-text">上智面未来直接谈</div>
          </div>
        </div>

        <div class="icon-box chat-icon">
          <div class="icon">
            <el-icon class="chat-badge"><ChatDotRound /></el-icon>
          </div>
          <div class="icon-text">
            <div class="primary-text">沟通</div>
            <div class="secondary-text">在线职位及时沟通</div>
          </div>
        </div>

        <div class="icon-box job-icon">
          <div class="icon">
            <el-icon class="job-badge"><Suitcase /></el-icon>
          </div>
          <div class="icon-text">
            <div class="primary-text">任性选</div>
            <div class="secondary-text">各大行业职位任你选</div>
          </div>
        </div>
      </div>

      <!-- 右侧表单部分 -->
      <div class="login-form">
        <div class="tab-header">
          <div class="qr-tab">
            <img src="@/assets/imgs/qr.png" alt="QR Code" class="qr-icon" />
            <span>APP扫码登录</span>
          </div>
        </div>

        <div class="form-content">
          <h2 class="form-title">欢迎登录智面未来</h2>
          <div class="form-subtitle">首次验证通过即注册智面未来账号</div>

          <el-form ref="formRef" :model="data.form" :rules="data.rules">
            <!-- 角色选择 -->
            <el-form-item prop="role">
              <div class="role-buttons">
                <el-button
                    size="large"
                    type="primary"
                    :plain="data.form.role !== 'USER'"
                    :class="['role-btn', { active: data.form.role === 'USER' }]"
                    @click="data.form.role = 'USER'"
                >我要找工作</el-button>
                <el-button
                    size="large"
                    type="primary"
                    :plain="data.form.role !== 'EMPLOY'"
                    :class="['role-btn', { active: data.form.role === 'EMPLOY' }]"
                    @click="data.form.role = 'EMPLOY'"
                >我要招聘</el-button>
              </div>
            </el-form-item>

            <!-- 用户名输入 -->
            <el-form-item prop="username">
              <el-input
                  size="large"
                  v-model="data.form.username"
                  placeholder="请输入账号"
              ></el-input>
            </el-form-item>

            <!-- 密码输入 -->
            <el-form-item prop="password">
              <el-input
                  show-password
                  size="large"
                  v-model="data.form.password"
                  placeholder="请输入密码"
                  @keyup.enter="login"
              ></el-input>
            </el-form-item>

            <!-- 登录按钮 -->
            <el-form-item>
              <el-button size="large" type="primary" class="submit-btn" @click="login">登录</el-button>
            </el-form-item>
          </el-form>

          <!-- 其他登录选项 -->
          <div class="login-options">
            <router-link to="/register" class="register-link">没有账号？立即注册</router-link>
          </div>

          <!-- 协议同意 -->
          <div class="terms-wrapper">
            <el-checkbox v-model="userAgreement" class="terms-checkbox">
              已阅读并同意智面未来《<a href="#" class="link">用户协议</a>》《<a href="#" class="link">隐私政策</a>》，允许智面未来统一管理本人账号信息
            </el-checkbox>
          </div>

          <!-- 页脚  -->
          <div class="footer">
            <div class="footer-line">客服电话：187-6811-5216 工作时间：8:00-22:00</div>
            <div class="footer-line">人力资源服务许可证｜营业执照｜朝阳区人社局监督电话</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { ChatDotRound, Suitcase } from '@element-plus/icons-vue';
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

// 将协议同意移到外面，不作为表单的一部分
const userAgreement = ref(false);

// form 初始为空对象
const data = reactive({
  form: {
    role: "USER" // 只设置默认角色
  },
  rules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ],
    role: [
      { required: true, message: '请选择角色', trigger: 'change' }
    ]
  }
})

const formRef = ref()

const login = () => {
  // 检查用户是否同意协议
  if (!userAgreement.value) {
    ElMessage.warning('请阅读并同意用户协议和隐私政策')
    return
  }

  formRef.value.validate(valid => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res && res.code === '200') {
          ElMessage.success('登录成功')
          localStorage.setItem('xm-user', JSON.stringify(res.data))
          if ('USER' === res.data.role) {
            location.href = '/front/home'
          } else if ('ADMIN' === res.data.role) {
            router.push('/manager/home')
          } else {
            router.push('/manager/position')
          }
        } else {
          ElMessage.error(res?.msg || '登录失败')
        }
      }).catch(error => {
        console.error('登录请求失败:', error)
        ElMessage.error('登录失败，请检查网络连接或联系管理员')
      })
    }
  })
}
</script>

<style scoped>

.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #5cbdb9;
}

.login-card {
  display: flex;
  width: 980px;
  height: 600px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  background-color: #fff;
}

.login-sidebar {
  width: 340px;
  padding: 80px 30px;
  background-color: #f0f9f9;
  display: flex;
  flex-direction: column;
  gap: 60px;
}

.icon-box {
  display: flex;
  align-items: center;
  gap: 16px;
}

.icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  background-color: #e0f2f2;
  display: flex;
  justify-content: center;
  align-items: center;
}

.boss-badge {
  background-color: #5cbdb9;
  color: white;
  font-weight: bold;
  padding: 4px 8px;
  border-radius: 4px;
}

.chat-badge, .job-badge {
  color: #5cbdb9;
  font-size: 24px;
}

.icon-text {
  display: flex;
  flex-direction: column;
}

.primary-text {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 4px;
}

.secondary-text {
  font-size: 14px;
  color: #666;
}

.login-form {
  flex: 1;
  padding: 40px;
  position: relative;
}

.tab-header {
  position: absolute;
  top: 20px;
  right: 30px;
}

.qr-tab {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  font-size: 14px;
  cursor: pointer;
}

.qr-icon {
  width: 20px;
  height: 20px;
}

.form-content {
  margin-top: 50px;
  max-width: 360px;
  margin-left: auto;
  margin-right: auto;
}

.form-title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  margin-bottom: 6px;
  color: #333;
}

.form-subtitle {
  font-size: 14px;
  color: #666;
  text-align: center;
  margin-bottom: 20px;
}

.role-buttons {
  display: flex;
  gap: 16px;
  width: 100%;
}

.role-btn {
  flex: 1;
  border-radius: 4px;
  height: 42px;
}

.role-btn.active {
  font-weight: bold;
}

.submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  border-radius: 4px;
  background-color: #5cbdb9;
  border-color: #5cbdb9;
}

.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 18px;
  margin-top: 16px;
}

.wechat-login {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #5cbdb9;
}

.wechat-icon {
  width: 20px;
  height: 20px;
}

.register-link {
  color: #5cbdb9;
  text-decoration: none;
  font-size: 14px;
}

/* 优化了协议同意部分 */
.terms-wrapper {
  margin-bottom: 24px;
  max-width: 360px;
  display: flex;
  justify-content: center; /* 如果 .terms-checkbox 不是100%宽度，这可以帮助居中 */
}

.terms-checkbox { /* 这是 el-checkbox 组件的类 */
  font-size: 12px;
  color: #666;
  width: 100%; /* 使其填充 .terms-wrapper */

  /* 使用 flex 布局来对齐内部的 checkbox 输入部分和文本标签部分 */
  display: flex;
  align-items: flex-start; /* 尝试将 checkbox 输入区域的顶部与文本标签区域的顶部对齐 */
}

/*
  针对 el-checkbox 内部结构的深度选择器样式
  这些样式将帮助我们微调复选框方块相对于文本的位置
*/
.terms-checkbox :deep(.el-checkbox__input) {
  margin-top: 2px; /* << 关键调整：尝试修改这个值，例如从 0px 到 5px 之间找到最佳视觉效果 */
}

.terms-checkbox :deep(.el-checkbox__label) {
  /* el-checkbox__label 是包裹文本内容的 span 元素 */
  white-space: normal;   /* 允许文本自动换行 */
  word-break: break-word; /* 确保长单词能正确断开 */
  line-height: 1.4;      /* 设置文本的行高 */
  padding-left: 8px;     /* 复选框与文本之间的间距 */
}

.link {
  color: #5cbdb9;
  text-decoration: none;
}

/* 优化了页脚部分 */
.footer {
  font-size: 12px;
  color: #999;
  margin-top: 24px;
  text-align: center;
}

.footer-line {
  line-height: 1.6;
  margin-bottom: 4px;
}

/* Override Element Plus styles - 以下是您原有的通用 Element Plus 样式覆盖 */
:deep(.el-button--primary) {
  --el-button-bg-color: #5cbdb9;
  --el-button-border-color: #5cbdb9;
  --el-button-hover-bg-color: #4aa8a4;
  --el-button-hover-border-color: #4aa8a4;
  --el-button-active-bg-color: #3d9c98;
  --el-button-active-border-color: #3d9c98;
}

:deep(.el-button--primary.is-plain) {
  --el-button-bg-color: #ffffff;
  --el-button-border-color: #5cbdb9;
  --el-button-text-color: #5cbdb9;
  --el-button-hover-text-color: #ffffff;
  --el-button-hover-bg-color: #5cbdb9;
  --el-button-hover-border-color: #5cbdb9;
  --el-button-active-bg-color: #3d9c98;
  --el-button-active-border-color: #3d9c98;
}

:deep(.el-input__wrapper), :deep(.el-select__wrapper) {
  height: 42px; /* Reduced from 44px */
  border-radius: 4px;
}

:deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background-color: #5cbdb9;
  border-color: #5cbdb9;
}

/* 注意：如果 .terms-checkbox :deep(.el-checkbox__label) 中已经设置了 color，
   这个通用规则可能需要调整或确认其优先级。
   不过通常情况下，Element Plus 的 is-checked 优先级较高或需要明确覆盖。
   当前设置是针对所有已选中的复选框标签文本颜色。
*/
:deep(.el-checkbox__input.is-checked + .el-checkbox__label) {
  color: #666;
}

:deep(.el-select .el-input .el-select__caret) {
  color: #5cbdb9;
}

/*
  原有的通用 :deep(.el-checkbox) 样式：
  :deep(.el-checkbox) {
    height: auto;
    margin-right: 0;
    display: flex;
    align-items: flex-start;
  }
  这个通用样式已经为所有 el-checkbox 设置了 flex-start 对齐。
  .terms-checkbox 中的 display: flex 和 align-items: flex-start 是对其的再次确认或特定化。
  关键的调整依然是 .terms-checkbox :deep(.el-checkbox__input) 中的 margin-top。
  如果上述通用样式存在，可以简化 .terms-checkbox 中的 flex 设置，但保留它也无害。
*/

/* 通用 el-checkbox 内部结构样式（） */
:deep(.el-checkbox__inner) { /* 复选框方块的边框颜色 */
  border-color: #c0c4cc;
}

/* 这个通用标签样式也很好，确保它与 .terms-checkbox :deep(.el-checkbox__label) 协同工作 */
:deep(.el-checkbox__label) {
  white-space: normal;
  word-break: break-word;
  line-height: 1.4;
  padding-left: 8px;
}
</style>