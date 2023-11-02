<template>
  <div class="about">
    <h1>This is an about page</h1>
    <p>{{ state.msg }}</p>
  </div>
</template>
<script setup lang="ts">
import myAxios from "@/plugins/myAxios";
import { reactive } from "vue";

const state = reactive({
  msg: {},
});
myAxios.get("/info?text=" + "Hello").then((res) => {
  state.msg = res.data;
});
</script>
