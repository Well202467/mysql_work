<script setup>
import { computed, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCourseCareers, getCourseKnowledgePoints } from '../api/courses.js'
import request from '../api/request.js'
import CourseModal from '../components/CourseModal.vue'
import AppIcon from '../components/AppIcon.vue'

const route = useRoute()
const router = useRouter()
const majorId = ref('')
const majorDetail = ref(null)
const majorCourses = ref([])
const loading = ref(false)
const loadError = ref('')
const showModal = ref(false)
const selectedCourse = ref(null)
const selectedCourseKnowledgePoints = ref([])
const selectedCourseCareers = ref([])
const majorKnowledgePoints = ref([])
const majorCourseCareers = ref([])
const searchQuery = ref('')
const typeFilter = ref('all')
let loadVersion = 0
let knowledgePointLoadVersion = 0

watch(
  () => route.params.id,
  (value) => {
    majorId.value = String(value ?? '')
    majorDetail.value = null
    majorCourses.value = []
    majorKnowledgePoints.value = []
    majorCourseCareers.value = []
    loadError.value = ''
    showModal.value = false
    selectedCourse.value = null
    selectedCourseKnowledgePoints.value = []
    selectedCourseCareers.value = []
    searchQuery.value = ''
    typeFilter.value = 'all'
    loadMajorPage(majorId.value)
  },
  { immediate: true },
)

const major = computed(() => majorDetail.value)
const courses = computed(() => majorCourses.value)
const learningGoals = computed(() => {
  const map = new Map()

  for (const point of majorKnowledgePoints.value) {
    const name = toText(point.name)
    const description = toText(point.description)
    const text = name && description ? `${name}：${description}` : name || description
    if (text && !map.has(text)) map.set(text, text)
    if (map.size >= 6) break
  }

  return Array.from(map.values())
})
const abilityOverview = computed(() =>
  uniqueTextList([
    ...majorKnowledgePoints.value.flatMap((point) => splitTextList(point.ability)),
    ...majorCourseCareers.value.flatMap((career) => splitTextList(career.relatedAbility)),
  ]),
)
const careerProfiles = computed(() => {
  const map = new Map()

  for (const career of majorCourseCareers.value) {
    const id = toText(career.careerId ?? career.id)
    const name = toText(career.careerName ?? career.name)
    const key = id || name
    if (!key || map.has(key)) continue

    map.set(key, {
      ...career,
      id,
      name: name || '未命名职业',
      category: toText(career.category) || '未分类',
    })
  }

  return Array.from(map.values())
})
const softMetrics = computed(() => {
  const metrics = normalizeMetrics(major.value?.metrics)
  if (metrics.length) return metrics
  if (!major.value) return []

  return [
    { label: '专业编号', value: major.value.id || '暂无' },
    { label: '课程数量', value: `${courses.value.length} 门` },
  ]
})
const courseEmptyMessage = computed(() => {
  if (loading.value) return '正在加载课程数据...'
  if (loadError.value && major.value) return loadError.value
  if (courses.value.length === 0) return '暂无课程数据。'
  return '未找到符合条件的课程，请调整关键词或筛选条件。'
})

async function loadMajorPage(id) {
  const currentVersion = ++loadVersion

  if (!id) {
    loadError.value = '未找到对应的学科页面。'
    return
  }

  loading.value = true

  try {
    const majorResponse = await request.get(`/api/majors/${encodeURIComponent(id)}`)
    if (currentVersion !== loadVersion) return

    majorDetail.value = normalizeMajor(extractData(majorResponse))
    if (!majorDetail.value) {
      loadError.value = '未找到对应的学科页面。'
      return
    }

    try {
      const coursesResponse = await request.get(`/api/majors/${encodeURIComponent(id)}/courses`)
      if (currentVersion !== loadVersion) return
      majorCourses.value = normalizeCourses(extractArrayData(coursesResponse))
      await loadMajorCourseRelations(majorCourses.value, currentVersion)
    } catch (error) {
      if (currentVersion !== loadVersion) return
      console.error('获取专业课程失败', error)
      majorCourses.value = []
      majorKnowledgePoints.value = []
      majorCourseCareers.value = []
      loadError.value = '课程列表加载失败，请稍后重试。'
    }
  } catch (error) {
    if (currentVersion !== loadVersion) return
    console.error('获取专业详情失败', error)
    majorDetail.value = null
    majorCourses.value = []
    majorKnowledgePoints.value = []
    majorCourseCareers.value = []
    loadError.value = '专业详情加载失败，请稍后重试。'
  } finally {
    if (currentVersion === loadVersion) {
      loading.value = false
    }
  }
}

async function loadMajorCourseRelations(courseList, currentVersion) {
  const courseIds = uniqueTextList(courseList.map((course) => course?.id))

  if (!courseIds.length) {
    majorKnowledgePoints.value = []
    majorCourseCareers.value = []
    return
  }

  const [knowledgePointGroups, careerGroups] = await Promise.all([
    Promise.all(
      courseIds.map(async (courseId) => {
        try {
          const response = await getCourseKnowledgePoints(courseId)
          return normalizeKnowledgePoints(extractArrayData(response))
        } catch (error) {
          console.error(`获取课程 ${courseId} 的知识点失败`, error)
          return []
        }
      }),
    ),
    Promise.all(
      courseIds.map(async (courseId) => {
        try {
          const response = await getCourseCareers(courseId)
          return normalizeCourseCareers(extractArrayData(response))
        } catch (error) {
          console.error(`获取课程 ${courseId} 的关联岗位失败`, error)
          return []
        }
      }),
    ),
  ])

  if (currentVersion !== loadVersion) return

  majorKnowledgePoints.value = dedupeKnowledgePoints(knowledgePointGroups.flat())
  majorCourseCareers.value = dedupeCourseCareers(careerGroups.flat())
}

function normalizeList(value) {
  if (value == null) return []
  if (Array.isArray(value)) return value.map((item) => String(item).trim()).filter(Boolean)
  const text = String(value).trim()
  return text ? [text] : []
}

function splitTextList(value) {
  return normalizeList(value)
    .flatMap((item) => item.split(/[、,，;；/]/))
    .map((item) => item.trim())
    .filter(Boolean)
}

function uniqueTextList(value) {
  return Array.from(new Set(normalizeList(value)))
}

function extractData(response) {
  return response?.data ?? response
}

function extractArrayData(response) {
  const data = extractData(response)
  return Array.isArray(data) ? data : []
}

function normalizeMajor(value) {
  if (!value || Array.isArray(value) || typeof value !== 'object') return null

  const name = toText(value.name) || '未命名专业'
  const intro = toText(value.intro) || '暂无专业简介。'

  return {
    ...value,
    id: toText(value.id) || majorId.value,
    name,
    badge: toText(value.badge) || '专业详情',
    shortTitle: toText(value.shortTitle) || name,
    intro,
    overview: toText(value.overview) || intro,
    metrics: normalizeMetrics(value.metrics),
  }
}

function normalizeCourses(value) {
  return value.map((course, index) => normalizeCourse(course, index))
}

function normalizeCourse(course, index) {
  const source = course && typeof course === 'object' ? course : {}
  const id = toText(source.id ?? source.courseId) || `course-${index + 1}`
  const name = toText(source.name ?? source.courseName) || '未命名课程'
  const description = toText(source.description ?? source.courseDescription) || '暂无课程说明。'
  const credits = formatCredits(source.credits ?? source.duration)
  const skills = normalizeList(source.skills ?? source.goals)
  const jobs = normalizeList(source.jobs)
  const points = normalizeList(source.points)
  const interaction = toText(source.interaction) || '暂无学习建议。'
  const aiHint = toText(source.aiHint ?? source.jobExplanation) || '暂无 AI 提示。'

  return {
    ...source,
    id,
    name,
    description,
    type: toText(source.type ?? source.courseType) || '未分类',
    credits,
    duration: credits,
    skills,
    goals: skills,
    jobs,
    points,
    interaction,
    aiHint,
    jobExplanation: aiHint,
  }
}

function normalizeKnowledgePoints(value) {
  return value
    .map((item) => {
      const source = item && typeof item === 'object' ? item : {}
      return {
        ...source,
        id: source.id ?? source.knowledgePointId,
        name: toText(source.name ?? source.knowledgePointName),
        description: toText(source.description ?? source.knowledgePointDescription),
        ability: toText(source.ability),
      }
    })
    .filter((item) => item.name || item.description || item.ability)
}

function dedupeKnowledgePoints(value) {
  const map = new Map()

  for (const point of value) {
    const id = toText(point?.id)
    const name = toText(point?.name)
    const description = toText(point?.description)
    const ability = toText(point?.ability)
    const key = id || `${name}-${description}` || ability
    if (!key || map.has(key)) continue
    map.set(key, { ...point, id, name, description, ability })
  }

  return Array.from(map.values())
}

function normalizeCourseCareers(value) {
  return value
    .map((item, index) => {
      const source = item && typeof item === 'object' ? item : {}
      const careerId = toText(source.careerId ?? source.id)
      const careerName = toText(source.careerName ?? source.name) || '未命名职业'

      return {
        ...source,
        key: careerId || `${careerName}-${index}`,
        careerId,
        careerName,
        name: careerName,
        category: toText(source.category) || '未分类',
        courseRole: toText(source.courseRole),
      }
    })
    .filter((item) => item.careerId || item.careerName)
}

function dedupeCourseCareers(value) {
  const map = new Map()

  for (const career of value) {
    const id = toText(career?.careerId ?? career?.id)
    const name = toText(career?.careerName ?? career?.name)
    const key = id || name
    if (!key || map.has(key)) continue
    map.set(key, {
      ...career,
      id,
      careerId: id,
      careerName: name || '未命名职业',
      name: name || '未命名职业',
      category: toText(career?.category) || '未分类',
      relatedAbility: toText(career?.relatedAbility),
    })
  }

  return Array.from(map.values())
}

function normalizeMetrics(value) {
  if (!Array.isArray(value)) return []

  return value
    .map((item) => ({
      label: toText(item?.label),
      value: toText(item?.value),
    }))
    .filter((item) => item.label && item.value)
}

function formatCredits(value) {
  const text = toText(value)
  if (!text) return '暂无学分'
  return text.includes('学分') ? text : `${text} 学分`
}

function toText(value) {
  return String(value ?? '').trim()
}

function courseTitle(course) {
  return String(course?.name ?? '未命名课程').trim()
}

function courseType(course) {
  return String(course?.type ?? '未分类').trim()
}

function courseBlurb(course) {
  return String(course?.description ?? '暂无课程说明。').trim()
}

const filterOptions = computed(() => {
  const set = new Set()
  for (const course of courses.value) {
    const type = courseType(course)
    if (type) set.add(type)
  }
  return [{ value: 'all', label: '全部课程' }, ...Array.from(set).map((item) => ({ value: item, label: item }))]
})

const courseStructure = computed(() =>
  filterOptions.value
    .filter((item) => item.value !== 'all')
    .map((item, index) => {
      const related = courses.value.filter((course) => courseType(course) === item.value)
      return {
        key: item.value,
        title: item.label,
        count: related.length,
        samples: related.slice(0, 2).map((course) => courseTitle(course)),
        icon: ['intro', 'course', 'ability', 'focus', 'support'][index] ?? 'course',
      }
    }),
)

function abilityIconName(index) {
  return ['focus', 'ability', 'course', 'support'][index] ?? 'ability'
}

function jobIconName(index) {
  return ['job', 'map', 'major', 'support'][index] ?? 'job'
}

const filteredCourses = computed(() => {
  let list = courses.value
  const needle = searchQuery.value.trim().toLowerCase()

  if (needle) {
    list = list.filter((course) => {
      const text = [
        courseTitle(course),
        courseBlurb(course),
        ...normalizeList(course.skills),
        ...normalizeList(course.jobs),
      ]
        .join(' ')
        .toLowerCase()
      return text.includes(needle)
    })
  }

  if (typeFilter.value !== 'all') {
    list = list.filter((course) => courseType(course) === typeFilter.value)
  }

  return list
})

async function openCourse(course) {
  const currentVersion = ++knowledgePointLoadVersion
  selectedCourse.value = course
  selectedCourseKnowledgePoints.value = []
  selectedCourseCareers.value = []
  showModal.value = true

  const courseId = toText(course?.id)
  if (!courseId) return

  const [knowledgePointsResult, careersResult] = await Promise.allSettled([
    getCourseKnowledgePoints(courseId),
    getCourseCareers(courseId),
  ])

  if (currentVersion !== knowledgePointLoadVersion) return

  if (knowledgePointsResult.status === 'fulfilled') {
    selectedCourseKnowledgePoints.value = normalizeKnowledgePoints(extractArrayData(knowledgePointsResult.value))
  } else {
    console.error('获取课程知识点失败', knowledgePointsResult.reason)
    selectedCourseKnowledgePoints.value = []
  }

  if (careersResult.status === 'fulfilled') {
    selectedCourseCareers.value = normalizeCourseCareers(extractArrayData(careersResult.value))
  } else {
    console.error('获取课程关联岗位失败', careersResult.reason)
    selectedCourseCareers.value = []
  }
}

function closeModal() {
  knowledgePointLoadVersion += 1
  showModal.value = false
  selectedCourse.value = null
  selectedCourseKnowledgePoints.value = []
  selectedCourseCareers.value = []
}

function handleViewJob(name) {
  closeModal()
  const match = careerProfiles.value.find((career) => career.name === name || career.careerName === name)
  if (match) openCareerDetail(match)
}

function handleViewCareer(career) {
  const id = toText(career?.careerId ?? career?.id)
  if (!id) return
  closeModal()
  router.push(`/careers/${encodeURIComponent(id)}`)
}

function openCareerDetail(career) {
  const id = toText(career?.careerId ?? career?.id)
  if (!id) return
  router.push(`/careers/${encodeURIComponent(id)}`)
}
</script>

<template>
  <div class="page page-shell">
    <nav class="crumb">
      <RouterLink :to="{ name: 'home' }">返回首页</RouterLink>
      <span>/</span>
      <RouterLink :to="{ name: 'majors' }">学科览要</RouterLink>
      <span>/</span>
      <span>{{ major?.name ?? (loading ? '加载中' : '未找到学科') }}</span>
    </nav>

    <div v-if="loading && !major" class="empty">
      <p>正在加载专业数据...</p>
    </div>

    <template v-else-if="major">
      <section class="hero">
        <div class="hero__content">
          <span class="hero__tag">{{ major.badge }}</span>
          <h1 class="hero__title"><AppIcon name="major" :size="28" />{{ major.name }}</h1>
          <p class="hero__subtitle">{{ major.shortTitle }}</p>
          <p class="hero__intro">{{ major.intro }}</p>
        </div>

        <aside class="hero__panel">
          <div class="hero__meta">
            <div v-for="item in softMetrics" :key="item.label" class="hero__meta-item">
              <span class="hero__meta-label">{{ item.label }}</span>
              <span class="hero__meta-value">{{ item.value }}</span>
            </div>
          </div>
        </aside>
      </section>

      <section class="panel panel--intro">
        <div class="panel__head">
          <h2 class="panel__title"><AppIcon name="intro" :size="20" />专业认知</h2>
          <p class="panel__desc">先对这个专业形成整体印象，再往下看它对应的能力、岗位与课程路径。</p>
        </div>
        <p class="panel__lead">{{ major.overview }}</p>
      </section>

      <section class="panel">
        <div class="panel__head">
          <h2 class="panel__title"><AppIcon name="focus" :size="20" />学习重点</h2>
          <p class="panel__desc">这几个方面最值得优先理解，它们会贯穿相关课程与方向选择。</p>
        </div>
        <div v-if="learningGoals.length" class="focus-grid">
          <article v-for="item in learningGoals" :key="item" class="focus-card">
            <span class="focus-card__dot" aria-hidden="true"></span>
            <p>{{ item }}</p>
          </article>
        </div>
        <div v-else class="list-empty">
          <p>暂无学习重点数据。</p>
        </div>
      </section>

      <section class="feature-grid">
        <article class="panel panel--accent">
          <div class="panel__head">
            <h2 class="panel__title"><AppIcon name="ability" :size="20" />核心能力</h2>
            <p class="panel__desc">这些能力，是理解课程价值和判断发展方向时最重要的参照。</p>
          </div>
          <div v-if="abilityOverview.length" class="ability-grid">
            <article v-for="(item, index) in abilityOverview" :key="item" class="ability-card">
              <span class="ability-card__icon">
                <AppIcon :name="abilityIconName(index)" :size="18" />
              </span>
              <span class="ability-card__label">{{ item }}</span>
            </article>
          </div>
          <div v-else class="list-empty">
            <p>暂无能力数据。</p>
          </div>
        </article>

        <article class="panel panel--accent panel--jobs">
          <div class="panel__head">
            <h2 class="panel__title"><AppIcon name="job" :size="20" />典型岗位方向</h2>
            <p class="panel__desc">点击岗位，可进一步了解这一方向常见的工作内容与能力要求。</p>
          </div>
          <div v-if="careerProfiles.length" class="job-grid">
            <button
              v-for="(item, index) in careerProfiles"
              :key="item.id"
              type="button"
              class="job-card"
              @click="openCareerDetail(item)"
            >
              <span class="job-card__icon">
                <AppIcon :name="jobIconName(index)" :size="18" />
              </span>
              <span class="job-card__name">{{ item.name }}</span>
              <p class="job-card__summary">{{ item.category }}</p>
            </button>
          </div>
          <div v-else class="list-empty">
            <p>暂无岗位数据。</p>
          </div>
        </article>
      </section>

      <section class="panel">
        <div class="list-head">
          <div class="panel__head">
            <h2 class="panel__title"><AppIcon name="course" :size="20" />核心课程</h2>
            <p class="panel__desc">课程不只是一张清单，更是理解专业能力如何逐步形成的路径。</p>
          </div>
        </div>

        <div v-if="courseStructure.length" class="course-structure">
          <article v-for="item in courseStructure" :key="item.key" class="course-stage">
            <span class="course-stage__icon">
              <AppIcon :name="item.icon" :size="18" />
            </span>
            <div class="course-stage__body">
              <div class="course-stage__head">
                <h3>{{ item.title }}</h3>
                <span>{{ item.count }} 门</span>
              </div>
              <p>{{ item.samples.join('、') }}</p>
            </div>
          </article>
        </div>

        <div class="toolbar">
          <label class="search-field">
            <span class="sr-only">搜索课程</span>
            <input
              v-model.trim="searchQuery"
              class="search-input"
              type="search"
              placeholder="搜索课程 / 能力 / 岗位…"
              autocomplete="off"
            />
          </label>

          <div class="filter-group" role="group" aria-label="课程筛选">
            <button
              v-for="item in filterOptions"
              :key="item.value"
              type="button"
              class="filter-btn"
              :class="{ 'filter-btn--active': typeFilter === item.value }"
              @click="typeFilter = item.value"
            >
              {{ item.label }}
            </button>
          </div>
        </div>

        <div v-if="filteredCourses.length" class="course-grid">
          <article
            v-for="(course, index) in filteredCourses"
            :key="course.id"
            class="course-card"
            role="button"
            tabindex="0"
            @click="openCourse(course)"
            @keydown.enter.prevent="openCourse(course)"
            @keydown.space.prevent="openCourse(course)"
          >
            <div class="course-card__head">
              <span class="course-card__index">{{ String(index + 1).padStart(2, '0') }}</span>
              <span class="course-type">{{ courseType(course) }}</span>
              <span class="course-duration">{{ course.credits }}</span>
            </div>
            <h3 class="course-name">{{ courseTitle(course) }}</h3>
            <p class="course-desc">{{ courseBlurb(course) }}</p>

            <div v-if="normalizeList(course.skills).length" class="course-card__block">
              <span class="course-card__label">对应能力</span>
              <div class="chip-group">
                <span v-for="skill in normalizeList(course.skills).slice(0, 2)" :key="skill" class="data-chip">
                  {{ skill }}
                </span>
              </div>
            </div>

            <div v-if="normalizeList(course.jobs).length" class="course-card__block">
              <span class="course-card__label">岗位方向</span>
              <div class="course-row__chips">
                <button
                  v-for="job in normalizeList(course.jobs).slice(0, 2)"
                  :key="`${course.id}-${job}`"
                  type="button"
                  class="mini-chip mini-chip--job"
                  @click.stop="handleViewJob(job)"
                >
                  {{ job }}
                </button>
              </div>
            </div>
          </article>
        </div>

        <div v-else class="list-empty">
          <p>{{ courseEmptyMessage }}</p>
        </div>
      </section>

    </template>

    <div v-else class="empty">
      <p>{{ loadError || '未找到对应的学科页面。' }}</p>
      <RouterLink :to="{ name: 'majors' }">返回学科览要</RouterLink>
    </div>

    <CourseModal
      v-if="showModal && selectedCourse"
      :course="selectedCourse"
      :knowledge-points="selectedCourseKnowledgePoints"
      :careers="selectedCourseCareers"
      @close="closeModal"
      @view-job="handleViewJob"
      @view-career="handleViewCareer"
    />
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
.course-type,
.course-duration {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 999px;
}

.hero__tag {
  padding: 0.42rem 0.78rem;
  background: rgba(255, 255, 255, 0.12);
  font-size: 0.8rem;
  font-weight: 700;
}

.hero__title {
  margin: 1rem 0 0;
  font-size: clamp(2rem, 4vw, 2.85rem);
  line-height: 1.08;
  letter-spacing: -0.03em;
}

.hero__title,
.panel__title {
  display: inline-flex;
  align-items: center;
  gap: 0.55rem;
}

.hero__subtitle {
  margin: 0.7rem 0 0;
  font-size: 1.02rem;
  line-height: 1.7;
  color: rgba(255, 255, 255, 0.8);
}

.hero__intro,
.panel__desc,
.panel__lead,
.course-desc,
.job-card__summary,
.focus-card p {
  margin: 0;
  line-height: 1.78;
}

.hero__intro {
  margin-top: 1rem;
}

.hero__panel {
  display: flex;
  align-items: center;
}

.hero__meta {
  display: grid;
  gap: 0.85rem;
  width: 100%;
}

.hero__meta-item {
  padding-bottom: 0.85rem;
  border-bottom: 1px solid var(--line);
}

.hero__meta-item:last-child {
  padding-bottom: 0;
  border-bottom: none;
}

.hero__meta-label,
.course-card__label {
  display: block;
  font-size: 0.78rem;
  font-weight: 700;
  letter-spacing: 0.04em;
  color: var(--text-muted);
}

.hero__meta-value {
  display: block;
  margin-top: 0.35rem;
  color: var(--text-strong);
  font-weight: 600;
  line-height: 1.7;
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

.panel__lead {
  color: var(--text-main);
}

.panel--intro {
  background: rgba(255, 255, 255, 0.82);
}

.focus-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 0.85rem;
}

.focus-card {
  display: flex;
  gap: 0.8rem;
  padding: 1rem;
  border-radius: var(--radius-xl);
  background: rgba(255, 255, 255, 0.72);
  border: 1px solid var(--line);
}

.focus-card__dot {
  width: 0.6rem;
  height: 0.6rem;
  border-radius: 50%;
  background: var(--brand);
  flex: none;
  margin-top: 0.55rem;
}

.feature-grid {
  display: grid;
  grid-template-columns: minmax(0, 0.92fr) minmax(0, 1.08fr);
  gap: 1rem;
}

.panel--accent {
  background: rgba(252, 252, 250, 0.9);
}

.ability-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 0.8rem;
}

.ability-card {
  display: grid;
  gap: 0.72rem;
  padding: 1rem;
  border-radius: var(--radius-xl);
  background: rgba(66, 103, 154, 0.08);
  border: 1px solid rgba(66, 103, 154, 0.12);
}

.ability-card__icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: var(--icon-size-md);
  height: var(--icon-size-md);
  border-radius: var(--icon-radius);
  background: rgba(255, 255, 255, 0.92);
  color: var(--brand-strong);
  border: 1px solid var(--icon-soft-line);
}

.ability-card__label {
  color: var(--brand-strong);
  font-weight: 700;
  line-height: 1.68;
}

.job-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 0.8rem;
}

.job-card {
  display: grid;
  gap: 0.72rem;
  padding: 1rem;
  border-radius: var(--radius-xl);
  border: 1px solid rgba(201, 167, 106, 0.22);
  background: rgba(255, 249, 240, 0.9);
  text-align: left;
  cursor: pointer;
}

.job-card__icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: var(--icon-size-md);
  height: var(--icon-size-md);
  border-radius: var(--icon-radius);
  background: rgba(255, 255, 255, 0.92);
  color: #8c5b16;
  border: 1px solid rgba(201, 167, 106, 0.16);
}

.job-card__name {
  display: block;
  color: #8c5b16;
  font-weight: 700;
}

.job-card__summary {
  margin-top: 0.55rem;
  color: var(--text-main);
}

.list-head {
  display: flex;
  justify-content: space-between;
  gap: 1rem;
  align-items: flex-start;
}

.toolbar {
  display: flex;
  flex-direction: column;
  gap: 0.8rem;
}

.course-structure {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 0.85rem;
}

.course-stage {
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 0.8rem;
  padding: 1rem;
  border-radius: var(--radius-xl);
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.74);
}

.course-stage__icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: var(--icon-size-md);
  height: var(--icon-size-md);
  border-radius: var(--icon-radius);
  background: rgba(66, 103, 154, 0.08);
  color: var(--brand-strong);
  border: 1px solid var(--icon-soft-line);
}

.course-stage__body {
  min-width: 0;
}

.course-stage__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.6rem;
}

.course-stage__head h3 {
  margin: 0;
  font-size: 0.96rem;
  color: var(--text-strong);
}

.course-stage__head span {
  color: var(--text-muted);
  font-size: 0.8rem;
  font-weight: 700;
}

.course-stage__body p {
  margin: 0.55rem 0 0;
  line-height: 1.68;
  color: var(--text-main);
}

.search-input {
  width: 100%;
  min-height: 3rem;
  padding: 0.78rem 1rem;
  border-radius: 16px;
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.8);
  color: var(--text-strong);
}

.search-input:focus {
  outline: none;
  border-color: rgba(66, 103, 154, 0.36);
  box-shadow: 0 0 0 4px rgba(66, 103, 154, 0.08);
}

.filter-group {
  display: flex;
  flex-wrap: wrap;
  gap: 0.6rem;
}

.filter-btn {
  min-height: 2.4rem;
  padding: 0.5rem 0.88rem;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.72);
  color: var(--text-main);
  border: 1px solid var(--line);
}

.filter-btn--active {
  background: rgba(66, 103, 154, 0.1);
  color: var(--brand-strong);
  border-color: rgba(66, 103, 154, 0.22);
  font-weight: 700;
}

.course-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 0.9rem;
}

.course-card {
  display: grid;
  gap: 0.85rem;
  padding: 1.1rem;
  border-radius: var(--radius-xl);
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.72);
}

.course-card__head {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  flex-wrap: wrap;
}

.course-card__index {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 2rem;
  padding: 0.3rem 0.56rem;
  border-radius: 999px;
  background: rgba(66, 103, 154, 0.1);
  color: var(--brand-strong);
  font-size: 0.76rem;
  font-weight: 700;
}

.course-name {
  margin: 0;
  font-size: 1.08rem;
  font-weight: 700;
  color: var(--text-strong);
}

.course-type {
  padding: 0.32rem 0.6rem;
  font-size: 0.76rem;
  font-weight: 700;
  background: var(--brand-soft);
  color: var(--brand-strong);
}

.course-duration {
  padding: 0.32rem 0.6rem;
  font-size: 0.76rem;
  font-weight: 700;
  background: rgba(12, 31, 54, 0.06);
  color: var(--text-main);
}

.course-card__block {
  display: grid;
  gap: 0.5rem;
}

.chip-group,
.course-row__chips {
  display: flex;
  flex-wrap: wrap;
  gap: 0.55rem;
}

.data-chip {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.38rem 0.7rem;
  border-radius: 999px;
  background: rgba(66, 103, 154, 0.08);
  color: var(--brand-strong);
  font-size: 0.78rem;
  font-weight: 700;
}

.mini-chip {
  padding: 0.35rem 0.66rem;
  border-radius: 999px;
  border: 1px solid transparent;
  font-size: 0.78rem;
  font-weight: 700;
}

.mini-chip--job {
  background: rgba(201, 167, 106, 0.16);
  color: #8c5b16;
  border-color: rgba(201, 167, 106, 0.18);
  cursor: pointer;
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

.empty a {
  color: var(--brand-strong);
  text-decoration: none;
  font-weight: 600;
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

@media (max-width: 1080px) {
  .feature-grid,
  .course-grid,
  .focus-grid,
  .course-structure {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 960px) {
  .hero,
  .job-grid,
  .ability-grid {
    grid-template-columns: 1fr;
  }

  .list-head {
    flex-direction: column;
  }
}
</style>
