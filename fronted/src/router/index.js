import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import MajorCatalogView from '../views/MajorCatalogView.vue'
import RelationMapView from '../views/RelationMapView.vue'
import ResourceLibraryView from '../views/ResourceLibraryView.vue'
import MajorView from '../views/MajorView.vue'

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
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
