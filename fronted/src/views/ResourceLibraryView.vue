<script setup>
import { computed } from 'vue'
import { getAllMajors, getSupportModules } from '../data/majors.js'
import AppIcon from '../components/AppIcon.vue'

const majors = computed(() => getAllMajors())
const supportModules = computed(() => getSupportModules())

function supportIconName(id) {
  if (id === 'resource-recommendation') return 'resources'
  if (id === 'ability-practice') return 'ability'
  if (id === 'learning-support') return 'support'
  return 'focus'
}
</script>

<template>
  <div class="page page-shell">
    <section class="section page-section">
      <div class="section__head page-head page-head--with-scene">
        <div class="page-head__main">
          <p class="section__eyebrow"><AppIcon name="resources" :size="16" />博习斋</p>
          <h1 class="section__title"><AppIcon name="resources" :size="22" />博习斋</h1>
          <p class="section__desc">在这里梳理阅读、练习、笔记与学习建议，让研习内容更容易顺着路径展开。</p>
        </div>

        <div class="study-scene" aria-hidden="true">
          <div class="study-scene__sheet">
            <div class="study-scene__note">
              <span><AppIcon name="resources" :size="14" /> 阅读摘记</span>
              <span><AppIcon name="course" :size="14" /> 课程提要</span>
              <span><AppIcon name="focus" :size="14" /> 研习安排</span>
            </div>
            <div class="study-scene__path">
              <div class="study-scene__path-node">
                <AppIcon name="majors" :size="16" />
                <strong>浏览方向</strong>
              </div>
              <div class="study-scene__path-line"></div>
              <div class="study-scene__path-node">
                <AppIcon name="resources" :size="16" />
                <strong>查阅内容</strong>
              </div>
              <div class="study-scene__path-line"></div>
              <div class="study-scene__path-node">
                <AppIcon name="support" :size="16" />
                <strong>继续研习</strong>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="support-grid">
        <article v-for="item in supportModules" :key="item.id" class="support-card">
          <div class="support-card__icon">
            <AppIcon :name="supportIconName(item.id)" :size="18" />
          </div>
          <h3>{{ item.name }}</h3>
          <p>{{ item.tagline }}</p>
          <ul class="support-card__list">
            <li v-for="point in item.points" :key="point">{{ point }}</li>
          </ul>
        </article>
      </div>

      <div class="resource-sections">
        <section v-for="major in majors" :key="major.id" class="resource-group">
          <div class="resource-group__head">
            <h2>{{ major.name }}</h2>
            <RouterLink :to="{ name: 'major', params: { id: major.id } }">查看学科</RouterLink>
          </div>

          <div class="resource-list">
            <article v-for="resource in major.resources" :key="resource.id" class="resource-card">
              <span class="resource-card__type">{{ resource.type }}</span>
              <h3>{{ resource.name }}</h3>
              <p>{{ resource.description }}</p>
            </article>
          </div>
        </section>
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
.support-card h3 {
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

.page-head--with-scene {
  display: grid;
  grid-template-columns: minmax(0, 1.15fr) minmax(280px, 0.85fr);
  gap: 1.2rem;
  align-items: center;
}

.page-head__main {
  min-width: 0;
}

.study-scene {
  position: relative;
  min-height: 204px;
  border-radius: calc(var(--radius-lg) + 0.25rem);
  border: 1px solid rgba(76, 96, 122, 0.12);
  background:
    radial-gradient(circle at top left, rgba(119, 144, 185, 0.1), transparent 42%),
    linear-gradient(180deg, rgba(255, 255, 255, 0.88), rgba(248, 246, 241, 0.96));
  overflow: hidden;
}

.study-scene__sheet {
  display: grid;
  gap: 0.9rem;
  height: 100%;
  padding: 1.2rem;
}

.study-scene__note {
  display: flex;
  flex-wrap: wrap;
  gap: 0.6rem;
}

.study-scene__note span,
.study-scene__path-node {
  display: inline-flex;
  align-items: center;
  gap: 0.45rem;
  padding: 0.5rem 0.68rem;
  border-radius: 999px;
  border: 1px solid rgba(76, 96, 122, 0.12);
  background: rgba(255, 255, 255, 0.72);
  color: var(--text-strong);
}

.study-scene__note span {
  font-size: 0.82rem;
  color: var(--text-main);
}

.study-scene__path {
  display: grid;
  gap: 0.78rem;
  align-content: end;
  flex: 1;
}

.study-scene__path-node {
  width: fit-content;
  max-width: 100%;
}

.study-scene__path-node strong {
  font-size: 0.9rem;
  font-weight: 700;
}

.study-scene__path-line {
  width: 2px;
  height: 0.9rem;
  margin-left: 1.1rem;
  background: linear-gradient(180deg, rgba(82, 102, 136, 0.34), rgba(82, 102, 136, 0.08));
}

.support-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 1rem;
  margin-top: 1.3rem;
}

.support-card,
.resource-card,
.resource-group {
  border-radius: var(--radius-lg);
  border: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.76);
  box-shadow: none;
}

.support-card {
  position: relative;
  padding: 1.2rem;
}

.support-card__icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: var(--icon-size-md);
  height: var(--icon-size-md);
  border-radius: var(--icon-radius);
  background: linear-gradient(180deg, rgba(98, 128, 172, 0.14), rgba(98, 128, 172, 0.06));
  color: var(--brand-strong);
  margin-bottom: 0.8rem;
}

.support-card h3,
.resource-group__head h2,
.resource-card h3 {
  margin: 0;
  color: var(--text-strong);
}

.support-card p,
.resource-card p {
  margin: 0.7rem 0 0;
  line-height: 1.72;
  color: var(--text-main);
}

.support-card__list {
  list-style: none;
  margin: 1rem 0 0;
  padding: 0;
  display: grid;
  gap: 0.6rem;
}

.support-card__list li {
  position: relative;
  padding-left: 1rem;
  line-height: 1.68;
}

.support-card__list li::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0.58rem;
  width: 0.42rem;
  height: 0.42rem;
  border-radius: 50%;
  background: var(--brand);
}

.resource-sections {
  display: grid;
  gap: 1rem;
  margin-top: 1.4rem;
}

.resource-group {
  padding: 1.2rem;
}

.resource-group__head {
  display: flex;
  justify-content: space-between;
  gap: 0.8rem;
  align-items: center;
  margin-bottom: 1rem;
}

.resource-group__head a {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  color: var(--brand-strong);
  text-decoration: none;
  font-weight: 700;
}

.resource-list {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 0.8rem;
}

.resource-card {
  padding: 1rem;
}

.resource-card__type {
  display: inline-flex;
  padding: 0.35rem 0.66rem;
  border-radius: 999px;
  background: var(--brand-soft);
  color: var(--brand-strong);
  font-size: 0.76rem;
  font-weight: 700;
}

.resource-card h3 {
  margin-top: 0.65rem;
  font-size: 1.02rem;
}

@media (max-width: 1100px) {
  .page-head--with-scene {
    grid-template-columns: 1fr;
  }

  .support-grid,
  .resource-list {
    grid-template-columns: 1fr 1fr;
  }
}

@media (max-width: 760px) {
  .study-scene {
    min-height: 0;
  }

  .study-scene__sheet {
    padding: 1rem;
  }

  .support-grid,
  .resource-list {
    grid-template-columns: 1fr;
  }

  .resource-group__head {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
