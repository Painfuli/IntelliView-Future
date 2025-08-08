<template>
  <div style="min-height: 500px; background-color: #f6f6f8; padding-bottom: 20px">
    <div style="height: 200px; background-color: #3b526a; padding: 50px 0">
      <div style="width: 80%; margin: 0 auto">
        <div style="display: flex; color: white">
          <img :src="data.employData.avatar" alt="" style="width: 70px; height: 70px; border-radius: 10px">
          <div style="margin-left: 20px">
            <div style="font-size: 28px; font-weight: bold">{{ data.employData.name }}</div>
            <div style="margin-top: 10px">
              <el-icon><Coordinate /></el-icon><span style="font-size: 16px; margin-right: 20px">{{ data.employData.stage }}</span>
              <el-icon><User /></el-icon><span style="font-size: 16px; margin-right: 20px">{{ data.employData.scale }}</span>
              <el-icon><CollectionTag /></el-icon><span style="font-size: 16px">在招职位：{{ data.positionData.length }}</span>
            </div>
          </div>
        </div>
        <div style="margin-top: 15px; color: white; font-size: 17px; display: flex; align-items: center">
          <el-icon><LocationInformation /></el-icon>
          <span style="margin-left: 5px">{{ data.employData.address }}</span>
        </div>
      </div>
    </div>
    <div style="width: 70%; margin: 20px auto; text-align: center">
      <el-input size="large" v-model="data.name" clearable @clear="reset" placeholder="搜索该企业正在招聘的岗位" style="width: 500px; margin-right: 5px"></el-input>
      <el-button size="large" type="primary" @click="loadPosition">搜索</el-button>
    </div>
    <div style="margin: 50px auto; width: 70%">
      <el-row :gutter="10">
        <el-col :span="8" v-for="it in data.positionData" style="margin-bottom: 20px">
          <div class="card" style="cursor: pointer" @click="navTo('/front/positionDetail?id=' + it.id)">
            <div style="display: flex; padding: 0 5px">
              <div style="flex: 1; text-align: left; font-size: 16px">{{ it.name }}</div>
              <div style="width: 100px; text-align: right; color: red">{{ it.salary }}</div>
            </div>
            <div style="margin: 10px 0; padding: 0 5px; text-align: left">
              <el-tag style="margin-right: 5px" type="info" v-for="tag in it.tagList">{{ tag }}</el-tag>
            </div>
            <div style="display: flex; align-items: center; padding: 10px 5px">
              <div style="width: 35px"><img :src="it.employAvatar" alt="" style="width: 35px; height: 35px; border-radius: 5px; border: 1px solid #cccccc"></div>
              <div style="width: 80px; margin-left: 10px">{{ it.employName }}</div>
              <div style="flex: 1">{{ it.industryName }}</div>
              <div style="width: 80px; text-align: right">{{ it.employStage }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

  </div>
</template>

<script setup>
import {reactive, onMounted} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  employId: router.currentRoute.value.query.id,
  employData: {},
  positionData: [],
  name: null,
})

const loadEmploy = () => {
  data.employId = router.currentRoute.value.query.id
  request.get('/employ/selectById/' + data.employId).then(res => {
    if (res.code === '200') {
      data.employData = res.data
    }
  })
}
const loadPosition = () => {
  data.employId = router.currentRoute.value.query.id
  request.get('/position/selectAll', {
    params: {
      employId: data.employId,
      name: data.name,
      status: "审核通过"
    }
  }).then(res => {
    if (res.code === '200') {
      data.positionData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const reset = () => {
  data.name = null
  loadPosition()
}

const navTo = (url) => {
  location.href = url
}

loadEmploy()
loadPosition()
</script>