<template>
  <div style="color: #666;font-size: 14px;">
    <div style="padding-bottom: 20px">
      <b>欢迎你！{{ user.nickname }}</b>
    </div>
    <span id="msg" style="font-weight: bold;
  font-size: 3rem;
  color: #4a73a3;
position: center"
    >{{ msg }}</span>
    <div style="height: 1px; background: #ddd; margin: 20px 0"/>

    <el-row :gutter="30">
      <el-col :span="12">
        <div style="padding: 20px 0; font-size: 20px">北京林业大学信息学院</div>
        <div>

        </div>
        <div class="m-10">
          <el-button type="primary" @click="downloadDoc('food')">报告模板下载</el-button>
        </div>
        <div class="m-10">
          <el-button type="danger" class="button" icon="el-icon-upload" @click="updateTemplate">报告生成</el-button>
        </div>
      </el-col>
      <el-col :span="12">
        <div style="padding: 20px 0; font-size: 20px">技术栈</div>
        <el-row>
          <el-col :span="12" style="line-height: 30px">
            <div><b>后端</b></div>
            <div>Spring Boot</div>
            <div>MySQL</div>
            <div>Poi</div>
            <div>Lombok</div>
            <div>Redis</div>
            <div>Mybatis/Mybatis-plus</div>
          </el-col>
          <el-col :span="12" style="line-height: 30px">
            <b>前端</b>
            <div>Vue2</div>
            <div>Vue-Router</div>
            <div>VueX</div>
            <div>Element UI</div>
            <div>Axios</div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

  </div>
</template>

<script>
export default {
  name: "Home",
  data() {
    return {
      msg: '食品安全风险评估自动报告生成系统',
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  methods:{
    downloadDoc(doc) {
      window.location.href = `http://localhost:8899/static/${doc}`
    },
    updateTemplate() {
      this.$router.push("/report")
    },
  }
}
</script>
