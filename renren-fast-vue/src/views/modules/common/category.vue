<template>
  <el-tree :data="menus" :props="defaultProps" node-key="catId" ref="menuTree" @node-click="nodeclick"></el-tree>
</template>

<script>
// 公共分类树组件：只负责展示与点击事件，具体业务由使用它的父组件处理
export default {
  components: {},
  props: {},
  data () {
    return {
      menus: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      }
    }
  },
  methods: {
    getMenus () {
      this.$http({
        url: this.$http.adornUrl('/product/category/list/tree'),
        method: 'get'
      }).then(({data}) => {
        this.menus = data.page
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
