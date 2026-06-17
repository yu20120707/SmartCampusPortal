<template>
  <div class="app-container campus-asset-page">
    <el-card shadow="never" class="asset-card">
      <div slot="header" class="panel-header">
        <span>可借资产</span>
        <el-button size="mini" icon="el-icon-refresh" @click="loadData">刷新</el-button>
      </div>
      <el-table v-loading="loading" :data="assets" size="small">
        <el-table-column prop="assetNo" label="资产编号" width="150" />
        <el-table-column prop="assetName" label="资产名称" min-width="180" />
        <el-table-column prop="assetType" label="类型" width="110">
          <template slot-scope="scope">{{ typeLabel(scope.row.assetType) }}</template>
        </el-table-column>
        <el-table-column prop="location" label="位置" min-width="140" />
        <el-table-column prop="availableQuantity" label="可借数量" width="100" />
        <el-table-column label="操作" width="120" fixed="right">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="openBorrow(scope.row)">申请借用</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card shadow="never" class="asset-card">
      <div slot="header" class="panel-header">
        <span>我的借用申请</span>
      </div>
      <el-table :data="borrows" size="small">
        <el-table-column prop="applyTime" label="申请时间" width="160" />
        <el-table-column prop="assetName" label="资产" min-width="160" />
        <el-table-column prop="purpose" label="用途" min-width="220" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag size="mini" :type="statusTag(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="approverName" label="审批人" width="100" />
        <el-table-column prop="reviewComment" label="审批意见" min-width="180" show-overflow-tooltip />
      </el-table>
    </el-card>

    <el-dialog title="申请借用资产" :visible.sync="dialogVisible" width="520px">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="资产名称">
          <el-input v-model="currentAssetName" disabled />
        </el-form-item>
        <el-form-item label="借用用途" prop="purpose">
          <el-input v-model="form.purpose" type="textarea" :rows="4" maxlength="500" show-word-limit />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBorrow">提交申请</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAvailableAssets, listMyAssetBorrows, applyAssetBorrow } from '@/api/campus/asset'

export default {
  name: 'CampusAssetIndex',
  data() {
    return {
      loading: true,
      dialogVisible: false,
      assets: [],
      borrows: [],
      currentAssetName: '',
      form: {
        assetId: null,
        purpose: ''
      },
      rules: {
        purpose: [{ required: true, message: '请输入借用用途', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      Promise.all([listAvailableAssets(), listMyAssetBorrows()]).then(([assetResponse, borrowResponse]) => {
        this.assets = assetResponse.data || []
        this.borrows = borrowResponse.data || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    openBorrow(row) {
      this.currentAssetName = row.assetName
      this.form = {
        assetId: row.assetId,
        purpose: ''
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.form) {
          this.$refs.form.clearValidate()
        }
      })
    },
    submitBorrow() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        applyAssetBorrow(this.form).then(() => {
          this.$modal.msgSuccess('借用申请已提交')
          this.dialogVisible = false
          this.loadData()
        })
      })
    },
    typeLabel(type) {
      const labels = { device: '设备', room: '空间', book: '图书', other: '其他' }
      return labels[type] || type
    },
    statusLabel(status) {
      const labels = { 1: '待审批', 2: '已通过', 3: '已驳回' }
      return labels[status] || status
    },
    statusTag(status) {
      const tags = { 1: 'warning', 2: 'success', 3: 'danger' }
      return tags[status] || 'info'
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
  margin-bottom: 16px;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}
</style>
