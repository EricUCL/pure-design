<template>
  <div style="width: 400px">
    <div>
      <el-form label-width="80px" :model="formLabelAlign" :rules="rules" id="form"
               ref="loginFormRef1">
        <el-form-item label="文档名称" prop="file">
          <el-input v-model="formLabelAlign.file" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="单位名称" prop="name">
          <el-input v-model="formLabelAlign.name" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="单位地点" prop="region">
          <el-input v-model="formLabelAlign.region" style="width: 200px"></el-input>
        </el-form-item>
        <el-form-item label="填写时间">
          <el-date-picker
              v-model="formLabelAlign.time"
              type="date"
              placeholder="选择日期"
              style="width: 200px">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <!--      <el-button id="button" type="danger" @click="submitForm">保存基本信息</el-button>-->
    </div>

    <div>
      <el-alert
          title="只允许上传.xlsx和.csv文件, 最大只能上传100Mb的文件, 如果上传成功会显示已上传的文件"
          type="warning"
          center
          show-icon>
      </el-alert>
      <!--      <el-upload-->
      <!--          class="upload-demo"-->
      <!--          :limit="1"-->
      <!--          drag-->
      <!--          accept=".xlsx,.csv"-->
      <!--          :on-success="successExcel"-->
      <!--          :on-exceed="handleExceed"-->
      <!--          :on-error="error"-->
      <!--          :on-progress="progress"-->
      <!--          :before-upload="uploadExcelBefore"-->
      <!--          :before-remove="beforeRemove"-->
      <!--          action="http://localhost:8899/uploadFile"-->
      <!--          id="upload">-->
      <!--        <i class="el-icon-upload"></i>-->
      <!--        <div class="el-upload__text">将Excel文件拖到此处，或<em>点击上传</em></div>-->
      <!--      </el-upload>-->

      <el-upload
          class="upload-demo"
          :limit="1"
          accept=".xlsx,.csv"
          action="http://localhost:8899/uploadFile"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :before-remove="beforeRemove"
          :before-upload="uploadExcelBefore"
          :on-exceed="handleExceed"
          :file-list="fileList">
        <el-button size="small" type="primary">点击上传抽检数据</el-button>
        <div slot="tip" class="el-upload__tip">只能上传xlsx/csv文件</div>
      </el-upload>



    </div>
    <!--    <div class="excel">-->
    <!--      <div v-loading="loading"-->
    <!--           element-loading-text="文档自动生成中，请稍等片刻"-->
    <!--           element-loading-spinner="el-icon-loading"-->
    <!--           element-loading-background="rgba(0, 0, 0, 0.8)">-->
    <!--        <el-alert-->
    <!--            title="只允许上传.doc和.docx文件, 最大只能上传100Mb的文件, 如果上传成功会显示已上传的文件"-->
    <!--            type="warning"-->
    <!--            center-->
    <!--            show-icon>-->
    <!--        </el-alert>-->
    <!--        <el-upload-->
    <!--            class="upload-demo"-->
    <!--            :limit="1"-->
    <!--            drag-->
    <!--            accept=".doc,.docx"-->
    <!--            :on-exceed="handleExceed"-->
    <!--            :on-error="error"-->
    <!--            :on-success="success"-->
    <!--            :on-progress="progress"-->
    <!--            :before-upload="uploadWordBefore"-->
    <!--            :before-remove="beforeRemove"-->
    <!--            action="http://localhost:8899/uploadFile"-->
    <!--            id="upload">-->
    <!--          <i class="el-icon-upload"></i>-->
    <!--          <div class="el-upload__text">将Word文件拖到此处，或<em>点击上传</em></div>-->
    <!--        </el-upload>-->
    <!--      </div>-->
    <!--    </div>-->

    <div style="margin-top: 40px">
      <el-upload
          class="upload-demo"
          ref="upload"
          :limit="1"
          action="http://localhost:8899/uploadFile"
          accept=".doc,.docx"
          :before-upload="uploadWordBefore"
          :on-exceed="handleExceed"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :file-list="fileList"
          :auto-upload="false">
        <el-button slot="trigger" size="small" type="primary">选取文档模版</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">生成报告</el-button>
        <div slot="tip" class="el-upload__tip">只能上传doc/docx文件</div>
      </el-upload>
    </div>
  </div>
</template>

<script>
import {getMessage} from "@/network/message";
import {generateFile} from "@/network/file"

export default {
  name: "Template",
  data() {
    return {
      formLabelAlign: {
        file: '',
        name: '',
        region: '',
        time: '',
        percentage: 0
      },
      rules: {
        file: [
          {required: true, message: '请输入生成文档名称', trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入单位名称', trigger: 'blur'}
        ],
        region: [
          {required: true, message: '请输入单位地点', trigger: 'blur'}
        ]
      },
      percentage: 0,
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
    handleExceed(files, fileList) {
      this.$message.error(`只能上传1个文件，如果要重新上传文件请先删除已上传的文件`);
    },
    error() {
      this.$message.error('上传失败,请打开后台服务')
      this.percentage = 0
    },
    uploadExcelBefore(file) {
      const strRegex = "(.xlsx|.csv)$"; //用于验证图片扩展名的正则表达式
      const re = new RegExp(strRegex);
      if (re.test(file.name)) {
        return true
      } else {
        this.$message.error(`${file.name}格式不对，请上传.xlsx和.csv文件`)
        return false
      }
    },
    uploadWordBefore(file) {
      const strRegex = "(.doc|.docx)$";
      const re = new RegExp(strRegex);
      if (re.test(file.name)) {
        return true
      } else {
        this.$message.error(`${file.name}格式不对，请上传.doc和.docx文件`)
        return false
      }
    },
    async success() {
      this.loading = true
      const res = await generateFile()
      if (res.status === 200) {
        for (let i = 1000000000; i > 0; i--) {
        }
        window.location.href = 'http://localhost:8899/word/updateWord'
        this.loading = false
        this.$message.success("文件生成成功！")
        this.$store.commit("setExcel", 0)
        this.$store.commit("setNext", 0)
        this.$router.push("/")
      }
    },
    async submitUpload() {
      this.submitForm();
      this.$refs.upload.submit();
      this.loading = true
      const res = await generateFile()
      if (res.status === 200) {
        for (let i = 1000000000; i > 0; i--) {
        }
        window.location.href = 'http://localhost:8899/word/updateWord'
        this.loading = false
        this.$message.success("文件生成成功！")
        this.$store.commit("setExcel", 0)
        this.$store.commit("setNext", 0)
        this.$router.push("/")
      }
    },
  }
}
</script>

<style scoped>

</style>