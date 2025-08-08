<template>
  <div class="resume-container">
    <div class="resume-card">
      <h1 class="resume-name">{{ data.resumeData.name }}</h1>

      <div class="profile-section">
        <img :src="data.resumeData.userAvatar" alt="" class="profile-image">
        <div class="profile-details">
          <h2 class="username">{{ data.resumeData.username }}</h2>
          <div class="basic-info">
            <span>{{ data.resumeData.sex }}</span>
            <span class="divider">|</span>
            <span>{{ data.resumeData.education }}</span>
            <span class="divider">|</span>
            <span>{{ data.resumeData.experience }}</span>
          </div>
          <div class="contact-info">电话：{{ data.resumeData.phone }}</div>
          <div class="contact-info">邮箱：{{ data.resumeData.email }}</div>
          <div class="contact-info">期望薪资：{{ data.resumeData.salary }}</div>
        </div>
      </div>

      <section v-if="data.resumeData.eduExpList.length" class="resume-section">
        <h3 class="section-title">教育经历</h3>
        <div v-for="item in data.resumeData.eduExpList" class="experience-item">
          <div class="experience-header">
            <div class="time-period">{{ item.start }} ~ {{ item.end ? item.end : '至今' }}</div>
            <div class="institution">{{ item.school }}</div>
            <div class="position">{{ item.speciality }}（{{ item.education }}）</div>
          </div>
          <div class="experience-detail">主修课程：{{ item.course }}</div>
        </div>
      </section>

      <section v-if="data.resumeData.workExpList.length" class="resume-section">
        <h3 class="section-title">工作经历</h3>
        <div v-for="item in data.resumeData.workExpList" class="experience-item">
          <div class="experience-header">
            <div class="time-period">{{ item.start }} ~ {{ item.end ? item.end : '至今' }}</div>
            <div class="institution">{{ item.employ }}</div>
            <div class="position">{{ item.position }}（{{ item.type }}）</div>
          </div>
          <div class="experience-detail"><strong>{{ item.project }}</strong>：{{ item.content }}</div>
        </div>
      </section>

      <section v-if="data.resumeData.proExpList.length" class="resume-section">
        <h3 class="section-title">项目经验</h3>
        <div v-for="item in data.resumeData.proExpList" class="experience-item">
          <div class="experience-header">
            <div class="time-period">{{ item.start }} ~ {{ item.end ? item.end : '至今' }}</div>
            <div class="institution">{{ item.name }}</div>
          </div>
          <div class="experience-detail">项目介绍：{{ item.content }}</div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  resumeId: router.currentRoute.value.query.id,
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  resumeData: {
    eduExpList: [],
    workExpList: [],
    proExpList: []
  },
});

const loadResume = () => {
  data.resumeId = router.currentRoute.value.query.id;
  if (data.resumeId) {
    request.get('/resume/selectById/' + data.resumeId).then(res => {
      if (res.code === '200') {
        data.resumeData = res.data;
      } else {
        ElMessage.error(res.msg);
      }
    });
  }
};

loadResume();
</script>

<style scoped>
.resume-container {
  padding: 40px 0;
  background-color: #f7f7f7;
  min-height: 100vh;
}

.resume-card {
  width: 65%;
  max-width: 900px;
  padding: 40px;
  background-color: white;
  margin: 0 auto;
  border-radius: 6px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.resume-name {
  font-size: 26px;
  text-align: center;
  font-weight: bold;
  margin: 0 0 40px 0;
  color: #000;
}

.profile-section {
  display: flex;
  margin-bottom: 40px;
}

.profile-image {
  width: 140px;
  height: 140px;
  border-radius: 4px;
  object-fit: cover;
  border: 1px solid #eee;
}

.profile-details {
  margin-left: 40px;
  flex: 1;
}

.username {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 12px 0;
  color: #000;
}

.basic-info {
  margin-bottom: 16px;
  font-size: 15px;
  color: #333;
}

.divider {
  margin: 0 12px;
  color: #ccc;
}

.contact-info {
  margin-bottom: 8px;
  font-size: 15px;
  color: #333;
}

.resume-section {
  margin-bottom: 30px;
}

.section-title {
  font-size: 18px;
  font-weight: 600;
  padding: 10px 0;
  margin: 0 0 16px 0;
  color: #000;
  border-bottom: 2px solid #000;
}

.experience-item {
  margin-bottom: 24px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.experience-item:last-child {
  border-bottom: none;
}

.experience-header {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
}

.time-period {
  flex: 1;
  font-weight: 500;
  color: #555;
}

.institution {
  flex: 1;
  font-weight: 600;
  color: #000;
}

.position {
  flex: 1;
  font-weight: 600;
  color: #000;
}

.experience-detail {
  font-size: 15px;
  line-height: 1.6;
  color: #333;
}

@media (max-width: 768px) {
  .resume-card {
    width: 90%;
    padding: 20px;
  }

  .profile-section {
    flex-direction: column;
  }

  .profile-image {
    margin-bottom: 20px;
  }

  .profile-details {
    margin-left: 0;
  }

  .experience-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .time-period, .institution, .position {
    margin-bottom: 4px;
  }
}
</style>