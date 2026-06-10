<script setup>
import { computed, ref } from 'vue'
import { marked } from 'marked'
import DOMPurify from 'dompurify'
import request from '../api/request.js'
import AppIcon from '../components/AppIcon.vue'

const pathSteps = [
  {
    title: '专业 / 学科',
    desc: '先看自己所学的专业方向，理解整体课程脉络。',
    icon: 'majors',
  },
  {
    title: '课程认知',
    desc: '再由课程入手，明白各门课分别在解决什么问题。',
    icon: 'course',
  },
  {
    title: '能力理解',
    desc: '进一步看课程如何沉淀为分析、开发、表达与协作等能力。',
    icon: 'ability',
  },
  {
    title: '岗位方向',
    desc: '再由能力映照岗位，理解不同方向各自看重什么。',
    icon: 'job',
  },
  {
    title: '学习辅助',
    desc: '最后结合资源与问学内容，继续补足自己的薄弱环节。',
    icon: 'support',
  },
]

const assistantPrompts = ['课程如何对应岗位？', '当前阶段先学什么？', '怎样补足能力短板？']
const userQuestion = ref('')
const assistantAnswer = ref('')
const assistantLoading = ref(false)
const assistantError = ref('')

const renderedAiAnswer = computed(() => {
  if (!assistantAnswer.value) return ''
  const rawHtml = marked.parse(assistantAnswer.value)
  return DOMPurify.sanitize(rawHtml)
})

async function askAi() {
  const question = userQuestion.value.trim()
  assistantError.value = ''
  assistantAnswer.value = ''

  if (!question) {
    assistantError.value = '请输入你想咨询的问题。'
    return
  }

  userQuestion.value = question
  assistantLoading.value = true

  try {
    const result = await request.post('/api/ai/chat', { message: userQuestion.value.trim() }, { timeout: 60000 })
    if (result?.code !== 200) {
      assistantError.value = result?.message || 'AI 学习助手暂时无法回答。'
      return
    }

    assistantAnswer.value = String(result?.data?.answer ?? '').trim() || 'AI 学习助手暂时没有返回内容。'
  } catch (error) {
    assistantError.value = error?.response?.status
      ? error?.response?.data?.message || 'AI 学习助手请求失败，请稍后重试。'
      : 'AI 接口暂时不可用，请检查后端服务是否启动。'
  } finally {
    assistantLoading.value = false
  }
}

async function askQuickQuestion(question) {
  userQuestion.value = question
  await askAi()
}
</script>

<template>
  <div class="page page-shell">
    <section class="hero page-section">
      <div class="hero__content">
        <p class="hero__eyebrow">知点序章</p>
        <h1 class="hero__title">
          <AppIcon name="home" :size="28" />
          帮助你看清专业学习与发展路径
        </h1>
        <p class="hero__subtitle">
          围绕专业、课程、能力、岗位与学习内容之间的关系，
          帮助学生更清楚地理解自己正在学什么，下一步该往哪里走。
        </p>
        <div class="hero__actions">
          <RouterLink class="hero__action hero__action--primary" :to="{ name: 'majors' }">
            <AppIcon name="majors" :size="16" />
            进入学科览要
          </RouterLink>
        </div>
      </div>

      <aside class="hero__panel">
        <div class="panel-card panel-card--visual">
          <span class="panel-card__tag">学习路径</span>

          <div class="knowledge-orbit" aria-hidden="true">
            <div class="knowledge-orbit__core">
              <span class="knowledge-orbit__core-icon">
                <AppIcon name="majors" :size="24" />
              </span>
              <span class="knowledge-orbit__core-text">专业认知</span>
            </div>

            <div class="knowledge-orbit__ring"></div>

            <div class="knowledge-node knowledge-node--top">
              <span class="knowledge-node__icon"><AppIcon name="course" :size="16" /></span>
              <span>课程</span>
            </div>
            <div class="knowledge-node knowledge-node--right">
              <span class="knowledge-node__icon"><AppIcon name="ability" :size="16" /></span>
              <span>能力</span>
            </div>
            <div class="knowledge-node knowledge-node--bottom">
              <span class="knowledge-node__icon"><AppIcon name="support" :size="16" /></span>
              <span>学习支持</span>
            </div>
            <div class="knowledge-node knowledge-node--left">
              <span class="knowledge-node__icon"><AppIcon name="job" :size="16" /></span>
              <span>岗位方向</span>
            </div>
          </div>

          <p class="panel-card__caption">由专业起步，渐次看清课程、能力、岗位与学习支持之间的关系。</p>
        </div>
      </aside>
    </section>

    <section class="section page-section">
      <div class="section__head page-head">
        <p class="section__eyebrow">
          <AppIcon name="map" :size="16" />
          使用路径
        </p>
        <h2 class="section__title">
          <AppIcon name="map" :size="22" />
          从专业认识出发，逐步看清学习与发展关系
        </h2>
        <p class="section__desc">这条路径把网站的核心结构连在一起，让你不用读很多说明，也能先看懂内容是如何层层展开的。</p>
      </div>

      <div class="relation-visual" aria-label="首页关系闭环图示">
        <div class="relation-visual__track" aria-hidden="true"></div>

        <article
          v-for="(item, index) in pathSteps"
          :key="item.title"
          class="relation-node"
          :class="`relation-node--${index + 1}`"
        >
          <span class="relation-node__index">{{ String(index + 1).padStart(2, '0') }}</span>
          <span class="relation-node__icon">
            <AppIcon :name="item.icon" :size="20" />
          </span>
          <div class="relation-node__body">
            <h3>{{ item.title }}</h3>
            <p>{{ item.desc }}</p>
          </div>
        </article>
      </div>
    </section>

    <section class="section section--assistant page-section">
      <div class="section__head page-head">
        <p class="section__eyebrow">
          <AppIcon name="support" :size="16" />
          学习辅助
        </p>
        <h2 class="section__title">
          <AppIcon name="support" :size="22" />
          AI 学习助手
        </h2>
        <p class="section__desc">
          当你已经初步看懂专业、课程与岗位关系后，可借由问学对话继续梳理重点与方向。
        </p>
      </div>

      <div class="assistant-panel">
        <div class="assistant-chat">
          <div class="assistant-prompts">
            <button
              v-for="item in assistantPrompts"
              :key="item"
              type="button"
              class="assistant-prompt"
              :disabled="assistantLoading"
              @click="askQuickQuestion(item)"
            >
              {{ item }}
            </button>
          </div>

          <div v-if="assistantLoading" class="chat-bubble chat-bubble--assistant chat-bubble--loading">
            <span class="chat-bubble__role">AI 学习助手</span>
            <p>正在思考...</p>
          </div>

          <form class="assistant-input" @submit.prevent="askAi">
            <label class="sr-only" for="home-ai-question">AI 学习助手问题</label>
            <textarea
              id="home-ai-question"
              v-model="userQuestion"
              class="assistant-textarea"
              rows="4"
              placeholder="请输入你想咨询的课程、能力或岗位方向"
              :disabled="assistantLoading"
            ></textarea>
            <div class="assistant-input__footer">
              <p v-if="assistantError" class="assistant-error">{{ assistantError }}</p>
              <button class="assistant-input__action" type="submit" :disabled="assistantLoading">
                <AppIcon name="support" :size="15" />
                {{ assistantLoading ? '思考中...' : '提问' }}
              </button>
            </div>
          </form>

          <div v-if="assistantAnswer" class="chat-bubble chat-bubble--assistant assistant-result">
            <span class="chat-bubble__role">AI 学习助手</span>
            <div
              class="ai-answer markdown-body"
              v-html="renderedAiAnswer"
            ></div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.hero {
  display: grid;
  grid-template-columns: minmax(0, 1.3fr) minmax(280px, 0.7fr);
  gap: 1rem;
}

.hero__content,
.hero__panel,
.section {
  border-radius: var(--radius-2xl);
}

.hero__content {
  padding: 2rem 1.95rem;
  background: linear-gradient(180deg, rgba(44, 62, 86, 0.96) 0%, rgba(56, 76, 101, 0.94) 100%);
  color: rgba(255, 255, 255, 0.92);
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: 320px;
}

.hero__panel,
.section {
  background: transparent;
}

.hero__panel {
  padding: 1.8rem;
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.78);
  box-shadow: none;
  display: flex;
  align-items: center;
}

.hero__eyebrow,
.section__eyebrow {
  margin: 0 0 0.8rem;
  font-size: 0.78rem;
  font-weight: 700;
  letter-spacing: 0.12em;
}

.hero__title,
.section__title,
.panel-card__title {
  margin: 0;
  color: var(--text-strong);
}

.hero__title,
.section__title,
.panel-card__title,
.hero__action,
.section__eyebrow {
  display: inline-flex;
  align-items: center;
  gap: 0.55rem;
}

.hero__title {
  max-width: 12ch;
  font-size: clamp(2rem, 4vw, 3rem);
  line-height: 1.18;
  letter-spacing: -0.03em;
  color: #fff;
}

.hero__subtitle,
.section__desc,
.panel-card__text,
.entry-card p {
  margin: 0;
  line-height: 1.8;
}

.hero__subtitle {
  margin-top: 0.95rem;
  max-width: 42rem;
  color: rgba(255, 255, 255, 0.8);
}

.hero__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 0.8rem;
  margin-top: 1.5rem;
}

.hero__action {
  justify-content: center;
  min-height: 2.9rem;
  padding: 0.65rem 1.15rem;
  border-radius: 999px;
  border: 1px solid rgba(255, 255, 255, 0.22);
  color: rgba(255, 255, 255, 0.88);
  text-decoration: none;
  font-weight: 600;
  background: rgba(255, 255, 255, 0.06);
}

.hero__action--primary {
  background: rgba(255, 255, 255, 0.92);
  color: var(--bg-deep);
  border-color: rgba(255, 255, 255, 0.92);
}

.panel-card__tag {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.42rem 0.74rem;
  border-radius: 999px;
  background: rgba(66, 103, 154, 0.12);
  color: var(--brand-strong);
  font-size: 0.8rem;
  font-weight: 700;
}

.panel-card__title {
  margin-top: 1rem;
  font-size: 1.32rem;
  line-height: 1.2;
}

.panel-card__text {
  margin-top: 0.9rem;
  color: var(--text-main);
}

.panel-card--visual {
  display: grid;
  justify-items: center;
  gap: 0.85rem;
  width: 100%;
}

.knowledge-orbit {
  position: relative;
  width: min(100%, 292px);
  aspect-ratio: 1 / 1;
  display: grid;
  place-items: center;
}

.knowledge-orbit__ring {
  position: absolute;
  inset: 16%;
  border-radius: 50%;
  border: 1px solid rgba(66, 103, 154, 0.14);
  background: radial-gradient(circle, rgba(255, 255, 255, 0.34) 0%, rgba(255, 255, 255, 0) 72%);
}

.knowledge-orbit__core {
  position: relative;
  z-index: 1;
  display: grid;
  justify-items: center;
  gap: 0.55rem;
  width: 6.8rem;
  height: 6.8rem;
  padding: 1rem;
  border-radius: 50%;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.98) 0%, rgba(244, 247, 252, 0.94) 100%);
  border: 1px solid rgba(66, 103, 154, 0.16);
}

.knowledge-orbit__core-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 2.75rem;
  height: 2.75rem;
  border-radius: 999px;
  background: rgba(66, 103, 154, 0.1);
  color: var(--brand-strong);
}

.knowledge-orbit__core-text {
  color: var(--text-strong);
  font-weight: 700;
  text-align: center;
}

.knowledge-node {
  position: absolute;
  z-index: 1;
  display: inline-flex;
  align-items: center;
  gap: 0.45rem;
  min-height: 2.4rem;
  padding: 0.52rem 0.78rem;
  border-radius: 999px;
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.92);
  color: var(--text-main);
  font-size: 0.82rem;
  font-weight: 600;
  white-space: nowrap;
}

.knowledge-node__icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 1.6rem;
  height: 1.6rem;
  border-radius: 999px;
  background: rgba(66, 103, 154, 0.08);
  color: var(--brand-strong);
}

.knowledge-node--top {
  top: 4%;
  left: 50%;
  transform: translateX(-50%);
}

.knowledge-node--right {
  right: 2%;
  top: 50%;
  transform: translateY(-50%);
}

.knowledge-node--bottom {
  bottom: 4%;
  left: 50%;
  transform: translateX(-50%);
}

.knowledge-node--left {
  left: 2%;
  top: 50%;
  transform: translateY(-50%);
}

.panel-card__caption {
  margin: 0;
  max-width: 24rem;
  text-align: center;
  line-height: 1.7;
  color: var(--text-muted);
  font-size: 0.9rem;
}

.section__eyebrow {
  color: var(--brand-strong);
}

.section__title {
  font-size: clamp(1.4rem, 3vw, 2rem);
}

.section__desc {
  margin-top: 0.7rem;
  color: var(--text-muted);
}

.relation-visual {
  position: relative;
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 1rem;
  margin-top: 1.35rem;
}

.relation-visual__track {
  position: absolute;
  left: 8%;
  right: 8%;
  top: 3.2rem;
  height: 1px;
  background: linear-gradient(90deg, rgba(66, 103, 154, 0.12) 0%, rgba(66, 103, 154, 0.28) 50%, rgba(111, 149, 143, 0.16) 100%);
}

.relation-node {
  position: relative;
  display: grid;
  gap: 0.72rem;
  padding: 1rem;
  border-radius: var(--radius-xl);
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.78);
}

.relation-node__index {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 2rem;
  width: fit-content;
  padding: 0.26rem 0.52rem;
  border-radius: 999px;
  background: rgba(66, 103, 154, 0.1);
  color: var(--brand-strong);
  font-size: 0.72rem;
  font-weight: 700;
}

.relation-node__icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: var(--icon-size-md);
  height: var(--icon-size-md);
  border-radius: var(--icon-radius);
  background: rgba(255, 255, 255, 0.96);
  border: 1px solid var(--icon-soft-line);
  color: var(--brand-strong);
  z-index: 1;
}

.relation-node__body h3 {
  margin: 0;
  font-size: 1rem;
  color: var(--text-strong);
}

.relation-node__body p {
  margin: 0.65rem 0 0;
  line-height: 1.72;
  color: var(--text-main);
}

.section--assistant {
  background: rgba(255, 255, 255, 0.88);
}

.assistant-panel {
  display: block;
  width: 100%;
  max-width: 980px;
  box-sizing: border-box;
  margin: 0 auto;
}

.assistant-chat {
  width: 100%;
  max-width: 980px;
  min-width: 0;
  margin: 0 auto;
  box-sizing: border-box;
  padding: 1.3rem;
  border-radius: var(--radius-xl);
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.8);
  box-shadow: none;
  overflow: hidden;
}

.chat-bubble p {
  margin: 0;
  line-height: 1.8;
}

.assistant-chat {
  display: grid;
  gap: 1rem;
}

.chat-bubble {
  max-width: 88%;
  padding: 0.95rem 1rem;
  border-radius: 22px;
  border: 1px solid var(--line);
}

.chat-bubble--user {
  justify-self: end;
  background: rgba(38, 103, 255, 0.1);
  border-color: rgba(38, 103, 255, 0.16);
}

.chat-bubble--assistant {
  justify-self: start;
  background: rgba(255, 255, 255, 0.94);
}

.chat-bubble__role {
  display: inline-block;
  margin-bottom: 0.45rem;
  font-size: 0.78rem;
  font-weight: 700;
  color: var(--text-muted);
}

.chat-bubble p {
  color: var(--text-main);
}

.assistant-prompts {
  display: flex;
  flex-wrap: wrap;
  gap: 0.65rem;
}

.assistant-prompt {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.42rem 0.78rem;
  border-radius: 999px;
  border: 1px solid rgba(66, 103, 154, 0.12);
  background: rgba(66, 103, 154, 0.08);
  color: var(--brand-strong);
  font-size: 0.8rem;
  font-weight: 700;
  cursor: pointer;
}

.assistant-prompt:disabled {
  cursor: wait;
  opacity: 0.72;
}

.assistant-input {
  display: grid;
  gap: 0.85rem;
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  padding: 0.95rem 1rem;
  border-radius: 18px;
  border: 1px solid var(--line);
  background: rgba(247, 250, 255, 0.94);
}

.assistant-textarea {
  width: 100%;
  max-width: 100%;
  min-height: 150px;
  max-height: 220px;
  box-sizing: border-box;
  resize: vertical;
  border: none;
  background: transparent;
  color: var(--text-strong);
  font: inherit;
  line-height: 1.6;
}

.assistant-textarea:focus {
  outline: none;
}

.assistant-textarea::placeholder {
  color: var(--text-muted);
}

.assistant-input__footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.8rem;
}

.assistant-input__action {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 0.4rem;
  min-width: 4.5rem;
  padding: 0.55rem 0.8rem;
  border-radius: 999px;
  border: 1px solid var(--brand-strong);
  background: var(--brand-strong);
  color: #fff;
  font-size: 0.88rem;
  font-weight: 700;
  cursor: pointer;
}

.assistant-input__action:disabled,
.assistant-textarea:disabled {
  cursor: wait;
  opacity: 0.72;
}

.assistant-error {
  margin: 0;
  color: #9f2f2f;
  font-size: 0.88rem;
  line-height: 1.6;
}

.chat-bubble--loading p {
  color: var(--text-muted);
}

.assistant-result {
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  overflow: hidden;
  word-break: break-word;
  overflow-wrap: anywhere;
}

.ai-answer {
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  max-height: 520px;
  color: var(--text-main);
  line-height: 1.8;
  overflow-y: auto;
  overflow-x: hidden;
  word-break: break-word;
  overflow-wrap: anywhere;
}

.markdown-body :deep(*) {
  box-sizing: border-box;
}

.markdown-body :deep(h1),
.markdown-body :deep(h2),
.markdown-body :deep(h3),
.markdown-body :deep(h4),
.markdown-body :deep(h5),
.markdown-body :deep(h6) {
  margin: 0.9rem 0 0.45rem;
  color: var(--text-strong);
  line-height: 1.35;
  font-weight: 800;
}

.markdown-body :deep(h1) {
  font-size: 1.45rem;
}

.markdown-body :deep(h2) {
  font-size: 1.28rem;
}

.markdown-body :deep(h3) {
  font-size: 1.12rem;
}

.markdown-body :deep(h4),
.markdown-body :deep(h5),
.markdown-body :deep(h6) {
  font-size: 1rem;
}

.markdown-body :deep(p),
.markdown-body :deep(ul),
.markdown-body :deep(ol),
.markdown-body :deep(blockquote),
.markdown-body :deep(pre) {
  margin: 0.55rem 0 0;
}

.markdown-body :deep(p:first-child),
.markdown-body :deep(h1:first-child),
.markdown-body :deep(h2:first-child),
.markdown-body :deep(h3:first-child),
.markdown-body :deep(ul:first-child),
.markdown-body :deep(ol:first-child),
.markdown-body :deep(pre:first-child) {
  margin-top: 0;
}

.markdown-body :deep(ul),
.markdown-body :deep(ol) {
  padding-left: 1.35rem;
}

.markdown-body :deep(li + li) {
  margin-top: 0.25rem;
}

.markdown-body :deep(strong) {
  color: var(--text-strong);
  font-weight: 800;
}

.markdown-body :deep(code) {
  padding: 0.12rem 0.32rem;
  border-radius: 6px;
  background: rgba(66, 103, 154, 0.1);
  color: var(--text-strong);
  font-family: Consolas, 'Liberation Mono', Menlo, monospace;
  font-size: 0.92em;
}

.markdown-body :deep(pre) {
  padding: 0.85rem;
  border-radius: 12px;
  background: rgba(30, 41, 59, 0.95);
  overflow-x: auto;
}

.markdown-body :deep(pre code) {
  padding: 0;
  background: transparent;
  color: rgba(255, 255, 255, 0.92);
}

.markdown-body :deep(blockquote) {
  padding-left: 0.85rem;
  border-left: 3px solid rgba(66, 103, 154, 0.24);
  color: var(--text-muted);
}

.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border: 0;
}

@media (max-width: 960px) {
  .hero,
  .relation-visual {
    grid-template-columns: 1fr;
  }

  .relation-visual__track {
    left: 1.15rem;
    right: auto;
    top: 1.15rem;
    bottom: 1.15rem;
    width: 1px;
    height: auto;
    background: linear-gradient(180deg, rgba(66, 103, 154, 0.12) 0%, rgba(66, 103, 154, 0.28) 50%, rgba(111, 149, 143, 0.16) 100%);
  }

  .relation-node {
    padding-left: 3.2rem;
  }

  .relation-node__icon {
    position: absolute;
    left: 1rem;
    top: 1rem;
  }

  .hero__content {
    min-height: auto;
  }

  .knowledge-orbit {
    width: min(100%, 252px);
  }

  .knowledge-node {
    font-size: 0.78rem;
  }

  .chat-bubble {
    max-width: 100%;
  }

  .assistant-input__footer {
    flex-direction: column;
    align-items: flex-start;
  }
}

@media (max-width: 620px) {
  .hero__panel {
    padding: 1.2rem;
  }

  .knowledge-orbit {
    width: 100%;
    max-width: 232px;
  }

  .knowledge-node {
    padding: 0.46rem 0.68rem;
  }
}
</style>
