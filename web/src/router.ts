import { createRouter, createWebHistory } from 'vue-router'
import HomeView from './views/Home.vue'
import About from './views/About.vue'
import Setting from './views/Setting.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'About',
    component: About
  }, {
    path: '/setting',
    name: 'Setting',
    component: Setting
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
