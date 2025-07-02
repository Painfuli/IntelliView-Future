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
  <a href="#-部署指南">部署指南</a> •
  <a href="#-api文档">API文档</a> •
  <a href="#-特别感谢">特别感谢</a> 
</p>

---

## 🌟 项目概述

智面未来是一个现代化的智能招聘平台，致力于通过AI技术革新传统招聘流程。平台不仅提供完整的招聘生态系统（职位发布、简历管理、投递追踪），更创新性地引入了**AI语音模拟面试**功能，帮助求职者在真实面试前进行充分准备。

### 🎯 设计理念

- **智能化**: 深度整合AI技术，提供个性化的求职体验
- **专业化**: 基于行业最佳实践，构建稳定可靠的系统架构
- **人性化**: 关注用户体验，简化复杂的招聘流程
- **开放性**: 模块化设计，支持功能扩展和定制

## ✨ 功能特色

### 🔍 核心招聘功能

- **多角色权限管理**: 求职者、企业、管理员三方独立权限体系
- **智能职位发布**: 企业可发布职位，支持富文本编辑，平台审核机制
- **在线简历系统**: 支持多份简历创建、在线编辑、一键投递
- **应用状态追踪**: 实时跟踪投递状态，透明化招聘流程
- **个性化推荐**: 基于协同过滤算法，智能推荐匹配职位

### 🤖 AI模拟面试 (核心创新)

> **📢 开发状态说明**: 
> 
> 当前AI多模态面试模块正在持续优化中，部分高级功能仍在完善阶段。为了确保用户获得最佳体验，该模块暂未包含在本次GitHub发布版本中。
> 
> 🔄 **预计更新**: 功能完善后将在下个版本中正式发布，敬请期待！
> 
> 💡 **当前可体验**: 您可以查看项目文档中的详细技术实现和功能演示

---

> 这是本项目的最大亮点，采用讯飞星火V3.5大模型驱动的智能面试系统

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
└── 📄 README.md                     # 项目说明文档

🔮 即将添加:
├── 📄 InterviewSetup.vue            # AI面试设置组件 (优化中)
└── 📄 AIVoiceChat.vue               # AI模拟面试主界面 (优化中)
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

### 📥 克隆项目

```bash
git clone https://github.com/painfuli/IntelliView-Future.git
cd IntelliView-Future
```


### 🔧 后端启动

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

### 🔑 AI服务配置 (可选)

如需配置讯飞API密钥：

> **获取API密钥**: 访问 [讯飞开放平台](https://www.xfyun.cn/) 注册并创建应用

## 📚 API文档

### 用户认证接口

| 方法 | 路径 | 描述 |
|------|------|------|
| POST | `/api/user/login` | 用户登录 |
| POST | `/api/user/register` | 用户注册 |
| GET | `/api/user/profile` | 获取用户信息 |

### 职位管理接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/jobs` | 获取职位列表 |
| GET | `/api/jobs/{id}` | 获取职位详情 |
| POST | `/api/jobs` | 发布职位 (企业) |
| PUT | `/api/jobs/{id}` | 更新职位信息 |

### 简历与投递接口

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/resumes` | 获取个人简历列表 |
| POST | `/api/resumes` | 创建简历 |
| POST | `/api/applications` | 投递简历 |
| GET | `/api/applications/status` | 查看投递状态 |

> **完整API文档**: 启动后端服务后访问 http://localhost:9090/swagger-ui.html

## 🚢 部署指南

### Docker部署 (推荐)

1. **构建Docker镜像**
   ```bash
   # 后端镜像
   cd springboot
   docker build -t intelliview-backend .
   
   # 前端镜像  
   cd ../vue
   docker build -t intelliview-frontend .
   ```

2. **使用Docker Compose**
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

## 🐛 问题反馈

如果您在使用过程中遇到任何问题，请通过以下方式反馈：

- **GitHub Issues**: [提交Issue](https://github.com/painfuli/IntelliView-Future/issues)
- **邮箱**: 1608887504@qq.com


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