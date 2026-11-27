<template>
  <el-dialog
    :title="!dataForm.attrId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
    @closed="dialogClose">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" label-width="120px">
    <el-form-item label="属性名" prop="attrName">
      <el-input v-model="dataForm.attrName" placeholder="属性名"></el-input>
    </el-form-item>
    <el-form-item label="属性类型" prop="attrType">
      <el-select v-model="dataForm.attrType" placeholder="请选择">
        <el-option label="规格参数" :value="1"></el-option>
        <el-option label="销售属性" :value="0"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="值类型" prop="valueType">
      <el-switch v-model="dataForm.valueType" active-text="允许多个值" inactive-text="只能单个值"
                 active-color="#13ce66" inactive-color="#ff4949" :active-value="1" :inactive-value="0"></el-switch>
    </el-form-item>
    <el-form-item label="可选值" prop="valueSelect">
      <el-select v-model="dataForm.valueSelect" filterable multiple allow-create placeholder="请输入内容"></el-select>
    </el-form-item>
    <el-form-item label="属性图标" prop="icon">
      <el-input v-model="dataForm.icon" placeholder="属性图标"></el-input>
    </el-form-item>
    <el-form-item label="所属分类" prop="catelogId">
      <category-cascader ref="categoryCascader" :catelogPath.sync="catelogPath"></category-cascader>
    </el-form-item>
    <el-form-item label="所属分组" prop="attrGroupId" v-if="type === 1">
      <el-select ref="groupSelect" v-model="dataForm.attrGroupId" placeholder="请选择"
                 @visible-change="handleGroupVisibleChange">
        <el-option v-for="item in attrGroups" :key="item.attrGroupId" :label="item.attrGroupName"
                   :value="item.attrGroupId"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="可检索" prop="searchType" v-if="type === 1">
      <el-switch v-model="dataForm.searchType" active-color="#13ce66" inactive-color="#ff4949"
                 :active-value="1" :inactive-value="0"></el-switch>
    </el-form-item>
    <el-form-item label="快速展示" prop="showDesc" v-if="type === 1">
      <el-switch v-model="dataForm.showDesc" active-color="#13ce66" inactive-color="#ff4949"
                 :active-value="1" :inactive-value="0"></el-switch>
    </el-form-item>
    <el-form-item label="启用状态" prop="enable">
      <el-switch v-model="dataForm.enable" active-color="#13ce66" inactive-color="#ff4949"
                 :active-value="1" :inactive-value="0"></el-switch>
    </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import CategoryCascader from '../common/category-cascader'

export default {
  components: {
    CategoryCascader
  },
  props: {
    type: {
      type: Number,
      default: 1
    }
  },
  data () {
    return {
      visible: false,
      catelogPath: [],
      attrGroups: [],
      dataForm: {
        attrId: 0,
        attrName: '',
        attrType: 1,
        valueType: 1,
        valueSelect: [],
        icon: '',
        catelogId: '',
        attrGroupId: '',
        searchType: 0,
        showDesc: 0,
        enable: 1
      },
      dataRule: {
        attrName: [
          { required: true, message: '属性名不能为空', trigger: 'blur' }
        ],
        attrType: [
          { required: true, message: '属性类型不能为空', trigger: 'blur' }
        ],
        valueType: [
          { required: true, message: '值类型不能为空', trigger: 'blur' }
        ],
        icon: [
          { required: true, message: '属性图标不能为空', trigger: 'blur' }
        ],
        catelogId: [
          { required: true, message: '需要选择正确的三级分类', trigger: 'blur' }
        ],
        searchType: [
          { required: true, message: '是否需要检索不能为空', trigger: 'blur' }
        ],
        showDesc: [
          { required: true, message: '快速展示不能为空', trigger: 'blur' }
        ],
        enable: [
          { required: true, message: '启用状态不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    // 分类路径变化后，按三级分类查询所属分组
    catelogPath (path) {
      this.attrGroups = []
      this.dataForm.attrGroupId = ''
      if (!path || path.length === 0) {
        this.dataForm.catelogId = ''
        return
      }
      this.dataForm.catelogId = path[path.length - 1]
      if (path.length === 3) {
        this.queryAttrGroups(path[path.length - 1])
      } else {
        this.$message.error('请选择正确的分类')
        this.dataForm.catelogId = ''
      }
    }
  },
  methods: {
    // 按三级分类查询所属分组
    queryAttrGroups (catelogId) {
      this.$http({
        url: this.$http.adornUrl(`/product/attrgroup/list/${catelogId}`),
        method: 'get',
        params: this.$http.adornParams({
          page: 1,
          limit: 1000
        })
      }).then(({data}) => {
        if (data && data.code === 0) {
          this.attrGroups = data.page.list || []
        } else {
          this.attrGroups = []
          this.$message.error(data.msg || '查询分组失败')
        }
      })
    },
    // 打开分组下拉时如果还没有数据，重新查询，避免首次请求失败后一直空白
    handleGroupVisibleChange (visible) {
      if (visible && this.attrGroups.length === 0 && this.dataForm.catelogId) {
        this.queryAttrGroups(this.dataForm.catelogId)
      }
    },
    init (id) {
      this.dataForm.attrId = id || 0
      this.visible = true
      this.$nextTick(() => {
        // 弹窗关闭后组件不会销毁，每次打开都重新拉取分类，避免上次请求失败后一直为空
        if (this.$refs.categoryCascader) {
          this.$refs.categoryCascader.getCategories()
        }
        this.$refs['dataForm'].resetFields()
        // resetFields 会把字段还原到挂载时的初始值（attrType 默认为 1），
        // 因此必须在 reset 之后再按页面类型设置，销售属性页新增才不会落库成规格参数
        this.dataForm.attrType = this.type
        if (this.dataForm.attrId) {
          this.$http({
            url: this.$http.adornUrl(`/product/attr/info/${this.dataForm.attrId}`),
            method: 'get',
            params: this.$http.adornParams()
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.dataForm.attrName = data.attr.attrName
              this.dataForm.searchType = data.attr.searchType
              this.dataForm.valueType = data.attr.valueType
              this.dataForm.icon = data.attr.icon
              this.dataForm.valueSelect = data.attr.valueSelect ? data.attr.valueSelect.split(';') : []
              this.dataForm.attrType = data.attr.attrType
              this.dataForm.enable = data.attr.enable
              this.dataForm.catelogId = data.attr.catelogId
              this.dataForm.showDesc = data.attr.showDesc
              // 分类路径与分组回显，由 P77 的 AttrRespVo 提供
              this.catelogPath = data.attr.catelogPath || []
              this.$nextTick(() => {
                this.dataForm.attrGroupId = data.attr.attrGroupId
              })
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
            url: this.$http.adornUrl(`/product/attr/${!this.dataForm.attrId ? 'save' : 'update'}`),
            method: 'post',
            data: this.$http.adornData({
              'attrId': this.dataForm.attrId || undefined,
              'attrName': this.dataForm.attrName,
              'searchType': this.dataForm.searchType,
              'valueType': this.dataForm.valueType,
              'icon': this.dataForm.icon,
              'valueSelect': this.dataForm.valueSelect.join(';'),
              'attrType': this.dataForm.attrType,
              'enable': this.dataForm.enable,
              'catelogId': this.dataForm.catelogId,
              'attrGroupId': this.dataForm.attrGroupId,
              'showDesc': this.dataForm.showDesc
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
    },
    // 关闭弹窗时清空分类路径
    dialogClose () {
      this.catelogPath = []
    }
  }
}
</script>
