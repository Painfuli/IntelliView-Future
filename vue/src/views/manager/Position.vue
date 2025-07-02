<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入职位名称查询"></el-input>
      <el-input v-if="data.user.role === 'ADMIN'" v-model="data.employName" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入公司名称查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px" v-if="data.user.role === 'EMPLOY'">
      <el-button type="primary" plain @click="handleAdd">发布职位</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="职位名称" />
        <el-table-column prop="employName" label="招聘企业" />
        <el-table-column prop="industryName" label="所属行业" />
        <el-table-column prop="type" label="求职类型" />
        <el-table-column prop="experience" label="工作经验" />
        <el-table-column prop="salary" label="薪资待遇" />
        <el-table-column prop="education" label="学历要求" />
        <el-table-column prop="content" label="职位描述" width="100">
          <template v-slot="scope">
            <el-button type="primary" @click="showContent(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="tags" label="职位标签" show-overflow-tooltip/>
        <el-table-column prop="status" label="职位状态">
          <template v-slot="scope">
            <el-tag v-if="scope.row.status === '审核通过'" type="success">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '审核不通过'" type="danger">{{ scope.row.status }}</el-tag>
            <el-tag v-if="scope.row.status === '待审核'" type="warning">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template v-slot="scope">
            <el-button v-if="data.user.role === 'EMPLOY'" type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button v-if="data.user.role === 'ADMIN'" type="primary" circle :icon="Check" @click="changeStatus(scope.row, '审核通过')"></el-button>
            <el-button v-if="data.user.role === 'ADMIN'" type="primary" circle :icon="Close" @click="changeStatus(scope.row, '审核不通过')"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>

    <el-dialog title="职位信息" v-model="data.formVisible" width="60%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding: 20px">
        <el-form-item prop="name" label="职位名称">
          <el-input v-model="data.form.name" placeholder="请输入职位名称题"></el-input>
        </el-form-item>
        <el-form-item prop="type" label="求职类型">
          <el-select v-model="data.form.type" placeholder="请选择求职类型">
            <el-option label="全职" value="全职"></el-option>
            <el-option label="兼职" value="兼职"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="experience" label="工作经验">
          <el-select v-model="data.form.experience" placeholder="请选择工作经验型">
            <el-option label="在校生" value="在校生"></el-option>
            <el-option label="应届生" value="应届生"></el-option>
            <el-option label="经验不限" value="经验不限"></el-option>
            <el-option label="1年以内" value="1年以内"></el-option>
            <el-option label="1到3年" value="1到3年"></el-option>
            <el-option label="3到5年" value="3到5年"></el-option>
            <el-option label="5到10年" value="5到10年"></el-option>
            <el-option label="10年以上" value="10年以上"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="salary" label="薪资待遇">
          <el-select v-model="data.form.salary" placeholder="请选择薪资待遇">
            <el-option label="3k以下" value="3k以下"></el-option>
            <el-option label="3-5k" value="3-5k"></el-option>
            <el-option label="5-10k" value="5-10k"></el-option>
            <el-option label="10-20k" value="10-20k"></el-option>
            <el-option label="20-50k" value="20-50k"></el-option>
            <el-option label="50k以上" value="50k以上"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="education" label="学历要求">
          <el-select v-model="data.form.education" placeholder="请选择学历要求">
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
        <el-form-item prop="tags" label="职位标签">
          <el-input v-model="data.form.tags" placeholder="请输入职位标签，多个标签请用英文逗号隔开"></el-input>
        </el-form-item>
        <el-form-item prop="content" label="职位描述">
          <div style="border: 1px solid #ccc; width: 100%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
            />
            <Editor
                style="height: 500px; overflow-y: hidden;"
                v-model="data.form.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog title="职位信息" v-model="data.editorVisible" width="60%" destroy-on-close>
      <div style="padding: 0 20px" v-html="data.viewContent"></div>
    </el-dialog>
  </div>
</template>

<script setup>

import {onBeforeUnmount, reactive, ref, shallowRef} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import {Check, Close, Delete, Edit} from "@element-plus/icons-vue";
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible: false,
  editorVisible: false,
  form: {},
  tableData: [],
  pageNum: 1,
  pageSize: 5,
  total: 0,
  name: null,
  employName: null,
  ids: [],
  viewContent: null
})

/* wangEditor5 初始化开始 */
const editorRef = shallowRef()  // 编辑器实例，必须用 shallowRef
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
// 图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  server: 'http://localhost:9090/files/wang/upload',  // 服务端图片上传接口
  fieldName: 'file'  // 服务端图片上传接口参数
}
// 组件销毁时，也及时销毁编辑器，否则可能会造成内存泄漏
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
// 记录 editor 实例，重要！
const handleCreated = (editor) => {
  editorRef.value = editor
}
/* wangEditor5 初始化结束 */

const showContent = (content) => {
  data.viewContent = content
  data.editorVisible = true
}

const changeStatus = (row, status) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.form.status = status
  save()
}

const load = () => {
  request.get('/position/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      employName: data.employName
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
  request.post('/position/add', data.form).then(res => {
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
  request.put('/position/update', data.form).then(res => {
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
    request.delete('/position/delete/' + id).then(res => {
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
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete("/position/delete/batch", {data: data.ids}).then(res => {
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

const reset = () => {
  data.name = null
  data.employName = null
  load()
}

load()
</script>
