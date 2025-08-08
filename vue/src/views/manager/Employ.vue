<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入企业名称查询"></el-input>
      <el-select v-model="data.status" placeholder="请选择审核状态" style="width: 240px; margin-right: 10px">
        <el-option label="待审核" value="待审核"></el-option>
        <el-option label="审核通过" value="审核通过"></el-option>
        <el-option label="审核不通过" value="审核不通过"></el-option>
      </el-select>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="username" label="企业账号" />
        <el-table-column prop="name" label="企业名称" />
        <el-table-column prop="avatar" label="企业logo">
          <template v-slot="scope">
            <el-image style="width: 40px; height: 40px; border-radius: 50%; display: block" v-if="scope.row.avatar"
                      :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="city" label="所在城市" />
        <el-table-column prop="address" label="详细地址" show-overflow-tooltip />
        <el-table-column prop="industryName" label="所属行业" />
        <el-table-column prop="scale" label="公司规模" />
        <el-table-column prop="stage" label="融资阶段" />
        <el-table-column prop="status" label="审核状态">
          <template v-slot="scope">
            <el-tag v-if="scope.row.status === '待审核'" type="warning">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '审核通过'" type="success">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '审核不通过'" type="danger">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="企业信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="75px" style="padding: 20px">
        <el-form-item prop="username" label="企业账号">
          <el-input v-model="data.form.username" placeholder="请输入企业账号"></el-input>
        </el-form-item>
        <el-form-item prop="name" label="企业名称">
          <el-input v-model="data.form.name" placeholder="请输入企业名称"></el-input>
        </el-form-item>
        <el-form-item prop="avatar" label="企业logo">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :on-success="handleFileUpload"
              list-type="picture"
              >
            <el-button type="primary">上传logo</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="city" label="所在城市">
          <el-input v-model="data.form.city" placeholder="请输入城市"></el-input>
        </el-form-item>
        <el-form-item prop="address" label="详细地址">
          <el-input v-model="data.form.address" placeholder="请输入详细地址"></el-input>
        </el-form-item>
        <el-form-item prop="industryId" label="所属行业">
          <el-select v-model="data.form.industryId" placeholder="请选择行业">
            <el-option
                v-for="item in data.industryData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="scale" label="公司规模">
          <el-select v-model="data.form.scale" placeholder="请选择规模">
            <el-option label="0-20人" value="0-20人"></el-option>
            <el-option label="21-99人" value="21-99人"></el-option>
            <el-option label="100-499人" value="100-499人"></el-option>
            <el-option label="500-999人" value="500-999人"></el-option>
            <el-option label="1000-9999人" value="1000-9999人"></el-option>
            <el-option label="10000人以上" value="10000人以上"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="stage" label="融资阶段">
          <el-select v-model="data.form.stage" placeholder="请选择融资阶段">
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
          <el-select v-model="data.form.status" placeholder="请选择审核状态">
            <el-option label="待审核" value="待审核"></el-option>
            <el-option label="审核通过" value="审核通过"></el-option>
            <el-option label="审核不通过" value="审核不通过"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

const baseUrl = import.meta.env.VITE_BASE_URL

const data = reactive({
  formVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 10,
  total: 0,
  name: null,
  status: null,
  ids: [],
  industryData: []
})

const load = () => {
  request.get('/employ/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      status: data.status
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data?.list || []
      data.total = res.data?.total
    }
  })
}
const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}
const add = () => {
  request.post('/employ/add', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put('/employ/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
}

const del = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/employ/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}
const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("请选择数据")
    return
  }
}
const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
}

const handleFileUpload = (res) => {
  data.form.avatar = res.data
}

const reset = () => {
  data.name = null
  data.status = null
  load()
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

load()
loadIndustry()
</script>