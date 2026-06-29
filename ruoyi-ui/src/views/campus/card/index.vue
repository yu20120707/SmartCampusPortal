<template>
  <div class="app-container campus-card-page">
    <el-row :gutter="16">
      <el-col :xs="24" :md="8">
        <el-card shadow="never" class="card-panel balance-card">
          <div class="card-label">一卡通余额</div>
          <div class="balance">¥ {{ account.balance || '0.00' }}</div>
          <div class="meta">{{ account.holderName || '-' }} · {{ account.cardNo || '未开卡' }}</div>
          <el-tag size="mini" :type="account.status === '0' ? 'success' : 'info'">
            {{ account.status === '0' ? '正常' : '停用' }}
          </el-tag>
          <el-button class="recharge-button" type="primary" size="small" @click="dialogVisible = true">
            演示充值
          </el-button>
        </el-card>
      </el-col>

      <el-col :xs="24" :md="16">
        <el-card shadow="never" class="card-panel">
          <div slot="header" class="panel-header">
            <span>消费 / 充值流水</span>
            <el-button size="mini" icon="el-icon-refresh" @click="loadData">刷新</el-button>
          </div>
          <el-table v-loading="loading" :data="transactions" :size="isMobile ? 'mini' : 'small'">
            <el-table-column prop="transactionTime" label="时间" width="160" />
            <el-table-column prop="sceneName" label="场景" min-width="160" />
            <el-table-column prop="transactionType" label="类型" width="100">
              <template slot-scope="scope">{{ typeLabel(scope.row.transactionType) }}</template>
            </el-table-column>
            <el-table-column prop="amount" label="金额" width="110">
              <template slot-scope="scope">
                <span :class="scope.row.transactionType === 'consume' ? 'amount-out' : 'amount-in'">
                  {{ scope.row.transactionType === 'consume' ? '-' : '+' }}{{ scope.row.amount }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="balanceAfter" label="交易后余额" width="120" />
            <el-table-column prop="status" label="状态" width="90">
              <template slot-scope="scope">
                <el-tag size="mini" type="success">{{ scope.row.status === 'success' ? '成功' : scope.row.status }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog title="一卡通演示充值" :visible.sync="dialogVisible" :fullscreen="isMobile" width="420px">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="充值金额" prop="amount">
          <el-input-number v-model="form.amount" :min="0.01" :max="1000" :precision="2" :step="10" style="width: 100%" />
        </el-form-item>
      </el-form>
      <div class="hint">当前为门户演示充值，只写入本系统一卡通账户和流水，不调用真实支付渠道。</div>
      <div slot="footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitRecharge">确认充值</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCardAccount, listCardTransactions, rechargeCard } from '@/api/campus/card'
import mobileMixin from '@/mixins/mobile'

export default {
  name: 'CampusCardIndex',
  mixins: [mobileMixin],
  data() {
    return {
      loading: true,
      dialogVisible: false,
      account: {},
      transactions: [],
      form: {
        amount: 20
      },
      rules: {
        amount: [{ required: true, message: '请输入充值金额', trigger: 'change' }]
      }
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      Promise.all([getCardAccount(), listCardTransactions()]).then(([accountResponse, txResponse]) => {
        this.account = accountResponse.data || {}
        this.transactions = txResponse.data || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    submitRecharge() {
      this.$refs.form.validate(valid => {
        if (!valid) {
          return
        }
        rechargeCard({ amount: this.form.amount }).then(() => {
          this.$modal.msgSuccess('充值成功')
          this.dialogVisible = false
          this.loadData()
        })
      })
    },
    typeLabel(type) {
      const labels = { recharge: '充值', consume: '消费', refund: '退款' }
      return labels[type] || type
    }
  }
}
</script>

<style scoped>
.campus-card-page {
  background: #f5f7fb;
  min-height: calc(100vh - 84px);
}

.card-panel {
  border-radius: 8px;
  transition: box-shadow .2s ease;
}

.card-panel:hover {
  box-shadow: 0 2px 8px rgba(13,124,107,.05);
}

.balance-card {
  min-height: 260px;
}

.card-label {
  color: #7a869a;
  font-size: 13px;
}

.balance {
  color: #17233d;
  font-size: 40px;
  font-weight: 700;
  line-height: 1.3;
  margin: 10px 0;
  letter-spacing: -0.5px;
}

.meta {
  color: #606266;
  margin-bottom: 16px;
}

.recharge-button {
  display: block;
  margin-top: 28px;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}

.amount-in {
  color: #13a86b;
  font-weight: 600;
}

.amount-out {
  color: #e05555;
  font-weight: 600;
}

.hint {
  color: #909399;
  font-size: 12px;
  line-height: 1.6;
}

/* Mobile adaptation */
@media screen and (max-width: 991px) {
  .balance {
    font-size: 30px;
  }
}
</style>
