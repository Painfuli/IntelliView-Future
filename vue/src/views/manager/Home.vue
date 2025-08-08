<template>
  <div>
<!--    <div class="card" style="margin-bottom: 5px">您好！{{ data.user?.name }}，欢迎使用本系统！</div>-->
    <div style="display: flex">
      <div style="flex: 1; display: flex; align-items: center" class="card">
        <img src="@/assets/imgs/employ.jpg" alt="" style="width: 80px; height: 80px">
        <div style="margin-left: 20px">
          <div style="font-size: 20px">平台在招企业数量</div>
          <div style="font-size: 20px; margin-top: 10px">{{ data.employNum }}</div>
        </div>
      </div>
      <div style="flex: 1; margin: 0 10px; display: flex; align-items: center" class="card">
        <img src="@/assets/imgs/employee.jpg" alt="" style="width: 80px; height: 80px">
        <div style="margin-left: 20px">
          <div style="font-size: 20px">平台求职用户数量</div>
          <div style="font-size: 20px; margin-top: 10px">{{ data.employeeNum }}</div>
        </div>
      </div>
      <div style="flex: 1; display: flex; align-items: center" class="card">
        <img src="@/assets/imgs/positon.jpg" alt="" style="width: 80px; height: 80px">
        <div style="margin-left: 20px">
          <div style="font-size: 20px">平台在招岗位数量</div>
          <div style="font-size: 20px; margin-top: 10px">{{ data.positionNum }}</div>
        </div>
      </div>
      <div style="flex: 1; margin-left: 10px; display: flex; align-items: center" class="card">
        <img src="@/assets/imgs/submit.jpg" alt="" style="width: 80px; height: 80px">
        <div style="margin-left: 20px">
          <div style="font-size: 20px">平台岗位总投递数量</div>
          <div style="font-size: 20px; margin-top: 10px">{{ data.submitNum }}</div>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px; height: 350px" class="card" id="bar"></div>
    <div style="margin-top: 10px; height: 350px" class="card" id="pie"></div>
    <div style="margin-top: 10px; height: 350px" class="card" id="line"></div>
  </div>
</template>

<script setup>

import {reactive, onMounted} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import * as echarts from "echarts";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  employNum: 0,
  employeeNum: 0,
  positionNum: 0,
  submitNum: 0,
})

const loadBaseData = () => {
  request.get('/statistics/base').then(res => {
    if (res.code === '200') {
      data.employNum = res.data.employNum
      data.employeeNum = res.data.employeeNum
      data.positionNum = res.data.positionNum
      data.submitNum = res.data.submitNum
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadPieData = () => {
  // echarts.dispose(document.getElementById('pie'))
  request.get('/statistics/pie').then(res => {
    if (res.code === '200') {
      let chartDom = document.getElementById('pie')
      let myChart = echarts.init(chartDom)
      pieOptions.series[0].data = res.data
      myChart.setOption(pieOptions)
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadBarData = () => {
  request.get('/statistics/bar').then(res => {
    if (res.code === '200') {
      let chartDom = document.getElementById('bar')
      let myChart = echarts.init(chartDom)
      // 更新一下数据结构里面需要渲染的部分的数据（从后端接口返回过来的）
      barOptions.xAxis.data = res.data.xAxis
      barOptions.series[0].data = res.data.yAxis
      myChart.setOption(barOptions)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadLineData = () => {
  request.get('/statistics/line').then(res => {
    if (res.code === '200') {
      let chartDom = document.getElementById('line')
      let myChart = echarts.init(chartDom)
      // 更新一下数据结构里面需要渲染的部分的数据（从后端接口返回过来的）
      lineOptions.xAxis.data = res.data.xAxis
      lineOptions.series[0].data = res.data.yAxis
      myChart.setOption(lineOptions)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

onMounted(() => {
  loadBaseData()
  loadPieData()
  loadBarData()
  loadLineData()
})


// 饼图数据结构
let pieOptions = {
  title: {
    text: '职位投递热度统计', // 主标题
    subtext: '统计维度：职位名称', // 副标题
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '投递占比', // 鼠标移上去显示内容
      type: 'pie',
      radius: '50%',
      center: ['50%', '60%'],
      data: [
        {value: 1048, name: '瑞幸咖啡'}, // 示例数据：name表示维度，value表示对应的值
        {value: 735, name: '雀巢咖啡'},
        {value: 580, name: '星巴克咖啡'},
        {value: 484, name: '栖巢咖啡'},
        {value: 300, name: '小武哥咖啡'}
      ]
    }
  ]
}
// 柱状图数据结构
let barOptions = {
  title: {
    text: '不同行业在招职位数量统计', // 主标题
    subtext: '维度：行业分类', // 副标题
    left: 'center'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], // 示例数据：统计的维度（横坐标）
    axisLabel: {
      show: true, // 是否显示刻度标签，默认显示
      interval: 0, // 坐标轴刻度标签的显示间隔，在类目轴中有效；默认会采用标签不重叠的策略间隔显示标签；可以设置成0强制显示所有标签；如果设置为1，表示『隔一个标签显示一个标签』，如果值为2，表示隔两个标签显示一个标签，以此类推。
      rotate: -60, // 刻度标签旋转的角度，在类目轴的类目标签显示不下的时候可以通过旋转防止标签之间重叠；旋转的角度从-90度到90度
      inside: false, // 刻度标签是否朝内，默认朝外
      margin: 6, // 刻度标签与轴线之间的距离
    },
  },
  yAxis: {
    type: 'value'
  },
  tooltip: {
    trigger: 'item'
  },
  series: [
    {
      data: [120, 200, 150, 80, 70, 110, 130], // 示例数据：横坐标维度对应的值（纵坐标）
      type: 'bar',
      itemStyle: {
        normal: {
          color:function(){return "#"+Math.floor(Math.random()*(256*256*256-1)).toString(16);}
        },
      },
    }
  ]
}
// 平滑折线图数据结构
let lineOptions = {
  title: {
    text: '近三个月岗位投递走势图', // 主标题
    subtext: '统计维度：日期', // 副标题
    left: 'center'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], // 示例数据：统计的维度（横坐标）
  },
  yAxis: {
    type: 'value'
  },
  tooltip: {
    trigger: 'item'
  },
  series: [
    {
      data: [120, 200, 150, 80, 70, 110, 130], // 示例数据：横坐标维度对应的值（纵坐标）
      type: 'line',
      smooth: true
    }
  ]
}
</script>