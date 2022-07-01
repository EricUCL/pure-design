<template>
  <div style="width: 400px">
    <div>
      <el-form label-width="80px" :model="formLabelAlign" :rules="rules" id="form"
               ref="loginFormRef1">
        <el-form-item label="中文题目" prop="Ctopic">
          <el-input v-model="formLabelAlign.Ctopic" style="width: 400px"></el-input>
        </el-form-item>
        <el-form-item label="英文题目" prop="Etopic">
          <el-input v-model="formLabelAlign.Etopic" style="width: 400px"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="formLabelAlign.name" style="width: 200px"></el-input>
        </el-form-item>
      </el-form>
      <el-button style="margin-left: 10px;" size="small" type="success" @click="submit">生成封面</el-button>
    </div>
  </div>
</template>

<script>
import {getMessage} from "@/network/message";
import {generateFile} from "@/network/file"
import {setRoutes} from "@/router";
import {serverIp} from "../../public/config";

export default {
  name: "Template",
  data() {
    return {
      formLabelAlign: {
        Etopic: '',
        name: '',
        Ctopic: '',
      },
      rules: {
        Ctopic: [
          {required: true, message: '请输入中文题目', trigger: 'blur'}
        ],
        Etopic: [
          {required: true, message: '请输入英文题目', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'}
        ],
      },
      loading: false
    }
  },
  methods: {
    submitForm() {
      this.$refs.loginFormRef1.validate(async valid => {
        if (!valid) return;
        const res = await getMessage(this.formLabelAlign)
        if (res.data.code === 200) {
          this.$message.success("信息保存成功！")
        } else {
          this.$message.error("信息保存失败！请检查网络连接！")
        }
      })
    },
    error() {
      this.$message.error('上传失败,请打开后台服务')
      this.percentage = 0
    },
    async submitUpload() {
      //this.submitForm();
      this.loading = true
      const res = await generateFile()
      if (res.status === 200) {
        for (let i = 1000000000; i > 0; i--) {
        }
        window.location.href = '/word/updateWord'
        this.loading = false
        this.$message.success("文件生成成功！")
        //this.$router.push("/")
      }
    },
    submit(){
      this.$refs.loginFormRef1.validate(async valid => {
        if (valid) {
          this.request.post("/word/saveWord", this.formLabelAlign).then(res => {
            this.$message.success("文件生成成功！")
            //window.location.href = 'http://localhost:9090/word/updateWord'
            window.open(`http://${serverIp}:9090/word/generateWord`)
            //this.$router.push("/")
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>