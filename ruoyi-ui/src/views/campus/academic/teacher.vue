<template>
  <div class="app-container campus-page">
    <el-card shadow="never" class="campus-panel">
      <div slot="header" class="panel-header">
        <span>任课视图</span>
        <el-tag v-if="profile" size="mini">{{ profile.title }}</el-tag>
      </div>
      <el-descriptions v-if="profile" :column="4" size="small" border>
        <el-descriptions-item label="姓名">{{ profile.teacherName }}</el-descriptions-item>
        <el-descriptions-item label="工号">{{ profile.teacherNo }}</el-descriptions-item>
        <el-descriptions-item label="学院">{{ profile.collegeName }}</el-descriptions-item>
        <el-descriptions-item label="职称">{{ profile.title }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-row :gutter="16">
      <el-col :xs="24" :lg="14">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">
            <span>教学安排</span>
          </div>
          <el-table v-loading="loading" :data="schedule" size="small">
            <el-table-column prop="courseName" label="课程" min-width="150" />
            <el-table-column prop="weekday" label="星期" width="80">
              <template slot-scope="scope">周{{ scope.row.weekday }}</template>
            </el-table-column>
            <el-table-column label="节次" width="100">
              <template slot-scope="scope">{{ scope.row.startSection }}-{{ scope.row.endSection }}</template>
            </el-table-column>
            <el-table-column prop="classroom" label="地点" width="120" />
            <el-table-column prop="studentCount" label="人数" width="80" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :xs="24" :lg="10">
        <el-card shadow="never" class="campus-panel">
          <div slot="header" class="panel-header">
            <span>考试安排</span>
          </div>
          <el-table v-loading="loading" :data="exams" size="small">
            <el-table-column prop="courseName" label="课程" min-width="130" />
            <el-table-column prop="examTime" label="时间" width="160" />
            <el-table-column prop="classroom" label="考场" width="110" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="campus-panel">
      <div slot="header" class="panel-header">
        <span>任课课程</span>
      </div>
      <el-table v-loading="loading" :data="courses" size="small">
        <el-table-column prop="courseCode" label="课程编码" width="120" />
        <el-table-column prop="courseName" label="课程" min-width="150" />
        <el-table-column prop="termName" label="学期" width="130" />
        <el-table-column prop="weekRange" label="周次" width="120" />
        <el-table-column prop="studentCount" label="选课人数" width="100" />
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getTeacherProfile, listTeachingCourses, listTeachingSchedule, listTeachingExams } from '@/api/campus/academic'

export default {
  name: 'CampusAcademicTeacher',
  data() {
    return {
      loading: true,
      profile: null,
      courses: [],
      schedule: [],
      exams: []
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      this.loading = true
      Promise.all([
        getTeacherProfile(),
        listTeachingCourses(),
        listTeachingSchedule(),
        listTeachingExams()
      ]).then(([profile, courses, schedule, exams]) => {
        this.profile = profile.data
        this.courses = courses.data || []
        this.schedule = schedule.data || []
        this.exams = exams.data || []
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
</style>
