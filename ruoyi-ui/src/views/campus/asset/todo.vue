<template>
  <div class="app-container campus-asset-page">
    <el-card shadow="never" class="asset-card">
      <div slot="header" class="panel-header">
        <span>资产借用审批</span>
        <el-tag size="mini" type="warning">{{ borrows.length }} 条待处理</el-tag>
      </div>
      <el-table v-loading="loading" :data="borrows" size="small">
        <el-table-column prop="applyTime" label="申请时间" width="160" />
        <el-table-column prop="assetNo" label="资产编号" width="140" />
        <el-table-column prop="assetName" label="资产" min-width="160" />
        <el-table-column prop="applicantName" label="申请人" width="100" />
        <el-table-column prop="purpose" label="用途" min-width="220" show-overflow-tooltip />
        <el-table-column label="操作" width="180" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="success" @click="openReview(scope.row, 'approve')">同意</el-button>
            <el-button size="mini" type="danger" @click="openReview(scope.row, 'reject')">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="reviewTitle" :visible.sync="dialogVisible" width="520px">
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
import { listTodoAssetBorrows, approveAssetBorrow, rejectAssetBorrow } from '@/api/campus/asset'

export default {
  name: 'CampusAssetTodo',
  data() {
    return {
      loading: true,
      dialogVisible: false,
      reviewAction: 'approve',
      currentBorrow: null,
      reviewForm: {
        reviewComment: ''
      },
      borrows: []
    }
  },
  computed: {
    reviewTitle() {
      return this.reviewAction === 'approve' ? '同意借用' : '驳回借用'
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listTodoAssetBorrows().then(response => {
        this.borrows = response.data || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    openReview(row, action) {
      this.currentBorrow = row
      this.reviewAction = action
      this.reviewForm = {
        reviewComment: action === 'approve' ? '同意借用' : '不符合借用条件'
      }
      this.dialogVisible = true
    },
    submitReview() {
      const request = this.reviewAction === 'approve' ? approveAssetBorrow : rejectAssetBorrow
      request(this.currentBorrow.borrowId, this.reviewForm).then(() => {
        this.$modal.msgSuccess('审批已处理')
        this.dialogVisible = false
        this.getList()
      })
    }
  }
}
</script>

<style scoped>
.campus-asset-page {
  background: #f5f7fb;
  min-height: calc(100vh - 84px);
}

.asset-card {
  border-radius: 6px;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}
</style>
