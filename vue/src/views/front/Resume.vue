<template>
  <div style="min-height: 1000px; background-color: #f6f6f8; padding: 50px 0">
    <div style="width: 70%; margin: 0 auto; text-align: center; font-size: 20px; font-weight: bold">
      我的简历（{{ data.resumeData.length }}）
      <el-button type="info" @click="navTo('/front/resumeEdit')">创建新的简历</el-button>
    </div>
    <div style="width: 70%; margin: 50px auto; text-align: center">
      <el-row :gutter="10">
       <el-col :span="6" v-for="item in data.resumeData">
         <img @click="navTo('/front/resumeEdit?id=' + item.id)" src="@/assets/imgs/img.png" alt="" style="width: 100%; cursor: pointer">
         <div style="display: flex; align-items: center; background-color: white; padding: 10px; margin-bottom: 15px; border-bottom-right-radius: 10px; border-bottom-left-radius: 10px">
           <div style="flex: 1">{{ item.name }}</div>
           <a :href="'/resumeView?id=' + item.id" target="_blank">
             <el-icon size="large" style="width: 30px; color: #00bebd; cursor: pointer"><Position /></el-icon>
           </a>
           <el-icon @click="delResume(item.id)" size="large" style="width: 30px; color: red; cursor: pointer;"><Delete /></el-icon>
         </div>
       </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  resumeData: []
})

const navTo = (url) => {
  location.href = url
}
const loadResume = () => {
  request.get('/resume/selectAll', {
    params: {
      userId: data.user.id
    }
  }).then(res => {
    if (res.code === '200') {
      data.resumeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const delResume = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/resume/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        loadResume()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

loadResume()
</script>