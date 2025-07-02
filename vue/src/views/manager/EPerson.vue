<template>
  <div style="width: 50%" class="card">
    <el-form ref="user" :model="data.user" label-width="75px" style="padding: 20px">
      <el-form-item prop="avatar" label="企业logo">
        <el-upload
            :action="baseUrl + '/files/upload'"
            :on-success="handleFileUpload"
            :show-file-list="false"
            class="avatar-uploader"
        >
          <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item prop="username" label="企业账号">
        {{ data.user.username }}
      </el-form-item>
      <el-form-item prop="name" label="企业名称">
        <el-input v-model="data.user.name" placeholder="请输入企业名称"></el-input>
      </el-form-item>
      <el-form-item prop="city" label="所在城市">
        <el-input v-model="data.user.city" placeholder="请输入城市"></el-input>
      </el-form-item>
      <el-form-item prop="address" label="详细地址">
        <el-input v-model="data.user.address" placeholder="请输入详细地址"></el-input>
      </el-form-item>
      <el-form-item prop="industryId" label="所属行业">
        <el-select v-model="data.user.industryId" placeholder="请选择行业">
          <el-option
              v-for="item in data.industryData"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="scale" label="公司规模">
        <el-select v-model="data.user.scale" placeholder="请选择规模">
          <el-option label="0-20人" value="0-20人"></el-option>
          <el-option label="21-99人" value="21-99人"></el-option>
          <el-option label="100-499人" value="100-499人"></el-option>
          <el-option label="500-999人" value="500-999人"></el-option>
          <el-option label="1000-9999人" value="1000-9999人"></el-option>
          <el-option label="10000人以上" value="10000人以上"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="stage" label="融资阶段">
        <el-select v-model="data.user.stage" placeholder="请选择融资阶段">
          <el-option label="未融资" value="未融资"></el-option>
          <el-option label="天使轮" value="天使轮"></el-option>
          <el-option label="A轮" value="A轮"></el-option>
          <el-option label="B轮" value="B轮"></el-option>
          <el-option label="C轮" value="C轮"></el-option>
          <el-option label="D轮及以上" value="D轮及以上"></el-option>
          <el-option label="已上市" value="已上市"></el-option>
          <el-option label="不需要融资" value="不需要融资"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item prop="status" label="审核状态">
        <el-tag v-if="data.user.status === '审核通过'" type="success">{{ data.user.status }}</el-tag>
        <el-tag v-if="data.user.status === '审核不通过'" type="danger">{{ data.user.status }}</el-tag>
        <el-tag v-if="data.user.status === '待审核'" type="warning">{{ data.user.status }}</el-tag>
      </el-form-item>
      <div style="text-align: center">
        <el-button type="primary" @click="update">保 存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const baseUrl = import.meta.env.VITE_BASE_URL

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  industryData: []
})

const handleFileUpload = (res) => {
  data.user.avatar = res.data
}

const emit = defineEmits(['updateUser'])
const update = () => {
  if (data.user.role === 'EMPLOY') {
    ElMessageBox.confirm('更新企业信息后需要审核通过才可以发布岗位信息，您确定更新吗？', '更新确认', { type: 'warning' }).then(res => {
      data.user.status = '待审核'
      request.put('/employ/update', data.user).then(res => {
        if (res.code === '200') {
          ElMessage.success('保存成功')
          localStorage.setItem('xm-user', JSON.stringify(data.user))
          emit('updateUser')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }).catch(err => {
      console.error(err)
    })
  }
}

const loadEmploy = () => {
  request.get('/employ/selectById/' + data.user.id).then(res => {
    if (res.code === '200') {
      data.user = res.data
      localStorage.setItem('xm-user', JSON.stringify(data.user))
    }
  })
}
const loadIndustry = () => {
  request.get('/industry/selectAll').then(res => {
    if (res.code === '200') {
      data.industryData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

loadEmploy()
loadIndustry()
</script>

<style>
.avatar-uploader {
  height: 120px;
}
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>