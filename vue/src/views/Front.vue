<template>
  <div>
    <div class="front-notice"><el-icon><Bell /></el-icon>公告：{{ data.top }}</div>
    <div class="front-header">
      <div class="front-header-left">
        <div class="title" @click="router.push('/front/home')">智面未来</div>
      </div>
      <div class="front-header-center">
        <el-menu :default-active="router.currentRoute.value.path" router mode="horizontal">
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item v-if="data.user && data.user.role === 'USER'" index="/front/collect">我的收藏</el-menu-item>
          <el-menu-item v-if="data.user && data.user.role === 'USER'" index="/front/resume">我的简历</el-menu-item>
          <el-menu-item v-if="data.user && data.user.role === 'USER'" index="/front/submit">我的投递</el-menu-item>
          <el-menu-item v-if="data.user && data.user.role === 'USER'" index="/front/person">个人中心</el-menu-item>
        </el-menu>
      </div>
      <div class="front-header-right">
        <div v-if="!data.user.id">
          <el-button @click="router.push('/login')">登录</el-button>
          <el-button @click="router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown style="cursor: pointer; height: 50px">
            <div style="display: flex; align-items: center">
              <img style="width: 35px; height: 35px; border-radius: 50%; border: 2px solid #34daa9" :src="data.user.avatar" alt="">
              <span style="margin-left: 5px; color: white">{{ data.user.name }}</span><el-icon><arrow-down /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <div class="main-body">
      <RouterView @updateUser="updateUser" />
    </div>
  </div>
</template>

<script setup>
  import router from "@/router/index.js";
  import { reactive } from "vue";
  import request from "@/utils/request.js";

  const data = reactive({
    user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
    top: '',
    noticeData: []
  })

  const logout = () => {
    localStorage.removeItem('xm-user')
    router.push('/login')
  }

  const updateUser = () => {
    data.user =  JSON.parse(localStorage.getItem('xm-user') || '{}')
  }

  const loadNotice = () => {
    request.get('/notice/selectAll').then(res => {
      data.noticeData = res.data
      let i = 0
      if (data.noticeData && data.noticeData.length) {
        data.top = data.noticeData[0].content
        setInterval(() => {
          data.top = data.noticeData[i].content
          i++
          if (i === data.noticeData.length) {
            i = 0
          }
        }, 2500)
      }
    })
  }
  loadNotice()
</script>

<style scoped>
@import "@/assets/css/front.css";
</style>