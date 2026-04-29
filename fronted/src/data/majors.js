function createCourse(
  id,
  name,
  type,
  credits,
  description,
  skills,
  jobs,
  points,
  interaction,
  aiHint,
) {
  return {
    id,
    name,
    type,
    credits,
    duration: credits,
    description,
    skills,
    goals: skills,
    jobs,
    points,
    interaction,
    aiHint,
    jobExplanation: aiHint,
  }
}

function createResource(
  id,
  name,
  type,
  format,
  description,
  relatedCourseIds,
  skills,
  jobs,
  interaction,
  aiHint,
) {
  return {
    id,
    name,
    type,
    format,
    description,
    relatedCourseIds,
    skills,
    jobs,
    interaction,
    aiHint,
  }
}

function createJobProfile(id, name, category, summary, responsibilities, requirements) {
  return {
    id,
    name,
    category,
    summary,
    responsibilities,
    requirements,
  }
}

function uniqueList(items) {
  return Array.from(new Set(items.filter(Boolean)))
}

function hasKeyword(name, keywords) {
  return keywords.some((keyword) => name.includes(keyword))
}

const creditsByType = {
  专业导论: '1 学分',
  基础课: '3 学分',
  核心课: '3 学分',
  实践课: '2 学分',
  选修课: '2 学分',
}

const majorJobMap = {
  'software-engineering': [
    '软件工程师',
    'Java开发工程师',
    '后端开发工程师',
    '前端开发工程师',
    'Android软件工程师',
    '测试开发工程师',
  ],
  'data-science': [
    '大数据开发工程师',
    '数据分析师',
    '数据工程师',
    '算法工程师',
    'Python开发工程师',
    'AI训练师',
  ],
  'cyber-security': [
    '网络安全工程师',
    '网络信息安全运营工程师',
    '安全运营工程师',
    '渗透测试工程师',
    '安全分析工程师',
    '后端开发工程师',
  ],
}

export const jobProfiles = [
  createJobProfile(
    'software-engineer',
    '软件工程师',
    '软件研发',
    '面向业务系统与产品功能建设，贯通需求分析、设计实现、联调测试与版本交付的综合研发岗位。',
    [
      '参与软件项目的需求分析、方案设计、编码实现与联调测试。',
      '负责模块开发、性能优化、缺陷修复与技术文档整理。',
      '与产品、测试、前后端同学协作推进项目按期交付。',
    ],
    [
      '具备扎实的编程基础、良好的代码规范与工程协作意识。',
      '熟悉至少一种主流开发语言及常见框架，理解数据库与接口设计。',
      '能够独立定位问题，并持续学习新技术解决实际业务场景。',
    ],
  ),
  createJobProfile(
    'java-engineer',
    'Java开发工程师',
    '后端研发',
    '以 Java 技术栈为核心的后端开发岗位，常见于企业级业务系统、物联网平台与微服务场景。',
    [
      '基于 Java、SpringBoot、MyBatis 等框架完成后端模块开发。',
      '负责接口设计、数据库建模、缓存与消息链路接入。',
      '参与性能调优、故障排查与分布式服务治理。',
    ],
    [
      '熟练掌握 Java 语言与常用开发框架，具备数据结构与算法基础。',
      '熟悉 MySQL、Redis、消息队列、Linux、Nginx 等常见技术组件。',
      '理解网络通信原理，具备良好的编码规范与团队协作能力。',
    ],
  ),
  createJobProfile(
    'backend-engineer',
    '后端开发工程师',
    '服务端研发',
    '聚焦业务系统、服务接口与数据链路的设计开发，是多数互联网与企业系统的核心研发岗位。',
    [
      '负责系统需求分析、架构设计、服务开发与上线迭代。',
      '建设稳定高效的接口、缓存、数据库与任务调度链路。',
      '快速响应线上问题并持续推进系统性能优化。',
    ],
    [
      '具备完整的后端模块开发经验，理解服务治理与高可用设计。',
      '熟悉至少一种后端语言与主流框架，能够完成接口联调与故障排查。',
      '逻辑清晰，具备良好的问题定位能力与跨团队沟通能力。',
    ],
  ),
  createJobProfile(
    'frontend-engineer',
    '前端开发工程师',
    'Web 前端',
    '负责 Web、管理端、H5 或小程序等前端界面的实现与交互体验优化，是连接用户界面与业务流程的重要岗位。',
    [
      '完成页面结构、样式交互与组件模块开发。',
      '负责前后端接口联调、状态处理、页面性能优化与问题修复。',
      '与设计、后端、产品协作推进页面方案落地。',
    ],
    [
      '熟练掌握 HTML5、CSS3、JavaScript 与主流前端框架。',
      '理解工程化构建流程、浏览器渲染机制与接口联调方法。',
      '具备良好的需求理解、问题排查与文档整理能力。',
    ],
  ),
  createJobProfile(
    'android-engineer',
    'Android软件工程师',
    '移动研发',
    '负责 Android 平台应用与 SDK 模块开发，常涉及移动端业务、终端接入与设备协同场景。',
    [
      '使用 Java 或 Kotlin 完成 Android 程序设计、功能开发与调试。',
      '参与 App 架构设计、性能优化、设备适配与版本迭代。',
      '配合团队完成接口联调、问题定位与移动端交付。',
    ],
    [
      '熟悉 Android App 开发流程、调试工具与常见组件机制。',
      '掌握 Java 或 Kotlin，具备一定面向对象设计能力。',
      '有 SDK、C++ 或 3D 场景开发经验者更具优势。',
    ],
  ),
  createJobProfile(
    'test-engineer',
    '测试开发工程师',
    '测试与质量',
    '面向系统质量保障与自动化测试建设的岗位，既强调测试思维，也强调脚本与工具能力。',
    [
      '制定测试方案并完成功能、接口、回归与自动化测试。',
      '跟踪缺陷流转，协助定位问题并推动修复闭环。',
      '建设测试脚本、质量指标与持续集成中的测试环节。',
    ],
    [
      '理解软件开发流程、测试方法与常见缺陷类型。',
      '具备脚本编写能力，能够设计高价值测试用例。',
      '沟通细致，关注系统稳定性与交付质量。',
    ],
  ),
  createJobProfile(
    'big-data-engineer',
    '大数据开发工程师',
    '数据平台',
    '围绕海量数据采集、存储、计算与数仓建设展开，是数据平台与大数据应用场景中的关键岗位。',
    [
      '建设离线或实时数据链路，完成数据接入、ETL、计算与数仓开发。',
      '使用 Hadoop、Spark、Hive 等组件完成平台开发与性能优化。',
      '将业务数据转化为可复用的数据服务、分析能力或教学资源。',
    ],
    [
      '熟悉大数据核心组件、分布式存储与计算框架。',
      '具备 Java 或 Python 编程能力，能够完成数据链路开发。',
      '理解数仓建模、数据治理与集群部署运维基础。',
    ],
  ),
  createJobProfile(
    'data-analyst',
    '数据分析师',
    '数据分析',
    '聚焦数据清洗、指标分析、图表呈现与业务洞察，是连接数据与决策的重要岗位。',
    [
      '完成数据整理、统计分析、报表制作与结果解读。',
      '构建指标体系，追踪业务变化并形成分析结论。',
      '与业务团队沟通需求，输出可执行的分析建议。',
    ],
    [
      '熟悉 Python、SQL、统计学与常见可视化方法。',
      '能够将数据结果转化为清晰图表与文字说明。',
      '具备业务理解能力、逻辑表达能力与数据敏感度。',
    ],
  ),
  createJobProfile(
    'data-engineer',
    '数据工程师',
    '数据工程',
    '负责数据平台、数据仓库与稳定数据服务的建设，是分析、算法与业务系统之间的重要支撑岗位。',
    [
      '建设数据接入、清洗、存储、调度与服务化链路。',
      '完成数仓模型设计、数据质量治理与性能优化。',
      '支撑分析、算法与业务系统的数据需求落地。',
    ],
    [
      '熟悉数据库、ETL、数据建模与常见数据开发工具。',
      '理解分布式系统、数据调度与工程化交付方法。',
      '具备良好的脚本能力、排障能力与跨团队协作能力。',
    ],
  ),
  createJobProfile(
    'algorithm-engineer',
    '算法工程师',
    '算法与智能',
    '面向机器学习、深度学习与智能应用落地，强调模型理解、训练评估与工程部署能力。',
    [
      '负责特征处理、模型训练、效果评估与策略迭代。',
      '结合业务场景选择合适算法并优化模型表现。',
      '与数据、后端、产品团队协作推进算法服务上线。',
    ],
    [
      '具备扎实的数学、统计学、机器学习与编程基础。',
      '熟悉 Python 及主流算法框架，理解常见模型原理。',
      '能够结合业务目标解释模型效果与改进方向。',
    ],
  ),
  createJobProfile(
    'python-engineer',
    'Python开发工程师',
    'Python 后端',
    '常见于数据服务、AI 平台、自动化系统与 Web 服务建设，强调脚本能力与服务部署能力。',
    [
      '使用 Flask、FastAPI、Django 等框架完成服务开发与接口建设。',
      '在 Linux 环境中完成部署运维、日志排查与性能优化。',
      '与 AI 或数据平台协同，支撑模型服务与自动化能力落地。',
    ],
    [
      '熟练掌握 Python 编程与至少一种 Web 框架。',
      '熟悉 Linux、Shell、Docker、Kubernetes 与基础监控方案。',
      '具备良好的自动化思维、问题排查能力与团队协作能力。',
    ],
  ),
  createJobProfile(
    'python-image-engineer',
    'Python图像开发工程师',
    '图像处理',
    '面向图像处理与视觉应用场景，结合 Python 服务开发与图像算法实现完成业务落地。',
    [
      '基于 Flask 开发图像处理服务并设计接口。',
      '使用 OpenCV、Pillow、NumPy 实现图像裁剪、合成、变换与优化。',
      '负责生产问题定位、系统重构与性能提升。',
    ],
    [
      '精通 Python3 及面向对象编程，熟悉 Flask 与 RESTful API 设计。',
      '掌握常见图像处理库与像素级、矩阵级运算方法。',
      '具备 Linux、日志排查、服务部署与数据库基础能力。',
    ],
  ),
  createJobProfile(
    'ai-trainer',
    'AI训练师',
    'AIGC 生产',
    '围绕提示词设计、模型微调、素材生成与工作流搭建展开，是 AI 内容生产与模型调优方向的重要岗位。',
    [
      '将业务需求转化为高质量提示词与分镜描述。',
      '完成模型微调、LoRA 训练与风格一致性控制。',
      '搭建并优化文生图、图生视频等自动化工作流。',
    ],
    [
      '理解生成式 AI 工具与常见工作流，具备一定审美与表达能力。',
      '熟悉模型训练、参数调优与素材后期处理流程。',
      '能够持续沉淀方法论并提升内容产出效率。',
    ],
  ),
  createJobProfile(
    'cyber-security-engineer',
    '网络安全工程师',
    '安全防护',
    '负责安全产品部署、策略配置、系统加固与安全事件处置，是网络安全方向最常见的核心岗位之一。',
    [
      '完成安全产品实施部署、调试、加固与日常维护。',
      '参与网络攻防分析、安全策略配置与风险排查。',
      '支撑售前选型、交付实施与售后运维保障。',
    ],
    [
      '熟悉网络协议、安全产品、防护思路与常见攻防技术。',
      '具备系统、网络、日志、漏洞等方面的基础理解能力。',
      '具有良好的沟通协作与持续学习能力，持证者更具优势。',
    ],
  ),
  createJobProfile(
    'cyber-ops-engineer',
    '网络信息安全运营工程师',
    '安全运营',
    '偏向网络安全策略规划、重大场景保障与持续运营治理，常见于运营商、大型政企和平台型组织。',
    [
      '制定网络安全策略、资源规划与组网方案。',
      '开展日常巡检、风险挖掘、故障处置与应急保障。',
      '围绕重大活动、5G 专网、边缘场景推进专项安全方案落地。',
    ],
    [
      '具备网络、安全、通信等复合基础，理解大型网络运行体系。',
      '能够分析安全指标、制定方案并推进运营执行。',
      '适合具备系统性思维、责任意识与现场应变能力的同学。',
    ],
  ),
  createJobProfile(
    'security-ops-engineer',
    '安全运营工程师',
    '安全运营',
    '聚焦告警研判、日志分析、事件响应与日常监测，是安全团队中的持续运营岗位。',
    [
      '监测安全告警、分析日志并完成事件初判与上报。',
      '执行处置流程、协助复盘并完善安全运营规范。',
      '跟踪防护效果，推动风险问题形成闭环。',
    ],
    [
      '熟悉常见日志、告警、流量与安全运营流程。',
      '具备较强的分析耐心、文档意识与风险判断能力。',
      '理解网络与系统基础，能够快速学习新工具与新场景。',
    ],
  ),
  createJobProfile(
    'penetration-tester',
    '渗透测试工程师',
    '攻防测试',
    '面向 Web、系统、应用和网络环境开展安全测试与漏洞验证，强调漏洞原理理解与风险复现能力。',
    [
      '执行渗透测试、漏洞验证、风险评估与修复建议输出。',
      '分析常见 Web、安全协议与系统层风险成因。',
      '配合研发和安全团队推动漏洞修复与加固。',
    ],
    [
      '熟悉常见漏洞类型、测试思路、抓包分析与脚本工具使用。',
      '理解网络、操作系统、Web 后端与安全协议基础。',
      '具备良好的风险意识、报告编写能力与合规边界意识。',
    ],
  ),
  createJobProfile(
    'security-analyst',
    '安全分析工程师',
    '安全分析',
    '侧重风险研判、日志与情报分析、攻击链理解与安全事件复盘，是连接检测与处置的重要岗位。',
    [
      '分析安全事件、攻击特征、异常日志与告警来源。',
      '辅助研判漏洞影响面并输出风险报告。',
      '参与事件复盘，完善检测规则与处置建议。',
    ],
    [
      '具备系统、网络、恶意代码或日志分析基础。',
      '能够较清晰地描述攻击路径、风险影响与防护建议。',
      '具备持续学习能力与较强的信息梳理能力。',
    ],
  ),
  createJobProfile(
    'cpp-engineer',
    'C/C++开发工程师',
    '系统开发',
    '聚焦底层模块、性能敏感场景与系统级软件开发，对代码质量与问题定位能力要求较高。',
    [
      '负责 C/C++ 相关模块开发、维护与性能优化。',
      '参与系统设计、接口实现与问题排查。',
      '独立推进模块交付并保障代码质量。',
    ],
    [
      '熟练掌握 C 或 C++ 语言，具备良好的编码习惯。',
      '能够独立完成模块开发，并具备问题分析与解决能力。',
      '沟通协作顺畅，适应工程化开发流程。',
    ],
  ),
]

const jobProfileMap = new Map(jobProfiles.map((item) => [item.name, item]))

function buildCourseProfile(name, majorId) {
  const defaultJobs = majorJobMap[majorId] ?? []
  const fallback = {
    description: `${name}是 ${majorId === 'data-science' ? '数据方向' : majorId === 'cyber-security' ? '安全方向' : '软件方向'}课程体系中的重要环节，帮助你建立课程认知，并看清它与能力培养、岗位方向之间的联系。`,
    skills: ['专业基础理解能力', '知识迁移能力', '课程关联认知'],
    jobs: defaultJobs.slice(0, 3),
    points: [`把握 ${name} 的核心概念与基本方法。`, '能够将课程内容与相关课程及岗位场景建立联系。'],
    interaction: '建议结合课程案例、课堂实验与岗位情境理解课程价值。',
    aiHint: '可结合当前课程进度，选择更合适的补充内容。',
  }

  if (name.includes('导论')) {
    return {
      description: `${name}帮助你建立专业全貌认知，理解本方向的课程结构、能力重点与典型岗位去向。`,
      skills: ['专业认知能力', '方向辨析能力', '学习规划能力'],
      jobs: defaultJobs.slice(0, 3),
      points: ['了解专业课程体系的整体结构。', '建立课程学习与岗位发展的初步对应关系。'],
      interaction: '适合在入门阶段结合专业介绍、岗位画像与课程地图一同学习。',
      aiHint: '可结合自己的兴趣与目标岗位，先判断更适合从哪部分内容看起。',
    }
  }

  if (hasKeyword(name, ['高级语言程序设计（java）', 'Java程序设计', 'Java web开发', 'Java EE开发技术', 'Java应用实践'])) {
    return {
      description: `${name}聚焦 Java 语言与工程开发方法，是软件开发、服务端开发与企业级系统实践的重要基础课程。`,
      skills: ['Java编程能力', '面向对象设计能力', '工程编码能力'],
      jobs: uniqueList(['Java开发工程师', '后端开发工程师', majorId === 'data-science' ? '大数据开发工程师' : '', majorId === 'software-engineering' ? '软件工程师' : '']),
      points: ['理解面向对象编程、常见集合与工程编码规范。', '能够把语言基础迁移到 Web、后端或平台开发场景。'],
      interaction: '建议配合接口练习、业务案例与调试训练一并巩固。',
      aiHint: '可根据练习情况，回看更适合当前阶段的项目片段与训练内容。',
    }
  }

  if (name.includes('python') || name.includes('Python')) {
    return {
      description: `${name}围绕 Python 语法、脚本思维与问题求解展开，是数据处理、自动化开发和智能应用方向的常用基础课程。`,
      skills: ['Python编程能力', '脚本开发能力', '问题求解能力'],
      jobs: uniqueList(['Python开发工程师', majorId === 'data-science' ? '数据分析师' : '', majorId === 'data-science' ? '算法工程师' : '', majorId === 'cyber-security' ? '安全分析工程师' : '']),
      points: ['掌握流程控制、函数、容器与常用模块使用方法。', '理解 Python 在数据、自动化与智能方向中的应用位置。'],
      interaction: '建议结合脚本练习、实验任务与场景化案例同步学习。',
      aiHint: '可根据代码练习情况，回看更适合自己的补弱内容。',
    }
  }

  if (name.includes('离散数学')) {
    return {
      description: '离散数学帮助学生建立逻辑推理、集合关系、图论与证明方法，是算法、数据结构和计算机理论课程的重要基础。',
      skills: ['逻辑推理能力', '形式化表达能力', '抽象建模能力'],
      jobs: uniqueList([defaultJobs[0], defaultJobs[1], majorId === 'data-science' ? '算法工程师' : '']),
      points: ['理解命题逻辑、关系、图与组合等核心内容。', '为算法设计、系统分析与安全原理打好理论基础。'],
      interaction: '适合结合图示推导、题型训练与相关课程实例一起理解。',
      aiHint: '可先回到图示推导与基础题型，逐步理清逻辑链条。',
    }
  }

  if (name.includes('数据结构')) {
    return {
      description: '数据结构帮助学生理解常见数据组织方式与算法效率，是软件开发、算法面试与系统设计中的核心基础课程。',
      skills: ['结构选型能力', '复杂度分析能力', '抽象建模能力'],
      jobs: uniqueList(['Java开发工程师', '后端开发工程师', majorId === 'data-science' ? '算法工程师' : '', majorId === 'cyber-security' ? '安全分析工程师' : '']),
      points: ['掌握线性表、树、图、哈希等典型结构。', '理解结构选择对性能与问题求解方式的影响。'],
      interaction: '建议结合可视化演示、典型题型与代码实现同步巩固。',
      aiHint: '可先分清是结构理解还不够稳，还是实现细节还需多练。',
    }
  }

  if (hasKeyword(name, ['计算机组成原理', 'Linux原理与应用', '操作系统', '国产操作系统应用开发'])) {
    return {
      description: `${name}帮助学生建立底层系统运行机制的理解，是系统调优、性能分析、安全分析与工程部署的重要基础。`,
      skills: ['系统理解能力', '资源调度认知', '运行环境分析能力'],
      jobs: uniqueList([
        majorId === 'data-science' ? '大数据开发工程师' : '',
        majorId === 'cyber-security' ? '网络安全工程师' : '',
        '后端开发工程师',
        'C/C++开发工程师',
      ]),
      points: ['理解进程、内存、文件系统或指令执行等关键机制。', '建立软件系统与运行环境之间的联系。'],
      interaction: '适合结合实验环境、性能观测与系统案例一并学习。',
      aiHint: '可结合实验结果与常见故障案例，进一步理解系统机制。',
    }
  }

  if (hasKeyword(name, ['数据库系统概论', '网络数据库技术'])) {
    return {
      description: `${name}关注数据建模、查询组织、事务管理与存储设计，是业务系统开发与数据平台建设的重要课程。`,
      skills: ['数据建模能力', 'SQL应用能力', '数据组织能力'],
      jobs: uniqueList(['后端开发工程师', 'Java开发工程师', '数据工程师', '数据分析师']),
      points: ['理解表结构设计、查询优化与事务一致性的基本原则。', '能够将数据库知识迁移到业务系统与数据平台场景。'],
      interaction: '建议结合业务案例、报表需求和数据链路设计进行学习。',
      aiHint: '可根据目标方向，侧重业务库、数仓或高并发场景的练习内容。',
    }
  }

  if (name.includes('算法分析与设计')) {
    return {
      description: '算法分析与设计帮助学生形成更系统的问题求解思维，是进入高阶开发、数据智能与安全分析方向的重要桥梁课程。',
      skills: ['算法设计能力', '复杂问题拆解能力', '性能优化意识'],
      jobs: uniqueList(['后端开发工程师', '算法工程师', 'Java开发工程师', majorId === 'cyber-security' ? '安全分析工程师' : '']),
      points: ['理解分治、动态规划、贪心等常见算法思想。', '建立算法效率与工程实现之间的连接。'],
      interaction: '建议结合典型题型、复杂度比较与实战题训练一并巩固。',
      aiHint: '可根据做题情况，判断自己更适合先补思路还是代码实现。',
    }
  }

  if (name.includes('软件工程') || hasKeyword(name, ['软件构造', '软件需求分析', '软件项目管理', '软件开发实践', '软件设计与体系结构'])) {
    return {
      description: `${name}强调工程方法、协作流程、需求设计与系统交付，是把课程知识转化为真实研发能力的关键课程。`,
      skills: ['工程协作能力', '需求分析能力', '系统设计能力'],
      jobs: uniqueList(['软件工程师', '后端开发工程师', '测试开发工程师', majorId === 'software-engineering' ? '前端开发工程师' : '']),
      points: ['理解需求、设计、编码、测试与交付之间的完整链路。', '能够从工程视角看待软件系统的组织方式。'],
      interaction: '适合结合项目案例、流程图、需求文档与复盘材料学习。',
      aiHint: '可多结合项目进度与角色分工，体会团队协作中的关键环节。',
    }
  }

  if (hasKeyword(name, ['软件质量保证与测试'])) {
    return {
      description: `${name}帮助学生建立测试思维、质量意识与缺陷定位方法，是研发与交付过程中不可缺少的重要课程。`,
      skills: ['测试思维', '缺陷分析能力', '质量保障意识'],
      jobs: ['测试开发工程师', '软件工程师', '后端开发工程师'],
      points: ['理解测试用例设计、缺陷流转与质量评估方法。', '认识测试活动在软件交付中的关键作用。'],
      interaction: '建议结合缺陷案例、回归测试任务与自动化脚本训练一起学习。',
      aiHint: '可结合缺陷记录与测试情况，继续补看更贴近当前阶段的内容。',
    }
  }

  if (hasKeyword(name, ['计算机网络', '移动网络技术'])) {
    return {
      description: `${name}帮助学生理解网络通信、协议分层与数据传输机制，是后端系统、移动互联与网络安全方向的重要基础。`,
      skills: ['网络协议理解能力', '通信链路认知', '网络问题定位能力'],
      jobs: uniqueList([
        majorId === 'cyber-security' ? '网络安全工程师' : '',
        majorId === 'cyber-security' ? '安全运营工程师' : '',
        '后端开发工程师',
        'Android软件工程师',
      ]),
      points: ['理解 TCP/IP、HTTP 等常见协议与传输机制。', '建立网络行为与系统服务之间的联系。'],
      interaction: '适合结合抓包示意、协议流程图与故障案例同步学习。',
      aiHint: '可回看协议流程与典型案例，进一步加深理解。',
    }
  }

  if (hasKeyword(name, ['网络信息安全', '网络与信息安全', '现代密码学', '信息安全管理', '恶意代码检测与防护', '系统安全', '计算机病毒防治', '信息隐藏与数字水印', '网络攻击与防御'])) {
    return {
      description: `${name}围绕常见风险、防护机制与安全治理展开，是网络空间安全方向形成系统认知的重要课程。`,
      skills: ['安全分析能力', '风险识别能力', '防护策略理解能力'],
      jobs: uniqueList(['网络安全工程师', '安全运营工程师', '渗透测试工程师', '安全分析工程师']),
      points: ['理解典型风险的形成原因、检测方法与处置思路。', '能够把课程知识映射到真实安全场景中的防护任务。'],
      interaction: '建议结合安全案例、攻防链路图与风险复盘材料一并学习。',
      aiHint: '可按自己更关注的方向，侧重攻防、运营或分析相关内容。',
    }
  }

  if (hasKeyword(name, ['人工智能原理', '人工智能', '深度学习', '机器视觉'])) {
    return {
      description: `${name}帮助学生理解智能算法、模型训练与应用场景，是迈向算法、视觉和智能应用方向的重要课程。`,
      skills: ['模型理解能力', '智能应用认知', '算法实践能力'],
      jobs: uniqueList(['算法工程师', 'AI训练师', majorId === 'data-science' ? 'Python开发工程师' : '', name.includes('机器视觉') ? 'Python图像开发工程师' : '']),
      points: ['理解典型模型或视觉方法的基本原理与适用场景。', '认识人工智能课程在工程应用中的价值与边界。'],
      interaction: '建议结合模型效果展示、实验记录与真实应用案例一起学习。',
      aiHint: '可按自己的兴趣，先从视觉、训练或智能应用相关内容看起。',
    }
  }

  if (hasKeyword(name, ['大数据导论', '大数据原理', '大数据开发技术', '大数据分析技术', '数据采集与预处理', '数据分析与可视化', '数据挖掘和机器学习', '分布式系统与云计算', '工业大数据项目实践', '数字孪生'])) {
    return {
      description: `${name}聚焦数据链路、分析建模或平台实现，是数据科学与大数据技术方向形成岗位能力的重要课程。`,
      skills: ['数据处理能力', '分析建模能力', '平台应用能力'],
      jobs: uniqueList(['大数据开发工程师', '数据工程师', '数据分析师', '算法工程师']),
      points: ['理解数据从采集、清洗、计算到分析展示的完整过程。', '建立课程内容与数据平台、分析场景之间的联系。'],
      interaction: '适合结合业务数据案例、数仓流程图和可视化样例一并学习。',
      aiHint: '可按当前阶段侧重分析、工程或算法相关内容。',
    }
  }

  if (hasKeyword(name, ['统计学', '随机过程', '最优化理论'])) {
    return {
      description: `${name}提供数据分析与算法建模所需的数学工具，是理解模型、评估结果与优化策略的重要基础课程。`,
      skills: ['统计理解能力', '模型分析能力', '定量推理能力'],
      jobs: uniqueList(['数据分析师', '算法工程师', '数据工程师']),
      points: ['掌握常见统计、概率或优化方法的基本思想。', '理解数学基础在数据分析和模型构建中的作用。'],
      interaction: '建议结合图表示例、推导过程与业务案例同步理解。',
      aiHint: '可先回看统计与推导基础，再逐步补齐数理环节。',
    }
  }

  if (hasKeyword(name, ['网络安全协议分析', '信息论与编码', '信息安全数学基础'])) {
    return {
      description: `${name}从协议、编码或数学基础层面帮助学生理解安全机制，是安全分析与安全产品理解的重要支撑课程。`,
      skills: ['安全机制理解能力', '协议分析能力', '形式化思考能力'],
      jobs: ['网络安全工程师', '安全分析工程师', '渗透测试工程师'],
      points: ['理解安全协议、编码机制或数学基础的核心逻辑。', '建立底层原理与安全应用之间的联系。'],
      interaction: '适合结合协议流程、加密示意与安全案例学习。',
      aiHint: '可结合协议流程与案例解读，加深对安全机制的理解。',
    }
  }

  if (hasKeyword(name, ['网络编程基础', '网络编程提高', 'Web开发基础', 'Web后端技术', 'XML语言'])) {
    return {
      description: `${name}帮助学生建立网络应用开发与服务端组织能力，是 Web 系统、接口服务和工程项目实践中的常用课程。`,
      skills: ['Web开发能力', '接口理解能力', '服务组织能力'],
      jobs: uniqueList(['前端开发工程师', '后端开发工程师', 'Java开发工程师', majorId === 'cyber-security' ? '渗透测试工程师' : '']),
      points: ['理解页面、接口、服务端逻辑或数据交换方式。', '能够把课程内容迁移到实际 Web 系统开发场景。'],
      interaction: '建议结合页面案例、接口调试与后端练习任务一并巩固。',
      aiHint: '可结合练习结果，再补看接口或页面开发案例。',
    }
  }

  if (hasKeyword(name, ['移动核心服务开发', '移动开发技术'])) {
    return {
      description: `${name}围绕移动场景中的应用开发、服务接入与端云协同展开，是移动研发方向的重要拓展课程。`,
      skills: ['移动开发能力', '端云协同理解能力', '应用调试能力'],
      jobs: ['Android软件工程师', '后端开发工程师', '前端开发工程师'],
      points: ['理解移动应用与后端服务之间的协作方式。', '建立从界面、接口到移动能力交付的完整认知。'],
      interaction: '建议结合移动端页面、接口调用与终端调试训练一并学习。',
      aiHint: '可根据兴趣选择更偏移动端实现或服务端支撑的练习内容。',
    }
  }

  if (name.includes('人机交互')) {
    return {
      description: `${name}帮助学生理解用户研究、交互流程与界面体验，是连接软件产品、设计思维与实现落地的重要课程。`,
      skills: ['交互设计理解能力', '用户体验意识', '需求表达能力'],
      jobs: ['前端开发工程师', '软件工程师', 'Android软件工程师'],
      points: ['理解交互设计原则与用户任务流组织方式。', '认识体验设计在软件产品中的实际价值。'],
      interaction: '适合结合原型示例、任务流分析与产品案例共同学习。',
      aiHint: '可结合课程表现，继续补看界面设计与交互案例。',
    }
  }

  if (name.includes('项目实践') || name.includes('实践')) {
    return {
      description: `${name}通过项目任务把前序课程知识串联起来，帮助学生在实践中理解岗位真实需要的综合能力。`,
      skills: ['综合交付能力', '项目协作能力', '问题沟通能力'],
      jobs: defaultJobs.slice(0, 4),
      points: ['把前序课程知识整合到完整任务链中。', '通过实践建立课程体系与岗位工作的真实连接。'],
      interaction: '建议结合任务拆解、周报复盘与成果展示同步推进。',
      aiHint: '可结合实践记录，整理自己的阶段收获与改进方向。',
    }
  }

  return fallback
}

function createCourses(prefix, majorId, items) {
  return items.map((item, index) => {
    const profile = buildCourseProfile(item.name, majorId)
    return createCourse(
      `${prefix}-${String(index + 1).padStart(2, '0')}`,
      item.name,
      item.type,
      item.credits ?? creditsByType[item.type] ?? '3 学分',
      profile.description,
      profile.skills,
      profile.jobs,
      profile.points,
      profile.interaction,
      profile.aiHint,
    )
  })
}

function buildCourseIdMap(courses) {
  return Object.fromEntries(courses.map((course) => [course.name, course.id]))
}

function createResources(prefix, courseIdMap, items) {
  return items.map((item, index) =>
    createResource(
      `${prefix}-resource-${String(index + 1).padStart(2, '0')}`,
      item.name,
      item.type,
      item.format,
      item.description,
      item.relatedCourses.map((courseName) => courseIdMap[courseName]).filter(Boolean),
      item.skills,
      item.jobs,
      item.interaction,
      item.aiHint,
    ),
  )
}

const softwareEngineeringCourses = createCourses('se', 'software-engineering', [
  { name: '软件工程专业导论', type: '专业导论' },
  { name: '高级语言程序设计（java）', type: '基础课', credits: '4 学分' },
  { name: '高级语言程序设计（python）', type: '基础课', credits: '4 学分' },
  { name: '离散数学', type: '基础课', credits: '4 学分' },
  { name: '数据结构', type: '核心课', credits: '4 学分' },
  { name: '计算机组成原理', type: '基础课' },
  { name: '数据库系统概论', type: '核心课' },
  { name: '算法分析与设计', type: '核心课', credits: '4 学分' },
  { name: 'Java程序设计', type: '核心课' },
  { name: '操作系统', type: '核心课' },
  { name: '软件工程', type: '核心课' },
  { name: '计算机网络', type: '基础课' },
  { name: '软件质量保证与测试', type: '核心课' },
  { name: '软件项目管理', type: '核心课' },
  { name: '软件需求分析', type: '核心课' },
  { name: '网络信息安全', type: '核心课' },
  { name: '人工智能原理', type: '拓展课' },
  { name: '软件开发实践', type: '实践课' },
  { name: '软件设计与体系结构', type: '核心课' },
  { name: '人机交互的软件工程方法', type: '选修课' },
  { name: '国产操作系统应用开发', type: '选修课' },
  { name: 'Java web开发', type: '选修课' },
  { name: '软件构造', type: '选修课' },
  { name: '机器视觉', type: '选修课' },
  { name: '大数据分析技术', type: '选修课' },
  { name: 'XML语言', type: '选修课' },
  { name: 'Java EE开发技术', type: '选修课' },
  { name: '移动核心服务开发', type: '选修课' },
  { name: '移动开发技术', type: '选修课' },
])

const dataScienceCourses = createCourses('ds', 'data-science', [
  { name: '大数据导论', type: '专业导论' },
  { name: '高级语言程序设计（java）', type: '基础课', credits: '4 学分' },
  { name: '高级语言程序设计（python）', type: '基础课', credits: '4 学分' },
  { name: '离散数学', type: '基础课', credits: '4 学分' },
  { name: '数据结构', type: '核心课', credits: '4 学分' },
  { name: 'Linux原理与应用', type: '基础课' },
  { name: '大数据原理', type: '核心课' },
  { name: '数据库系统概论', type: '核心课' },
  { name: '操作系统', type: '核心课' },
  { name: '计算机组成原理', type: '基础课' },
  { name: '计算机网络', type: '基础课' },
  { name: '统计学', type: '核心课' },
  { name: '数据采集与预处理', type: '核心课' },
  { name: '算法分析与设计', type: '核心课', credits: '4 学分' },
  { name: '数据分析与可视化', type: '核心课' },
  { name: 'Java程序设计', type: '基础课' },
  { name: 'Java应用实践', type: '实践课' },
  { name: '大数据开发技术', type: '核心课' },
  { name: '软件工程', type: '核心课' },
  { name: '数据挖掘和机器学习', type: '核心课' },
  { name: '分布式系统与云计算', type: '核心课' },
  { name: '随机过程', type: '核心课' },
  { name: '人工智能', type: '核心课' },
  { name: '最优化理论', type: '核心课' },
  { name: '网络信息安全', type: '拓展课' },
  { name: '深度学习', type: '核心课' },
  { name: '机器视觉', type: '拓展课' },
  { name: '工业大数据项目实践', type: '实践课' },
  { name: '数字孪生', type: '选修课' },
  { name: '国产操作系统应用开发', type: '选修课' },
  { name: '移动核心服务开发', type: '选修课' },
  { name: '移动开发技术', type: '选修课' },
])

const cyberSecurityCourses = createCourses('cs', 'cyber-security', [
  { name: '网络空间安全专业导论', type: '专业导论' },
  { name: '高级语言程序设计（java）', type: '基础课', credits: '4 学分' },
  { name: '高级语言程序设计（python）', type: '基础课', credits: '4 学分' },
  { name: '离散数学', type: '基础课', credits: '4 学分' },
  { name: '数据结构', type: '核心课', credits: '4 学分' },
  { name: '计算机组成原理', type: '基础课' },
  { name: '操作系统', type: '基础课' },
  { name: '计算机网络', type: '基础课' },
  { name: '信息安全数学基础', type: '核心课' },
  { name: '信息论与编码', type: '核心课' },
  { name: '网络安全协议分析', type: '核心课' },
  { name: '网络数据库技术', type: '核心课' },
  { name: '网络与信息安全', type: '核心课' },
  { name: '现代密码学', type: '核心课' },
  { name: '信息安全管理', type: '核心课' },
  { name: '网络编程基础', type: '核心课' },
  { name: 'Web开发基础', type: '核心课' },
  { name: '网络编程提高', type: '核心课' },
  { name: 'Web后端技术', type: '核心课' },
  { name: '恶意代码检测与防护', type: '核心课' },
  { name: '系统安全', type: '核心课' },
  { name: '计算机病毒防治', type: '核心课' },
  { name: '人工智能原理', type: '拓展课' },
  { name: '软件工程', type: '拓展课' },
  { name: '算法分析与设计', type: '核心课', credits: '4 学分' },
  { name: '信息隐藏与数字水印', type: '核心课' },
  { name: '移动网络技术', type: '拓展课' },
  { name: '网络攻击与防御', type: '实践课' },
])

const softwareEngineeringCourseIds = buildCourseIdMap(softwareEngineeringCourses)
const dataScienceCourseIds = buildCourseIdMap(dataScienceCourses)
const cyberSecurityCourseIds = buildCourseIdMap(cyberSecurityCourses)

export const supportModules = [
  {
    id: 'resource-recommendation',
    name: '推荐学习资源',
    tagline: '围绕课程难点与岗位方向，择其要者，供循序研习。',
    points: ['案例讲解', '专题导读', '资源索引'],
  },
  {
    id: 'ability-practice',
    name: '能力巩固',
    tagline: '将课程理解转为可操作训练，便于稳步积累。',
    points: ['编程练习', '实训任务', '阶段复盘'],
  },
  {
    id: 'learning-support',
    name: '学习辅助',
    tagline: '借由提示与答疑，帮助学生辨明疑点与路径。',
    points: ['课程提示', '方向指引', '难点释义'],
  },
  {
    id: 'learning-advice',
    name: '学习建议',
    tagline: '结合阶段目标与能力短板，理清接下来的精进方向。',
    points: ['阶段建议', '补弱方向', '岗位准备'],
  },
]

export const majors = [
  {
    id: 'software-engineering',
    name: '软件工程',
    shortTitle: '围绕软件系统开发、工程协作与稳定交付而设的专业方向',
    tagline: '由课程见能力，由能力通岗位，帮助学生明晰软件方向的成长次第。',
    badge: '专业方向',
    audience: '面向软件类专业学生 / 学业认知 / 岗位参考',
    duration: '建议分阶段查阅',
    intro:
      '软件工程专业强调需求分析、系统设计、编码实现、质量保障与团队协作。平台以课程为脉络，帮助学生理解课程为何而学、能力如何沉淀、岗位从何而来。',
    overview:
      '此方向以工程实践为核心，课程之间层层相依。若能把基础编程、系统课程、工程方法与项目实践放在同一视角下理解，专业认知会更加清晰。',
    majorVision:
      '把课程、能力、岗位与学习内容放在一起看，软件工程的学习脉络会更清楚。',
    abilityOverview: ['工程化开发能力', '系统分析与设计能力', '测试与质量保障能力', '团队协作与项目推进能力'],
    careerFocus: majorJobMap['software-engineering'],
    metrics: [
      { label: '课程总数', value: `${softwareEngineeringCourses.length} 门` },
      { label: '能力方向', value: '开发 / 设计 / 测试 / 协作' },
      { label: '典型岗位', value: '研发 / 前端 / 后端 / 测试' },
    ],
    learningGoals: [
      '理解专业核心课程之间的逻辑关系。',
      '看懂课程如何支撑软件开发与工程协作岗位。',
      '明确下一步如何选择学习内容与实践路径。',
    ],
    courses: softwareEngineeringCourses,
    resources: createResources('se', softwareEngineeringCourseIds, [
      {
        name: '推荐学习资源：程序设计与工程入门导引',
        type: '推荐学习资源',
        format: '专题导读',
        description: '围绕程序设计、数据结构与软件工程课程整理学习次序，适合刚开始梳理专业时参考。',
        relatedCourses: ['高级语言程序设计（java）', '高级语言程序设计（python）', '数据结构', '软件工程'],
        skills: ['编程表达能力', '抽象建模能力', '工程协作能力'],
        jobs: ['软件工程师', 'Java开发工程师'],
        interaction: '适合在学期初或转向软件方向时查阅。',
        aiHint: '可按当前进度，先从更合适的课程顺序看起。',
      },
      {
        name: '能力巩固：数据库与后端实践题册',
        type: '能力巩固',
        format: '训练任务',
        description: '围绕数据库系统概论、Java Web 与软件设计课程整理训练任务，帮助学生逐步建立后端实现能力。',
        relatedCourses: ['数据库系统概论', 'Java web开发', 'Java EE开发技术', '软件设计与体系结构'],
        skills: ['数据建模能力', '服务组织能力', '工程编码能力'],
        jobs: ['Java开发工程师', '后端开发工程师'],
        interaction: '适合在核心课程学习后配合练习与项目使用。',
        aiHint: '可结合完成情况，继续补看接口练习或数据库案例。',
      },
      {
        name: '学习辅助：课程与岗位对应小札',
        type: '学习辅助',
        format: '方向说明',
        description: '按“课程 - 能力 - 岗位”整理常见对应关系，帮助学生快速建立专业认知。',
        relatedCourses: ['软件工程', '软件质量保证与测试', '软件项目管理'],
        skills: ['课程关联认知', '岗位辨析能力', '学习规划能力'],
        jobs: ['软件工程师', '测试开发工程师', '前端开发工程师'],
        interaction: '适合在选课、准备实践或梳理方向时查看。',
        aiHint: '可根据目标岗位，先看更贴近自己的导学内容。',
      },
      {
        name: '学习建议：软件方向阶段精进指引',
        type: '学习建议',
        format: '阶段建议',
        description: '结合课程进度与目标岗位，提示应优先夯实基础、补工程方法，还是加强项目实践。',
        relatedCourses: ['软件开发实践', '软件质量保证与测试', '软件项目管理'],
        skills: ['项目协作能力', '质量保障意识', '综合交付能力'],
        jobs: ['后端开发工程师', '测试开发工程师', '软件工程师'],
        interaction: '适合在课程中期或阶段总结时集中查看。',
        aiHint: '适合在阶段复盘时，对照自己的学习进展查阅。',
      },
    ]),
    aiAssistant: {
      title: '学途建议',
      defaultBody:
        '可结合当前关注的课程与岗位，想一想先夯何课、后补何项、宜看何种资源。',
      prompts: [
        '若目标偏研发岗位，应优先打牢程序设计、数据结构、数据库与工程实践。',
        '若质量意识较弱，可多关注测试、需求分析与项目协作相关内容。',
        '若还拿不准顺序，可先从与目标岗位关系更紧的课程看起。',
      ],
    },
  },
  {
    id: 'data-science',
    name: '数据科学与大数据技术',
    shortTitle: '围绕数据处理、分析建模、平台开发与业务洞察而设的专业方向',
    tagline: '使学生看清数据课程之间的次第脉络，并理解其与数据岗位之间的对应关系。',
    badge: '专业方向',
    audience: '面向数据类专业学生 / 学习规划 / 岗位参考',
    duration: '建议分阶段查阅',
    intro:
      '数据科学与大数据技术专业强调数据采集、清洗、分析、建模与应用。平台聚焦课程链路与岗位画像，帮助学生理解不同课程分别支撑怎样的能力方向。',
    overview:
      'Python、数据库、统计学、可视化与机器学习并非彼此割裂，而是共同构成数据方向的完整学习路径。把它们放在岗位场景中理解，会更容易形成整体认知。',
    majorVision:
      '把课程、能力、岗位与学习内容放在一起看，数据方向的学习路径会更清楚。',
    abilityOverview: ['数据处理能力', '分析建模能力', '可视化表达能力', '业务理解能力'],
    careerFocus: majorJobMap['data-science'],
    metrics: [
      { label: '课程总数', value: `${dataScienceCourses.length} 门` },
      { label: '能力方向', value: '分析 / 工程 / 建模 / 可视化' },
      { label: '典型岗位', value: '数据分析 / 数据工程 / 算法' },
    ],
    learningGoals: [
      '理解数据类课程之间的前后依赖关系。',
      '看懂课程如何映射到分析、工程和算法岗位。',
      '根据目标岗位选择更合适的学习内容。',
    ],
    courses: dataScienceCourses,
    resources: createResources('ds', dataScienceCourseIds, [
      {
        name: '推荐学习资源：数据方向入门书目',
        type: '推荐学习资源',
        format: '专题导读',
        description: '围绕 Python、统计学、数据库与大数据原理整理入门导学，帮助初学者建立整体视角。',
        relatedCourses: ['高级语言程序设计（python）', '统计学', '数据库系统概论', '大数据原理'],
        skills: ['数据处理能力', '统计理解能力', '数据组织能力'],
        jobs: ['数据分析师', '数据工程师'],
        interaction: '适合在刚开始梳理数据方向时翻阅。',
        aiHint: '可先按自己当前关注的课程，从更合适的顺序读起。',
      },
      {
        name: '能力巩固：分析建模案例习作',
        type: '能力巩固',
        format: '案例任务',
        description: '结合数据挖掘、机器学习、深度学习课程，整理由浅入深的分析建模练习。',
        relatedCourses: ['数据挖掘和机器学习', '深度学习', '人工智能'],
        skills: ['分析建模能力', '模型理解能力', '算法实践能力'],
        jobs: ['算法工程师', '数据分析师', 'AI训练师'],
        interaction: '适合在核心课程学习后逐步开展。',
        aiHint: '可按练习表现，继续选择更偏分析或算法的训练内容。',
      },
      {
        name: '学习辅助：数据岗位择向说明',
        type: '学习辅助',
        format: '方向说明',
        description: '帮助学生分辨数据分析、数据工程与算法岗位之间的侧重点与能力要求。',
        relatedCourses: ['数据分析与可视化', '大数据开发技术', '分布式系统与云计算'],
        skills: ['岗位辨析能力', '方向规划能力', '课程关联认知'],
        jobs: ['数据分析师', '数据工程师', '大数据开发工程师'],
        interaction: '适合在准备竞赛、实训或实习前查看。',
        aiHint: '可根据目标岗位，先看更贴近自己的选课与训练方向。',
      },
      {
        name: '学习建议：数据方向阶段精进指引',
        type: '学习建议',
        format: '阶段建议',
        description: '结合学生当前阶段与岗位目标，提示先补编程、统计、工程链路还是建模能力。',
        relatedCourses: ['高级语言程序设计（python）', '统计学', '大数据开发技术', '数据挖掘和机器学习'],
        skills: ['学习规划能力', '分析建模能力', '平台应用能力'],
        jobs: ['数据分析师', '大数据开发工程师', '算法工程师'],
        interaction: '适合在课程中期或能力复盘阶段查阅。',
        aiHint: '适合在阶段复盘时，对照自己的学习进展查阅。',
      },
    ]),
    aiAssistant: {
      title: '学途建议',
      defaultBody:
        '可结合目标岗位、关注课程与能力短板，想一想接下来更适合先补哪一部分。',
      prompts: [
        '若目标偏数据分析岗位，应优先理解 Python、统计与可视化之间的关系。',
        '若目标偏数据工程，应更多关注数据库、分布式系统与大数据开发链路。',
        '若拿不准方向，可先从与你更关注岗位关系更紧的课程看起。',
      ],
    },
  },
  {
    id: 'cyber-security',
    name: '网络空间安全',
    shortTitle: '围绕安全防护、漏洞分析、系统加固与持续运营而设的专业方向',
    tagline: '帮助学生理解安全课程如何沉淀为分析、防护与运营能力，并映射到典型岗位场景。',
    badge: '专业方向',
    audience: '面向安全类专业学生 / 岗位认知 / 学习规划',
    duration: '建议分阶段查阅',
    intro:
      '网络空间安全专业关注系统安全、网络攻防、漏洞分析和安全运营。平台从课程、能力、岗位与推荐内容四个层面帮助学生梳理安全方向的成长路径。',
    overview:
      '安全岗位并不只是会使用工具，更需要网络、系统、协议、密码学与风险分析等多门课程共同支撑。把课程链路看清，岗位认知才会更稳固。',
      majorVision:
      '把课程、能力、岗位与学习内容放在一起看，安全方向的学习关系会更清楚。',
    abilityOverview: ['安全分析能力', '漏洞理解能力', '网络防护能力', '风险处置能力'],
    careerFocus: majorJobMap['cyber-security'],
    metrics: [
      { label: '课程总数', value: `${cyberSecurityCourses.length} 门` },
      { label: '能力方向', value: '攻防 / 分析 / 防护 / 运营' },
      { label: '典型岗位', value: '安全工程 / 运营 / 渗透 / 分析' },
    ],
    learningGoals: [
      '理解安全基础课程如何支撑不同岗位方向。',
      '看懂安全能力并不等于只会使用工具。',
      '明确不同岗位方向对应的推荐学习内容。',
    ],
    courses: cyberSecurityCourses,
    resources: createResources('cs', cyberSecurityCourseIds, [
      {
        name: '推荐学习资源：安全基础与攻防导读',
        type: '推荐学习资源',
        format: '案例导读',
        description: '围绕网络、系统、密码与攻防课程整理安全入门资源，帮助学生建立较完整的安全基础认知。',
        relatedCourses: ['计算机网络', '操作系统', '现代密码学', '网络攻击与防御'],
        skills: ['网络防护能力', '安全机制理解能力', '漏洞理解能力'],
        jobs: ['网络安全工程师', '渗透测试工程师'],
        interaction: '适合在专业初期或安全方向转入阶段查阅。',
        aiHint: '可先按自己关注的岗位方向，调整案例与课程的阅读顺序。',
      },
      {
        name: '能力巩固：日志研判与处置任务',
        type: '能力巩固',
        format: '实训任务',
        description: '结合网络与信息安全、系统安全、恶意代码检测等课程，整理可直接上手的分析与处置训练。',
        relatedCourses: ['网络与信息安全', '系统安全', '恶意代码检测与防护', '计算机病毒防治'],
        skills: ['风险处置能力', '日志分析能力', '安全分析能力'],
        jobs: ['安全运营工程师', '安全分析工程师'],
        interaction: '适合在安全分析与运营方向训练时配合使用。',
        aiHint: '可结合训练表现，继续补看更合适的日志样本或研判任务。',
      },
      {
        name: '学习辅助：安全岗位门径说明',
        type: '学习辅助',
        format: '方向说明',
        description: '区分网络安全工程、渗透测试、安全运营与安全分析方向的能力侧重，帮助学生少走弯路。',
        relatedCourses: ['网络安全协议分析', '信息安全管理', '网络攻击与防御'],
        skills: ['方向辨析能力', '岗位认知能力', '课程关联认知'],
        jobs: ['网络安全工程师', '安全运营工程师', '渗透测试工程师'],
        interaction: '适合在准备实训、认证或实习前查看。',
        aiHint: '可根据目标岗位，先按更贴近自己的顺序来读这些内容。',
      },
      {
        name: '学习建议：安全方向阶段精进指引',
        type: '学习建议',
        format: '阶段建议',
        description: '根据当前课程进度与岗位意向，提示应先补网络基础、系统原理，还是加强漏洞分析与处置能力。',
        relatedCourses: ['计算机网络', '操作系统', '网络攻击与防御', '恶意代码检测与防护'],
        skills: ['学习规划能力', '风险识别能力', '综合分析能力'],
        jobs: ['网络安全工程师', '安全运营工程师', '安全分析工程师'],
        interaction: '适合在阶段复盘与岗位选择前集中查看。',
        aiHint: '适合在阶段复盘时，对照自己的学习进展查阅。',
      },
    ]),
    aiAssistant: {
      title: '学途建议',
      defaultBody:
        '可结合当前课程与岗位方向，想一想是先补网络基础、系统原理，还是加强安全分析与处置能力。',
      prompts: [
        '若目标偏安全运营，应优先理解网络、日志与告警处置流程。',
        '若目标偏攻防方向，应强化系统原理、Web 后端与漏洞成因分析。',
        '若仍拿不准侧重点，可先从与你目标方向更相关的课程入手。',
      ],
    },
  },
]

export function getMajorById(id) {
  return majors.find((major) => major.id === id) || null
}

export function getAllMajors() {
  return majors
}

export function getSupportModules() {
  return supportModules
}

export function getAllJobProfiles() {
  return jobProfiles
}

export function getJobProfileByName(name) {
  if (!name) return null
  return jobProfileMap.get(String(name).trim()) || null
}
