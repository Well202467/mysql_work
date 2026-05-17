<script setup>
import { computed, onMounted, ref } from 'vue'
import request from '../api/request.js'
import AppIcon from '../components/AppIcon.vue'

const rawCareers = ref([])
const loading = ref(true)
const loadError = ref('')

const careers = computed(() => rawCareers.value.map((career, index) => normalizeCareer(career, index)))
const emptyMessage = computed(() => {
  if (loading.value) return '正在加载职业数据...'
  return loadError.value || '暂无职业数据。'
})

onMounted(() => {
  loadCareers()
})

async function loadCareers() {
  loading.value = true
  loadError.value = ''

  try {
    const response = await request.get('/api/careers')
    rawCareers.value = normalizeResponseArray(response)
  } catch (error) {
    console.error('获取职业列表失败', error)
    rawCareers.value = []
    loadError.value = '职业列表加载失败，请稍后重试。'
  } finally {
    loading.value = false
  }
}

function normalizeResponseArray(response) {
  const data = response?.data ?? response
  return Array.isArray(data) ? data : []
}

function normalizeCareer(career, index) {
  const source = career && typeof career === 'object' ? career : {}
  const id = toText(source.id) || `career-${index + 1}`
  const name = toText(source.name) || '未命名职业'
  const category = toText(source.category) || '未分类'

  return {
    ...source,
    id,
    name,
    category,
  }
}

function toText(value) {
  return String(value ?? '').trim()
}
</script>

<template>
  <div class="page page-shell">
    <section class="section page-section">
      <div class="section__head page-head">
        <p class="section__eyebrow"><AppIcon name="job" :size="16" />职业方向</p>
        <h1 class="section__title"><AppIcon name="job" :size="22" />职业方向</h1>
        <p class="section__desc">
          这里汇总当前系统中的职业方向。你可以先了解不同岗位类别，后续再进入职业详情查看关联课程、职业能力与知识点。
        </p>
      </div>

      <div v-if="careers.length" class="career-grid">
        <RouterLink
          v-for="career in careers"
          :key="career.id"
          class="career-card"
          :to="`/careers/${encodeURIComponent(career.id)}`"
        >
          <div class="career-card__top">
            <span class="career-card__badge">
              <AppIcon name="job" :size="16" />
              {{ career.category }}
            </span>
            <span class="career-card__meta">查看详情</span>
          </div>

          <div class="career-card__body">
            <span class="career-card__icon" aria-hidden="true">
              <AppIcon name="job" :size="20" />
            </span>
            <h2>{{ career.name }}</h2>
            <p>职业编号：{{ career.id }}</p>
          </div>

          <span class="career-card__cta">
            <AppIcon name="job" :size="16" />
            进入职业详情
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
.career-card__badge,
.career-card__cta {
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

.career-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 1.1rem;
  margin-top: 1.45rem;
}

.career-card {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  min-height: 100%;
  padding: 1.35rem;
  border-radius: var(--radius-xl);
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.76);
  color: inherit;
  text-decoration: none;
  transition: border-color 0.18s ease, background 0.18s ease;
}

.career-card:hover {
  border-color: rgba(66, 103, 154, 0.26);
  background: rgba(255, 255, 255, 0.9);
}

.career-card__top {
  display: flex;
  justify-content: space-between;
  gap: 0.8rem;
  align-items: center;
}

.career-card__badge,
.career-card__meta {
  justify-content: center;
  border-radius: 999px;
  font-size: 0.8rem;
  font-weight: 700;
}

.career-card__badge {
  padding: 0.42rem 0.74rem;
  background: rgba(255, 178, 77, 0.16);
  color: #9a5e0e;
}

.career-card__meta {
  display: inline-flex;
  align-items: center;
  padding: 0.38rem 0.68rem;
  background: rgba(255, 255, 255, 0.72);
  color: var(--text-muted);
}

.career-card__body {
  display: grid;
  gap: 0.75rem;
}

.career-card__icon {
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

.career-card__body h2 {
  margin: 0;
  font-size: 1.08rem;
  line-height: 1.5;
  color: var(--text-strong);
}

.career-card__body p {
  margin: 0;
  line-height: 1.72;
  color: var(--text-muted);
}

.career-card__cta {
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
  .career-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 720px) {
  .career-grid {
    grid-template-columns: 1fr;
  }
}
</style>
