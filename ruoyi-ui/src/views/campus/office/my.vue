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

    <el-dialog title="新增申请" :visible.sync="dialogVisible" width="560px">
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
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMyApplications, addApplication } from '@/api/campus/office'

export default {
  name: 'CampusOfficeMy',
  data() {
    return {
      loading: true,
      dialogVisible: false,
      applications: [],
      form: {
        applicationType: 'leave',
        title: '',
        content: ''
      },
      rules: {
        applicationType: [{ required: true, message: '请选择申请类型', trigger: 'change' }],
        title: [{ required: true, message: '请输入申请标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入申请说明', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
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
      this.form = {
        applicationType: 'leave',
        title: '',
        content: ''
      }
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },
    submitForm() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        addApplication(this.form).then(() => {
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
  border-radius: 6px;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}
</style>
