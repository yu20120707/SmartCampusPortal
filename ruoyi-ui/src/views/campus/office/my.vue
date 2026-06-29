<template>
  <div class="app-container campus-page">
    <el-card shadow="never" class="campus-panel">
      <div slot="header" class="panel-header">
        <span>我的申请</span>
        <el-button type="primary" size="mini" icon="el-icon-plus" @click="openDialog">新增申请</el-button>
      </div>
      <el-table v-loading="loading" :data="applications" size="small">
        <el-table-column prop="applicationNo" label="编号" width="160" />
        <el-table-column prop="applicationType" label="类型" width="110">
          <template slot-scope="scope">{{ typeLabel(scope.row.applicationType) }}</template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag size="mini" :type="statusTag(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="submitTime" label="提交时间" width="160" />
        <el-table-column prop="approverName" label="审批人" width="100" />
        <el-table-column prop="reviewComment" label="审批意见" min-width="180" show-overflow-tooltip />
      </el-table>
    </el-card>

    <el-dialog title="新增申请" :visible.sync="dialogVisible" :fullscreen="isMobile" width="560px">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="申请类型" prop="applicationType">
          <el-select v-model="form.applicationType" placeholder="请选择类型" style="width: 100%">
            <el-option label="请假" value="leave" />
            <el-option label="报修" value="repair" />
            <el-option label="用章" value="seal" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请标题" prop="title">
          <el-input v-model="form.title" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="申请说明" prop="content">
          <el-input v-model="form.content" type="textarea" :rows="5" maxlength="1000" show-word-limit />
        </el-form-item>
        <el-form-item label="审批人" prop="approverUserId">
          <el-select v-model="form.approverUserId" placeholder="请选择审批人" style="width: 100%" @change="onApproverChange">
            <el-option
              v-for="leader in validLeaders"
              :key="leader.userId"
              :label="leader.userName"
              :value="leader.userId"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMyApplications, addApplication, getLeaders } from '@/api/campus/office'
import mobileMixin from '@/mixins/mobile'

export default {
  name: 'CampusOfficeMy',
  mixins: [mobileMixin],
  data() {
    return {
      loading: true,
      dialogVisible: false,
      leaders: [],
      applications: [],
      form: {
        applicationType: 'leave',
        title: '',
        content: '',
        approverUserId: null,
        approverName: ''
      },
      rules: {
        applicationType: [{ required: true, message: '请选择申请类型', trigger: 'change' }],
        title: [{ required: true, message: '请输入申请标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入申请说明', trigger: 'blur' }],
        approverUserId: [{ required: true, message: '请选择审批人', trigger: 'change' }]
      }
    }
  },
  computed: {
    validLeaders() {
      return (this.leaders || []).filter(u => u != null && u.userId != null)
    }
  },
  created() {
    this.getList()
    this.fetchLeaders()
  },
  methods: {
    getList() {
      this.loading = true
      listMyApplications().then(response => {
        this.applications = response.data || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    openDialog() {
      this.dialogVisible = true
      // 逐个重置而非替换整个对象，保持 Vue 响应式绑定
      this.form.applicationType = 'leave'
      this.form.title = ''
      this.form.content = ''
      this.form.approverUserId = null
      this.form.approverName = ''
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },
    async fetchLeaders() {
      try {
        const response = await getLeaders()
        console.log('[我的申请] /leaders 原始响应:', JSON.parse(JSON.stringify(response)))
        console.log('[我的申请] response 类型:', typeof response, 'isArray:', Array.isArray(response))
        console.log('[我的申请] response 顶层 keys:', Object.keys(response || {}))

        // 兼容多种可能的响应格式
        let list = null
        if (Array.isArray(response)) {
          // 情况1: 响应直接被拦截器提取为数组
          list = response
        } else if (response && Array.isArray(response.data)) {
          // 情况2: AjaxResult 标准格式 { code, msg, data }
          list = response.data
        } else if (response && Array.isArray(response.rows)) {
          // 情况3: 分页格式 { code, msg, rows, total }
          list = response.rows
        } else if (response && typeof response === 'object') {
          // 情况4: 遍历查找第一个数组值
          for (const key of Object.keys(response)) {
            if (Array.isArray(response[key])) {
              console.log('[我的申请] 在 response.' + key + ' 中找到数组数据')
              list = response[key]
              break
            }
          }
        }

        if (list && list.length > 0) {
          // 过滤掉 null 元素（MyBatis LEFT JOIN 可能产生 null 行）
          const clean = list.filter(u => u != null)
          this.$set(this, 'leaders', clean)
          console.log('[我的申请] 领导列表加载成功，原始 ' + list.length + ' 条，有效 ' + clean.length + ' 人:')
          if (clean.length > 0) {
            console.table(clean.map(u => ({
              userId: u.userId,
              userName: u.userName,
              nickName: u.nickName
            })))
          }
        } else {
          console.warn('[我的申请] 领导列表为空！')
          console.warn('[我的申请] 可能原因: 数据库中 sys_user_role 表没有 role_id IN (1,111,112) 的用户')
          this.$set(this, 'leaders', [])
        }
      } catch (err) {
        console.error('[我的申请] 获取领导列表异常:', err)
        this.$set(this, 'leaders', [])
      }
    },
    onApproverChange(userId) {
      if (userId != null) {
        const selected = this.validLeaders.find(u => u.userId === userId)
        this.form.approverName = selected ? selected.userName : ''
      } else {
        this.form.approverName = ''
      }
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        console.log('[我的申请] 提交表单数据:', JSON.stringify(this.form, null, 2))
        addApplication(this.form).then(response => {
          console.log('[我的申请] 提交成功，响应:', response)
          this.$modal.msgSuccess('申请已提交')
          this.dialogVisible = false
          this.getList()
        })
      })
    },
    typeLabel(type) {
      const labels = { leave: '请假', repair: '报修', seal: '用章', other: '其他' }
      return labels[type] || type
    },
    statusLabel(status) {
      const labels = { 0: '草稿', 1: '待审批', 2: '已通过', 3: '已驳回' }
      return labels[status] || status
    },
    statusTag(status) {
      const tags = { 0: 'info', 1: 'warning', 2: 'success', 3: 'danger' }
      return tags[status] || 'info'
    }
  }
}
</script>

<style scoped>
.campus-page {
  background: #f5f7fb;
  min-height: calc(100vh - 84px);
}

.campus-panel {
  border-radius: 8px;
  transition: box-shadow .2s ease;
}

.campus-panel:hover {
  box-shadow: 0 2px 8px rgba(13,124,107,.05);
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}
</style>
