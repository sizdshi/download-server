<template>
    <el-card>
        <div class="container">
            <div class="left-box">
                <div class="protocol">HTTP</div>
            </div>
            <div class="right-box">
                <div class="titleBar">
                    <div>
                        {{ fileName }} <el-icon style="top: 3px"><Link /></el-icon>
                    </div>
                    <div>
                        <el-icon v-show="isShowControl" :size="20" style="margin-right: 5px" @click="pause"><VideoPause /></el-icon>
                        <el-icon v-show="!isShowControl" :size="20" style="margin-right: 5px" @click="start"><VideoPlay /></el-icon>
                        <el-icon :size="20" @click="stop"><DeleteFilled /></el-icon>
                    </div>
                </div>
                <el-progress :percentage="50" :show-text="false" :striped="true" :striped-flow="true" :duration="10" style="margin-top: 6px" />
                <div class="statusBar">
                    <div>{{ fileSizeCurrent }}/{{ fileSizeTotal }}</div>
                    <div>
                        <el-icon style="color: #58b271; top: 2px"><SortDown /></el-icon>{{ speed }}
                    </div>
                    <div>剩余时间 {{ remaining }} {{ createTime }}</div>
                </div>
            </div>
        </div>
    </el-card>
</template>

<script lang="ts" setup>
import { ref, toRefs, defineProps, defineEmits } from "vue";
const isShowControl = ref(true);

// 数据注入
const props = defineProps({
    fileName: String,
    fileSizeTotal: String,
    fileSizeCurrent: String,
    speed: String,
    remaining: String,
    createTime: String,
});

const { fileName, fileSizeTotal, fileSizeCurrent, speed, remaining, createTime } = toRefs(props);

// 事件触发
const emit = defineEmits(["start", "pause", "stop"]);
const start = () => {
    isShowControl.value = true;
    emit("start");
};

const pause = () => {
    isShowControl.value = false;
    emit("pause");
};

const stop = () => {
    emit("stop");
};
</script>

<style scoped>
.container {
    display: flex;
}

.left-box {
    margin-right: 15px;
}

.protocol {
    font-weight: 600;
    color: #58b271;
    margin-top: 25px;
}

.right-box {
    flex: 1;
    text-align: left;
}

.titleBar {
    display: flex;
    justify-content: space-between;
    color: #737272;
}

.statusBar {
    display: flex;
    justify-content: space-between;
    margin-top: 5px;
    font-weight: bold;
    color: #505050;
}
</style>
