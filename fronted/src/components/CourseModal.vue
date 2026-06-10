<script setup>
import { computed, onBeforeUnmount, onMounted } from 'vue'

const props = defineProps({
  course: {
    type: Object,
    required: true,
  },
  knowledgePoints: {
    type: Array,
    default: () => [],
  },
  careers: {
    type: Array,
    default: () => [],
  },
})

const emit = defineEmits(['close', 'view-career', 'view-job'])

function close() {
  emit('close')
}

function onKeydown(event) {
  if (event.key === 'Escape') {
    event.preventDefault()
    close()
  }
}

function toText(value) {
  return String(value ?? '').trim()
}

function uniqueList(items) {
  return Array.from(new Set(items.filter(Boolean)))
}

function normalizeCareer(career, index) {
  const source = career && typeof career === 'object' ? career : {}
  const id = toText(source.careerId ?? source.id)
  const name = toText(source.careerName ?? source.name) || '未命名职业'

  return {
    ...source,
    id,
    key: id || `${name}-${index}`,
    name,
    category: toText(source.category) || '未分类',
    courseRole: toText(source.courseRole),
  }
}

function viewCareer(career) {
  if (!career.id) return
  emit('view-career', { ...career, careerId: career.id })
}

onMounted(() => {
  document.addEventListener('keydown', onKeydown)
})

onBeforeUnmount(() => {
  document.removeEventListener('keydown', onKeydown)
})

const title = computed(() => props.course?.name?.trim() || '未命名课程')
const typeLabel = computed(() => String(props.course?.type ?? '未分类').trim() || '未分类')
const duration = computed(() => String(props.course?.credits ?? props.course?.duration ?? '2 学分').trim())
const description = computed(() => String(props.course?.description ?? '').trim() || '暂无说明')
const goals = computed(() => uniqueList(props.knowledgePoints.map((item) => toText(item?.ability))))
const relatedCareers = computed(() =>
  props.careers
    .map((item, index) => normalizeCareer(item, index))
    .filter((item) => item.id || item.name || item.category || item.courseRole),
)
const points = computed(() =>
  props.knowledgePoints
    .map((item) => {
      const name = toText(item?.name)
      const pointDescription = toText(item?.description)
      if (name && pointDescription) return `${name}：${pointDescription}`
      return name || pointDescription
    })
    .filter(Boolean),
)
</script>

<template>
  <Teleport to="body">
    <div class="modal-root" role="presentation">
      <div class="modal-backdrop" aria-hidden="true" @click.self="close" />
      <div
        class="modal-dialog"
        role="dialog"
        aria-modal="true"
        :aria-labelledby="`course-modal-title-${course.id ?? 'x'}`"
        tabindex="-1"
        @click.stop
      >
        <header class="modal-header">
          <div class="modal-header__main">
            <div class="modal-header__meta">
              <span class="meta-chip">{{ typeLabel }}</span>
              <span class="meta-chip meta-chip--ghost">{{ duration }}</span>
            </div>
            <h2 :id="`course-modal-title-${course.id ?? 'x'}`" class="modal-title">{{ title }}</h2>
          </div>
          <button type="button" class="modal-close" aria-label="关闭" @click="close">×</button>
        </header>

        <div class="modal-body">
          <section class="field">
            <h3 class="label">课程说明</h3>
            <p class="value value--multiline">{{ description }}</p>
          </section>

          <section class="field">
            <h3 class="label">对应能力</h3>
            <p v-if="goals.length === 0" class="muted">暂无目标说明</p>
            <ul v-else class="tag-list" aria-label="学习目标">
              <li v-for="(item, index) in goals" :key="`goal-${index}`" class="tag">{{ item }}</li>
            </ul>
          </section>

          <section class="field">
            <h3 class="label">重点知识梳理</h3>
            <p v-if="points.length === 0" class="muted">暂无重点梳理</p>
            <ul v-else class="point-list" aria-label="关键要点">
              <li v-for="(item, index) in points" :key="`point-${index}`">{{ item }}</li>
            </ul>
          </section>

          <section class="field">
            <h3 class="label">关联岗位</h3>
            <p v-if="relatedCareers.length === 0" class="muted">暂无岗位映射</p>
            <div v-else class="career-list" aria-label="关联岗位">
              <button
                v-for="career in relatedCareers"
                :key="career.key"
                type="button"
                class="career-card"
                :disabled="!career.id"
                @click="viewCareer(career)"
              >
                <span class="career-card__main">
                  <span class="career-card__name">{{ career.name }}</span>
                  <span class="career-card__category">{{ career.category }}</span>
                </span>
                <span v-if="career.courseRole" class="career-card__role">课程作用：{{ career.courseRole }}</span>
              </button>
            </div>
          </section>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<style scoped>
.modal-root {
  position: fixed;
  inset: 0;
  z-index: 1000;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;
}

.modal-backdrop {
  position: absolute;
  inset: 0;
  background: rgba(5, 17, 31, 0.56);
  backdrop-filter: blur(8px);
}

.modal-dialog {
  position: relative;
  z-index: 1;
  width: min(100%, 640px);
  max-height: min(88vh, 760px);
  overflow: auto;
  border-radius: 28px;
  background:
    linear-gradient(180deg, rgba(255, 255, 255, 0.96) 0%, rgba(244, 248, 255, 0.98) 100%);
  border: 1px solid rgba(255, 255, 255, 0.42);
  box-shadow: 0 26px 80px rgba(5, 17, 31, 0.24);
}

.modal-header {
  position: sticky;
  top: 0;
  z-index: 1;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 1rem;
  padding: 1.35rem 1.35rem 1rem;
  background: rgba(247, 250, 255, 0.94);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid var(--line);
}

.modal-header__main {
  min-width: 0;
}

.modal-header__meta {
  display: flex;
  gap: 0.55rem;
  flex-wrap: wrap;
  margin-bottom: 0.7rem;
}

.meta-chip {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.42rem 0.75rem;
  border-radius: 999px;
  background: var(--brand-soft);
  color: var(--brand-strong);
  font-size: 0.8rem;
  font-weight: 700;
}

.meta-chip--ghost {
  background: rgba(12, 31, 54, 0.08);
  color: var(--text-main);
}

.modal-title {
  margin: 0;
  font-size: 1.45rem;
  line-height: 1.24;
  color: var(--text-strong);
}

.modal-close {
  width: 2.4rem;
  height: 2.4rem;
  flex: none;
  border-radius: 14px;
  border: none;
  background: rgba(12, 31, 54, 0.08);
  color: var(--text-main);
  font-size: 1.45rem;
  line-height: 1;
}

.modal-body {
  padding: 1.2rem 1.35rem 1.4rem;
}

.field {
  margin-bottom: 1.15rem;
  padding: 1rem;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.72);
  border: 1px solid var(--line);
}

.field:last-child {
  margin-bottom: 0;
}

.label {
  margin: 0 0 0.65rem;
  font-size: 0.82rem;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  font-weight: 700;
  color: var(--text-muted);
}

.value,
.muted,
.point-list li {
  margin: 0;
  line-height: 1.74;
}

.value {
  color: var(--text-main);
}

.value--multiline {
  white-space: pre-wrap;
}

.muted {
  color: var(--text-muted);
}

.tag-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-wrap: wrap;
  gap: 0.55rem;
}

.tag {
  display: inline-flex;
  padding: 0.45rem 0.8rem;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.86);
  color: var(--text-strong);
  border: 1px solid var(--line);
  font-size: 0.88rem;
  font-weight: 600;
}

.point-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: grid;
  gap: 0.65rem;
}

.point-list li {
  position: relative;
  padding-left: 1rem;
  color: var(--text-main);
}

.point-list li::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0.6rem;
  width: 0.44rem;
  height: 0.44rem;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--brand) 0%, var(--teal) 100%);
}

.career-list {
  display: grid;
  gap: 0.65rem;
}

.career-card {
  display: grid;
  gap: 0.55rem;
  width: 100%;
  padding: 0.9rem 1rem;
  border-radius: 18px;
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.82);
  color: inherit;
  text-align: left;
  cursor: pointer;
}

.career-card:disabled {
  cursor: default;
}

.career-card:not(:disabled):hover {
  border-color: rgba(66, 103, 154, 0.28);
  background: rgba(255, 255, 255, 0.96);
}

.career-card__main {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 0.75rem;
}

.career-card__name {
  color: var(--text-strong);
  font-weight: 700;
  line-height: 1.5;
}

.career-card__category {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: none;
  padding: 0.32rem 0.62rem;
  border-radius: 999px;
  background: rgba(255, 178, 77, 0.16);
  color: #9a5e0e;
  font-size: 0.78rem;
  font-weight: 700;
}

.career-card__role {
  color: var(--text-muted);
  font-size: 0.88rem;
  line-height: 1.65;
}

@media (max-width: 640px) {
  .modal-header,
  .modal-body {
    padding-left: 1rem;
    padding-right: 1rem;
  }

  .modal-dialog {
    border-radius: 22px;
  }

  .career-card__main {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
