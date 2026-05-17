<script setup>
import { computed } from 'vue'

const entries = [
  { label: '专业管理', routeName: 'admin-majors', available: true },
  { label: '课程管理', routeName: 'admin-courses', available: true },
  { label: '专业课程关系管理', routeName: 'admin-major-courses', available: true },
  { label: '知识点管理', routeName: 'admin-knowledge-points', available: true },
  { label: '职业管理', routeName: 'admin-careers', available: true },
  { label: '职业画像管理', routeName: 'admin-career-profiles', available: true },
  { label: '职业能力管理', routeName: 'admin-career-abilities', available: true },
  { label: '职业课程关系', routeName: 'admin-career-courses', available: true },
  { label: '职业知识点关系', routeName: 'admin-career-knowledge-points', available: true },
]

const availableEntries = computed(() => entries.filter((item) => item.available))
const disabledEntries = computed(() => entries.filter((item) => !item.available))
</script>

<template>
  <section class="dashboard">
    <div class="dashboard__head">
      <h2>管理员后台</h2>
      <p>选择需要维护的数据模块。</p>
    </div>

    <div class="entry-grid">
      <RouterLink
        v-for="entry in availableEntries"
        :key="entry.label"
        class="entry-card"
        :to="{ name: entry.routeName }"
      >
        <strong>{{ entry.label }}</strong>
        <span>进入管理</span>
      </RouterLink>
      <article v-for="entry in disabledEntries" :key="entry.label" class="entry-card entry-card--disabled">
        <strong>{{ entry.label }}</strong>
        <span>后续开放</span>
      </article>
    </div>
  </section>
</template>

<style scoped>
.dashboard {
  display: grid;
  gap: 1rem;
  margin-top: 1rem;
}

.dashboard__head {
  padding: 1.15rem;
  border: 1px solid var(--line);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.84);
}

.dashboard__head h2,
.dashboard__head p {
  margin: 0;
}

.dashboard__head h2 {
  color: var(--text-strong);
  font-size: 1.35rem;
}

.dashboard__head p {
  margin-top: 0.45rem;
  color: var(--text-muted);
}

.entry-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 1rem;
}

.entry-card {
  min-height: 8.5rem;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 1.1rem;
  border: 1px solid var(--line);
  border-radius: 14px;
  color: inherit;
  background: rgba(255, 255, 255, 0.86);
  text-decoration: none;
}

.entry-card strong {
  color: var(--text-strong);
  font-size: 1.05rem;
}

.entry-card span {
  color: var(--brand-strong);
  font-weight: 700;
}

.entry-card--disabled {
  pointer-events: none;
  opacity: 0.62;
}

@media (max-width: 900px) {
  .entry-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 540px) {
  .entry-grid {
    grid-template-columns: 1fr;
  }
}
</style>
