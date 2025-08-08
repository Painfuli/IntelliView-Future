<template>
  <div style="min-height: 500px; width: 50%; margin: 20px auto">
    <div style="text-align: center; font-size: 20px">
      在线编辑您的简历
    </div>
    <div style="margin-top: 20px">
      <el-input style="width: 50%; padding-right: 5px" v-model="data.resumeData.name" placeholder="请输入简历名称"></el-input>
      <el-input style="width: 50%; padding-left: 5px" v-model="data.resumeData.username" placeholder="请输入姓名"></el-input>
    </div>
    <div style="margin-top: 10px">
      <el-select v-model="data.resumeData.sex" placeholder="请选择性别" style="width: 50%; padding-right: 5px">
        <el-option label="男" value="男"></el-option>
        <el-option label="女" value="女"></el-option>
      </el-select>
      <el-select v-model="data.resumeData.salary" placeholder="请选择你的期望薪资" style="width: 50%; padding-left: 5px">
        <el-option label="3k以下" value="3k以下"></el-option>
        <el-option label="3-5k" value="3-5k"></el-option>
        <el-option label="5-10k" value="5-10k"></el-option>
        <el-option label="10-20k" value="10-20k"></el-option>
        <el-option label="20-50k" value="20-50k"></el-option>
        <el-option label="50k以上" value="50k以上"></el-option>
      </el-select>
    </div>
    <div style="margin-top: 10px">
      <el-select v-model="data.resumeData.education" placeholder="请选择你的学历" style="width: 50%; padding-right: 5px">
        <el-option label="初中及以下" value="初中及以下"></el-option>
        <el-option label="中专/中技" value="中专/中技"></el-option>
        <el-option label="高中" value="高中"></el-option>
        <el-option label="大专" value="大专"></el-option>
        <el-option label="本科" value="本科"></el-option>
        <el-option label="硕士" value="硕士"></el-option>
        <el-option label="博士" value="博士"></el-option>
        <el-option label="博士后" value="博士后"></el-option>
      </el-select>
      <el-select v-model="data.resumeData.experience" placeholder="请选择你的工作年限" style="width: 50%; padding-left: 5px">
        <el-option label="在校生" value="在校生"></el-option>
        <el-option label="应届生" value="应届生"></el-option>
        <el-option label="1年以内" value="1年以内"></el-option>
        <el-option label="1到3年" value="1到3年"></el-option>
        <el-option label="3到5年" value="3到5年"></el-option>
        <el-option label="5到10年" value="5到10年"></el-option>
        <el-option label="10年以上" value="10年以上"></el-option>
      </el-select>
    </div>
    <div style="margin-top: 10px">
      <el-input style="width: 50%; padding-right: 5px" v-model="data.resumeData.phone" placeholder="请输入联系电话"></el-input>
      <el-input style="width: 50%; padding-left: 5px" v-model="data.resumeData.email" placeholder="请输入联系邮箱"></el-input>
    </div>
    <div style="margin-top: 20px">
      <el-button type="success" @click="addEduExp">添加教育经历</el-button>
    </div>
    <div style="margin-top: 10px; font-size: 16px" v-if="data.resumeData.eduExpList.length">
      <div style="font-weight: bold">教育经历：</div>
      <div style="margin-top: 10px; margin-bottom: 20px" v-for="item in data.resumeData.eduExpList">
        <div style="display: flex; align-items: center">
          <div style="flex: 1; color: #00bebd">{{ item.start }} ~ {{ item.end ? item.end : '至今' }}</div>
          <div style="flex: 1; color: #00bebd; font-weight: bold">{{ item.school }}</div>
          <div style="flex: 1; color: #00bebd; font-weight: bold">{{ item.speciality }}（{{ item.education }}）</div>
          <div style="display: flex; width: 30px">
            <el-icon style="color: #00bebd; cursor: pointer" @click="editEduExp(item)"><Edit /></el-icon>
            <el-icon style="color: red; cursor: pointer" @click="delEduExp(item.id)"><Delete /></el-icon>
          </div>
        </div>
        <div style="margin-top: 10px">主修课程：{{ item.course }}</div>
      </div>
      <div></div>
    </div>
    <div style="margin-top: 20px">
      <el-button type="success" @click="addWorkExp">添加工作经历</el-button>
    </div>
    <div style="margin-top: 10px; font-size: 16px" v-if="data.resumeData.workExpList.length">
      <div style="font-weight: bold">工作经历：</div>
      <div style="margin-top: 10px; margin-bottom: 20px" v-for="item in data.resumeData.workExpList">
        <div style="display: flex; align-items: center">
          <div style="flex: 1; color: #00bebd">{{ item.start }} ~ {{ item.end ? item.end : '至今' }}</div>
          <div style="flex: 1; color: #00bebd; font-weight: bold">{{ item.employ }}</div>
          <div style="flex: 1; color: #00bebd; font-weight: bold">{{ item.position }}（{{ item.type }}）</div>
          <div style="display: flex; width: 30px">
            <el-icon style="color: #00bebd; cursor: pointer" @click="editWorkExp(item)"><Edit /></el-icon>
            <el-icon style="color: red; cursor: pointer" @click="delWorkExp(item.id)"><Delete /></el-icon>
          </div>
        </div>
        <div style="margin-top: 10px"><strong>{{ item.project }}</strong>：{{ item.content }}</div>
      </div>
      <div></div>
    </div>
    <div style="margin-top: 20px">
      <el-button type="success" @click="addProExp">添加项目经验</el-button>
    </div>
    <div style="margin-top: 10px; font-size: 16px" v-if="data.resumeData.proExpList.length">
      <div style="font-weight: bold">工作经历：</div>
      <div style="margin-top: 10px; margin-bottom: 20px" v-for="item in data.resumeData.proExpList">
        <div style="display: flex; align-items: center">
          <div style="flex: 1; color: #00bebd">{{ item.start }} ~ {{ item.end ? item.end : '至今' }}</div>
          <div style="flex: 1; color: #00bebd; font-weight: bold">{{ item.name }}</div>
          <div style="display: flex; width: 30px">
            <el-icon style="color: #00bebd; cursor: pointer" @click="editProExp(item)"><Edit /></el-icon>
            <el-icon style="color: red; cursor: pointer" @click="delProExp(item.id)"><Delete /></el-icon>
          </div>
        </div>
        <div style="margin-top: 10px">项目介绍：{{ item.content }}</div>
      </div>
      <div></div>
    </div>


    <div style="margin-top: 50px; text-align: center">
      <el-button type="info" style="padding: 20px 30px" @click="saveResume">保存简历</el-button>
    </div>


    <el-dialog title="填写教育经历" v-model="data.eduFormVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.eduForm" label-width="70px" style="padding: 20px">
        <el-form-item prop="school" label="学校名称">
          <el-input v-model="data.eduForm.school" placeholder="请输入学校名称"></el-input>
        </el-form-item>
        <el-form-item prop="speciality" label="专业名称">
          <el-input v-model="data.eduForm.speciality" placeholder="请输入专业名称"></el-input>
        </el-form-item>
        <el-form-item prop="education" label="选择学历">
          <el-select v-model="data.eduForm.education" placeholder="请选择你的学历" style="width: 50%; padding-right: 5px; width: 100%">
            <el-option label="初中及以下" value="初中及以下"></el-option>
            <el-option label="中专/中技" value="中专/中技"></el-option>
            <el-option label="高中" value="高中"></el-option>
            <el-option label="大专" value="大专"></el-option>
            <el-option label="本科" value="本科"></el-option>
            <el-option label="硕士" value="硕士"></el-option>
            <el-option label="博士" value="博士"></el-option>
            <el-option label="博士后" value="博士后"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="start" label="入学时间">
          <el-date-picker style="width: 100%"
              v-model="data.eduForm.start"
              type="date"
              placeholder="请选择日期"
              value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item prop="end" label="毕业时间">
          <el-date-picker style="width: 100%"
              v-model="data.eduForm.end"
              type="date"
              placeholder="请选择日期"
              value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item prop="course" label="主修课程">
          <el-input type="textarea" :rows="4" v-model="data.eduForm.course" placeholder="请输入主修课程"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.eduFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveEduExp">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog title="填写工作经历" v-model="data.workFormVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.workForm" label-width="70px" style="padding: 20px">
        <el-form-item prop="employ" label="公司名称">
          <el-input v-model="data.workForm.employ" placeholder="请输入公司名称"></el-input>
        </el-form-item>
        <el-form-item prop="project" label="项目名称">
          <el-input v-model="data.workForm.project" placeholder="请输入项目名称"></el-input>
        </el-form-item>
        <el-form-item prop="position" label="职位名称">
          <el-input v-model="data.workForm.position" placeholder="请输入职位名称"></el-input>
        </el-form-item>
        <el-form-item prop="type" label="职位类型">
          <el-select v-model="data.workForm.type" placeholder="请选择职位类型" style="width: 50%; padding-right: 5px; width: 100%">
            <el-option label="全职" value="全职"></el-option>
            <el-option label="实习" value="实习"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="start" label="入职时间">
          <el-date-picker style="width: 100%"
                          v-model="data.workForm.start"
                          type="date"
                          placeholder="请选择日期"
                          value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item prop="end" label="离职时间">
          <el-date-picker style="width: 100%"
                          v-model="data.workForm.end"
                          type="date"
                          placeholder="请选择日期"
                          value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item prop="content" label="项目介绍">
          <el-input type="textarea" :rows="4" v-model="data.workForm.content" placeholder="请输入项目介绍"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.workFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveWorkExp">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog title="填写工项目经验" v-model="data.proFormVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.proForm" label-width="70px" style="padding: 20px">
        <el-form-item prop="name" label="项目名称">
          <el-input v-model="data.proForm.name" placeholder="请输入项目名称"></el-input>
        </el-form-item>
        <el-form-item prop="start" label="开始时间">
          <el-date-picker style="width: 100%"
                          v-model="data.proForm.start"
                          type="date"
                          placeholder="请选择日期"
                          value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item prop="end" label="结束时间">
          <el-date-picker style="width: 100%"
                          v-model="data.proForm.end"
                          type="date"
                          placeholder="请选择日期"
                          value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item prop="content" label="项目介绍">
          <el-input type="textarea" :rows="4" v-model="data.proForm.content" placeholder="请输入项目介绍"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.proFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveProExp">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  resumeId: router.currentRoute.value.query.id,
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  resumeData: {
    eduExpList: [],
    workExpList: [],
    proExpList: []
  },
  eduForm: {},
  eduFormVisible: false,

  workForm: {},
  workFormVisible: false,

  proForm: {},
  proFormVisible: false
})

const loadResume = () => {
  data.resumeId = router.currentRoute.value.query.id
  if (data.resumeId) {
    request.get('/resume/selectById/' + data.resumeId).then(res => {
      if (res.code === '200') {
        data.resumeData = res.data
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}

const saveResume = () => {
  if (data.resumeData.id) {
    // 更新
    request.put('/resume/update', data.resumeData).then(res => {
      if (res.code === '200') {
        ElMessage.success('保存成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  } else {
    // 新增新的简历
    data.resumeData.userId = data.user.id
    request.post('/resume/add', data.resumeData).then(res => {
      if (res.code === '200') {
        ElMessage.success('保存成功')
        setTimeout(() => {
          location.href = '/front/resume'
        }, 500)
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}

const addEduExp = () => {
  data.eduForm = {
    id: new Date().getTime() + Math.random().toString(36).substr(2)
  }
  data.eduFormVisible = true
}
const addWorkExp = () => {
  data.workForm = {
    id: new Date().getTime() + Math.random().toString(36).substr(2)
  }
  data.workFormVisible = true
}
const addProExp = () => {
  data.proForm = {
    id: new Date().getTime() + Math.random().toString(36).substr(2)
  }
  data.proFormVisible = true
}
const editEduExp = (item) => {
  data.eduForm = JSON.parse(JSON.stringify(item))
  data.eduFormVisible = true
}
const editWorkExp = (item) => {
  data.workForm = JSON.parse(JSON.stringify(item))
  data.workFormVisible = true
}
const editProExp = (item) => {
  data.proForm = JSON.parse(JSON.stringify(item))
  data.proFormVisible = true
}
const delEduExp = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    // 删除逻辑
    data.resumeData.eduExpList = data.resumeData.eduExpList.filter(v => v.id !== id)
  }).catch(err => {
    console.error(err)
  })
}
const delWorkExp = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    // 删除逻辑
    data.resumeData.workExpList = data.resumeData.workExpList.filter(v => v.id !== id)
  }).catch(err => {
    console.error(err)
  })
}
const delProExp = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    // 删除逻辑
    data.resumeData.proExpList = data.resumeData.proExpList.filter(v => v.id !== id)
  }).catch(err => {
    console.error(err)
  })
}
const saveEduExp = () => {
  let form = data.resumeData.eduExpList.filter(v => v.id === data.eduForm.id)
  if (form && form.length) {
    console.log('此时是更新')
    data.resumeData.eduExpList.forEach(item => {
      if (item.id === data.eduForm.id) {
        item.school = data.eduForm.school
        item.speciality = data.eduForm.speciality
        item.education = data.eduForm.education
        item.start = data.eduForm.start
        item.end = data.eduForm.end
        item.course = data.eduForm.course
      }
    })
  } else {
    console.log('此时是新增')
    data.resumeData.eduExpList.push(data.eduForm)
  }
  data.eduFormVisible = false
}
const saveWorkExp = () => {
  let form = data.resumeData.workExpList.filter(v => v.id === data.workForm.id)
  if (form && form.length) {
    console.log('此时是更新')
    data.resumeData.workExpList.forEach(item => {
      if (item.id === data.workForm.id) {
        item.employ = data.workForm.employ
        item.project = data.workForm.project
        item.position = data.workForm.position
        item.type = data.workForm.type
        item.start = data.workForm.start
        item.end = data.workForm.end
        item.content = data.workForm.content
      }
    })
  } else {
    console.log('此时是新增')
    data.resumeData.workExpList.push(data.workForm)
  }
  data.workFormVisible = false
}
const saveProExp = () => {
  let form = data.resumeData.proExpList.filter(v => v.id === data.proForm.id)
  if (form && form.length) {
    console.log('此时是更新')
    data.resumeData.proExpList.forEach(item => {
      if (item.id === data.proForm.id) {
        item.name = data.proForm.name
        item.start = data.proForm.start
        item.end = data.proForm.end
        item.content = data.proForm.content
      }
    })
  } else {
    console.log('此时是新增')
    data.resumeData.proExpList.push(data.proForm)
  }
  data.proFormVisible = false
}
loadResume()
</script>