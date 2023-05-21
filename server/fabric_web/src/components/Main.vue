<template>
  <el-main class="main">
    <div style="display: flex;justify-content: center;margin-top: 50px">
      <el-image
        style="width: 500px; height: 200px"
        :src="urlUpload"
        :fit="fit"
        :preview-src-list="srcListUpload">
      </el-image>
    </div>
    <div style="display: flex;justify-content: center;margin-top: 20px">
      <el-upload
        class="upload-demo"
        ref="upload"
        :action='uploadApi'
        :file-list="fileList"
        multiple
        :limit="5"
        :on-exceed="handleExceed"
        :before-upload="beforeUpload"
        :auto-upload="true"
        name='pic'>
        <el-button slot="trigger" size="small" type="primary">选取文件并上传</el-button>
<!--        <el-button style="margin-left: 10px;text-align: center" size="small" type="success" @click="submitUpload">上传到服务器</el-button>-->
        <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，分辨率不超过4096*4096，且不超过10mb</div>
      </el-upload>
    </div>
    <div style="display: flex;justify-content: center;margin-top: 50px">
      <el-image
        style="width: 128px; height: 128px"
        :src="url"
        :preview-src-list="srcList">
      </el-image>
    </div>
    <div style="display: flex;justify-content: center;margin-top: 20px">
      <el-button slot="trigger" size="small" type="primary" @click="generate">生成</el-button>
      <el-button style="margin-left: 10px;" size="small" type="success" @click="test_detect">检测</el-button>
    </div>
  </el-main>
</template>

<script>
    import axios from "axios";

    export default {
        name: "Main",
      data() {
          return {
            activeIndex: '1',
            session: sessionStorage.getItem('user'),
            uid: sessionStorage.getItem('uid'),
            fileList: [],
            uploadApi: '/picture/upload/' + sessionStorage.getItem('uid'),
            urlUpload: '',
            srcListUpload: [],
            url: '',
            srcList: [],
            genId: undefined
          }
        },
      methods: {
        submitUpload() {
          if(this.session === null){
            this.pleaseLogin();
           }else{
             this.$refs.upload.submit();
           }
        },
        pleaseLogin(){
          this.$message({
            message: '请登录！',
            type: 'warning'
          });
        },
        generate(){
          this.srcList = []
          this.genId = 'gen' + Math.floor(Math.random() * 100) + Math.floor(Math.random() * 1000)
          axios.post('/picture/generate/' + this.uid, {
            fileName: this.genId
          }, {
            responseType: 'blob'
          }).then(response => {
            // const blob = new Blob([response.data]);
            const fileReader = new FileReader()
            fileReader.readAsDataURL(response.data)
            fileReader.onload = e => {
              this.url = e.target.result
            }
          })
        },
        test_detect(){
          if(this.genId === undefined){
            alert("请先生成")
          }else{
            let filename = this.genId
            alert("提交成功，请耐心等待")
            axios.post('/picture/testdetect', {
              fileName: filename
            }, {
              responseType: 'blob'
            }).then(response => {
              // const blob = new Blob([response.data]);
              const fileReader = new FileReader()
              fileReader.readAsDataURL(response.data)
              fileReader.onload = e => {
                this.srcList.push(e.target.result)
              }
            })
          }
        },
        handleRemove(file, fileList) {

        },
        handleExceed(files, fileList) {
          this.$message.warning(`当前限制选择 5 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
        },
        beforeUpload(file){
          if(this.session === null){
              this.fileList = []
              this.pleaseLogin()
          }
          else{
              const fileReader = new FileReader()
              fileReader.readAsDataURL(file)
              fileReader.onload = e => {
              this.urlUpload = e.target.result
              this.srcListUpload.push(e.target.result)
            }
          }
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
