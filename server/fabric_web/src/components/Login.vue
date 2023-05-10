<template>
  <el-main>
    <div style="display: flex;justify-content: center;margin-top: 50px">
      <el-card style="width: 400px">
        <div slot="header">
          <span>登录</span>
        </div>
        <div>
          <p>
            用户名:&nbsp;<el-input v-model="user.userName" style="width: 250px" placeholder="请输入用户名"></el-input>
          </p>
          <p>
            密  &nbsp;&nbsp;码:&nbsp;<el-input v-model="user.pwd" type="password" style="width: 250px" placeholder="请输入密码"></el-input>
          </p>
          <p><el-button style="width: 350px" type="primary" @click="login">登录</el-button></p>
          <br/>
<!--          <span @click="diaVis=true">忘记密码</span>-->
        </div>
      </el-card>
    </div>


<!--    <el-dialog title="忘记密码" :visible.sync="diaVis" width="50%">-->
<!--      <div>-->
<!--        <p>用户名:&nbsp;<el-input v-model="changePwd.userName" placeholder="用户名" style="width: 200px;"></el-input></p>-->
<!--        <p>身份证:&nbsp;<el-input v-model="changePwd.identityNumber" placeholder="请输入身份证号" style="width: 200px;"></el-input></p>-->
<!--        <p>新密码:&nbsp;<el-input v-model="changePwd.newPwd1" type="password" placeholder="请输入新密码" style="width: 200px;"></el-input></p>-->
<!--        <p>新密码:&nbsp;<el-input v-model="changePwd.newPwd2" type="password" placeholder="请再输入新密码" style="width: 200px;"></el-input></p>-->
<!--        <p> <el-button @click="forgetPwd" style="width: 350px" type="primary">修改</el-button></p>-->
<!--      </div>-->
<!--    </el-dialog>-->
  </el-main>
</template>

<script>
    import axios from 'axios'
    export default {
      name: "Login",
      data(){
        return{
          diaVis: false,
          user:{
            userName: '',
            pwd: ''
          },
          changePwd: {
            userName: '',
            identityNumber: '',
            newPwd1: '',
            newPwd2: ''
          }
        }
      },
      methods:{
        login(){
          let _this = this;
          if(this.user.userName === '' || this.user.pwd === ''){
            this.$message({
              message: '用户名或密码为空',
              type: 'warning'
            });
          }
          else{
            axios.defaults.withCredentials = true;
            axios.post('/user/login',{
              userName: this.user.userName,
              pwd: this.user.pwd
            })
              .then(response=>{
                if(response.data.status === 200){
                  sessionStorage.setItem("user",this.user.userName);
                  sessionStorage.setItem("uid",response.data.msg);
                  this.$router.push({path: '/main'});
                  location.reload();
                } else{
                  this.$message({
                    message: '用户名或密码不正确',
                    type: 'warning'
                  });
                }
              })
          }
        },

        forgetPwd(){
          if(this.changePwd.newPwd1 !== this.changePwd.newPwd2){
            this.$message({
              message: '两次新密码不一致！',
              type: 'warning'
            });
          }
          else{
            axios.post('/user/forgetPwd',{
              userName: this.changePwd.userName,
              pwd: this.changePwd.identityNumber
            }).then(response=>{
              if(response.data.status === 500){
                this.$message({
                  message: response.data.msg,
                  type: 'warning'
                });
              }
              else{
                this.$message({
                  message: response.data.msg,
                  type: 'success'
                });

                axios.post('/user/changePwd',{
                  userName: this.changePwd.userName,
                  pwd: this.changePwd.newPwd1
                }).then(response1=>{
                  if(response1.data.status === 200){
                    this.$message({
                      message: '修改成功!',
                      type: 'success'
                    });
                  }
                  else{
                    this.$message({
                      message: response1.data.msg,
                      type: 'warning'
                    });
                  }
                })

              }
            })
          }
        },


      }
    }
</script>

<style scoped>

</style>
