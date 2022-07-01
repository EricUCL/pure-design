<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd" v-if="user.role === 'ROLE_ADMIN'">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
      <el-upload :action="'http://' + serverIp + ':9090/food/import'" :show-file-list="false" accept="xlsx" :on-success="handleExcelImportSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">导入 <i class="el-icon-bottom"></i></el-button>
      </el-upload>
      <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-top"></i></el-button>


    </div>
    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" fixed></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="code" label="食品抽检编号"></el-table-column>
      <el-table-column prop="productName" label="食品名称"></el-table-column>
      <el-table-column prop="productCn" label="食品生产厂家"></el-table-column>
      <el-table-column prop="productCa" label="食品生产厂家地址"></el-table-column>
      <el-table-column prop="productProvince" label="生产地省份"></el-table-column>
      <el-table-column prop="saleCompany" label="销售公司"></el-table-column>
      <el-table-column prop="saleCa" label="食品销售地址"></el-table-column>
      <el-table-column prop="scProvince" label="销售地省份"></el-table-column>
      <el-table-column prop="productDate" label="生产日期"></el-table-column>
      <el-table-column prop="classify" label="食品类别"></el-table-column>
      <el-table-column prop="sampleC" label="抽检单位"></el-table-column>
      <el-table-column prop="sampleDate" label="抽检日期"></el-table-column>
      <el-table-column prop="comment" label="备注"></el-table-column>
      <el-table-column prop="qualified" label="是否合格"></el-table-column>

      <el-table-column label="操作" width="280" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)" v-if="user.role === 'ROLE_ADMIN'">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference" v-if="user.role === 'ROLE_ADMIN'">删除 <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="添加食品抽检数据" :visible.sync="dialogFormVisible" width="60%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="食品抽检编号">
          <el-input v-model="form.code" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="食品名称">
          <el-input v-model="form.productName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="食品生产厂家">
          <el-input v-model="form.productCn" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="食品生产厂家地址">
          <el-input v-model="form.productCa" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="生产地省份">
          <el-input v-model="form.productProvince" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="销售公司">
          <el-input v-model="form.saleCompany" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="食品销售地址">
          <el-input v-model="form.saleCa" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="销售地省份">
          <el-input v-model="form.scProvince" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="生产日期">
          <el-input v-model="form.productDate" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="食品类别">
          <el-input v-model="form.classify" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="抽检单位">
          <el-input v-model="form.sampleC" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="抽检日期">
          <el-input v-model="form.sampleDate" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.comment" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="是否合格">
          <el-input v-model="form.qualified" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>

import axios from "axios";
import {serverIp} from "../../public/config";
export default {
  name: "Sample",
  data() {
    return {
      form: {},
      serverIp: serverIp,
      tableData: [],
      name: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 10,
      total: 0,
      dialogFormVisible: false,
      teachers: [],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      content: '',
      viewDialogVis: false
    }
  },
  created() {
    this.load()
  },
  methods: {
    view(content) {
      this.content = content
      this.viewDialogVis = true
    },
    // 绑定@imgAdd event
    imgAdd(pos, $file) {
      let $vm = this.$refs.md
      // 第一步.将图片上传到服务器.
      const formData = new FormData();
      formData.append('file', $file);
      axios({
        url: 'http://localhost:9090/file/upload',
        method: 'post',
        data: formData,
        headers: {'Content-Type': 'multipart/form-data'},
      }).then((res) => {
        // 第二步.将返回的url替换到文本原位置![...](./0) -> ![...](url)
        $vm.$img2Url(pos, res.data);
      })
    },
    load() {
      this.request.get("/food/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {

        this.tableData = res.data.records
        this.total = res.data.total

      })

    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },
    changeEnable(row) {
      this.request.post("/food/update", row).then(res => {
        if (res.code === '200') {
          this.$message.success("操作成功")
        }
      })
    },
    exp() {
      window.open(`http://${serverIp}:9090/food/export`)
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/food/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/food/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    save() {
      this.request.post("/food", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    download(url) {
      window.open(url)
    },
  }
}
</script>

<style scoped>

</style>
