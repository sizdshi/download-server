<script setup lang="ts">

</script>

<template>

    <h1>Settings</h1>
    <form th:action="@{/settings}" th:object="${setting}" method="post">
      <label for="storePath">Download Path</label><br>
      <input type="text" id="storePath" th:field="*{storePath}" value="/data/downloads"><br>

      <label for="maxTasks">Max Tasks</label><br>
      <input type="number" id="maxTasks" th:field="*{maxTasks}" min="0" value="4"><br>

      <label for="maxDownloadSpeed">Max Download Speed</label><br>
      <input type="number" id="maxDownloadSpeed" th:field="*{maxDownloadSpeed}" min="0" value="1"><a>MB/S</a><br>

      <label for="maxUploadSpeed">Max Upload Speed</label><br>
      <input type="number" id="maxUploadSpeed" th:field="*{maxUploadSpeed}" min="0" value="1"><a>MB/S</a><br>

      <button>Save settings</button>
    </form>

</template>

<style scoped>
.setting-container{
  position: relative;
  left: 250px;

}
.setting-container input{
  height: 30px;
  font-size: 15px;

  border-radius: 10px;
  margin-top: 5px;
  margin-bottom: 15px;
  width: 50px;
}
#storePath{
  width: 500px;
}
.setting-container button{
  border-radius: 10px;
  font-size: 20px;
  color:aliceblue;
  height: 40px;
  background-color: rgb(187, 142, 229);
}
</style>