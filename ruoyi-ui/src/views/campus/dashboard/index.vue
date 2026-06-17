<template>
  <div class="app-container campus-page">
    <el-row :gutter="12">
      <el-col v-for="card in cards" :key="card.title" :xs="12" :sm="6">
        <el-card shadow="never" class="metric-card">
          <div class="metric-title">{{ card.title }}</div>
          <div class="metric-value">
            {{ card.value }}
            <span>{{ card.unit }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="12">
      <el-col v-for="card in operationCards" :key="card.title" :xs="12" :sm="6">
        <el-card shadow="never" class="metric-card operation-card">
          <div class="metric-title">{{ card.title }}</div>
          <div class="metric-value">
            {{ card.value }}
            <span>{{ card.unit }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">课程类型分布</div>
          <el-table v-loading="loading" :data="courseTypeStats" size="small">
            <el-table-column prop="name" label="类型" />
            <el-table-column prop="value" label="数量" width="90" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">学院学生分布</div>
          <el-table v-loading="loading" :data="collegeStudentStats" size="small">
            <el-table-column prop="name" label="学院" />
            <el-table-column prop="value" label="人数" width="90" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">平均成绩趋势</div>
          <el-table v-loading="loading" :data="scoreTrend" size="small">
            <el-table-column prop="name" label="学期" />
            <el-table-column prop="value" label="平均分" width="90" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="6">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">审批运行概览</div>
          <el-table v-loading="loading" :data="approvalStats" size="small">
            <el-table-column prop="name" label="指标" />
            <el-table-column prop="value" label="数量" width="90" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="6">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">缴费运行概览</div>
          <el-table v-loading="loading" :data="paymentStats" size="small">
            <el-table-column prop="name" label="指标" />
            <el-table-column prop="value" label="数值" width="90" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="6">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">一卡通交易概览</div>
          <el-table v-loading="loading" :data="cardTransactionStats" size="small">
            <el-table-column prop="name" label="类型" />
            <el-table-column prop="value" label="笔数" width="90" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="6">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">资产使用概览</div>
          <el-table v-loading="loading" :data="assetStats" size="small">
            <el-table-column prop="name" label="指标" />
            <el-table-column prop="value" label="数量" width="90" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getLeaderDashboard } from '@/api/campus/dashboard'

export default {
  name: 'CampusDashboard',
  data() {
    return {
      loading: true,
      dashboard: {}
    }
  },
  computed: {
    cards() {
      return this.dashboard.cards || []
    },
    courseTypeStats() {
      return this.dashboard.courseTypeStats || []
    },
    collegeStudentStats() {
      return this.dashboard.collegeStudentStats || []
    },
    scoreTrend() {
      return this.dashboard.scoreTrend || []
    },
    operationCards() {
      return this.dashboard.operationCards || []
    },
    approvalStats() {
      return this.dashboard.approvalStats || []
    },
    paymentStats() {
      return this.dashboard.paymentStats || []
    },
    cardTransactionStats() {
      return this.dashboard.cardTransactionStats || []
    },
    assetStats() {
      return this.dashboard.assetStats || []
    }
  },
  created() {
    this.getDashboard()
  },
  methods: {
    getDashboard() {
      this.loading = true
      getLeaderDashboard().then(response => {
        this.dashboard = response.data || {}
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
.campus-page {
  background: #f5f7fb;
  min-height: calc(100vh - 84px);
}

.metric-card,
.campus-panel {
  margin-bottom: 16px;
  border-radius: 6px;
}

.metric-title {
  color: #606266;
  font-size: 13px;
}

.metric-value {
  margin-top: 12px;
  color: #1f2d3d;
  font-size: 28px;
  font-weight: 700;
}

.metric-value span {
  margin-left: 4px;
  color: #909399;
  font-size: 13px;
  font-weight: 400;
}

.operation-card {
  border-left: 3px solid #409eff;
}

.panel-header {
  font-weight: 600;
}
</style>
