<template>
  <div class="app-container campus-student-page">
    <el-row :gutter="16">
      <el-col :xs="24" :md="8" v-for="item in recordStats" :key="item.name">
        <el-card shadow="never" class="metric-card">
          <div class="metric-title">{{ item.name }}</div>
          <div class="metric-value">{{ item.value }} <span>条</span></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="student-card">
      <div slot="header" class="panel-header">
        <span>学工档案概览</span>
        <el-tag size="mini">{{ profiles.length }} 名学生</el-tag>
      </div>
      <el-table v-loading="loading" :data="profiles" size="small">
        <el-table-column prop="studentNo" label="学号" width="110" />
        <el-table-column prop="studentName" label="姓名" width="100" />
        <el-table-column prop="collegeName" label="学院" min-width="140" />
        <el-table-column prop="majorName" label="专业" min-width="140" />
        <el-table-column prop="className" label="班级" width="110" />
        <el-table-column prop="counselorName" label="辅导员" width="110" />
        <el-table-column prop="dormitory" label="宿舍" width="130" />
        <el-table-column prop="statusSummary" label="状态摘要" min-width="240" show-overflow-tooltip />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getStudentAffairsOverview } from '@/api/campus/student'

export default {
  name: 'CampusStudentOverview',
  data() {
    return {
      loading: true,
      profiles: [],
      recordStats: []
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      getStudentAffairsOverview().then(response => {
        const data = response.data || {}
        this.profiles = data.profiles || []
        this.recordStats = data.recordStats || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped>
.campus-student-page {
  background: #f5f7fb;
  min-height: calc(100vh - 84px);
}

.student-card,
.metric-card {
  margin-bottom: 16px;
  border-radius: 6px;
}

.metric-title {
  color: #606266;
  font-size: 13px;
}

.metric-value {
  margin-top: 8px;
  color: #303133;
  font-size: 26px;
  font-weight: 600;
}

.metric-value span {
  font-size: 13px;
  font-weight: 400;
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}
</style>
