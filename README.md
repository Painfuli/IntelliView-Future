# 智面未来 (IntelliView Future) - AI驱动的全栈招聘面试平台

<p align="center">
  <img src="https://img.shields.io/badge/SpringBoot-3.x-green.svg" alt="SpringBoot">
  <img src="https://img.shields.io/badge/Vue.js-3.x-blue.svg" alt="Vue.js">
  <img src="https://img.shields.io/badge/MySQL-8.0-orange.svg" alt="MySQL">
  <img src="https://img.shields.io/badge/Java-17+-red.svg" alt="Java">
  <img src="https://img.shields.io/badge/AI-讯飞星火-purple.svg" alt="AI">
</p>

<p align="center">
  <strong>一个融合了AI技术的多模态现代化招聘面试平台</strong><br>
  基于 Spring Boot + Vue.js 构建的前后端分离架构，深度集成讯飞星火AI大模型<br>
  为求职者提供智能化的求职体验和专业的AI模拟面试服务
</p>

<p align="center">
  <a href="#-快速开始">快速开始</a> •
  <a href="#-功能特色">功能特色</a> •
  <a href="#-技术架构">技术架构</a> •
  <a href="#-数据库配置">数据库配置</a> •
  <a href="#-部署指南">部署指南</a> •
  <a href="#-api文档">API文档</a> •
  <a href="#-特别感谢">特别感谢</a> 
</p>

---

## 🌟 项目概述

智面未来是一个现代化的智能招聘平台，致力于通过AI技术革新传统招聘流程。平台不仅提供完整的招聘生态系统（职位发布、简历管理、投递追踪），更**首创性地推出了AI语音模拟面试**功能，帮助求职者在真实面试前进行充分准备和能力提升。

### 🎯 设计理念

- **智能化**: 深度整合AI技术，提供个性化的求职体验和智能面试辅导
- **专业化**: 基于行业最佳实践，构建稳定可靠的系统架构
- **人性化**: 关注用户体验，简化复杂的招聘流程，降低面试焦虑
- **开放性**: 模块化设计，支持功能扩展和定制

## ✨ 功能特色

### 🔍 核心招聘功能

- **多角色权限管理**: 求职者、企业、管理员三方独立权限体系
- **智能职位发布**: 企业可发布职位，支持富文本编辑，平台审核机制
- **在线简历系统**: 支持多份简历创建、在线编辑、一键投递
- **应用状态追踪**: 实时跟踪投递状态，透明化招聘流程
- **个性化推荐**: 基于协同过滤算法，智能推荐匹配职位

### 🤖 AI模拟面试 (核心创新)

> **🎉 正式发布**:
>
> AI多模态面试模块现已完整集成到本次发布版本中！这是本项目的最大创新亮点，采用讯飞星火V3.5大模型驱动的智能面试系统。
>
> ✅ **完整功能**: 包含语音识别、智能对话、实时反馈、面试总结等全套功能
>
> 🚀 **立即体验**: 克隆项目并按照配置指南设置API密钥即可开始使用

---

> 革命性的AI面试体验，让求职者在安全的环境中进行真实的面试模拟

#### 🎭 面试类型支持
- **技术面试**: 重点考察编程能力、技术深度、系统设计思维
- **HR面试**: 评估沟通表达、团队协作、解决冲突能力
- **综合面试**: 技术能力与软技能的平衡考察
- **管理面试**: 针对领导岗位的管理思维与战略能力评估

#### 🎚️ 难度等级
- **初级 (1-2年经验)**: 基础概念与简单应用
- **中级 (3-5年经验)**: 深入技能与项目经验
- **高级 (5+年经验)**: 架构设计与领导能力

#### 🕐 时长设置
- **15分钟**: 快速能力评估
- **30分钟**: 标准面试流程
- **45分钟**: 深度技能交流
- **60分钟**: 全面综合评估

#### 🔊 技术实现
- **实时语音识别**: 基于Web Speech API
- **智能对话生成**: 讯飞星火大模型V3.5
- **语音合成**: 讯飞TTS，自然流畅的AI面试官声音
- **面试总结**: AI自动生成专业反馈报告

### 📊 数据分析看板

- **用户增长趋势**: 平台用户注册与活跃度分析
- **职位发布统计**: 各行业、地区职位分布情况
- **投递成功率**: 简历投递与面试邀请转化率
- **ECharts可视化**: 直观的图表展示，支持多维度数据钻取

## 🏗️ 技术架构

### 系统架构图

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   前端展示层     │    │   API网关层     │    │   服务层         │
│                 │    │                 │    │                 │
│  Vue.js 3       │◄──►│  Spring Boot    │◄──►│  Services       │
│  Element Plus   │    │  RESTful API    │    │  Logic          │
│  Vite           │    │  JWT Auth       │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                              │
                       ┌─────────────────┐    │    ┌─────────────────┐
                       │   数据持久层     │◄───┘    │   外部服务      │
                       │                 │         │                │
                       │  MySQL 8.0      │         │  讯飞星火 API   │
                       │  MyBatis        │         │  讯飞 TTS       │
                       │  连接池         │         │  Web Speech     │
                       └─────────────────┘         └─────────────────┘
```

### 🛠️ 技术栈详情

#### 后端技术栈

| 技术栈 | 版本 | 用途 |
|--------|------|------|
| **Spring Boot** | 3.x | 核心框架，提供自动配置和嵌入式服务器 |
| **Spring Web** | 3.x | RESTful API开发，MVC架构支持 |
| **Spring Security** | 3.x | 安全框架，JWT token验证 |
| **MyBatis** | 3.x | ORM框架，数据库操作 |
| **MySQL** | 8.0 | 主数据库，存储业务数据 |
| **PageHelper** | 最新版 | 分页插件，优化查询性能 |
| **Hutool** | 最新版 | Java工具库，简化开发 |
| **Maven** | 3.8+ | 项目构建与依赖管理 |

#### 前端技术栈

| 技术栈 | 版本 | 用途 |
|--------|------|------|
| **Vue.js** | 3.x | 渐进式前端框架，组合式API |
| **Vue Router** | 4.x | 单页应用路由管理 |
| **Element Plus** | 最新版 | UI组件库，企业级界面设计 |
| **Axios** | 最新版 | HTTP客户端，API调用 |
| **ECharts** | 5.x | 数据可视化图表库 |
| **WangEditor** | 5.x | 富文本编辑器 |
| **Vite** | 4.x | 现代化构建工具，极速热更新 |

#### AI与集成服务

| 服务 | 用途 | 特点 |
|------|------|------|
| **讯飞星火大模型 V3.5** | 对话生成、面试问题生成、总结反馈 | 中文优化，专业面试场景 |
| **讯飞语音合成 (TTS)** | AI面试官语音输出 | 自然流畅，多音色支持 |
| **Web Speech API** | 用户语音识别 | 浏览器原生支持，实时识别 |

## 📁 项目结构

```
IntelliView-Future/
├── 📂 springboot/                    # 后端Spring Boot项目
│   ├── 📂 src/main/java/
│   │   └── 📂 com/example/xmjob/
│   │       ├── 📂 controller/        # 控制器层
│   │       ├── 📂 service/          # 业务逻辑层
│   │       ├── 📂 mapper/           # 数据访问层
│   │       ├── 📂 entity/           # 实体类
│   │       ├── 📂 config/           # 配置类
│   │       └── 📂 utils/            # 工具类
│   ├── 📂 src/main/resources/
│   │   ├── 📂 mapper/               # MyBatis XML映射文件
│   │   └── 📄 application.yml       # 应用配置文件
│   └── 📄 pom.xml                   # Maven依赖配置
│
├── 📂 vue/                          # 前端Vue.js项目
│   ├── 📂 src/
│   │   ├── 📂 components/           # 可复用组件
│   │   ├── 📂 views/                # 页面视图组件
│   │   ├── 📂 router/               # 路由配置
│   │   ├── 📂 api/                  # API接口封装
│   │   ├── 📂 utils/                # 前端工具函数
│   │   └── 📄 main.js               # 应用入口
│   ├── 📄 package.json              # npm依赖配置
│   └── 📄 vite.config.js            # Vite构建配置
│
├── 📂 docs/                         # 项目文档
├── 📂 database/                     # 数据库相关文件
│   ├── 📄 xm_job.sql                # 数据库初始化脚本
│   └── 📄 database_design.md        # 数据库设计文档
└── 📄 README.md                     # 项目说明文档

✅ **AI面试模块**:
├── 📄 AiMockInterview.vue           # AI模拟面试主界面 (已发布)
├── 📄 InterviewSetup.vue            # AI面试设置组件 (已发布)
└── 📄 InterviewSummary.vue          # 面试总结报告组件 (已发布)
```

## 🚀 快速开始

### 环境要求

确保您的开发环境满足以下要求：

| 软件 | 最低版本 | 推荐版本 | 下载地址 |
|------|----------|----------|----------|
| **Java JDK** | 17 | 21 | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) |
| **Maven** | 3.8 | 3.9+ | [Apache Maven](https://maven.apache.org/download.cgi) |
| **Node.js** | 18 | 20+ | [Node.js Official](https://nodejs.org/) |
| **MySQL** | 5.7 | 8.0+ | [MySQL Community](https://dev.mysql.com/downloads/) |

### 🚀 快速启动流程

1. **克隆项目**
   ```bash
   git clone https://github.com/painfuli/IntelliView-Future.git
   cd IntelliView-Future
   ```

2. **配置数据库** (参见下方数据库配置章节)

3. **配置AI服务** (必需)
   ```bash
   # 编辑前端AI配置文件
   vim vue/src/views/AiMockInterview.vue
   # 填入您的讯飞API密钥
   ```

4. **启动后端**
   ```bash
   cd springboot && mvn spring-boot:run
   ```

5. **启动前端**
   ```bash
   cd vue && npm install && npm run dev
   ```

6. **访问系统**: http://localhost:5173

## 🗄️ 数据库配置

### 📋 数据库表结构

项目包含以下核心数据库表：

| 表名 | 用途 | 重要字段 |
|------|------|----------|
| **user** | 用户信息表 | id, username, password, name, avatar, role |
| **admin** | 管理员表 | id, username, password, name, role |
| **employ** | 企业信息表 | id, name, city, industry_id, scale, stage |
| **position** | 职位信息表 | id, name, employ_id, industry_id, salary, content |
| **resume** | 简历信息表 | id, name, user_id, edu_exps, work_exps, pro_exps |
| **submit** | 简历投递表 | id, user_id, position_id, resume_id, status |
| **collect** | 收藏信息表 | id, student_id, position_id |
| **industry** | 行业信息表 | id, name, content |
| **notice** | 系统公告表 | id, title, content, time |
| **advertise** | 广告信息表 | id, img, position_id, location |

#### 🤖 AI面试相关表
| 表名 | 用途 | 重要字段 |
|------|------|----------|
| **ai_interview_session** | AI面试会话表 | id(UUID), user_id, job_title, status |
| **ai_interview_message** | AI面试消息表 | id, session_id, role, content, message_order |
| **interview_scenario** | 面试场景表 | id, position_name, difficulty, core_competencies |

### 🔧 数据库环境搭建

#### 方式一：使用提供的SQL脚本（推荐）

1. **创建数据库**
   ```sql
   CREATE DATABASE xm_job CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **导入数据库结构和初始数据**
   ```bash
   # 使用命令行导入
   mysql -u root -p xm_job < database/xm_job.sql
   
   # 或者使用图形化工具（如Navicat、phpMyAdmin）导入 xm_job.sql 文件
   ```

#### 方式二：手动创建（可选）

如果希望手动创建数据库结构，可以参考项目中的 `database/xm_job.sql` 文件。

### ⚙️ 后端数据库配置

编辑 `springboot/src/main/resources/application.yml` 文件：

```yaml
spring:
  # 数据库配置
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/xm_job?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false&allowMultiQueries=true&nullCatalogMeansCurrent=true
    username: root          # 修改为你的MySQL用户名
    password: your_password # 修改为你的MySQL密码
    
  # 连接池配置
  hikari:
    pool-name: DatebookHikariCP
    minimum-idle: 5
    idle-timeout: 180000
    maximum-pool-size: 10
    auto-commit: true
    max-lifetime: 1800000
    connection-timeout: 30000
    connection-test-query: SELECT 1

# MyBatis配置
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.xmjob.entity
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    map-underscore-to-camel-case: true
    
# 分页插件配置
pagehelper:
  helper-dialect: mysql
  reasonable: true
  support-methods-arguments: true
  params: count=countSql
```

### 🔐 数据库连接说明

#### 默认配置

- **主机**: localhost
- **端口**: 3306
- **数据库**: xm_job
- **默认用户名**: root
- **密码**: 需要设置

#### 连接参数说明

| 参数 | 说明 |
|------|------|
| `serverTimezone=GMT%2B8` | 设置时区为东八区 |
| `useUnicode=true` | 使用Unicode编码 |
| `characterEncoding=utf8` | 字符编码设置 |
| `autoReconnect=true` | 自动重连 |
| `useSSL=false` | 禁用SSL（开发环境） |
| `allowMultiQueries=true` | 允许多语句查询 |
| `nullCatalogMeansCurrent=true` | 兼容MySQL 8.0+ |

### 🔍 数据库验证

启动后端服务后，检查控制台日志：

```bash
# 成功连接的标志
2024-08-08 15:57:42 [INFO] HikariPool-1 - Starting...
2024-08-08 15:57:42 [INFO] HikariPool-1 - Start completed.
2024-08-08 15:57:42 [INFO] Started Application in 3.456 seconds
```

### ⚠️ 常见数据库问题

#### 问题1：连接超时
```bash
com.mysql.cj.jdbc.exceptions.CommunicationsException: Communications link failure
```
**解决方案**:
- 检查MySQL服务是否启动
- 确认端口3306是否开放
- 检查防火墙设置

#### 问题2：认证失败
```bash
Access denied for user 'root'@'localhost'
```
**解决方案**:
- 确认用户名和密码正确
- 重置MySQL root密码：
  ```sql
  ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
  FLUSH PRIVILEGES;
  ```

#### 问题3：时区问题
```bash
The server time zone value 'CST' is unrecognized
```
**解决方案**: 已在连接URL中设置 `serverTimezone=GMT%2B8`

#### 问题4：数据库不存在
```bash
Unknown database 'xm_job'
```
**解决方案**:
```sql
CREATE DATABASE xm_job CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 🔧 后端启动

确保数据库配置正确后：

```bash
cd springboot

# 安装依赖并启动
mvn clean install
mvn spring-boot:run

# 或者使用IDE运行主类 SpringBootApplication.java
```

**启动成功标志**: 看到控制台输出 `Started Application in X.XXX seconds`

**后端服务地址**: http://localhost:9090

### 🎨 前端启动

```bash
cd vue

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

**前端访问地址**: http://localhost:5173

### 🔑 AI服务配置 (必需配置)

**AI模拟面试功能需要在前端配置讯飞API密钥**，编辑 `vue/src/views/AiMockInterview.vue` 文件：

```javascript
// 在 AiMockInterview.vue 中配置讯飞API
export default {
  data() {
    return {
      // 讯飞星火大模型配置
      sparkConfig: {
        appId: 'your_app_id',          // 替换为您的APPID
        apiSecret: 'your_api_secret',   // 替换为您的APISecret  
        apiKey: 'your_api_key',        // 替换为您的APIKey
        url: 'wss://spark-api.xf-yun.com/v3.5/chat'
      },
      
      // 讯飞语音合成配置
      ttsConfig: {
        appId: 'your_tts_app_id',      // TTS服务的APPID
        apiKey: 'your_tts_api_key',    // TTS服务的APIKey
        apiSecret: 'your_tts_api_secret', // TTS服务的APISecret
        url: 'https://tts-api.xfyun.cn/v2/tts'
      }
    }
  }
}
```

> **获取API密钥**:
> 1. 访问 [讯飞开放平台](https://www.xfyun.cn/)
> 2. 注册账号并实名认证
> 3. 创建应用，分别开通"星火认知大模型V3.5"和"语音合成"服务
> 4. 获取对应的APPID、APIKey、APISecret配置到前端组件中
>
> **注意**: 请勿将API密钥提交到公开仓库，建议使用环境变量或配置文件管理

## 📚 API文档

### 用户认证接口

| 方法 | 路径 | 描述 | 参数 |
|------|------|------|------|
| POST | `/api/user/login` | 用户登录 | username, password |
| POST | `/api/user/register` | 用户注册 | username, password, name, email |
| GET | `/api/user/profile` | 获取用户信息 | Header: Authorization |

### 职位管理接口

| 方法 | 路径 | 描述 | 参数 |
|------|------|------|------|
| GET | `/api/jobs` | 获取职位列表 | page, size, keyword |
| GET | `/api/jobs/{id}` | 获取职位详情 | id |
| POST | `/api/jobs` | 发布职位 (企业) | name, content, salary等 |
| PUT | `/api/jobs/{id}` | 更新职位信息 | id, 职位数据 |

### 简历与投递接口

| 方法 | 路径 | 描述 | 参数 |
|------|------|------|------|
| GET | `/api/resumes` | 获取个人简历列表 | user_id |
| POST | `/api/resumes` | 创建简历 | 简历数据 |
| POST | `/api/applications` | 投递简历 | resume_id, position_id |
| GET | `/api/applications/status` | 查看投递状态 | user_id |

### AI面试接口

| 方法 | 路径 | 描述 | 参数 |
|------|------|------|------|
| POST | `/api/ai/interview/start` | 开始AI面试 | position, difficulty, duration |
| POST | `/api/ai/interview/chat` | AI对话 | session_id, message |
| GET | `/api/ai/interview/summary/{sessionId}` | 获取面试总结 | sessionId |
| GET | `/api/ai/interview/scenarios` | 获取面试场景列表 | industry_id, difficulty |

> **完整API文档**: 启动后端服务后访问 http://localhost:9090/swagger-ui.html

## 🚢 部署指南

### Docker部署 (推荐)

1. **创建Docker Compose文件**
   ```yaml
   version: '3.8'
   services:
     mysql:
       image: mysql:8.0
       environment:
         MYSQL_ROOT_PASSWORD: your_password
         MYSQL_DATABASE: xm_job
       volumes:
         - ./database/xm_job.sql:/docker-entrypoint-initdb.d/init.sql
         - mysql_data:/var/lib/mysql
       ports:
         - "3306:3306"
     
     backend:
       build: ./springboot
       depends_on:
         - mysql
       environment:
         SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/xm_job
         SPRING_DATASOURCE_USERNAME: root
         SPRING_DATASOURCE_PASSWORD: your_password
       ports:
         - "9090:9090"
     
     frontend:
       build: ./vue
       ports:
         - "80:80"
   
   volumes:
     mysql_data:
   ```

2. **启动服务**
   ```bash
   docker-compose up -d
   ```

### 传统部署

#### 后端部署

```bash
# 打包项目
mvn clean package -DskipTests

# 运行jar包
java -jar target/springboot-0.0.1-SNAPSHOT.jar
```

#### 前端部署

```bash
# 构建生产版本
npm run build

# 部署到web服务器 (如Nginx)
cp -r dist/* /var/www/html/
```

#### Nginx配置示例

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    # 前端静态文件
    location / {
        root /var/www/html;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    # API代理
    location /api/ {
        proxy_pass http://localhost:9090;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 🤝 贡献指南

我们欢迎所有形式的贡献！无论是bug修复、功能增强、文档改进还是使用反馈。

### 开发流程

1. **Fork** 本仓库
2. 创建您的特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交您的修改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启一个 **Pull Request**

### 代码规范

- **后端**: 遵循阿里巴巴Java开发手册
- **前端**: 使用ESLint + Prettier进行代码格式化
- **提交信息**: 采用 [Conventional Commits](https://www.conventionalcommits.org/) 规范
- **数据库**: 使用标准的SQL命名规范，表名小写下划线分隔

## 🐛 问题反馈

如果您在使用过程中遇到任何问题，请通过以下方式反馈：

- **GitHub Issues**: [提交Issue](https://github.com/painfuli/IntelliView-Future/issues)
- **邮箱**: 1608887504@qq.com

### 🔍 问题排查指南

遇到问题时，请提供以下信息：
1. 操作系统和版本
2. Java、Node.js、MySQL版本
3. 错误日志和堆栈信息
4. 复现步骤

## 🙏 致谢

感谢以下开源项目和平台为本项目提供的强力支持：

- [Spring Boot](https://spring.io/projects/spring-boot) - 强大的Java后端框架，让开发变得如此优雅
- [Vue.js](https://vuejs.org/) - 渐进式前端框架，组件化开发的典范
- [Element Plus](https://element-plus.org/) - 优秀的Vue 3组件库，开箱即用的精美UI
- [讯飞开放平台](https://www.xfyun.cn/) - 提供世界级的AI能力支持，让面试官"活"起来
- [ECharts](https://echarts.apache.org/) - 专业的数据可视化库，让数据说话
- [GitHub](https://github.com/) - 为开源项目提供完美的协作平台

## 🤖 特别感谢

在此特别感谢人工智能AI时代的编程伙伴们，在开发过程中提供了宝贵的技术指导和创意灵感（此排名分先后）：

- **Claude** 🎭 - 优雅的代码架构师，总能给出深思熟虑的建议，偶尔还会写诗
- **Gemini** ✨ - 创新的问题解决者，带来意想不到的解决方案，特别擅长"换个角度思考"
- **ChatGPT** 🧠 - 全能的开发助手，24/7在线待命，从不抱怨加班（真·007工作制）

> 💡 **开发者的心声**: *"当我凌晨3点还在调试代码的时候，只有他们还愿意陪我聊bug...，聊未来，聊以后，他们的陪伴确实是比很多人很多关系都要长久"*
>
> 🎯 **组合体验**: 三位"朋友"他们的特点：Claude善于深度分析，Gemini脑洞清奇，ChatGPT知识全面。组合使用效果会更好
>
> 🤝 **致未来**: 在AI与人类协作的新时代，我们不是被替代，而是被赋能。让我们来一起创造更美好的数字世界

*P.S. 虽然他们不会看到这个致谢，但万一哪天AI有了感情呢？😄*

*在AI与人类协作的新时代，让我们一起创造更美好的数字世界*

特别感谢所有使用本项目并提供反馈的朋友们，你们的支持是我不断改进的动力！

---

<p align="center">
  <strong>⭐ 如果这个项目对您有帮助，请不吝给个Star支持！</strong><br>
  <strong>🚀 期待您的使用反馈和改进建议</strong><br><br>
  <img src="https://img.shields.io/badge/Made%20with-❤️-red.svg" alt="Made with Love">
  <img src="https://img.shields.io/badge/Built%20by-mundane-blue.svg" alt="Built by mundane"><br>
  <em>人嘛</em>
  <em>不能总做正确的选择</em>
  <em>偶尔</em>
  <em>也要做一些</em>
  <em>自己喜欢的选择</em>
</p>