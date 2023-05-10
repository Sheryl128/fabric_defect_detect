<template>
  <el-main class="main">
    <div style="display: flex;justify-content: center;margin-top: 50px">
      <el-upload
        class="upload-demo"
        ref="upload"
        :action='uploadApi'
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :file-list="fileList"
        :auto-upload="false"
        name='pic'>
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
        <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，分辨率不超过4096*4096，且不超过10mb</div>
      </el-upload>
    </div>
  </el-main>
</template>

<script>
    export default {
        name: "Main",
      data() {
          return {
            activeIndex: '1',
            session: sessionStorage.getItem('user'),
            uid: sessionStorage.getItem('uid'),
            fileList: [],
            uploadApi: ""
          }
        },
      methods: {
        submitUpload() {
          if(this.session === null){
            this.pleaseLogin();
          }else{
            this.uploadApi = '/picture/upload/' + this.uid
            this.$refs.upload.submit();
          }
        },
        pleaseLogin(){
          this.$message({
            message: '请登录！',
            type: 'warning'
          });
        },
        handleRemove(file, fileList) {
          console.log(file, fileList);
        },
        handlePreview(file) {
          console.log(file);
        }
      }
    }
</script>

<style scoped>
  .main{
    height: 100%;
    padding: 0;
  }
</style>
