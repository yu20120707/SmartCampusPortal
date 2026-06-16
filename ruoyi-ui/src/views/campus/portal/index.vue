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
</style>
