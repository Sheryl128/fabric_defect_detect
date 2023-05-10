<template>
  <el-main>
    <div style="display: flex;justify-content: center;margin-top: 50px">
      <el-card style="width: 400px">
        <div slot="header">
          <span>注册</span>
        </div>
        <div>
          <p>
            用户名:&nbsp;<el-input v-model="user.userName" style="width: 250px" placeholder="请输入用户名"></el-input>
          </p>
          <p>
            密  &nbsp;码:&nbsp;&nbsp;<el-input v-model="user.pwd1" type="password" style="width: 250px" placeholder="请输入密码"></el-input>
          </p>
          <p>
            密  &nbsp;码:&nbsp;&nbsp;<el-input v-model="user.pwd2" type="password" style="width: 250px" placeholder="请再输入密码"></el-input>
          </p>
          <p><el-button style="width: 350px" type="primary" @click="register">注册</el-button></p>
        </div>
      </el-card>
    </div>
  </el-main>
</template>

<script>
import axios from "axios";

export default {
  name: "Register",
  data() {
    return {
      user: {
        userName: '',
        pwd1: '',
        pwd2: ''
      }
    }
  },
  methods: {
    register() {
      if (this.user.userName === '' || this.user.pwd1 === '' || this.user.pwd2 === '') {
        this.$message({
          message: '用户名或密码为空',
          type: 'warning'
        });
      } else if (this.user.pwd1 !== this.user.pwd2) {
        this.$message({
          message: '两次密码不一致',
          type: 'warning'
        });
      } else {
        axios.post('/user/register', {
          uId: '',
          userName: this.user.userName,
          pwd: this.user.pwd1
        }).then(response => {
          if (response.data.status === 200) {
            this.$message({
              message: '注册成功',
              type: 'success'
            });
          } else {
            this.$message({
              message: response.data.msg,
              type: 'warning'
            });
          }
        })
      }
    }
  }
}
</script>

<style scoped>

</style>
