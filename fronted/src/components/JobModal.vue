<script setup>
import { computed, onBeforeUnmount, onMounted } from 'vue'

const props = defineProps({
  job: {
    type: Object,
    required: true,
  },
})

const emit = defineEmits(['close'])

function close() {
  emit('close')
}

function onKeydown(event) {
  if (event.key === 'Escape') {
    event.preventDefault()
    close()
  }
}

function normalizeList(value) {
  if (value == null) return []
  if (Array.isArray(value)) return value.map((item) => String(item).trim()).filter(Boolean)
  const text = String(value).trim()
  return text ? [text] : []
}

onMounted(() => {
  document.addEventListener('keydown', onKeydown)
})

onBeforeUnmount(() => {
  document.removeEventListener('keydown', onKeydown)
})

const title = computed(() => String(props.job?.name ?? '').trim() || '岗位介绍')
const category = computed(() => String(props.job?.category ?? '岗位方向').trim())
const summary = computed(() => String(props.job?.summary ?? '').trim() || '暂无岗位说明。')
const responsibilities = computed(() => normalizeList(props.job?.responsibilities))
const requirements = computed(() => normalizeList(props.job?.requirements))
</script>

<template>
  <Teleport to="body">
    <div class="modal-root" role="presentation">
      <div class="modal-backdrop" aria-hidden="true" @click.self="close" />
      <div
        class="modal-dialog"
        role="dialog"
        aria-modal="true"
        :aria-labelledby="`job-modal-title-${job.id ?? 'x'}`"
        tabindex="-1"
        @click.stop
      >
        <header class="modal-header">
          <div class="modal-header__main">
            <div class="modal-header__meta">
              <span class="meta-chip">{{ category }}</span>
            </div>
            <h2 :id="`job-modal-title-${job.id ?? 'x'}`" class="modal-title">{{ title }}</h2>
          </div>
          <button type="button" class="modal-close" aria-label="关闭" @click="close">×</button>
        </header>

        <div class="modal-body">
          <section class="field field--intro">
            <h3 class="label">岗位概览</h3>
            <p class="value value--multiline">{{ summary }}</p>
          </section>

          <section class="field">
            <h3 class="label">常见职责</h3>
            <p v-if="responsibilities.length === 0" class="muted">暂无职责说明</p>
            <ul v-else class="point-list" aria-label="岗位职责">
              <li v-for="(item, index) in responsibilities" :key="`resp-${index}`">{{ item }}</li>
            </ul>
          </section>

          <section class="field">
            <h3 class="label">能力要求</h3>
            <p v-if="requirements.length === 0" class="muted">暂无要求说明</p>
            <ul v-else class="point-list" aria-label="岗位要求">
              <li v-for="(item, index) in requirements" :key="`req-${index}`">{{ item }}</li>
            </ul>
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
  width: min(100%, 680px);
  max-height: min(88vh, 780px);
  overflow: auto;
  border-radius: 28px;
  background: linear-gradient(180deg, rgba(255, 255, 255, 0.96) 0%, rgba(248, 250, 255, 0.98) 100%);
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
  background: rgba(255, 178, 77, 0.16);
  color: #9a5e0e;
  font-size: 0.8rem;
  font-weight: 700;
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

.field--intro {
  background: linear-gradient(135deg, rgba(255, 178, 77, 0.12) 0%, rgba(255, 255, 255, 0.86) 100%);
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
  background: linear-gradient(135deg, #f7a645 0%, #f2c36a 100%);
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
}
</style>
