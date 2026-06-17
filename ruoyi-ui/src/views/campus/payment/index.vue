<template>
  <div class="app-container campus-payment-page">
    <el-row :gutter="16">
      <el-col :xs="24" :md="9">
        <el-card shadow="never" class="payment-card summary-card">
          <div class="summary-label">待缴总额</div>
          <div class="summary-value">¥ {{ pendingTotal }}</div>
          <div class="summary-meta">{{ pendingItems.length }} 个待缴项目</div>
          <div class="summary-note">当前为演示支付：只更新本系统缴费状态，不调用真实支付渠道。</div>
        </el-card>
      </el-col>

      <el-col :xs="24" :md="15">
        <el-card shadow="never" class="payment-card">
          <div slot="header" class="panel-header">
            <span>待缴项目</span>
            <el-button size="mini" icon="el-icon-refresh" @click="loadData">刷新</el-button>
          </div>
          <el-table v-loading="loading" :data="pendingItems" size="small">
            <el-table-column prop="itemNo" label="编号" width="150" />
            <el-table-column prop="itemName" label="项目" min-width="180" />
            <el-table-column prop="itemType" label="类型" width="100">
              <template slot-scope="scope">{{ typeLabel(scope.row.itemType) }}</template>
            </el-table-column>
            <el-table-column prop="amount" label="金额" width="110" />
            <el-table-column prop="dueDate" label="截止日期" width="120" />
            <el-table-column label="操作" width="120" fixed="right">
              <template slot-scope="scope">
                <el-button type="primary" size="mini" @click="confirmPay(scope.row)">演示支付</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="payment-card records-card">
      <div slot="header" class="panel-header">
        <span>缴费记录</span>
      </div>
      <el-table :data="records" size="small">
        <el-table-column prop="paidTime" label="支付时间" width="160" />
        <el-table-column prop="paymentNo" label="支付流水" width="180" />
        <el-table-column prop="itemName" label="项目" min-width="180" />
        <el-table-column prop="itemType" label="类型" width="100">
          <template slot-scope="scope">{{ typeLabel(scope.row.itemType) }}</template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="110" />
        <el-table-column prop="channel" label="渠道" width="100">
          <template slot-scope="scope">{{ scope.row.channel === 'demo' ? '演示' : scope.row.channel }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template slot-scope="scope">
            <el-tag size="mini" type="success">{{ scope.row.status === 'paid' ? '已支付' : scope.row.status }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { listPendingPaymentItems, listPaymentRecords, demoPayPaymentItem } from '@/api/campus/payment'

export default {
  name: 'CampusPaymentIndex',
  data() {
    return {
      loading: true,
      pendingItems: [],
      records: []
    }
  },
  computed: {
    pendingTotal() {
      const total = this.pendingItems.reduce((sum, item) => sum + Number(item.amount || 0), 0)
      return total.toFixed(2)
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      Promise.all([listPendingPaymentItems(), listPaymentRecords()]).then(([itemResponse, recordResponse]) => {
        this.pendingItems = itemResponse.data || []
        this.records = recordResponse.data || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    confirmPay(row) {
      this.$confirm('确认将该项目标记为已支付？此操作仅用于演示，不会调用真实支付渠道。', '演示支付', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return demoPayPaymentItem(row.itemId)
      }).then(() => {
        this.$modal.msgSuccess('演示支付成功')
        this.loadData()
      })
    },
    typeLabel(type) {
      const labels = { tuition: '学费', dorm: '住宿费', exam: '考试费', other: '其他' }
      return labels[type] || type
    }
  }
}
</script>

<style scoped>
.campus-payment-page {
  background: #f5f7fb;
  min-height: calc(100vh - 84px);
}

.payment-card {
  border-radius: 6px;
  margin-bottom: 16px;
}

.summary-card {
  min-height: 230px;
}

.summary-label {
  color: #7a869a;
  font-size: 13px;
}

.summary-value {
  color: #17233d;
  font-size: 38px;
  font-weight: 700;
  line-height: 1.4;
  margin: 10px 0;
}

.summary-meta {
  color: #606266;
  margin-bottom: 20px;
}

.summary-note {
  color: #909399;
  font-size: 12px;
  line-height: 1.6;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}

.records-card {
  margin-top: 2px;
}
</style>
