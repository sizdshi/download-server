<template>
    <div>
        <el-input class="input" v-model="input" placeholder="Please input" style="width: 60%; margin-right: 10px">
            <template #prepend>Http://</template>
        </el-input>
        <el-button color="#626aef" type="success" round>Success</el-button>
        <span></span>
    </div>
    <div style="display: flex; justify-content: space-between; margin-top: 5px">
        <div class="controlBox">
            <el-checkbox v-model="checked1" label="Option 1" size="large" />
            <el-select v-model="value" class="m-2" placeholder="Select">
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
        </div>
        <div>
            <el-icon :size="20" style="margin-right: 5px"><Close /></el-icon>
            <el-icon :size="20" style="margin-right: 5px"><RefreshRight /></el-icon>
            <el-icon v-show="!isShowControl" :size="20" style="margin-right: 5px" @click="pause"><VideoPause /></el-icon>
            <el-icon v-show="!isShowControl" :size="20" style="margin-right: 5px" @click="start"><VideoPlay /></el-icon>
        </div>
    </div>
</template>

<script setup>
import { ref } from "vue";

const checked1 = ref(true);

const value = ref("");

const options = [
    {
        value: "Option1",
        label: "Option1",
    },
    {
        value: "Option2",
        label: "Option2",
    },
    {
        value: "Option3",
        label: "Option3",
    },
    {
        value: "Option4",
        label: "Option4",
    },
    {
        value: "Option5",
        label: "Option5",
    },
];
</script>

<style scoped></style>
