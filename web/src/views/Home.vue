<template>
  <!--  <div>home page</div>-->
  <!--  <p>{{state.msg}}</p>-->
  <div class="common-layout">
    <el-container class="layout-container-demo" style="height: 500px">
      <el-aside width="200px">Aside
      {{ state.msg }}
      </el-aside>
      <el-container>
        <el-header>Header
          <el-input  class="input" v-model="input" placeholder="Please input">
            <template #prepend>Http://</template>
          </el-input>
          <el-button color="#626aef"  type="success" round>Success</el-button>
          <el-icon :size="20" style="margin-right: 5px"><Close /></el-icon>
          <el-icon :size="20" style="margin-right: 5px"><RefreshRight /></el-icon>
          <el-icon v-show="!isShowControl" :size="20" style="margin-right: 5px" @click="pause"><VideoPause /></el-icon>
          <el-icon v-show="!isShowControl" :size="20" style="margin-right: 5px" @click="start"><VideoPlay /></el-icon>
        </el-header>
        <el-main>Main
          <Download
              style="width: 930px;margin-left: 60px;margin-top: 16px"
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
        </el-main>
      </el-container>
    </el-container>
  </div>




</template>

<script setup lang="ts">
//
// myAxios.get("/info?text="+"Hello").then((res)=>{
//   state.msg = res.data;
// });
// import {ref} from "vue";
// import Download from "./Download.vue";
import Download from "../components/Download.vue";
import {ref} from "vue";
import myAxios from "../plugins/myAxios.ts";
import {reactive} from "vue";


const fileName = ref("window-test-iso.rar");
const filesizeTotal = ref("1.93 GB");
const filesizeCurrent = ref("198.38 MB");
const speed = ref("13.6 MB/s");
const remaining = ref("2m 11s");
const createTime = ref("1 minuts ago");
const input = ref('')
const state = reactive({
  msg: {},
});
const startData = {
  id: "1705234447153963010"
};
const requestData = new URLSearchParams();
requestData.append('id', '1705234447153963010');

const startHandle = () => {
  myAxios.post("/task/start",requestData).then(response => {
    // 请求成功时的处理
    state.msg = response.data;
    console.log('POST请求成功', response.data);
  })
      .catch(error => {
        // 请求失败时的处理
        console.error('POST请求失败', error);
      });
  console.log("startHandle")
};

const pauseHandle = () => {
  console.log("pauseHandle")
};

const stopHandle = () => {
  console.log("stopHandle")
};

</script>

<style scoped>
.common-layout{
  width: 1200px;
  height: 980px;
}
.layout-container-demo .el-header {
  position: relative;
  background-color: var(--el-color-primary-light-7);
  height:230px;
  width: 1000px;
  color: var(--el-text-color-primary);
}
.layout-container-demo .el-aside {
  width: 200px;
  height: 560px;
  color: var(--el-text-color-primary);
  background: var(--el-color-primary-light-8);
}
.layout-container-demo .el-menu {
  border-right: none;

}
.layout-container-demo .el-main {
  padding: 0;
  height: 750px;
  width: 1000px;
  color: var(--el-text-color-primary);
  background: var(--el-color-primary-light-9);
}
.layout-container-demo .toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
.input{
  width: 437px;
  height: 30px;
}


</style>