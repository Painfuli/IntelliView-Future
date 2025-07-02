<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-select v-model="data.location" placeholder="请选择广告位置" style="width: 240px; margin-right: 10px">
        <el-option label="中心大图" value="中心大图"></el-option>
        <el-option label="中心小图" value="中心小图"></el-option>
        <el-option label="左侧大图" value="左侧大图"></el-option>
        <el-option label="左侧小图" value="左侧小图"></el-option>
        <el-option label="右侧大图" value="右侧大图"></el-option>
        <el-option label="右侧小图" value="右侧小图"></el-option>
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
        <el-table-column prop="img" label="广告主图">
          <template v-slot="scope">
            <el-image style="width: 60px; height: 40px; border-radius: 5px; display: block" v-if="scope.row.img"
                      :src="scope.row.img" :preview-src-list="[scope.row.img]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="positionName" label="推荐职位">
          <template v-slot="scope">
            {{ scope.row.employName }} / {{scope.row.positionName }} / {{scope.row.positionEducation }} / {{scope.row.positionSalary }}
<!--            {{ scope.row.positionName + ' / ' + scope.row.positionEducation + ' / ' + scope.row.positionSalary }}-->
          </template>
        </el-table-column>
        <el-table-column prop="location" label="广告位置" />
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

    <el-dialog title="广告位信息" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="75px" style="padding: 20px">
        <el-form-item prop="img" label="广告主图">
          <el-upload
              :action="baseUrl + '/files/upload'"
              :on-success="handleFileUpload"
              list-type="picture"
              >
            <el-button type="primary">上传主图</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item prop="positionId" label="推荐职位">
          <el-select v-model="data.form.positionId" placeholder="请选择职位">
            <el-option
                v-for="item in data.positionData"
                :key="item.id"
                :label="item.employName + ' / ' + item.name + ' / ' + item.education + ' / ' + item.salary"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="location" label="广告位置">
          <el-select v-model="data.form.location" placeholder="请选择广告位置">
            <el-option label="中心大图" value="中心大图"></el-option>
            <el-option label="中心小图" value="中心小图"></el-option>
            <el-option label="左侧大图" value="左侧大图"></el-option>
            <el-option label="左侧小图" value="左侧小图"></el-option>
            <el-option label="右侧大图" value="右侧大图"></el-option>
            <el-option label="右侧小图" value="右侧小图"></el-option>
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
  location: null,
  ids: [],
  positionData: []
})

const load = () => {
  request.get('/advertise/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      location: data.location,
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
  request.post('/advertise/add', data.form).then(res => {
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
  request.put('/advertise/update', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('操作成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
}


const delBatch = () => {
  if (!data.ids.length) {
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete("/advertise/delete/batch", {data: data.ids}).then(res => {
      if (res.code === '200') {
        ElMessage.success('操作成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}
const handleSelectionChange = (rows) => {
  data.ids = rows.map(v => v.id)
}

const handleFileUpload = (res) => {
  data.form.img = res.data
}

const reset = () => {
  data.location = null
  load()
}
const loadPosition = () => {
  request.get('/position/selectAll').then(res => {
    if (res.code === '200') {
      data.positionData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

load()
loadPosition()
</script>