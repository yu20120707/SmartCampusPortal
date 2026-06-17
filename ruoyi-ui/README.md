## SmartCampusPortal Frontend

高校智慧校园门户前端，面向学生、教师、领导三类用户提供不同的门户首页、业务入口和聚合信息展示。

当前 MVP 聚焦 Web 端，移动端先不实现。

## Development

```bash
# 安装依赖
npm install

# 如国内网络较慢，可使用镜像源
npm install --registry=https://registry.npmmirror.com

# 启动开发服务
npm run dev
```

默认浏览器访问地址由 `port` 环境变量决定。本地联调时可通过 `VUE_APP_PROXY_TARGET` 指向后端服务。

## Build

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```

## Scope

- 学生：课程、成绩、考试、一卡通、缴费、申请、资产借用、学工信息。
- 教师：任课、成绩录入、考试安排、审批申请、资产借用、个人门户。
- 领导：驾驶舱统计、待办审批、资产/缴费/学工概览、全校数据看板。

扩展能力先记录在 `docs/ai/product-scope.md`，不在 MVP 内一次性做完。
