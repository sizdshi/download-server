<template>
  <div>home page</div>
  <p>{{state.msg}}</p>
</template>

<script setup lang="ts">

import {reactive} from "vue";
import myAxios from "../plugins/myAxios.ts";

const state = reactive({
  msg: {},
});

myAxios.get("/info?text="+"Hello").then((res)=>{
  state.msg = res.data;
});

</script>