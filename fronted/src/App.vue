<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import AppIcon from './components/AppIcon.vue'

const route = useRoute()

const navItems = [
  { name: 'home', label: '知点序章', icon: 'home' },
  { name: 'majors', label: '学科览要', icon: 'majors' },
  { name: 'careers', label: '职业方向', icon: 'job' },
]

const currentNavName = computed(() => route.meta?.navName ?? route.name)
const currentLabel = computed(() => route.meta?.title ?? '知点智学')
const currentIcon = computed(() => navItems.find((item) => item.name === currentNavName.value)?.icon ?? 'major')
const isAdminPage = computed(() => Boolean(route.meta?.adminPage))
</script>

<template>
  <div class="layout">
    <header v-if="!isAdminPage" class="topbar">
      <div class="topbar__inner">
        <RouterLink class="brand" :to="{ name: 'home' }">
          <span class="brand__mark" aria-hidden="true">
            <span>知点</span>
            <span>智学</span>
          </span>
          <span class="brand__meta">
            <span class="brand__eyebrow">知点智学</span>
            <span class="brand__sub">基于专业能力图谱的学业认知与发展参考平台</span>
          </span>
        </RouterLink>

        <nav class="topbar__nav" aria-label="主导航">
          <RouterLink
            v-for="item in navItems"
            :key="item.name"
            class="topbar__link"
            :class="{ 'topbar__link--active': currentNavName === item.name }"
            :to="{ name: item.name }"
          >
            <AppIcon :name="item.icon" :size="16" />
            {{ item.label }}
          </RouterLink>
        </nav>

        <span class="topbar__chip">
          <AppIcon :name="currentIcon" :size="16" />
          {{ currentLabel }}
        </span>
      </div>
    </header>

    <main class="main" :class="{ 'main--admin': isAdminPage }">
      <RouterView />
    </main>
  </div>
</template>

<style scoped>
.layout {
  min-height: 100vh;
}

.topbar {
  position: sticky;
  top: 0;
  z-index: 20;
  padding: 1rem 1rem 0.15rem;
}

.topbar__inner {
  max-width: var(--max-width);
  margin: 0 auto;
  padding: 0.9rem 1.1rem;
  display: grid;
  grid-template-columns: auto 1fr auto;
  align-items: center;
  gap: 1.2rem;
  border: 1px solid var(--line);
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.9);
  box-shadow: none;
}

.brand {
  display: flex;
  align-items: center;
  gap: 0.9rem;
  min-width: 0;
  text-decoration: none;
}

.brand__mark {
  display: inline-grid;
  align-items: center;
  justify-content: center;
  grid-auto-rows: min-content;
  gap: 0.08rem;
  width: 3rem;
  height: 3rem;
  border-radius: 16px;
  background: linear-gradient(180deg, #51739f 0%, #3f5d84 100%);
  color: #fff;
  font-weight: 800;
  font-size: 0.96rem;
  line-height: 1;
  flex: none;
  text-align: center;
}

.brand__mark span {
  display: block;
}

.brand__meta {
  display: flex;
  flex-direction: column;
  gap: 0.2rem;
  min-width: 0;
}

.brand__eyebrow {
  font-size: 0.98rem;
  letter-spacing: 0.04em;
  color: var(--brand-strong);
  font-weight: 700;
}

.brand__sub {
  font-size: 0.84rem;
  color: var(--text-muted);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.topbar__nav {
  display: flex;
  justify-content: center;
  gap: 0.95rem;
  flex-wrap: wrap;
}

.topbar__link {
  position: relative;
  display: inline-flex;
  align-items: center;
  gap: 0.42rem;
  padding: 0.35rem 0;
  color: var(--text-main);
  text-decoration: none;
  font-weight: 600;
  border-bottom: 1px solid transparent;
}

.topbar__link :deep(.app-icon),
.topbar__chip :deep(.app-icon) {
  opacity: 0.88;
}

.topbar__link--active {
  color: var(--brand-strong);
  border-bottom-color: rgba(66, 103, 154, 0.36);
}

.topbar__chip {
  display: inline-flex;
  align-items: center;
  gap: 0.42rem;
  padding: 0.35rem 0;
  color: var(--text-muted);
  font-size: 0.86rem;
  border-bottom: 1px solid var(--line);
}

.main {
  padding: 1.15rem 1rem 2.8rem;
}

.main--admin {
  padding: 0;
}

@media (max-width: 1040px) {
  .topbar__inner {
    grid-template-columns: 1fr;
    border-radius: 22px;
  }

  .topbar__nav {
    justify-content: flex-start;
  }

  .topbar__chip {
    justify-self: start;
  }
}

@media (max-width: 560px) {
  .topbar {
    padding-top: 0.8rem;
  }

  .topbar__inner {
    padding: 0.9rem;
  }

  .brand {
    align-items: flex-start;
  }

  .brand__sub {
    white-space: normal;
  }
}
</style>
