<script setup>
import { computed, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import {
  getCareerAbilities,
  getCareerById,
  getCareerCourses,
  getCareerKnowledgePoints,
} from '../api/careers.js'
import AppIcon from '../components/AppIcon.vue'

const route = useRoute()
const careerId = ref('')
const careerDetail = ref(null)
const careerCourses = ref([])
const careerAbilities = ref([])
const careerKnowledgePoints = ref([])
const loading = ref(false)
const loadError = ref('')
let loadVersion = 0

watch(
  () => route.params.id,
  (value) => {
    careerId.value = toText(value)
    careerDetail.value = null
    careerCourses.value = []
    careerAbilities.value = []
    careerKnowledgePoints.value = []
    loadError.value = ''
    loadCareerPage(careerId.value)
  },
  { immediate: true },
)

const career = computed(() => careerDetail.value)
const basicInfoItems = computed(() => {
  if (!career.value) return []

  return [
    { label: '职业编号', value: career.value.id },
    { label: '职业类别', value: career.value.category },
    { label: '排序权重', value: career.value.sortOrder },
    { label: '创建时间', value: formatDate(career.value.createdAt) },
    { label: '更新时间', value: formatDate(career.value.updatedAt) },
  ].filter((item) => item.value)
})

async function loadCareerPage(id) {
  const currentVersion = ++loadVersion

  if (!id) {
    loadError.value = '未找到对应的职业页面。'
    return
  }

  loading.value = true

  const [careerResult, coursesResult, abilitiesResult, knowledgePointsResult] = await Promise.allSettled([
    getCareerById(id),
    getCareerCourses(id),
    getCareerAbilities(id),
    getCareerKnowledgePoints(id),
  ])

  if (currentVersion !== loadVersion) return

  if (careerResult.status === 'fulfilled') {
    careerDetail.value = normalizeCareer(extractData(careerResult.value))
  } else {
    console.error('获取职业详情失败', careerResult.reason)
    loadError.value = '职业详情加载失败，请稍后重试。'
  }

  if (!careerDetail.value && !loadError.value) {
    loadError.value = '未找到对应的职业页面。'
  }

  if (coursesResult.status === 'fulfilled') {
    careerCourses.value = normalizeCourses(extractArrayData(coursesResult.value))
  } else {
    console.error('获取职业相关课程失败', coursesResult.reason)
    careerCourses.value = []
  }

  if (abilitiesResult.status === 'fulfilled') {
    careerAbilities.value = normalizeAbilities(extractArrayData(abilitiesResult.value))
  } else {
    console.error('获取职业核心能力失败', abilitiesResult.reason)
    careerAbilities.value = []
  }

  if (knowledgePointsResult.status === 'fulfilled') {
    careerKnowledgePoints.value = normalizeKnowledgePoints(extractArrayData(knowledgePointsResult.value))
  } else {
    console.error('获取职业相关知识点失败', knowledgePointsResult.reason)
    careerKnowledgePoints.value = []
  }

  loading.value = false
}

function extractData(response) {
  return response?.data ?? response
}

function extractArrayData(response) {
  const data = extractData(response)
  return Array.isArray(data) ? data : []
}

function normalizeCareer(value) {
  if (!value || Array.isArray(value) || typeof value !== 'object') return null

  const name = toText(value.name ?? value.careerName) || '未命名职业'
  const category = toText(value.category) || '未分类'

  return {
    ...value,
    id: toText(value.id ?? value.careerId) || careerId.value,
    name,
    category,
    description: toText(value.description ?? value.summary ?? value.intro) || '暂无职业简介。',
    sortOrder: toText(value.sortOrder),
    createdAt: toText(value.createdAt ?? value.createTime),
    updatedAt: toText(value.updatedAt ?? value.updateTime),
  }
}

function normalizeCourses(value) {
  return value.map((course, index) => normalizeCourse(course, index))
}

function normalizeCourse(course, index) {
  const source = course && typeof course === 'object' ? course : {}
  const id = toText(source.courseId ?? source.id) || `course-${index + 1}`
  const name = toText(source.courseName ?? source.name) || '未命名课程'

  return {
    ...source,
    id,
    name,
    description: toText(source.courseDescription ?? source.description) || '暂无课程说明。',
    courseRole: toText(source.courseRole) || '暂无课程作用',
    relatedAbility: toText(source.relatedAbility) || '暂无关联能力',
    relatedPoints: toText(source.relatedPoints) || '暂无关联知识点',
    importance: toText(source.importance) || '未标注',
    coreLabel: formatCoreLabel(source.isCore),
    learningStage: toText(source.learningStage) || '暂无学习阶段',
  }
}

function normalizeAbilities(value) {
  return value.map((ability, index) => normalizeAbility(ability, index))
}

function normalizeAbility(ability, index) {
  const source = ability && typeof ability === 'object' ? ability : {}
  const id = toText(source.id) || `ability-${index + 1}`
  const name = toText(source.name) || '未命名能力'

  return {
    ...source,
    id,
    name,
    description: toText(source.description) || '暂无能力说明。',
    abilityType: toText(source.abilityType) || '能力类型未标注',
    importance: toText(source.importance) || '未标注',
    learningAdvice: toText(source.learningAdvice) || '暂无学习建议。',
  }
}

function normalizeKnowledgePoints(value) {
  return value.map((point, index) => normalizeKnowledgePoint(point, index))
}

function normalizeKnowledgePoint(point, index) {
  const source = point && typeof point === 'object' ? point : {}
  const id = toText(source.knowledgePointId ?? source.id) || `knowledge-${index + 1}`
  const name = toText(source.knowledgePointName ?? source.name) || '未命名知识点'

  return {
    ...source,
    id,
    name,
    description: toText(source.knowledgePointDescription ?? source.description) || '暂无知识点说明。',
    courseName: toText(source.courseName) || '暂无关联课程',
    useScene: toText(source.useScene) || '暂无应用场景',
    importance: toText(source.importance) || '未标注',
    learningAdvice: toText(source.learningAdvice) || '暂无学习建议。',
    source: toText(source.source) || '暂无来源',
  }
}

function formatCoreLabel(value) {
  const text = toText(value)
  if (!text) return '未标注'
  if (text === '1' || text.toLowerCase() === 'true' || text === '核心') return '核心课程'
  if (text === '0' || text.toLowerCase() === 'false') return '关联课程'
  return text
}

function formatDate(value) {
  const text = toText(value)
  return text ? text.replace('T', ' ') : ''
}

function toText(value) {
  return String(value ?? '').trim()
}
</script>

<template>
  <div class="page page-shell">
    <nav class="crumb">
      <RouterLink :to="{ name: 'home' }">返回首页</RouterLink>
      <span>/</span>
      <RouterLink :to="{ name: 'careers' }">职业方向</RouterLink>
      <span>/</span>
      <span>{{ career?.name ?? (loading ? '加载中' : '未找到职业') }}</span>
    </nav>

    <div v-if="loading && !career" class="empty">
      <p>正在加载职业数据...</p>
    </div>

    <template v-else-if="career">
      <section class="hero">
        <div class="hero__content">
          <span class="hero__tag">
            <AppIcon name="job" :size="16" />
            {{ career.category }}
          </span>
          <h1 class="hero__title"><AppIcon name="job" :size="28" />{{ career.name }}</h1>
          <p class="hero__intro">{{ career.description }}</p>
        </div>

        <aside class="hero__panel" aria-label="职业数据概览">
          <div class="summary-grid">
            <div class="summary-item">
              <span class="summary-item__value">{{ careerCourses.length }}</span>
              <span class="summary-item__label">相关课程</span>
            </div>
            <div class="summary-item">
              <span class="summary-item__value">{{ careerAbilities.length }}</span>
              <span class="summary-item__label">核心能力</span>
            </div>
            <div class="summary-item">
              <span class="summary-item__value">{{ careerKnowledgePoints.length }}</span>
              <span class="summary-item__label">知识点</span>
            </div>
          </div>
        </aside>
      </section>

      <section class="panel">
        <div class="panel__head">
          <h2 class="panel__title"><AppIcon name="intro" :size="20" />职业基础信息</h2>
          <p class="panel__desc">先明确职业的基础分类与系统编号，再查看它关联的课程、能力和知识点。</p>
        </div>

        <div v-if="basicInfoItems.length" class="info-grid">
          <div v-for="item in basicInfoItems" :key="item.label" class="info-item">
            <span class="info-item__label">{{ item.label }}</span>
            <span class="info-item__value">{{ item.value }}</span>
          </div>
        </div>
        <div v-else class="list-empty">
          <p>暂无基础信息。</p>
        </div>
      </section>

      <section class="panel">
        <div class="panel__head">
          <h2 class="panel__title"><AppIcon name="course" :size="20" />相关课程</h2>
          <p class="panel__desc">这些课程与当前职业方向存在关联，可作为理解岗位能力来源的参考。</p>
        </div>

        <div v-if="careerCourses.length" class="card-grid card-grid--courses">
          <article v-for="course in careerCourses" :key="course.id" class="data-card">
            <div class="data-card__top">
              <span class="data-card__badge">{{ course.importance }}</span>
              <span class="data-card__badge data-card__badge--muted">{{ course.coreLabel }}</span>
            </div>
            <h3>{{ course.name }}</h3>
            <p>{{ course.description }}</p>
            <dl class="detail-list">
              <div>
                <dt>课程作用</dt>
                <dd>{{ course.courseRole }}</dd>
              </div>
              <div>
                <dt>关联能力</dt>
                <dd>{{ course.relatedAbility }}</dd>
              </div>
              <div>
                <dt>相关知识点</dt>
                <dd>{{ course.relatedPoints }}</dd>
              </div>
              <div>
                <dt>学习阶段</dt>
                <dd>{{ course.learningStage }}</dd>
              </div>
            </dl>
          </article>
        </div>
        <div v-else class="list-empty">
          <p>暂无相关课程</p>
        </div>
      </section>

      <section class="panel">
        <div class="panel__head">
          <h2 class="panel__title"><AppIcon name="ability" :size="20" />核心能力</h2>
          <p class="panel__desc">这里展示该职业方向较常涉及的能力要求和学习建议。</p>
        </div>

        <div v-if="careerAbilities.length" class="card-grid">
          <article v-for="ability in careerAbilities" :key="ability.id" class="data-card data-card--soft">
            <div class="data-card__top">
              <span class="data-card__badge">{{ ability.abilityType }}</span>
              <span class="data-card__badge data-card__badge--muted">{{ ability.importance }}</span>
            </div>
            <h3>{{ ability.name }}</h3>
            <p>{{ ability.description }}</p>
            <div class="advice-box">
              <span>学习建议</span>
              <p>{{ ability.learningAdvice }}</p>
            </div>
          </article>
        </div>
        <div v-else class="list-empty">
          <p>暂无能力信息</p>
        </div>
      </section>

      <section class="panel">
        <div class="panel__head">
          <h2 class="panel__title"><AppIcon name="focus" :size="20" />相关知识点</h2>
          <p class="panel__desc">这些知识点说明了课程内容在职业场景中的具体落点。</p>
        </div>

        <div v-if="careerKnowledgePoints.length" class="card-grid">
          <article v-for="point in careerKnowledgePoints" :key="point.id" class="data-card data-card--knowledge">
            <div class="data-card__top">
              <span class="data-card__badge">{{ point.courseName }}</span>
              <span class="data-card__badge data-card__badge--muted">{{ point.importance }}</span>
            </div>
            <h3>{{ point.name }}</h3>
            <p>{{ point.description }}</p>
            <dl class="detail-list">
              <div>
                <dt>应用场景</dt>
                <dd>{{ point.useScene }}</dd>
              </div>
              <div>
                <dt>学习建议</dt>
                <dd>{{ point.learningAdvice }}</dd>
              </div>
              <div>
                <dt>来源</dt>
                <dd>{{ point.source }}</dd>
              </div>
            </dl>
          </article>
        </div>
        <div v-else class="list-empty">
          <p>暂无知识点信息</p>
        </div>
      </section>
    </template>

    <div v-else class="empty">
      <p>{{ loadError || '未找到对应的职业页面。' }}</p>
      <RouterLink :to="{ name: 'careers' }">返回职业方向</RouterLink>
    </div>
  </div>
</template>

<style scoped>
.crumb {
  display: flex;
  align-items: center;
  gap: 0.55rem;
  flex-wrap: wrap;
  font-size: 0.9rem;
  color: var(--text-muted);
}

.crumb a {
  color: var(--brand-strong);
  text-decoration: none;
  font-weight: 600;
}

.hero {
  display: grid;
  grid-template-columns: minmax(0, 1.25fr) minmax(260px, 0.75fr);
  gap: 1rem;
}

.hero__content,
.hero__panel,
.panel {
  padding: 1.6rem;
  border-radius: var(--radius-2xl);
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.88);
}

.hero__content {
  background: linear-gradient(180deg, rgba(54, 74, 98, 0.96) 0%, rgba(68, 88, 112, 0.94) 100%);
  color: rgba(255, 255, 255, 0.92);
}

.hero__tag,
.panel__title,
.data-card__badge {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

.hero__tag {
  width: fit-content;
  padding: 0.42rem 0.78rem;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  font-size: 0.8rem;
  font-weight: 700;
}

.hero__title {
  display: inline-flex;
  align-items: center;
  gap: 0.55rem;
  margin: 1rem 0 0;
  font-size: clamp(2rem, 4vw, 2.85rem);
  line-height: 1.08;
  color: #fff;
}

.hero__intro,
.panel__desc,
.data-card p,
.advice-box p {
  margin: 0;
  line-height: 1.78;
}

.hero__intro {
  margin-top: 1rem;
  color: rgba(255, 255, 255, 0.82);
}

.hero__panel {
  display: flex;
  align-items: stretch;
}

.summary-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 0.85rem;
  width: 100%;
}

.summary-item {
  display: grid;
  gap: 0.2rem;
  padding: 1rem;
  border-radius: var(--radius-xl);
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.7);
}

.summary-item__value {
  color: var(--brand-strong);
  font-size: 1.6rem;
  font-weight: 800;
}

.summary-item__label {
  color: var(--text-muted);
  font-size: 0.84rem;
  font-weight: 700;
}

.panel {
  display: grid;
  gap: 1rem;
}

.panel__head {
  display: grid;
  gap: 0.45rem;
}

.panel__title {
  margin: 0;
  color: var(--text-strong);
}

.panel__desc {
  color: var(--text-muted);
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 0.85rem;
}

.info-item {
  padding: 1rem;
  border-radius: var(--radius-xl);
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.74);
}

.info-item__label,
.detail-list dt,
.advice-box span {
  display: block;
  font-size: 0.78rem;
  font-weight: 700;
  letter-spacing: 0.04em;
  color: var(--text-muted);
}

.info-item__value {
  display: block;
  margin-top: 0.4rem;
  color: var(--text-strong);
  font-weight: 700;
  line-height: 1.65;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 0.9rem;
}

.card-grid--courses {
  grid-template-columns: repeat(3, minmax(0, 1fr));
}

.data-card {
  display: grid;
  align-content: start;
  gap: 0.85rem;
  padding: 1.1rem;
  border-radius: var(--radius-xl);
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.74);
}

.data-card--soft {
  background: rgba(66, 103, 154, 0.06);
}

.data-card--knowledge {
  background: rgba(250, 249, 245, 0.92);
}

.data-card__top {
  display: flex;
  flex-wrap: wrap;
  gap: 0.55rem;
}

.data-card__badge {
  justify-content: center;
  min-height: 1.9rem;
  padding: 0.34rem 0.66rem;
  border-radius: 999px;
  background: rgba(66, 103, 154, 0.1);
  color: var(--brand-strong);
  font-size: 0.78rem;
  font-weight: 700;
}

.data-card__badge--muted {
  background: rgba(12, 31, 54, 0.06);
  color: var(--text-main);
}

.data-card h3 {
  margin: 0;
  color: var(--text-strong);
  font-size: 1.08rem;
  line-height: 1.5;
}

.data-card p {
  color: var(--text-main);
}

.detail-list {
  display: grid;
  gap: 0.75rem;
  margin: 0;
}

.detail-list div {
  display: grid;
  gap: 0.3rem;
}

.detail-list dd {
  margin: 0;
  color: var(--text-main);
  line-height: 1.68;
}

.advice-box {
  display: grid;
  gap: 0.4rem;
  padding: 0.85rem;
  border-radius: var(--radius-lg);
  background: rgba(255, 255, 255, 0.68);
  border: 1px solid var(--line);
}

.list-empty,
.empty {
  padding: 1.2rem;
  border-radius: var(--radius-xl);
  border: 1px dashed var(--line-strong);
  background: rgba(255, 255, 255, 0.64);
  color: var(--text-main);
}

.list-empty p,
.empty p {
  margin: 0;
}

.empty {
  display: grid;
  gap: 0.8rem;
}

.empty a {
  color: var(--brand-strong);
  text-decoration: none;
  font-weight: 600;
}

@media (max-width: 1100px) {
  .card-grid--courses,
  .info-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 900px) {
  .hero,
  .card-grid,
  .card-grid--courses,
  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>
