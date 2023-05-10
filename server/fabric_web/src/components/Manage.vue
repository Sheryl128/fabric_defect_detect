<template>
  <div style="margin: 30px 0 0 100px; width: 80%;">
    <el-table :data="tableData">
<!--      <el-table-column prop="historyId" label="编号" ></el-table-column>-->
      <el-table-column prop="uId" label="用户编号"></el-table-column>
      <el-table-column prop="time" label="上传时间"></el-table-column>
      <el-table-column prop="fileName" label="文件名"></el-table-column>
      <el-table-column  label="操作">
        <template slot-scope="scope">
          <el-button @click="download(scope.row)" size="small" type="warning">下载</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div>
      <el-pagination
        @current-change="handleCurrentChange"
        background
        :current-page= currentPage
        :page-size="5"
        layout="total, prev, pager, next"
        :total= totalPage>
      </el-pagination>
    </div>
  </div>
</template>

<script>
import axios from "axios";

    export default {
      name: "Manage",
      mounted: function (){

        axios.post('/picture/show/'+sessionStorage.getItem('uid'),{
        }).then(response=>{
          const size = response.data.length;
          for (let i = 0; i < size; i++) {
            response.data[i].time = this.timeChange(response.data[i].time);
          }
          this.tableData = response.data;
        });
      },
      data() {
        return {
          tableData: [],
          userName: '',
          currentPage: 1,
          totalPage: undefined,
          history: {
            historyId: '',
            uId: '',
            time: undefined,
            inputPath: '',
            outputPath: '',
            fileName: ''
          },
        }
      },
      methods: {
        handleCurrentChange(val) {
          this.currentPage=val;
          axios.post('/order/searchOrder/' + this.currentPage,{
            userName: this.userName,
          }).then(response=>{
            this.tableData = response.data;
          })
        },
        timeChange(time){
          const date = new Date(time);
          const Y = date.getFullYear() + '-';
          const M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
          const D = date.getDate() + ' ';
          const h = date.getHours() + ':';
          const m = date.getMinutes();
          return (Y+M+D+h+m);
        },
        search(){
          axios.post('/order/querySearchPageNum',{
            userName: this.userName
          }).then(response=>{
            this.totalPage =  response.data;
          });
          axios.post('/order/searchOrder/1',{
            userName: this.userName
          }).then(response=>{
            this.tableData = response.data;
          })
        },

        download(row){
          axios.post('/picture/download/' + row.historyId,{},{responseType: 'blob'}).then(response=>{
            const blob = new Blob([response.data]);
            // 设置文件的名称
            const fileName = 'download.png';

            // document.createElement()是在对象中创建一个对象
            const elink = document.createElement('a');

            // js下载通过 download 属性下载 fileName 这个文件
            elink.download = fileName;
            // 样式 为 none
            elink.style.display = 'none';

            //可以获取当前文件的一个内存URL
            elink.href = URL.createObjectURL(blob);

            //body中添加一个子结点
            document.body.appendChild(elink);

            elink.click();

            // 释放URL 对象
            URL.revokeObjectURL(elink.href);

            // 删除指定的元素
            document.body.removeChild(elink);
          })
        }
      }
    }
</script>

<style scoped>

</style>
