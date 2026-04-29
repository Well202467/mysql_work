<script setup>
import { computed } from 'vue'
import { getAllMajors } from '../data/majors.js'
import AppIcon from '../components/AppIcon.vue'

const majors = computed(() => getAllMajors())

function normalizeList(value) {
  if (value == null) return []
  if (Array.isArray(value)) return value.map((item) => String(item).trim()).filter(Boolean)
  const text = String(value).trim()
  return text ? [text] : []
}

const relationSteps = [
  { title: '专业 / 学科', desc: '先辨明自己所处的学习方向。', icon: 'majors' },
  { title: '课程', desc: '再由课程理解所学内容。', icon: 'course' },
  { title: '能力', desc: '由课程沉淀出关键能力。', icon: 'ability' },
  { title: '岗位', desc: '再看能力对应的方向选择。', icon: 'job' },
  { title: '学习辅助', desc: '最后借助资源继续深入。', icon: 'support' },
]

const relationPreview = computed(() =>
  majors.value.map((major) => {
    const skills = new Set()
    const jobs = new Set()
    for (const course of major.courses ?? []) {
      for (const item of normalizeList(course.skills)) skills.add(item)
      for (const item of normalizeList(course.jobs)) jobs.add(item)
    }
    return {
      id: major.id,
      name: major.name,
      intro: major.shortTitle,
      courses: (major.courses ?? []).slice(0, 3).map((item) => item.name),
      skills: Array.from(skills).slice(0, 4),
      jobs: Array.from(jobs).slice(0, 4),
      resources: (major.resources ?? []).slice(0, 2).map((item) => item.name),
    }
  }),
)
</script>

<template>
  <div class="page page-shell">
    <section class="section page-section">
      <div class="section__head page-head">
        <p class="section__eyebrow"><AppIcon name="map" :size="16" />学职图谱</p>
        <h1 class="section__title"><AppIcon name="map" :size="22" />学职图谱</h1>
        <p class="section__desc">
          这一页把专业、课程、能力、岗位与学习辅助之间的关系整理成一条清楚的路径，便于快速理解整站结构。
        </p>
      </div>

      <section class="diagram-stage" aria-label="学习关系主图示">
        <div class="diagram-stage__line" aria-hidden="true"></div>
        <article v-for="(step, index) in relationSteps" :key="step.title" class="diagram-node">
          <span class="diagram-node__index">{{ String(index + 1).padStart(2, '0') }}</span>
          <span class="diagram-node__icon">
            <AppIcon :name="step.icon" :size="22" />
          </span>
          <div class="diagram-node__body">
            <h3>{{ step.title }}</h3>
            <p>{{ step.desc }}</p>
          </div>
          <span v-if="index < relationSteps.length - 1" class="diagram-node__arrow" aria-hidden="true">→</span>
        </article>
      </section>

      <div class="relation-summary">
        <div class="relation-summary__head">
          <h2><AppIcon name="major" :size="20" />各专业关系示意</h2>
          <p>以下用简化图示呈现不同专业大致会连向哪些课程重点、能力方向、岗位选择与学习内容。</p>
        </div>

        <div class="relation-preview">
          <article v-for="item in relationPreview" :key="item.id" class="relation-panel">
            <div class="relation-panel__head">
              <div class="relation-panel__title">
                <h3>{{ item.name }}</h3>
                <p>{{ item.intro }}</p>
              </div>
              <RouterLink :to="{ name: 'major', params: { id: item.id } }">进入专业</RouterLink>
            </div>

            <div class="relation-diagram">
              <div class="relation-diagram__major">
                <span class="relation-diagram__major-icon">
                  <AppIcon name="major" :size="18" />
                </span>
                <span>{{ item.name }}</span>
              </div>

              <div class="relation-diagram__steps">
                <div class="relation-panel__col">
                  <span class="relation-panel__label relation-panel__label--with-icon">
                    <AppIcon name="course" :size="15" />
                    课程
                  </span>
                  <div class="chip-group">
                    <span v-for="course in item.courses" :key="course" class="data-chip data-chip--course">{{ course }}</span>
                  </div>
                </div>
                <div class="relation-panel__arrow" aria-hidden="true">→</div>
                <div class="relation-panel__col">
                  <span class="relation-panel__label relation-panel__label--with-icon">
                    <AppIcon name="ability" :size="15" />
                    能力
                  </span>
                  <div class="chip-group">
                    <span v-for="skill in item.skills" :key="skill" class="data-chip">{{ skill }}</span>
                  </div>
                </div>
                <div class="relation-panel__arrow" aria-hidden="true">→</div>
                <div class="relation-panel__col">
                  <span class="relation-panel__label relation-panel__label--with-icon">
                    <AppIcon name="job" :size="15" />
                    岗位
                  </span>
                  <div class="chip-group">
                    <span v-for="job in item.jobs" :key="job" class="data-chip data-chip--job">{{ job }}</span>
                  </div>
                </div>
                <div class="relation-panel__arrow" aria-hidden="true">→</div>
                <div class="relation-panel__col">
                  <span class="relation-panel__label relation-panel__label--with-icon">
                    <AppIcon name="support" :size="15" />
                    学习内容
                  </span>
                  <div class="chip-group">
                    <span v-for="resource in item.resources" :key="resource" class="data-chip data-chip--resource">
                      {{ resource }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </article>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.section__eyebrow {
  margin: 0 0 0.8rem;
  font-size: 0.78rem;
  font-weight: 700;
  letter-spacing: 0.12em;
  color: var(--brand-strong);
}

.section__eyebrow,
.section__title,
.relation-summary__head h2 {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

.section__title {
  margin: 0;
  font-size: clamp(1.45rem, 3vw, 2.1rem);
  color: var(--text-strong);
}

.section__desc {
  margin: 0.7rem 0 0;
  line-height: 1.75;
  color: var(--text-muted);
}

.diagram-stage {
  position: relative;
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 0.8rem;
  margin-top: 1.35rem;
}

.diagram-stage__line {
  position: absolute;
  left: 8%;
  right: 8%;
  top: 4.15rem;
  height: 1px;
  background: linear-gradient(90deg, rgba(66, 103, 154, 0.12) 0%, rgba(66, 103, 154, 0.3) 48%, rgba(111, 149, 143, 0.18) 100%);
}

.diagram-node,
.relation-panel {
  position: relative;
  padding: 1rem;
  border-radius: var(--radius-lg);
  background: rgba(255, 255, 255, 0.76);
  border: 1px solid var(--line);
  box-shadow: none;
}

.diagram-node {
  display: grid;
  gap: 0.8rem;
}

.diagram-node__index {
  display: inline-flex;
  width: fit-content;
  padding: 0.34rem 0.6rem;
  border-radius: 999px;
  background: rgba(66, 103, 154, 0.1);
  color: var(--brand-strong);
  font-size: 0.76rem;
  font-weight: 700;
}

.diagram-node__icon {
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

.diagram-node__body h3,
.relation-panel__head h3 {
  margin: 0;
  color: var(--text-strong);
}

.diagram-node__body p {
  margin: 0.65rem 0 0;
  line-height: 1.72;
  color: var(--text-main);
}

.diagram-node__arrow {
  position: absolute;
  top: 3.5rem;
  right: -0.85rem;
  z-index: 1;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 1.65rem;
  height: 1.65rem;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.98);
  border: 1px solid var(--icon-soft-line);
  color: var(--brand-strong);
  font-weight: 600;
  font-size: 0.88rem;
}

.relation-summary {
  display: grid;
  gap: 1rem;
  margin-top: 1.35rem;
}

.relation-summary__head h2 {
  margin: 0;
  color: var(--text-strong);
}

.relation-summary__head p {
  margin: 0.65rem 0 0;
  line-height: 1.75;
  color: var(--text-muted);
}

.relation-preview {
  display: grid;
  gap: 1rem;
}

.relation-panel__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.8rem;
  margin-bottom: 1rem;
}

.relation-panel__title h3 {
  margin: 0;
}

.relation-panel__title p {
  margin: 0.45rem 0 0;
  line-height: 1.72;
  color: var(--text-muted);
}

.relation-panel__head a {
  color: var(--brand-strong);
  text-decoration: none;
  font-weight: 700;
}

.relation-diagram {
  display: grid;
  gap: 1rem;
}

.relation-diagram__major {
  display: inline-flex;
  align-items: center;
  gap: 0.55rem;
  width: fit-content;
  padding: 0.5rem 0.84rem;
  border-radius: 999px;
  background: rgba(66, 103, 154, 0.08);
  color: var(--brand-strong);
  font-weight: 700;
}

.relation-diagram__major-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: var(--icon-size-sm);
  height: var(--icon-size-sm);
  border-radius: var(--icon-radius);
  background: rgba(255, 255, 255, 0.92);
}

.relation-diagram__steps {
  display: grid;
  grid-template-columns: 1fr auto 1fr auto 1fr auto 1fr;
  gap: 0.8rem;
  align-items: center;
}

.relation-panel__col {
  display: grid;
  gap: 0.7rem;
}

.relation-panel__label {
  font-size: 0.8rem;
  font-weight: 700;
  color: var(--text-muted);
  letter-spacing: 0.04em;
}

.relation-panel__label--with-icon {
  display: inline-flex;
  align-items: center;
  gap: 0.42rem;
}

.relation-panel__arrow {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: var(--icon-size-sm);
  height: var(--icon-size-sm);
  border-radius: var(--icon-radius);
  background: rgba(66, 103, 154, 0.08);
  color: var(--brand-strong);
  font-size: 0.95rem;
  font-weight: 700;
}

.chip-group {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.data-chip {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.4rem 0.72rem;
  border-radius: 999px;
  background: var(--brand-soft);
  color: var(--brand-strong);
  font-size: 0.8rem;
  font-weight: 700;
}

.data-chip--job {
  background: rgba(255, 178, 77, 0.16);
  color: #9a5e0e;
}

.data-chip--course {
  background: rgba(66, 103, 154, 0.08);
  color: var(--brand-strong);
}

.data-chip--resource {
  background: rgba(111, 149, 143, 0.12);
  color: #567972;
}

@media (max-width: 1100px) {
  .diagram-stage {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .diagram-stage__line {
    display: none;
  }

  .diagram-node__arrow {
    display: none;
  }

  .relation-diagram__steps {
    grid-template-columns: 1fr;
  }

  .relation-panel__arrow {
    transform: rotate(90deg);
    justify-self: start;
  }
}

@media (max-width: 760px) {
  .diagram-stage {
    grid-template-columns: 1fr;
  }

  .relation-panel__head {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
