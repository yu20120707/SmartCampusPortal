<template>
  <div class="app-container campus-page">
    <el-card shadow="never" class="campus-panel">
      <div slot="header" class="panel-header">
        <span>我的教务</span>
        <el-tag v-if="profile" size="mini">{{ profile.className }}</el-tag>
      </div>
      <el-descriptions v-if="profile" :column="isMobile ? 1 : 4" :size="isMobile ? 'mini' : 'small'" border>
        <el-descriptions-item label="姓名">{{ profile.studentName }}</el-descriptions-item>
        <el-descriptions-item label="学号">{{ profile.studentNo }}</el-descriptions-item>
        <el-descriptions-item label="学院">{{ profile.collegeName }}</el-descriptions-item>
        <el-descriptions-item label="专业">{{ profile.majorName }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card shadow="never" class="campus-panel">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="课程表" name="schedule">
          <el-table v-loading="loading" :data="schedule" :size="isMobile ? 'mini' : 'small'">
            <el-table-column prop="courseName" label="课程" min-width="150" />
            <el-table-column prop="teacherName" label="教师" width="100" />
            <el-table-column prop="weekday" label="星期" width="80">
              <template slot-scope="scope">周{{ scope.row.weekday }}</template>
            </el-table-column>
            <el-table-column label="节次" width="100">
              <template slot-scope="scope">{{ scope.row.startSection }}-{{ scope.row.endSection }}</template>
            </el-table-column>
            <el-table-column prop="classroom" label="地点" width="120" />
            <el-table-column prop="weekRange" label="周次" width="120" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="课程列表" name="courses">
          <el-table v-loading="loading" :data="courses" :size="isMobile ? 'mini' : 'small'">
            <el-table-column prop="courseCode" label="课程编码" width="120" />
            <el-table-column prop="courseName" label="课程" min-width="150" />
            <el-table-column prop="teacherName" label="教师" width="100" />
            <el-table-column prop="termName" label="学期" width="130" />
            <el-table-column prop="classroom" label="地点" width="120" />
            <el-table-column label="操作" width="90" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="text" @click="dropCourse(scope.row)">退课</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="选课中心" name="electives">
          <el-table v-loading="loading" :data="electives" :size="isMobile ? 'mini' : 'small'">
            <el-table-column prop="courseCode" label="课程编码" width="120" />
            <el-table-column prop="courseName" label="课程" min-width="150" />
            <el-table-column prop="teacherName" label="教师" width="100" />
            <el-table-column prop="weekday" label="星期" width="80">
              <template slot-scope="scope">周{{ scope.row.weekday }}</template>
            </el-table-column>
            <el-table-column label="节次" width="100">
              <template slot-scope="scope">{{ scope.row.startSection }}-{{ scope.row.endSection }}</template>
            </el-table-column>
            <el-table-column prop="classroom" label="地点" width="120" />
            <el-table-column prop="studentCount" label="已选人数" width="90" />
            <el-table-column label="操作" width="90" fixed="right">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="enrollCourse(scope.row)">选课</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="成绩" name="grades">
          <el-table v-loading="loading" :data="grades" :size="isMobile ? 'mini' : 'small'">
            <el-table-column prop="courseName" label="课程" min-width="150" />
            <el-table-column prop="termName" label="学期" width="130" />
            <el-table-column prop="credit" label="学分" width="80" />
            <el-table-column prop="score" label="成绩" width="90" />
            <el-table-column prop="gradePoint" label="绩点" width="90" />
            <el-table-column prop="examType" label="类型" width="90" />
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="考试" name="exams">
          <el-table v-loading="loading" :data="exams" :size="isMobile ? 'mini' : 'small'">
            <el-table-column prop="courseName" label="课程" min-width="150" />
            <el-table-column prop="examTime" label="考试时间" width="160" />
            <el-table-column prop="classroom" label="考场" width="120" />
            <el-table-column prop="seatNo" label="座位" width="80" />
            <el-table-column prop="examType" label="类型" width="90" />
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { getStudentProfile, listMyCourses, listMySchedule, listMyGrades, listMyExams, listAvailableElectives, enrollElective, dropElective } from '@/api/campus/academic'
import mobileMixin from '@/mixins/mobile'

export default {
  name: 'CampusAcademicStudent',
  mixins: [mobileMixin],
  data() {
    return {
      loading: true,
      activeTab: 'schedule',
      profile: null,
      courses: [],
      electives: [],
      schedule: [],
      grades: [],
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
        getStudentProfile(),
        listMyCourses(),
        listMySchedule(),
        listMyGrades(),
        listMyExams(),
        listAvailableElectives()
      ]).then(([profile, courses, schedule, grades, exams, electives]) => {
        this.profile = profile.data
        this.courses = courses.data || []
        this.schedule = schedule.data || []
        this.grades = grades.data || []
        this.exams = exams.data || []
        this.electives = electives.data || []
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    enrollCourse(row) {
      enrollElective(row.sectionId).then(() => {
        this.$modal.msgSuccess('选课成功')
        this.loadData()
      })
    },
    dropCourse(row) {
      this.$modal.confirm(`确认退选《${row.courseName}》吗？`).then(() => {
        return dropElective(row.sectionId)
      }).then(() => {
        this.$modal.msgSuccess('退课成功')
        this.loadData()
      }).catch(() => {})
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
