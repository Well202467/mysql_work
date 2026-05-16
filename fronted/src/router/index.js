import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import MajorCatalogView from '../views/MajorCatalogView.vue'
import RelationMapView from '../views/RelationMapView.vue'
import ResourceLibraryView from '../views/ResourceLibraryView.vue'
import MajorView from '../views/MajorView.vue'
import AdminLogin from '../views/admin/AdminLogin.vue'
import AdminLayout from '../views/admin/AdminLayout.vue'
import AdminDashboard from '../views/admin/AdminDashboard.vue'
import AdminCourseManage from '../views/admin/AdminCourseManage.vue'

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
    path: '/map',
    name: 'map',
    component: RelationMapView,
    meta: { title: '学职图谱' },
  },
  {
    path: '/resources',
    name: 'resources',
    component: ResourceLibraryView,
    meta: { title: '博习斋' },
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
