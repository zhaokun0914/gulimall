<template>
  <el-cascader
    filterable
    clearable
    placeholder="试试搜索：手机"
    v-model="paths"
    :options="categorys"
    :props="setting"
    @visible-change="handleVisibleChange">
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
    // 打开下拉时如果还没有分类数据，重新请求，避免首次请求失败后一直空白
    handleVisibleChange (visible) {
      if (visible && this.categorys.length === 0) {
        this.getCategories()
      }
    },
    getCategories () {
      // 带时间戳参数，避免浏览器缓存了旧的空响应
      this.$http({
        url: this.$http.adornUrl('/product/category/list/tree'),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({data}) => {
        if (data && data.code === 0) {
          // 兼容后端返回 page 或 data 两种字段
          this.categorys = data.page || data.data || []
        } else {
          this.categorys = []
          if (data && data.msg) {
            this.$message.error(data.msg)
          }
        }
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
