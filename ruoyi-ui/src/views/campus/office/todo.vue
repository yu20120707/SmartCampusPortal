<template>
  <div class="app-container campus-page">
    <el-card shadow="never" class="campus-panel">
      <div slot="header" class="panel-header">
        <span>审批待办</span>
        <el-tag size="mini" type="warning">{{ applications.length }} 条待处理</el-tag>
      </div>
      <el-table v-loading="loading" :data="applications" size="small">
        <el-table-column prop="applicationNo" label="编号" width="160" />
        <el-table-column prop="applicationType" label="类型" width="100">
          <template slot-scope="scope">{{ typeLabel(scope.row.applicationType) }}</template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="180" />
        <el-table-column prop="applicantName" label="申请人" width="100" />
        <el-table-column prop="submitTime" label="提交时间" width="160" />
        <el-table-column prop="content" label="说明" min-width="220" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="success" @click="openReview(scope.row, 'approve')">同意</el-button>
            <el-button size="mini" type="danger" @click="openReview(scope.row, 'reject')">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="reviewTitle" :visible.sync="dialogVisible" :fullscreen="isMobile" width="520px">
      <el-form :model="reviewForm" label-width="90px">
        <el-form-item label="审批意见">
          <el-input v-model="reviewForm.reviewComment" type="textarea" :rows="4" maxlength="500" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReview">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTodoApplications, approveApplication, rejectApplication } from '@/api/campus/office'
import mobileMixin from '@/mixins/mobile'

export default {
  name: 'CampusOfficeTodo',
  mixins: [mobileMixin],
  data() {
    return {
      loading: true,
      dialogVisible: false,
      reviewAction: 'approve',
      currentApplication: null,
      reviewForm: {
        reviewComment: ''
      },
      applications: []
    }
  },
  computed: {
    reviewTitle() {
      return this.reviewAction === 'approve' ? '同意申请' : '驳回申请'
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listTodoApplications().then(response => {
        this.applications = response.data || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    openReview(row, action) {
      this.currentApplication = row
      this.reviewAction = action
      this.reviewForm = {
        reviewComment: action === 'approve' ? '同意' : '不通过'
      }
      this.dialogVisible = true
    },
    submitReview() {
      const request = this.reviewAction === 'approve' ? approveApplication : rejectApplication
      request(this.currentApplication.applicationId, this.reviewForm).then(() => {
        this.$modal.msgSuccess('审批已处理')
        this.dialogVisible = false
        this.getList()
      })
    },
    typeLabel(type) {
      const labels = { leave: '请假', repair: '报修', seal: '用章', other: '其他' }
      return labels[type] || type
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
