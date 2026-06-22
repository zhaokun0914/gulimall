<template>
  <div>
    <el-tree :data="menus"
             :props="defaultProps"
             show-checkbox
             node-key="catId"
             :expand-on-click-node="false"
             :default-expanded-keys="expandedKey">
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button v-if="node.level <= 2" type="text" size="mini" @click="() => append(data)">Append</el-button>
          <el-button type="text" size="mini" @click="() => edit(data)">Edit</el-button>
          <el-button v-if="node.childNodes.length === 0" type="text" size="mini" @click="() => remove(node, data)">Delete</el-button>
        </span>
      </span>
    </el-tree>

    <el-dialog
      :title="dialogType === 'add' ? '新增分类' : '修改分类'"
      width="30%"
      :visible.sync="dialogVisible">
      <el-form ref="form" :model="dialogData">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="dialogData.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="dialogData.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="计量单位" prop="productUnit">
          <el-input v-model="dialogData.productUnit" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitCategory">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// 这里可以导入其他文件(比如:组件，工具js，第三方插件js，json文件，图片文件等等)
// 例如:import《组件名称》from‘《组件路径》;
// import request from '@/utils/request'

export default {
  // import引入的组件需要注入到对象中才能使用
  components: {},
  props: {},
  data () {
    return {
      dialogType: '',
      dialogData: {
        'name': '',
        'parentCid': 0,
        'catLevel': 0,
        'showStatus': 1,
        'sort': 0,
        'icon': null,
        'productUnit': null,
        'productCount': 0
      },
      menus: [],
      expandedKey: [],
      dialogVisible: false,
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  // 计算属性类似于data概念
  computed: {},
  // 监控data中的数据变化
  watch: {},
  // 方法集合
  methods: {
    append (data) {
      console.log('append', data)
      this.dialogType = 'add'
      this.dialogData = {
        'name': '',
        'parentCid': data.catId,
        'catLevel': data.catLevel * 1 + 1,
        'showStatus': 1,
        'sort': 0,
        'icon': null,
        'productUnit': null,
        'productCount': 0
      }
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.resetFields()
        }
      })
      this.dialogVisible = true
    },
    edit (data) {
      console.log('edit', data)
      this.dialogType = 'edit'
      this.dialogVisible = true
      // 通过后台查询最新的数据显示
      this.$http({
        url: this.$http.adornUrl(`/product/category/info/${data.catId}`),
        method: 'get'
      }).then(({data}) => {
        // 请求成功
        console.log('要回显的数据', data)
        this.dialogData = {...data.data}// {...data} 会把 data 对象上所有可枚举属性都拷贝到 dialogData 中，包括 catId
      })
    },
    submitCategory () {
      const url = this.dialogType === 'add' ? '/product/category/save' : '/product/category/update'
      this.$http({
        url: this.$http.adornUrl(url),
        method: 'post',
        data: this.$http.adornData(this.dialogData, false)
      }).then(({data}) => {
        this.$message({
          type: 'success',
          message: this.dialogType === 'add' ? '菜单添加成功!' : '菜单修改成功!'
        })
        this.dialogVisible = false
        this.getMenus()
        this.expandedKey = [this.dialogData.parentCid]
      })
    },
    remove (node, data) {
      console.log('remove', node, data)
      var ids = [data.catId]
      this.$confirm(`是否删除【${data.name}】菜单?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/product/category/delete'),
          method: 'post',
          data: this.$http.adornData(ids, false)
        }).then(({data}) => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          })
          this.expandedKey = [node.parent.data.catId]
          this.getMenus()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    getMenus () {
      this.$http({
        url: this.$http.adornUrl('/product/category/list/tree'),
        method: 'get'
      }).then(({data}) => {
        console.log(data)
        this.menus = data.page
      })
    }
  },
  created () {
    this.getMenus()
  }, // 生命周期-创建完成(可以访问当前this实例)
  mounted () {

  }, // 生命周期挂载完成(可以访问DOM元素)
  beforecreate () {

  }, // 生命周期创建之前
  beforeMount () {

  }, // 生命周期挂载之前-
  beforeupdate () {

  }, // 生命周期- 更新之前
  updated () {

  }, // 生命周期 - 更新之后
  beforeDestroy () {

  }, // 生命周期- 销毁之前
  destroyed () {

  }, // 生命周期 -销毁完成
  activated () {

  }// 如果页面有keep-alive缓存功能,这个函数会触发
}
</script>

<style scoped>

</style>
