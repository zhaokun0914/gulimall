<template>
  <div>
    <el-switch v-model="draggable" active-text="开启拖拽" inactive-text="关闭拖拽"></el-switch>
    <el-button v-if="draggable" @click="batchSave">批量保存</el-button>
    <el-tree :data="menus"
             :props="defaultProps"
             show-checkbox
             node-key="catId"
             :expand-on-click-node="false"
             :default-expanded-keys="expandedKey"
             :draggable="draggable"
             :allow-drop="allowDrop"
             @node-drop="handleDrop">
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
      draggable: false,
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
      updateNodes: [],
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
    batchSave () {
      if (this.updateNodes.length === 0) {
        this.$message({ type: 'info', message: '没有需要保存的修改' })
        return
      }
      this.$http({
        url: this.$http.adornUrl('/product/category/update/sort'),
        method: 'post',
        data: this.$http.adornData(this.updateNodes, false)
      }).then(({ data }) => {
        if (data.code === 0) {
          this.$message({
            type: 'success',
            message: '批量保存成功!'
          })
          this.updateNodes = []
          this.getMenus()
        }
      })
    },
    handleDrop (draggingNode, dropNode, dropType) {
      // 拖拽时只收集变更，点「批量保存」时再统一提交
      // 拖拽结束后 Element 已经把节点放到了新位置，并重算了 level
      // inner 的新父节点是 dropNode；before/after 的新父节点是 dropNode 的父节点
      const parentNode = dropType === 'inner' ? dropNode : dropNode.parent
      const parentCid = parentNode.level === 0 ? 0 : parentNode.data.catId

      parentNode.childNodes.forEach((node, index) => {
        const item = { catId: node.data.catId, sort: index }
        if (node.data.catId === draggingNode.data.catId) {
          // 被拖节点：记录新父节点和新层级，并收集所有子孙的新层级
          item.parentCid = parentCid
          item.catLevel = node.level
          this.pushChildLevels(node)
        }
        this.updateNodes.push(item)
      })

      // 把本次涉及的新、旧父节点都记下来，批量保存后一起展开
      const relatedParents = [parentCid, draggingNode.data.parentCid]
      relatedParents.forEach(cid => {
        if (cid && this.expandedKey.indexOf(cid) === -1) {
          this.expandedKey = this.expandedKey.concat(cid)
        }
      })
      console.log('updateNodes', this.updateNodes)
    },
    pushChildLevels (node) {
      node.childNodes.forEach(child => {
        this.updateNodes.push({ catId: child.data.catId, catLevel: child.level })
        this.pushChildLevels(child)
      })
    },
    allowDrop (draggingNode, dropNode, type) {
      // inner 落到 dropNode 内部；prev/next 为同级兄弟
      const newLevel = type === 'inner' ? dropNode.level + 1 : dropNode.level
      const depth = this.getSubtreeDepth(draggingNode)
      // 最深子孙的层级 = newLevel + depth - 1
      return newLevel + depth - 1 <= 3
    },
    // 子树相对深度（含自身）：叶子为 1
    getSubtreeDepth (node) {
      let max = 0
      const children = node.childNodes || []
      for (let i = 0; i < children.length; i++) {
        max = Math.max(max, this.getSubtreeDepth(children[i]))
      }
      return max + 1
    },
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
