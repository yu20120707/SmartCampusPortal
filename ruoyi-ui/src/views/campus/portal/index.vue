<template>
  <div class="app-container campus-page">
    <el-row :gutter="16">
      <el-col :xs="24" :md="16">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">
            <span>校园门户</span>
            <el-tag size="mini" type="success">{{ roleLabel }}</el-tag>
          </div>
          <el-row :gutter="12">
            <el-col v-for="card in cards" :key="card.title" :xs="12" :sm="8" :md="6">
              <div class="metric-card">
                <div class="metric-title">{{ card.title }}</div>
                <div class="metric-value">
                  {{ card.value }}
                  <span>{{ card.unit }}</span>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
      <el-col :xs="24" :md="8">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">
            <span>快捷入口</span>
          </div>
          <el-button
            v-for="shortcut in shortcuts"
            :key="shortcut.path"
            class="shortcut-button"
            icon="el-icon-position"
            plain
            @click="go(shortcut.path)"
          >
            {{ shortcut.title }}
          </el-button>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">
            <span>公告通知</span>
            <el-tag size="mini">公告</el-tag>
          </div>
          <div v-if="announcements.length" class="portal-list">
            <div v-for="item in announcements" :key="'notice-' + item.noticeId" class="portal-list-item">
              <div class="item-title">{{ item.title }}</div>
              <div class="item-meta">{{ item.createTime || '刚刚' }}</div>
            </div>
          </div>
          <div v-else class="empty-text">暂无公告</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">
            <span>校园新闻</span>
            <el-tag size="mini" type="success">新闻</el-tag>
          </div>
          <div v-if="news.length" class="portal-list">
            <div v-for="item in news" :key="'news-' + item.noticeId" class="portal-list-item">
              <div class="item-title">{{ item.title }}</div>
              <div class="item-meta">{{ item.createTime || '刚刚' }}</div>
            </div>
          </div>
          <div v-else class="empty-text">暂无新闻</div>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="8">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">
            <span>我的待办</span>
            <el-tag size="mini" type="warning">{{ todoItems.length }} 项</el-tag>
          </div>
          <div v-if="todoItems.length" class="portal-list">
            <div
              v-for="item in todoItems"
              :key="item.type + item.title + item.time"
              class="portal-list-item clickable"
              @click="go(item.path)"
            >
              <div class="item-title">{{ item.title }}</div>
              <div class="item-meta">{{ item.type }} · {{ item.subtitle }} · {{ item.time || '待处理' }}</div>
            </div>
          </div>
          <div v-else class="empty-text">暂无待办</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">
            <span>最近消费</span>
            <el-tag size="mini" type="info">一卡通</el-tag>
          </div>
          <el-table v-loading="loading" :data="recentTransactions" size="small">
            <el-table-column prop="sceneName" label="场景" min-width="130" />
            <el-table-column prop="transactionType" label="类型" width="90">
              <template slot-scope="scope">{{ transactionTypeLabel(scope.row.transactionType) }}</template>
            </el-table-column>
            <el-table-column prop="amount" label="金额" width="90">
              <template slot-scope="scope">￥{{ scope.row.amount }}</template>
            </el-table-column>
            <el-table-column prop="transactionTime" label="时间" width="160" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="12">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">
            <span>待缴费</span>
            <el-tag size="mini" type="danger">{{ pendingPayments.length }} 项</el-tag>
          </div>
          <el-table v-loading="loading" :data="pendingPayments" size="small">
            <el-table-column prop="itemName" label="项目" min-width="150" />
            <el-table-column prop="amount" label="金额" width="100">
              <template slot-scope="scope">￥{{ scope.row.amount }}</template>
            </el-table-column>
            <el-table-column prop="dueDate" label="截止日期" width="120" />
            <el-table-column label="操作" width="90">
              <template>
                <el-button type="text" size="mini" @click="go('/campus/payment')">去缴费</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="14">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">
            <span>近期课表</span>
          </div>
          <el-table v-loading="loading" :data="todaySchedule" size="small">
            <el-table-column prop="courseName" label="课程" min-width="140" />
            <el-table-column prop="teacherName" label="教师" width="100" />
            <el-table-column prop="weekday" label="星期" width="80">
              <template slot-scope="scope">周{{ scope.row.weekday }}</template>
            </el-table-column>
            <el-table-column label="节次" width="100">
              <template slot-scope="scope">{{ scope.row.startSection }}-{{ scope.row.endSection }}</template>
            </el-table-column>
            <el-table-column prop="classroom" label="地点" width="120" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">
            <span>考试安排</span>
          </div>
          <el-table v-loading="loading" :data="upcomingExams" size="small">
            <el-table-column prop="courseName" label="课程" min-width="130" />
            <el-table-column prop="examTime" label="时间" width="160" />
            <el-table-column prop="classroom" label="考场" width="110" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getCurrentPortal } from '@/api/campus/portal'

export default {
  name: 'CampusPortal',
  data() {
    return {
      loading: true,
      portal: {}
    }
  },
  computed: {
    roleLabel() {
      const labels = { student: '学生', teacher: '教师', leader: '领导', admin: '管理员' }
      return labels[this.portal.roleType] || '校园用户'
    },
    cards() {
      return this.portal.cards || []
    },
    shortcuts() {
      return this.portal.shortcuts || []
    },
    todaySchedule() {
      return this.portal.todaySchedule || []
    },
    upcomingExams() {
      return this.portal.upcomingExams || []
    },
    announcements() {
      return this.portal.announcements || []
    },
    news() {
      return this.portal.news || []
    },
    todoItems() {
      return this.portal.todoItems || []
    },
    recentTransactions() {
      return this.portal.recentTransactions || []
    },
    pendingPayments() {
      return this.portal.pendingPayments || []
    }
  },
  created() {
    this.getPortal()
  },
  methods: {
    getPortal() {
      this.loading = true
      getCurrentPortal().then(response => {
        this.portal = response.data || {}
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    go(path) {
      this.$router.push(path)
    },
    transactionTypeLabel(type) {
      const labels = { consume: '消费', recharge: '充值', refund: '退款' }
      return labels[type] || type || '-'
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
  margin-bottom: 16px;
  border-radius: 6px;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}

.metric-card {
  min-height: 88px;
  padding: 16px;
  margin-bottom: 12px;
  border: 1px solid #e8edf5;
  border-radius: 6px;
  background: #fff;
}

.metric-title {
  color: #606266;
  font-size: 13px;
}

.metric-value {
  margin-top: 12px;
  color: #1f2d3d;
  font-size: 26px;
  font-weight: 700;
}

.metric-value span {
  margin-left: 4px;
  color: #909399;
  font-size: 13px;
  font-weight: 400;
}

.shortcut-button {
  width: 100%;
  margin: 0 0 10px 0;
  text-align: left;
}

.portal-list {
  min-height: 172px;
}

.portal-list-item {
  padding: 10px 0;
  border-bottom: 1px solid #edf1f7;
}

.portal-list-item:last-child {
  border-bottom: 0;
}

.portal-list-item.clickable {
  cursor: pointer;
}

.portal-list-item.clickable:hover .item-title {
  color: #168060;
}

.item-title {
  color: #303133;
  font-size: 14px;
  font-weight: 600;
  line-height: 1.5;
}

.item-meta {
  margin-top: 4px;
  color: #909399;
  font-size: 12px;
}

.empty-text {
  min-height: 172px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 13px;
}
</style>
