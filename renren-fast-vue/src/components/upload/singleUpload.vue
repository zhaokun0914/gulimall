<template>
  <div>
    <el-upload
      :action="uploadAction"
      :data="uploadData"
      list-type="picture"
      :multiple="false"
      :show-file-list="showFileList"
      :file-list="fileList"
      :before-upload="beforeUpload"
      :on-remove="handleRemove"
      :on-success="handleUploadSuccess"
      :on-preview="handlePreview">
      <el-button size="small" type="primary">点击上传</el-button>
      <div slot="tip" class="el-upload__tip">只能上传 jpg/png 文件，且不超过 10MB</div>
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
  name: 'singleUpload',
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  computed: {
    fileList () {
      if (!this.value) {
        return []
      }
      return [{
        name: this.value.substring(this.value.lastIndexOf('/') + 1),
        url: this.value
      }]
    },
    showFileList () {
      return !!this.value
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
      this.$emit('input', this.uploadHost + '/' + this.uploadData.key)
    },
    handleRemove () {
      this.$emit('input', '')
    },
    handlePreview (file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    }
  }
}
</script>
