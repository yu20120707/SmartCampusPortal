<template>
  <div class="app-container home">
    <section class="hero">
      <div>
        <p class="eyebrow">SmartCampusPortal MVP</p>
        <h1>高校智慧校园门户</h1>
        <p class="summary">
          面向学生、教师、领导三类用户的统一信息服务入口，当前聚焦课程、成绩、考试、申请、缴费、一卡通、资产和运行驾驶舱的可演示闭环。
        </p>
      </div>
      <el-button type="primary" icon="el-icon-position" @click="goCampusPortal">
        进入校园门户
      </el-button>
    </section>

    <el-row :gutter="16">
      <el-col v-for="item in roleCards" :key="item.title" :xs="24" :md="8">
        <el-card shadow="never" class="role-card">
          <div class="role-header">
            <i :class="item.icon"></i>
            <span>{{ item.title }}</span>
          </div>
          <p>{{ item.description }}</p>
          <ul>
            <li v-for="feature in item.features" :key="feature">{{ feature }}</li>
          </ul>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="scope-card">
      <div slot="header" class="section-title">MVP 当前边界</div>
      <el-row :gutter="12">
        <el-col v-for="module in modules" :key="module.name" :xs="12" :sm="8" :lg="6">
          <div class="module-item">
            <strong>{{ module.name }}</strong>
            <span>{{ module.desc }}</span>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Index',
  data() {
    return {
      roleCards: [
        {
          title: '学生门户',
          icon: 'el-icon-user',
          description: '聚合学习、生活、缴费和申请入口，突出个人事项。',
          features: ['课程表', '我的成绩', '考试安排', '一卡通余额', '待缴费用', '学生申请']
        },
        {
          title: '教师门户',
          icon: 'el-icon-s-custom',
          description: '围绕任课、学生名单、成绩录入和办公申请展开。',
          features: ['任课课程', '教学安排', '成绩录入', '考试安排', '一卡通信息', '资产申请']
        },
        {
          title: '领导门户',
          icon: 'el-icon-data-analysis',
          description: '提供运行概览、审批待办和关键业务统计。',
          features: ['领导驾驶舱', '审批待办', '学工概览', '缴费统计', '资产统计', '一卡通交易']
        }
      ],
      modules: [
        { name: '统一门户', desc: '角色化首页和快捷入口' },
        { name: '教务', desc: '课程、课表、成绩、考试' },
        { name: '学工', desc: '档案、记录、概览' },
        { name: 'OA', desc: '申请、待办、审批' },
        { name: '一卡通', desc: '余额、流水、充值演示' },
        { name: '缴费', desc: '待缴、记录、支付演示' },
        { name: '资产', desc: '可借资产和审批' },
        { name: '驾驶舱', desc: '校园运行指标' }
      ]
    }
  },
  methods: {
    goCampusPortal() {
      this.$router.push('/campus/portal')
    }
  }
}
</script>

<style scoped>
.home {
  min-height: calc(100vh - 84px);
  background: #f5f7fb;
}

.hero {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  margin-bottom: 18px;
  padding: 28px 32px;
  border-radius: 8px;
  color: #fff;
  background: linear-gradient(135deg, #135d66 0%, #1f7a68 54%, #4f8a5f 100%);
}

.eyebrow {
  margin: 0 0 8px;
  font-size: 13px;
  letter-spacing: 0;
  opacity: .86;
}

.hero h1 {
  margin: 0;
  font-size: 28px;
  font-weight: 700;
}

.summary {
  max-width: 760px;
  margin: 12px 0 0;
  line-height: 1.7;
  opacity: .92;
}

.role-card,
.scope-card {
  margin-bottom: 16px;
  border-radius: 6px;
}

.role-header {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #1f2d3d;
  font-size: 16px;
  font-weight: 700;
}

.role-card p {
  min-height: 44px;
  color: #606266;
  line-height: 1.6;
}

.role-card ul {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 8px 12px;
  padding: 0;
  margin: 14px 0 0;
  list-style: none;
}

.role-card li {
  color: #303133;
  font-size: 13px;
}

.role-card li::before {
  content: "";
  display: inline-block;
  width: 6px;
  height: 6px;
  margin-right: 6px;
  border-radius: 50%;
  background: #1f7a68;
  vertical-align: 1px;
}

.section-title {
  font-weight: 700;
}

.module-item {
  min-height: 78px;
  padding: 14px;
  margin-bottom: 12px;
  border: 1px solid #e8edf5;
  border-radius: 6px;
  background: #fff;
}

.module-item strong,
.module-item span {
  display: block;
}

.module-item span {
  margin-top: 8px;
  color: #606266;
  font-size: 13px;
}

@media (max-width: 768px) {
  .hero {
    align-items: flex-start;
    flex-direction: column;
    padding: 24px;
  }

  .hero h1 {
    font-size: 24px;
  }
}
</style>
