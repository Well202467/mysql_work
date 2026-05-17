<script setup>
import { computed, onMounted, ref } from 'vue'
import request from '../api/request.js'
import AppIcon from '../components/AppIcon.vue'

const rawMajors = ref([])
const loading = ref(true)
const loadError = ref('')

const majors = computed(() => rawMajors.value.map((major, index) => normalizeMajor(major, index)))
const emptyMessage = computed(() => {
  if (loading.value) return '正在加载专业数据...'
  return loadError.value || '暂无专业数据。'
})

onMounted(() => {
  loadMajors()
})

async function loadMajors() {
  loading.value = true
  loadError.value = ''

  try {
    const response = await request.get('/api/majors')
    rawMajors.value = normalizeResponseArray(response)
  } catch (error) {
    console.error('获取专业列表失败', error)
    rawMajors.value = []
    loadError.value = '专业列表加载失败，请稍后重试。'
  } finally {
    loading.value = false
  }
}

function normalizeResponseArray(response) {
  const data = response?.data ?? response
  return Array.isArray(data) ? data : []
}

function normalizeMajor(major, index) {
  const source = major && typeof major === 'object' ? major : {}
  const id = toText(source.id) || `major-${index + 1}`
  const name = toText(source.name) || '未命名专业'
  const intro = toText(source.intro) || '暂无专业简介。'

  return {
    ...source,
    id,
    name,
    intro,
    shortTitle: toText(source.shortTitle) || name,
    tagline: toText(source.tagline) || intro,
    abilityOverview: normalizeList(source.abilityOverview),
    careerFocus: normalizeList(source.careerFocus),
  }
}

function normalizeList(value) {
  if (value == null) return []
  if (Array.isArray(value)) return value.map((item) => toText(item)).filter(Boolean)
  const text = toText(value)
  return text ? [text] : []
}

function toText(value) {
  return String(value ?? '').trim()
}
</script>

<template>
  <div class="page page-shell">
    <section class="section page-section">
      <div class="section__head page-head">
        <p class="section__eyebrow"><AppIcon name="majors" :size="16" />学科览要</p>
        <h1 class="section__title"><AppIcon name="majors" :size="22" />学科览要</h1>
        <p class="section__desc">
          这里是全站按专业进入内容的起点。你可以先浏览不同专业方向，再进入对应页面查看课程、能力、岗位与学习内容之间的联系。
        </p>

        <div class="hero-visual" aria-hidden="true">
          <div class="hero-visual__track"></div>
          <span class="hero-visual__chip hero-visual__chip--book">
            <AppIcon name="major" :size="16" />
            专业
          </span>
          <span class="hero-visual__chip hero-visual__chip--course">
            <AppIcon name="course" :size="16" />
            课程
          </span>
          <span class="hero-visual__chip hero-visual__chip--ability">
            <AppIcon name="ability" :size="16" />
            能力
          </span>
          <span class="hero-visual__chip hero-visual__chip--job">
            <AppIcon name="job" :size="16" />
            岗位
          </span>
        </div>
      </div>

      <div v-if="majors.length" class="major-grid">
        <RouterLink
          v-for="major in majors"
          :key="major.id"
          class="major-card"
          :to="{ name: 'major', params: { id: major.id } }"
        >
          <div class="major-card__top">
            <span class="major-card__badge">
              <AppIcon name="major" :size="16" />
              {{ major.name }}
            </span>
            <span class="major-card__meta">查看方向</span>
          </div>
          <p class="major-card__subtitle">{{ major.shortTitle }}</p>
          <p class="major-card__desc">{{ major.tagline }}</p>

          <div class="major-card__symbol" aria-hidden="true">
            <span class="major-card__symbol-core">
              <AppIcon
                :name="major.id === 'software-engineering' ? 'course' : major.id === 'data-science' ? 'ability' : 'job'"
                :size="20"
              />
            </span>
          </div>

          <div class="major-card__block">
            <span class="major-card__label">能力方向</span>
            <div class="chip-group">
              <span v-for="item in major.abilityOverview.slice(0, 3)" :key="item" class="data-chip">
                {{ item }}
              </span>
              <span v-if="major.abilityOverview.length === 0" class="data-chip data-chip--muted">
                暂无数据
              </span>
            </div>
          </div>

          <div class="major-card__block">
            <span class="major-card__label">岗位方向</span>
            <div class="chip-group">
              <span
                v-for="item in major.careerFocus.slice(0, 3)"
                :key="item"
                class="data-chip data-chip--job"
              >
                {{ item }}
              </span>
              <span v-if="major.careerFocus.length === 0" class="data-chip data-chip--muted">
                暂无数据
              </span>
            </div>
          </div>

          <span class="major-card__cta">
            <AppIcon name="major" :size="16" />
            进入该专业
          </span>
        </RouterLink>
      </div>

      <div v-else class="catalog-empty">
        <p>{{ emptyMessage }}</p>
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
.major-card__badge,
.major-card__cta {
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
  max-width: 60rem;
}

.hero-visual {
  position: relative;
  margin-top: 1.15rem;
  min-height: 4.3rem;
}

.hero-visual__track {
  position: absolute;
  left: 0.6rem;
  right: 0.6rem;
  top: 2.05rem;
  height: 1px;
  background: linear-gradient(90deg, rgba(66, 103, 154, 0.1) 0%, rgba(66, 103, 154, 0.24) 45%, rgba(111, 149, 143, 0.18) 100%);
}

.hero-visual__chip {
  position: absolute;
  display: inline-flex;
  align-items: center;
  gap: 0.45rem;
  padding: 0.42rem 0.72rem;
  border-radius: 999px;
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.92);
  color: var(--text-main);
  font-size: 0.8rem;
  font-weight: 600;
}

.hero-visual__chip--book {
  left: 0;
  top: 0;
}

.hero-visual__chip--course {
  left: 26%;
  top: 1.5rem;
}

.hero-visual__chip--ability {
  left: 50%;
  top: 0;
  transform: translateX(-50%);
}

.hero-visual__chip--job {
  right: 0;
  top: 1.5rem;
}

.major-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 1.1rem;
  margin-top: 1.45rem;
}

.major-card {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 1.45rem;
  border-radius: var(--radius-xl);
  text-decoration: none;
  color: inherit;
  background: rgba(255, 255, 255, 0.76);
  border: 1px solid var(--line);
  box-shadow: none;
  min-height: 100%;
  transition: border-color 0.18s ease, background 0.18s ease;
}

.major-card:hover {
  border-color: rgba(66, 103, 154, 0.26);
  background: rgba(255, 255, 255, 0.9);
}

.major-card__top {
  display: flex;
  justify-content: space-between;
  gap: 0.8rem;
  align-items: center;
}

.major-card__badge,
.major-card__meta,
.major-card__label {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.major-card__badge {
  padding: 0.42rem 0.74rem;
  border-radius: 999px;
  background: rgba(66, 103, 154, 0.1);
  color: var(--brand-strong);
  font-size: 0.84rem;
  font-weight: 700;
}

.major-card__meta {
  padding: 0.38rem 0.68rem;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.72);
  color: var(--text-muted);
  font-size: 0.8rem;
  font-weight: 700;
}

.major-card__subtitle {
  margin: 0;
  font-size: 1rem;
  font-weight: 600;
  line-height: 1.72;
  color: var(--text-strong);
}

.major-card__desc {
  margin: 0;
  line-height: 1.75;
  color: var(--text-muted);
}

.major-card__symbol {
  position: relative;
  width: 100%;
  height: 2.8rem;
  border-radius: 18px;
  background: linear-gradient(90deg, rgba(66, 103, 154, 0.05) 0%, rgba(255, 255, 255, 0.22) 100%);
  overflow: hidden;
}

.major-card__symbol-core {
  position: absolute;
  left: 1rem;
  top: 50%;
  transform: translateY(-50%);
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

.major-card__block {
  display: grid;
  gap: 0.55rem;
}

.major-card__label {
  font-size: 0.8rem;
  font-weight: 700;
  color: var(--text-muted);
  letter-spacing: 0.04em;
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

.data-chip--muted {
  background: rgba(12, 31, 54, 0.06);
  color: var(--text-muted);
}

.major-card__cta {
  margin-top: auto;
  color: var(--brand-strong);
  font-weight: 700;
}

.catalog-empty {
  margin-top: 1.45rem;
  padding: 1.2rem;
  border-radius: var(--radius-xl);
  border: 1px dashed var(--line-strong);
  background: rgba(255, 255, 255, 0.64);
  color: var(--text-main);
}

.catalog-empty p {
  margin: 0;
}

@media (max-width: 1100px) {
  .major-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .hero-visual__chip--course {
    left: 22%;
  }
}

@media (max-width: 720px) {
  .major-grid {
    grid-template-columns: 1fr;
  }

  .hero-visual {
    min-height: 8.6rem;
  }

  .hero-visual__track {
    left: 1.15rem;
    top: 0.7rem;
    bottom: 0.9rem;
    width: 1px;
    height: auto;
    background: linear-gradient(180deg, rgba(66, 103, 154, 0.1) 0%, rgba(66, 103, 154, 0.24) 45%, rgba(111, 149, 143, 0.18) 100%);
  }

  .hero-visual__chip {
    left: 2.1rem;
    right: auto;
    transform: none;
  }

  .hero-visual__chip--book {
    top: 0;
  }

  .hero-visual__chip--course {
    top: 2rem;
  }

  .hero-visual__chip--ability {
    top: 4rem;
  }

  .hero-visual__chip--job {
    top: 6rem;
  }
}
</style>
