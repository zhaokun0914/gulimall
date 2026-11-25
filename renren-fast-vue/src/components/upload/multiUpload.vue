<template>
  <div>
    <el-upload
      :action="uploadAction"
      :data="uploadData"
      list-type="picture-card"
      :file-list="fileList"
      :limit="maxCount"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-preview="handlePreview"
      :on-exceed="handleExceed">
      <i class="el-icon-plus"></i>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible" append-to-body>
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
import { policy } from './policy'
import { getUUID } from '@/utils'

export default {
  name: 'multiUpload',
  props: {
    value: {
      type: Array,
      default () {
        return []
      }
    },
    maxCount: {
      type: Number,
      default: 5
    }
  },
  computed: {
    fileList () {
      return this.value.map(url => ({
        name: url.substring(url.lastIndexOf('/') + 1),
        url
      }))
    }
  },
  data () {
    return {
      uploadAction: '',
      uploadHost: '',
      uploadData: {
        key: '',
        policy: '',
        OSSAccessKeyId: '',
        Signature: '',
        success_action_status: '200'
      },
      dialogImageUrl: '',
      dialogVisible: false
    }
  },
  methods: {
    beforeUpload (file) {
      const isImage = file.type === 'image/jpeg' || file.type === 'image/png'
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isImage) {
        this.$message.error('只能上传 jpg/png 格式的图片')
        return false
      }
      if (!isLt10M) {
        this.$message.error('图片大小不能超过 10MB')
        return false
      }
      return policy().then(data => {
        this.uploadAction = data.host
        this.uploadHost = data.host
        this.uploadData = {
          key: data.dir + getUUID() + '_' + file.name,
          policy: data.policy,
          OSSAccessKeyId: data.accessId,
          Signature: data.signature,
          success_action_status: '200'
        }
        return true
      }).catch(() => {
        this.$message.error('获取上传签名失败，请确认 third-party 服务已启动')
        return Promise.reject(new Error('获取上传签名失败'))
      })
    },
    handleUploadSuccess (res, file) {
      const url = this.uploadHost + '/' + this.uploadData.key
      this.$emit('input', this.value.concat(url))
    },
    handleRemove (file, fileList) {
      this.$emit('input', fileList.map(item => item.url).filter(url => url))
    },
    handlePreview (file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    handleExceed () {
      this.$message.warning(`最多只能上传 ${this.maxCount} 张图片`)
    }
  }
}
</script>
