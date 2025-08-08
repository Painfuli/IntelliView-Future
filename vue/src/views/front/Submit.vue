<template>
  <div style="min-height: 500px; margin: 20px auto; width: 80%; ">
    <div style="font-size: 18px">我投递的简历（{{ data.tableData.length }}）</div>
    <div class="card" style="margin-bottom: 5px; margin-top: 20px">
      <el-table stripe :data="data.tableData">
        <el-table-column prop="employName" label="企业名称" />
        <el-table-column prop="positionName" label="岗位名称">
          <template v-slot="scope">
            <a :href="'/front/positionDetail?id=' + scope.row.positionId">{{ scope.row.positionName }}</a>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="投递用户" />
        <el-table-column prop="resumeId" label="投递简历">
          <template v-slot="scope">
            <a :href="'/resumeView?id=' + scope.row.resumeId" target="_blank">简历预览</a>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="投递时间" />
        <el-table-column prop="status" label="投递状态">
          <template v-slot="scope">
            <el-tag v-if="scope.row.status === '不适合'" type="danger">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '面试中'" type="primary">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '通过'" type="success">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '不通过'" type="warning">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '已投递'" type="info">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template v-slot="scope">
            <el-button type="danger" :disabled="scope.row.status !== '已投递'" @click="cancel(scope.row.id)">取消投递</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="loadSubmit" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from "@element-plus/icons-vue";

const data = reactive({
  tableData: [],
  pageNum: 1,
  pageSize: 5,
  total: 0,
})

const loadSubmit = () => {
  request.get('/submit/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
    }
  })
}

const cancel = (id) => {
  ElMessageBox.confirm('现在工作不好找，您确定取消投递吗？', '删除取消', { type: 'warning' }).then(res => {
    request.delete('/submit/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success("取消成功")
        loadSubmit()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {
    console.error(err)
  })
}

loadSubmit()
</script>