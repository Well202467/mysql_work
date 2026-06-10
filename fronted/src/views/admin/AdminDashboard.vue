<script setup>
import { computed, onMounted, ref } from 'vue'
import { getTodayVisitStats, getVisitStatsHistory } from '../../api/admin.js'

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

const visitStats = ref({
  todayUv: 0,
  todayPv: 0,
  todaySessions: 0,
  todayDate: '',
  popularPages: [],
})
const historyStats = ref([])

const availableEntries = computed(() => entries.filter((item) => item.available))
const disabledEntries = computed(() => entries.filter((item) => !item.available))
const popularPages = computed(() =>
  Array.isArray(visitStats.value.popularPages) ? visitStats.value.popularPages : [],
)

onMounted(() => {
  loadVisitStats()
  loadHistoryStats()
})

async function loadVisitStats() {
  try {
    const response = await getTodayVisitStats()
    visitStats.value = normalizeStats(response)
  } catch (error) {
    visitStats.value = normalizeStats()
  }
}

function normalizeStats(response) {
  const data = response?.code === 200 ? response.data : response?.data || {}
  return {
    todayUv: Number(data?.todayUv || 0),
    todayPv: Number(data?.todayPv || 0),
    todaySessions: Number(data?.todaySessions || 0),
    todayDate: data?.todayDate || '',
    popularPages: Array.isArray(data?.popularPages) ? data.popularPages : [],
  }
}

async function loadHistoryStats() {
  try {
    const response = await getVisitStatsHistory()
    historyStats.value = normalizeHistoryStats(response)
  } catch (error) {
    historyStats.value = []
  }
}

function normalizeHistoryStats(response) {
  const data = response?.code === 200 ? response.data : response?.data || []
  if (!Array.isArray(data)) {
    return []
  }
  return data.map((item) => ({
    date: item?.date || '',
    uv: Number(item?.uv || 0),
    pv: Number(item?.pv || 0),
    sessions: Number(item?.sessions || 0),
  }))
}

</script>

<template>
  <section class="dashboard">
    <div class="dashboard__head">
      <h2>管理员首页</h2>
      <p>查看今日访问统计，并选择需要维护的数据模块。</p>
    </div>

    <div class="stats-grid">
      <article class="stat-card">
        <span>今日独立访客 UV</span>
        <strong>{{ visitStats.todayUv }}</strong>
        <small>{{ visitStats.todayDate || '今日' }}</small>
      </article>

      <article class="stat-card">
        <span>今日页面浏览量 PV</span>
        <strong>{{ visitStats.todayPv }}</strong>
        <small>{{ visitStats.todayDate || '今日' }}</small>
      </article>

      <article class="stat-card">
        <span>今日访问会话数 Session</span>
        <strong>{{ visitStats.todaySessions }}</strong>
        <small>{{ visitStats.todayDate || '今日' }}</small>
      </article>

      <article class="popular-card">
        <div class="popular-card__head">
          <strong>今日热门页面 Top 5</strong>
          <span>{{ visitStats.todayDate || '今日' }}</span>
        </div>
        <ol v-if="popularPages.length" class="popular-list">
          <li v-for="page in popularPages" :key="page.path">
            <span>{{ page.path }}</span>
            <strong>{{ page.count }}</strong>
          </li>
        </ol>
        <p v-else class="empty-text">暂无访问记录</p>
      </article>
    </div>

    <section class="history-panel">
      <div class="history-panel__head">
        <strong>历史访问统计（从开始记录至今）</strong>
        <span>按有访问记录的日期展示</span>
      </div>
      <div class="history-table-wrap">
        <table v-if="historyStats.length" class="history-table">
          <thead>
            <tr>
              <th>日期</th>
              <th>UV</th>
              <th>PV</th>
              <th>Session</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in historyStats" :key="item.date">
              <td>{{ item.date }}</td>
              <td>{{ item.uv }}</td>
              <td>{{ item.pv }}</td>
              <td>{{ item.sessions }}</td>
            </tr>
          </tbody>
        </table>
        <p v-else class="empty-text">暂无历史访问记录</p>
      </div>
    </section>

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

.stats-grid {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 1rem;
}

.stat-card,
.popular-card {
  border: 1px solid var(--line);
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.88);
}

.stat-card {
  min-height: 8.5rem;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 1.1rem;
}

.stat-card span,
.popular-card__head span,
.empty-text {
  color: var(--text-muted);
}

.stat-card strong {
  color: var(--text-strong);
  font-size: 2rem;
  line-height: 1;
}

.stat-card small {
  color: var(--brand-strong);
  font-weight: 700;
}

.popular-card {
  grid-column: span 2;
  padding: 1.1rem;
}

.popular-card__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
}

.popular-card__head strong {
  color: var(--text-strong);
  font-size: 1.05rem;
}

.popular-list {
  display: grid;
  gap: 0.55rem;
  margin: 0.85rem 0 0;
  padding: 0;
  list-style: none;
}

.popular-list li {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 0.58rem 0.7rem;
  border-radius: 10px;
  background: var(--brand-soft);
}

.popular-list li span {
  min-width: 0;
  overflow: hidden;
  color: var(--text-main);
  font-weight: 700;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.popular-list li strong {
  color: var(--brand-strong);
}

.empty-text {
  margin: 1.1rem 0 0;
}

.history-panel {
  padding: 1.1rem;
  border: 1px solid var(--line);
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.88);
}

.history-panel__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  margin-bottom: 0.9rem;
}

.history-panel__head strong {
  color: var(--text-strong);
  font-size: 1.05rem;
}

.history-panel__head span {
  color: var(--text-muted);
}

.history-table-wrap {
  overflow-x: auto;
}

.history-table {
  width: 100%;
  border-collapse: collapse;
  min-width: 520px;
}

.history-table th,
.history-table td {
  padding: 0.72rem 0.8rem;
  border-bottom: 1px solid var(--line);
  text-align: left;
}

.history-table th {
  color: var(--text-muted);
  font-size: 0.86rem;
  font-weight: 700;
}

.history-table td {
  color: var(--text-main);
  font-weight: 700;
}

.history-table tbody tr:last-child td {
  border-bottom: 0;
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
  .stats-grid,
  .entry-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 540px) {
  .stats-grid,
  .entry-grid {
    grid-template-columns: 1fr;
  }

  .popular-card {
    grid-column: span 1;
  }
}
</style>
