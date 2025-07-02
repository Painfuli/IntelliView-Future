import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/front/home' },
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      children: [
        { path: 'home', meta: { name: '系统首页' }, component: () => import('@/views/manager/Home.vue'),  },
        { path: 'admin', meta: { name: '管理员信息' }, component: () => import('@/views/manager/Admin.vue'), },
        { path: 'notice', meta: { name: '系统公告' }, component: () => import('@/views/manager/Notice.vue'), },
        { path: 'person', meta: { name: '个人资料' }, component: () => import('@/views/manager/Person.vue'), },
        { path: 'ePerson', meta: { name: '企业资料' }, component: () => import('@/views/manager/EPerson.vue'), },
        { path: 'password', meta: { name: '修改密码' }, component: () => import('@/views/manager/Password.vue'), },
        { path: 'industry', meta: { name: '行业信息' }, component: () => import('@/views/manager/Industry.vue'), },
        { path: 'employ', meta: { name: '企业信息' }, component: () => import('@/views/manager/Employ.vue'), },
        { path: 'user', meta: { name: '用户信息' }, component: () => import('@/views/manager/User.vue'), },
        { path: 'position', meta: { name: '职位信息' }, component: () => import('@/views/manager/Position.vue'), },
        { path: 'advertise', meta: { name: '职位信息' }, component: () => import('@/views/manager/Advertise.vue'), },
        { path: 'submit', meta: { name: '岗位投递' }, component: () => import('@/views/manager/Submit.vue'), },
      ]
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      children: [
        { path: 'home', component: () => import('@/views/front/Home.vue'),  },
        { path: 'person', component: () => import('@/views/front/Person.vue'),  },
        { path: 'positionDetail', component: () => import('@/views/front/PositionDetail.vue'),  },
        { path: 'collect', component: () => import('@/views/front/Collect.vue'),  },
        { path: 'resume', component: () => import('@/views/front/Resume.vue'),  },
        { path: 'resumeEdit', component: () => import('@/views/front/ResumeEdit.vue'),  },
        { path: 'submit', component: () => import('@/views/front/Submit.vue'),  },
        { path: 'employ', component: () => import('@/views/front/Employ.vue'),  },
        { path: 'search', component: () => import('@/views/front/Search.vue'),  },
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') },
    { path: '/404', component: () => import('@/views/404.vue') },
    { path: '/resumeView', component: () => import('@/views/ResumeView.vue') },
    { path: '/:pathMatch(.*)', redirect: '/404' }
  ]
})

export default router
