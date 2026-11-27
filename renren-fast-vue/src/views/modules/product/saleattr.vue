<template>
  <div>
    <base-attr :attrtype="0"></base-attr>
  </div>
</template>

<script>
// 销售属性页面：复用规格参数页面，通过 attrtype=0 切换到销售属性
import BaseAttr from './baseattr'

export default {
  components: {
    BaseAttr
  }
}
</script>

<style scoped>

</style>
