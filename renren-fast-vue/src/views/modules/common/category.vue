<template>
  <div>
    <!-- 输入关键字过滤分类树 -->
    <el-input v-model="filterText" placeholder="输入关键字进行过滤" clearable></el-input>
    <el-tree
      :data="menus"
      :props="defaultProps"
      node-key="catId"
      ref="menuTree"
      :filter-node-method="filterNode"
      :highlight-current="true"
      @node-click="nodeclick">
    </el-tree>
  </div>
</template>

<script>
// 公共分类树组件：只负责展示与点击事件，具体业务由使用它的父组件处理
export default {
  components: {},
  props: {},
  data () {
    return {
      filterText: '',
      menus: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  watch: {
    // 关键字变化时调用 el-tree 的过滤方法
    filterText (val) {
      if (this.$refs.menuTree) {
        this.$refs.menuTree.filter(val)
      }
    }
  },
  methods: {
    // 过滤规则：节点名称包含关键字即显示
    filterNode (value, data) {
      if (!value) {
        return true
      }
      return data.name.indexOf(value) !== -1
    },
    getMenus () {
      // 带时间戳参数，避免浏览器缓存了旧的空响应
      this.$http({
        url: this.$http.adornUrl('/product/category/list/tree'),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({data}) => {
        if (data && data.code === 0) {
          // 兼容后端返回 page 或 data 两种字段
          this.menus = data.page || data.data || []
        } else {
          this.menus = []
          if (data && data.msg) {
            this.$message.error(data.msg)
          }
        }
      })
    },
    // 子组件通过事件把点击数据传给父组件
    nodeclick (data, node, component) {
      this.$emit('tree-node-click', data, node, component)
    }
  },
  created () {
    this.getMenus()
  }
}
</script>

<style scoped>

</style>
