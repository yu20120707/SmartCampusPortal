<template>
  <div class="app-container campus-student-page">
    <el-card shadow="never" class="student-card">
      <div slot="header" class="panel-header">
        <span>我的学工档案</span>
        <el-tag v-if="profile" size="mini">{{ profile.counselorName }}</el-tag>
      </div>
      <el-descriptions v-if="profile" :column="isMobile ? 1 : 3" size="small" border>
        <el-descriptions-item label="姓名">{{ profile.studentName }}</el-descriptions-item>
        <el-descriptions-item label="学号">{{ profile.studentNo }}</el-descriptions-item>
        <el-descriptions-item label="班级">{{ profile.className }}</el-descriptions-item>
        <el-descriptions-item label="学院">{{ profile.collegeName }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ profile.majorName }}</el-descriptions-item>
        <el-descriptions-item label="政治面貌">{{ profile.politicalStatus }}</el-descriptions-item>
        <el-descriptions-item label="宿舍">{{ profile.dormitory }}</el-descriptions-item>
        <el-descriptions-item label="紧急联系人">{{ profile.emergencyContact }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ profile.emergencyPhone }}</el-descriptions-item>
        <el-descriptions-item label="状态摘要" :span="3">{{ profile.statusSummary }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card shadow="never" class="student-card">
      <div slot="header" class="panel-header">
        <span>学工记录</span>
        <el-tag size="mini" type="success">{{ records.length }} 条</el-tag>
      </div>
      <el-table v-loading="loading" :data="records" :size="isMobile ? 'mini' : 'small'">
        <el-table-column prop="recordDate" label="日期" width="120" />
        <el-table-column prop="recordType" label="类型" width="100">
          <template slot-scope="scope">{{ formatType(scope.row.recordType) }}</template>
        </el-table-column>
        <el-table-column prop="title" label="事项" min-width="180" />
        <el-table-column prop="levelName" label="级别" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">{{ scope.row.status === 'active' ? '有效' : scope.row.status }}</template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getMyStudentAffairsProfile, listMyStudentAffairsRecords } from '@/api/campus/student'
import mobileMixin from '@/mixins/mobile'

export default {
  name: 'CampusStudentMy',
  mixins: [mobileMixin],
  data() {
    return {
      loading: true,
      profile: null,
      records: []
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      Promise.all([
        getMyStudentAffairsProfile(),
        listMyStudentAffairsRecords()
      ]).then(([profile, records]) => {
        this.profile = profile.data
        this.records = records.data || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    formatType(type) {
      const labels = {
        honor: '荣誉',
        aid: '资助',
        discipline: '处分',
        practice: '实践'
      }
      return labels[type] || type
    }
  }
}
</script>

<style scoped>
.campus-student-page {
  background: #f5f7fb;
  min-height: calc(100vh - 84px);
}

.student-card {
  margin-bottom: 16px;
  border-radius: 8px;
  transition: box-shadow .2s ease;
}

.student-card:hover {
  box-shadow: 0 2px 8px rgba(13,124,107,.05);
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-weight: 600;
}
</style>
