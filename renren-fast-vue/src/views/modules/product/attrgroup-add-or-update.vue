<template>
  <el-dialog
    :title="!dataForm.attrGroupId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    @closed="dialogClose">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="100px">
    <el-form-item label="组名" prop="attrGroupName">
      <el-input v-model="dataForm.attrGroupName" placeholder="组名"></el-input>
    </el-form-item>
    <el-form-item label="排序" prop="sort">
      <el-input v-model="dataForm.sort" placeholder="排序"></el-input>
    </el-form-item>
    <el-form-item label="描述" prop="descript">
      <el-input v-model="dataForm.descript" placeholder="描述"></el-input>
    </el-form-item>
    <el-form-item label="组图标" prop="icon">
      <el-input v-model="dataForm.icon" placeholder="组图标"></el-input>
    </el-form-item>
    <el-form-item label="所属分类" prop="catelogId">
      <el-cascader filterable v-model="dataForm.catelogPath" :options="categorys" :props="props"></el-cascader>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data () {
    return {
      visible: false,
      categorys: [],
      props: {
        // 指定选项的值为选项对象的某个属性值
        value: 'catId',
        // 指定选项标签为选项对象的某个属性值
        label: 'name',
        // 指定选项的子选项为选项对象的某个属性值
        children: 'children'
      },
      dataForm: {
        attrGroupId: 0,
        attrGroupName: '',
        sort: '',
        descript: '',
        icon: '',
        catelogId: 0,
        // 级联选择器选中的完整分类路径，提交时只取最后一级
        catelogPath: []
      },
      dataRule: {
        attrGroupName: [
          { required: true, message: '组名不能为空', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '排序不能为空', trigger: 'blur' }
        ],
        descript: [
          { required: true, message: '描述不能为空', trigger: 'blur' }
        ],
        icon: [
          { required: true, message: '组图标不能为空', trigger: 'blur' }
        ],
        catelogId: [
          { required: true, message: '所属分类id不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    // 关闭弹窗时清空级联选择器，避免下次打开还保留上次的选中路径
    dialogClose () {
      this.dataForm.catelogPath = []
    },
    // 获取三级分类数据，供级联选择器使用
    getCategories () {
      this.$http({
        url: this.$http.adornUrl('/product/category/list/tree'),
        method: 'get'
      }).then(({data}) => {
        this.categorys = data.page
      })
    },
    init (id) {
      this.dataForm.attrGroupId = id || 0
      this.visible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].resetFields()
        if (this.dataForm.attrGroupId) {
          this.$http({
            url: this.$http.adornUrl(`/product/attrgroup/info/${this.dataForm.attrGroupId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.attrGroupName = data.attrGroup.attrGroupName
              this.dataForm.sort = data.attrGroup.sort
              this.dataForm.descript = data.attrGroup.descript
              this.dataForm.icon = data.attrGroup.icon
              this.dataForm.catelogId = data.attrGroup.catelogId
              // 后台返回的完整分类路径，用于级联选择器回显
              this.dataForm.catelogPath = data.attrGroup.catelogPath
            }
          })
        }
      })
    },
    // 表单提交
    dataFormSubmit () {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(`/product/attrgroup/${!this.dataForm.attrGroupId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'attrGroupId': this.dataForm.attrGroupId || undefined,
              'attrGroupName': this.dataForm.attrGroupName,
              'sort': this.dataForm.sort,
              'descript': this.dataForm.descript,
              'icon': this.dataForm.icon,
              // 级联选择器给的是完整路径，只提交最后一级分类 id
              'catelogId': this.dataForm.catelogPath[this.dataForm.catelogPath.length - 1]
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.visible = false
                  this.$emit('refreshDataList')
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        }
      })
    }
  },
  created () {
    this.getCategories()
  }
}
</script>
