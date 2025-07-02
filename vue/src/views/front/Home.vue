<template>
  <div style="min-height: 1000px; background-color: #f6f6f8">
    <img src="@/assets/imgs/banner.jpg" alt="" style="width: 100%; height: 120px">
    <div style="margin: 25px auto; width: 70%; text-align: center">
      <el-input size="large" v-model="data.name" placeholder="请输入你感兴趣的职位" style="width: 500px; margin-right: 5px"></el-input>
      <el-button size="large" type="info" @click="search">搜索</el-button>
    </div>
    <div style="margin: 0 auto; width: 70%; text-align: center">
      <div style="display: flex">
        <div @click="navTo('/front/positionDetail?id=' + data.leftAd.positionId)" style="cursor: pointer; flex: 1"><img style="width: 100%; height: 240px; border-top-left-radius: 10px" :src="data.leftAd.img" alt=""></div>
        <div @click="navTo('/front/positionDetail?id=' + data.centerAd.positionId)" style="cursor: pointer; flex: 2; margin: 0 2px"><img style="width: 100%; height: 240px" :src="data.centerAd.img" alt=""></div>
        <div @click="navTo('/front/positionDetail?id=' + data.rightAd.positionId)" style="cursor: pointer; flex: 1"><img style="width: 100%; height: 240px; border-top-right-radius: 10px" :src="data.rightAd.img" alt=""></div>
      </div>
      <div style="display: flex">
        <div @click="navTo('/front/positionDetail?id=' + data.leftDownAd.positionId)" style="cursor: pointer; flex: 1"><img style="width: 100%; height: 120px; border-bottom-left-radius: 10px" :src="data.leftDownAd.img" alt=""></div>
        <div @click="navTo('/front/positionDetail?id=' + data.centerDownAd.positionId)" style="cursor: pointer; flex: 2; margin: 0 2px"><img style="width: 100%; height: 120px" :src="data.centerDownAd.img" alt=""></div>
        <div @click="navTo('/front/positionDetail?id=' + data.rightDownAd.positionId)" style="cursor: pointer; flex: 1"><img style="width: 100%; height: 120px; border-bottom-right-radius: 10px" :src="data.rightDownAd.img" alt=""></div>
      </div>
      <div style="margin: 30px; font-size: 22px; font-weight: bold; text-align: center">热招职位</div>
      <div>
        <el-tabs v-model="data.activeName" class="demo-tabs" @tab-change="handleClick">
          <el-tab-pane v-for="item in data.industryData" :label="item.name" :name="item.id">
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
                   <div style="width: 80px">{{ it.employName }}</div>
                   <div style="flex: 1">{{ item.name }}</div>
                   <div style="width: 80px">{{ it.employStage }}</div>
                 </div>
               </div>
             </el-col>
            </el-row>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  name: null,
  advertiseData: [],
  centerAd: {},
  leftAd: {},
  rightAd: {},
  centerDownAd: {},
  leftDownAd: {},
  rightDownAd: {},
  activeName: null,
  industryData: [],
  positionData: []
})

const loadAdvertise = () => {
  request.get('/advertise/selectAll').then(res => {
    if (res.code === '200') {
      data.advertiseData = res.data
      // 做一下过滤，把六个广告位信息过滤出来
      let centerArr = res.data.filter(v => v.location === '中心大图')
      data.centerAd = centerArr && centerArr.length > 0 ? centerArr[0] : {}
      let leftArr = res.data.filter(v => v.location === '左侧大图')
      data.leftAd = leftArr && leftArr.length > 0 ? leftArr[0] : {}
      let rightArr = res.data.filter(v => v.location === '右侧大图')
      data.rightAd = rightArr && rightArr.length > 0 ? rightArr[0] : {}

      let centerDownArr = res.data.filter(v => v.location === '中心小图')
      data.centerDownAd = centerDownArr && centerDownArr.length > 0 ? centerDownArr[0] : {}
      let leftDownArr = res.data.filter(v => v.location === '左侧小图')
      data.leftDownAd = leftDownArr && leftDownArr.length > 0 ? leftDownArr[0] : {}
      let rightDownArr = res.data.filter(v => v.location === '右侧小图')
      data.rightDownAd = rightDownArr && rightDownArr.length > 0 ? rightDownArr[0] : {}
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadIndustry = () => {
  request.get('/industry/selectAll').then(res => {
    if (res.code === '200') {
      data.industryData = res.data
      data.activeName = data.industryData[0].id
      handleClick(data.activeName)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleClick = (industryId) => {
  request.get('/position/selectAll', {
    params: {
      industryId: industryId,
      status: '审核通过'
    }
  }).then(res => {
    if (res.code === '200') {
      data.positionData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const navTo = (url) => {
  location.href = url
}
const search = () => {
  location.href = '/front/search?name=' + data.name
}

loadAdvertise()
loadIndustry()
</script>