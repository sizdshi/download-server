import { createRouter, createWebHashHistory, RouteRecordRaw } from "vue-router";
import IndexPage from "../pages/IndexPage.vue";
import AboutView from "@/views/AboutView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: IndexPage,
  },
  {
    path: "/about",
    name: "about",
    component: AboutView,
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
