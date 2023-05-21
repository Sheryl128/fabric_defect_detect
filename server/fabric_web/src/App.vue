<template>
  <div id="app">
    <el-container>
      <el-header style="padding: 0; margin-top: 20px;">
        <span style="font: 25px large;padding-left: 150px;padding-top: 30px;position: relative;">Fabric Defect Detect System</span>
        <div v-if="session === null"  style="display: inline">
          <span style="margin: 10px 0 0 900px;font: 15px small;">
            <a style="text-decoration: none; color: black;">
              <router-link to="/login">登录</router-link>
            </a> |
            <a  style="text-decoration: none; color: black;"><router-link to="/register">注册</router-link></a>
          </span>
        </div>
        <div v-else style="display: inline">
          <span style="margin: 10px 0 0 900px;font: 15px small;">欢迎，{{session}} | <a href="#" @click="logout">退出</a></span>
        </div>
      </el-header>
      <el-header style="padding: 0;">
        <el-menu :default-active="activeIndex" mode="horizontal" style="background-color: #409EFF;">
          <el-menu-item index="1" style="color: white;width: 150px;text-align: center;margin-left: 150px;" >
            <router-link to="/main">上传</router-link>
          </el-menu-item>
          <el-menu-item index="3" style="color: white;width: 150px;text-align: center;">
            <span v-if="session === null"  @click="pleaseLogin">下载</span>
            <router-link v-else to="/manage">下载</router-link>
          </el-menu-item>
        </el-menu>
      </el-header>
      <router-view></router-view>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      activeIndex: '1',
      session: sessionStorage.getItem('user'),
    }
  },
  methods: {
    logout(){
      sessionStorage.clear();
      this.$router.push({path: '/main'});
      location.reload();
    },
    pleaseLogin(){
      this.$message({
        message: '请登录！',
        type: 'warning'
      });
    }
  }
}
</script>

<style>
  body{
    margin: 0;
  }
</style>
