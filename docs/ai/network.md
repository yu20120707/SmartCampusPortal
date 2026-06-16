# 网络通信

## 目的

描述本项目中各层之间的网络通信方式。

## 前端 ↔ 后端

- 通信协议：HTTP / HTTPS
- 请求方式：Axios（Vue）→ REST API（Spring Boot）
- 认证方式：JWT Token（通过请求头 `Authorization: Bearer <token>`）
- 前端开发代理：`ruoyi-ui/.env.development` 中配置 `VUE_APP_BASE_API` 代理到后端

## 后端架构

```
Vue Axios → Nginx（可选） → Spring Boot Controller → Service → Mapper → MySQL
                                                   ↓
                                              Redis（缓存）
```

## 跨域配置

RuoYi 已在 `ruoyi-framework/config/CorsConfig.java` 中配置了跨域支持。新增 Controller 不需要重复配置。

## 外部接口

本项目不对接真实外部系统，但预留了 Service 层的扩展点。未来如需对接真实教务/学工系统，可以在 Service 中接入 HTTP 客户端（RestTemplate / WebClient）。
