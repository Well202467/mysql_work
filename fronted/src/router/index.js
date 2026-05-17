import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import MajorCatalogView from '../views/MajorCatalogView.vue'
import CareerCatalogView from '../views/CareerCatalogView.vue'
import CareerDetailView from '../views/CareerDetailView.vue'
import MajorView from '../views/MajorView.vue'
import AdminLogin from '../views/admin/AdminLogin.vue'
import AdminLayout from '../views/admin/AdminLayout.vue'
import AdminDashboard from '../views/admin/AdminDashboard.vue'
import AdminMajorManage from '../views/admin/AdminMajorManage.vue'
import AdminCourseManage from '../views/admin/AdminCourseManage.vue'
import AdminMajorCourseManage from '../views/admin/AdminMajorCourseManage.vue'
import AdminKnowledgePointManage from '../views/admin/AdminKnowledgePointManage.vue'
import AdminCareerManage from '../views/admin/AdminCareerManage.vue'
import AdminCareerProfileManage from '../views/admin/AdminCareerProfileManage.vue'
import AdminCareerAbilityManage from '../views/admin/AdminCareerAbilityManage.vue'
import AdminCareerCourseManage from '../views/admin/AdminCareerCourseManage.vue'
import AdminCareerKnowledgePointManage from '../views/admin/AdminCareerKnowledgePointManage.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: { title: '知点序章' },
  },
  {
    path: '/majors',
    name: 'majors',
    component: MajorCatalogView,
    meta: { title: '学科览要' },
  },
  {
    path: '/careers',
    name: 'careers',
    component: CareerCatalogView,
    meta: { title: '职业方向' },
  },
  {
    path: '/careers/:id',
    name: 'career-detail',
    component: CareerDetailView,
    meta: { title: '职业详情', navName: 'careers' },
  },
  {
    path: '/major/:id',
    name: 'major',
    component: MajorView,
    meta: { title: '学科详情' },
  },
  {
    path: '/admin/login',
    name: 'admin-login',
    component: AdminLogin,
    meta: { title: '管理员登录', adminPage: true },
  },
  {
    path: '/admin',
    component: AdminLayout,
    meta: { adminPage: true, requiresAdmin: true },
    children: [
      {
        path: '',
        redirect: { name: 'admin-dashboard' },
      },
      {
        path: 'dashboard',
        name: 'admin-dashboard',
        component: AdminDashboard,
        meta: { title: '管理员后台', adminPage: true, requiresAdmin: true },
      },
      {
        path: 'courses',
        name: 'admin-courses',
        component: AdminCourseManage,
        meta: { title: '课程管理', adminPage: true, requiresAdmin: true },
      },
      {
        path: 'majors',
        name: 'admin-majors',
        component: AdminMajorManage,
        meta: { title: '专业管理', adminPage: true, requiresAdmin: true },
      },
      {
        path: 'major-courses',
        name: 'admin-major-courses',
        component: AdminMajorCourseManage,
        meta: { title: '专业课程关系管理', adminPage: true, requiresAdmin: true },
      },
      {
        path: 'knowledge-points',
        name: 'admin-knowledge-points',
        component: AdminKnowledgePointManage,
        meta: { title: '知识点管理', adminPage: true, requiresAdmin: true },
      },
      {
        path: 'careers',
        name: 'admin-careers',
        component: AdminCareerManage,
        meta: { title: '职业管理', adminPage: true, requiresAdmin: true },
      },
      {
        path: 'career-profiles',
        name: 'admin-career-profiles',
        component: AdminCareerProfileManage,
        meta: { title: '职业画像管理', adminPage: true, requiresAdmin: true },
      },
      {
        path: 'career-abilities',
        name: 'admin-career-abilities',
        component: AdminCareerAbilityManage,
        meta: { title: '职业能力管理', adminPage: true, requiresAdmin: true },
      },
      {
        path: 'career-courses',
        name: 'admin-career-courses',
        component: AdminCareerCourseManage,
        meta: { title: '职业课程关系', adminPage: true, requiresAdmin: true },
      },
      {
        path: 'career-knowledge-points',
        name: 'admin-career-knowledge-points',
        component: AdminCareerKnowledgePointManage,
        meta: { title: '职业知识点关系', adminPage: true, requiresAdmin: true },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach((to) => {
  const token = localStorage.getItem('adminToken')
  if (to.name === 'admin-login' && token) {
    return { name: 'admin-dashboard' }
  }
  if (to.meta.requiresAdmin && !token) {
    return {
      name: 'admin-login',
      query: { redirect: to.fullPath },
    }
  }
  return true
})

export default router
