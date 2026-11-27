<template>
  <el-cascader
    filterable
    clearable
    placeholder="试试搜索：手机"
    v-model="paths"
    :options="categorys"
    :props="setting">
  </el-cascader>
</template>

<script>
// 分类级联选择器：通过 .sync 与父组件的 catelogPath 双向绑定
export default {
  props: {
    catelogPath: {
      type: Array,
      default () {
        return []
      }
    }
  },
  data () {
    return {
      setting: {
        value: 'catId',
        label: 'name',
        children: 'children'
      },
      categorys: [],
      paths: this.catelogPath
    }
  },
  watch: {
    catelogPath (val) {
      this.paths = val
    },
    paths (val) {
      this.$emit('update:catelogPath', val)
    }
  },
  methods: {
    getCategories () {
      this.$http({
        url: this.$http.adornUrl('/product/category/list/tree'),
        method: 'get'
      }).then(({data}) => {
        this.categorys = data.page
      })
    }
  },
  created () {
    this.getCategories()
  }
}
</script>

<style scoped>

</style>
