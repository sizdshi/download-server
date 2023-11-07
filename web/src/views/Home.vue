<template>
<!--  <div>home page</div>-->
<!--  <p>{{state.msg}}</p>-->

  <Download
      style="width: 1000px"
      :fileName="fileName"
      :filesizeTotal="filesizeTotal"
      :filesizeCurrent="filesizeCurrent"
      :speed="speed"
      :remaining="remaining"
      :createTime="createTime"
      @start="startHandle"
      @pause="pauseHandle"
      @stop="stopHandle">
  </Download>
</template>

<script setup lang="ts">
// import Download from "../components/Download.vue"; // 导入 Download 组件

// export default {
//   components: {
//     Download, // 在这里注册 Download 组件
//   },
//   // 其他组件配置
// }
// import {reactive} from "vue";
// import myAxios from "../plugins/myAxios.ts";

// const state = reactive({
//   msg: {},
// });
//
// myAxios.get("/info?text="+"Hello").then((res)=>{
//   state.msg = res.data;
// });
// import {ref} from "vue";
// import Download from "./Download.vue";

import Download from "../components/Download.vue";
import {ref} from "vue";


const fileName = ref("window-test-iso.rar");
const filesizeTotal = ref("1.93 GB");
const filesizeCurrent = ref("198.38 MB");
const speed = ref("13.6 MB/s");
const remaining = ref("2m 11s");
const createTime = ref("1 minuts ago");
const startHandle = () => {
  console.log("startHandle")
};

const pauseHandle = () => {
  console.log("pauseHandle")
};

const stopHandle = () => {
  console.log("stopHandle")
};

</script>