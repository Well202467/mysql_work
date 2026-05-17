<script setup>
import { useRouter } from 'vue-router'
import AppIcon from '../../components/AppIcon.vue'

const router = useRouter()

const navItems = [
  { name: 'admin-dashboard', label: '后台首页', icon: 'home' },
  { name: 'admin-majors', label: '专业管理', icon: 'major' },
  { name: 'admin-courses', label: '课程管理', icon: 'course' },
  { name: 'admin-major-courses', label: '专业课程关系', icon: 'map' },
  { name: 'admin-knowledge-points', label: '知识点管理', icon: 'ability' },
  { name: 'admin-careers', label: '职业管理', icon: 'job' },
  { name: 'admin-career-profiles', label: '职业画像管理', icon: 'intro' },
  { name: 'admin-career-abilities', label: '职业能力管理', icon: 'focus' },
  { name: 'admin-career-courses', label: '职业课程关系', icon: 'course' },
  { name: 'admin-career-knowledge-points', label: '职业知识点关系', icon: 'ability' },
]

function logout() {
  localStorage.removeItem('adminToken')
  router.push({ name: 'admin-login' })
}
</script>

<template>
  <div class="admin-layout">
    <aside class="admin-sidebar">
      <RouterLink class="admin-brand" :to="{ name: 'admin-dashboard' }">
        <span class="admin-brand__mark">管</span>
        <span>
          <strong>管理员后台</strong>
          <small>Smart Learning</small>
        </span>
      </RouterLink>

      <nav class="admin-nav" aria-label="后台导航">
        <RouterLink
          v-for="item in navItems"
          :key="item.name"
          class="admin-nav__link"
          :to="{ name: item.name }"
        >
          <AppIcon :name="item.icon" :size="17" />
          {{ item.label }}
        </RouterLink>
      </nav>
    </aside>

    <section class="admin-main">
      <header class="admin-topbar">
        <div>
          <p>管理控制台</p>
          <h1>管理员后台</h1>
        </div>
        <button type="button" class="logout-button" @click="logout">退出登录</button>
      </header>

      <RouterView />
    </section>
  </div>
</template>

<style scoped>
.admin-layout {
  min-height: 100vh;
  display: grid;
  grid-template-columns: 240px minmax(0, 1fr);
  background: #f4f5f1;
}

.admin-sidebar {
  padding: 1.2rem;
  border-right: 1px solid var(--line);
  background: rgba(255, 255, 255, 0.82);
}

.admin-brand {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  color: inherit;
  text-decoration: none;
}

.admin-brand__mark {
  display: inline-grid;
  place-items: center;
  width: 2.7rem;
  height: 2.7rem;
  border-radius: 12px;
  background: var(--brand);
  color: #fff;
  font-weight: 900;
}

.admin-brand strong,
.admin-brand small {
  display: block;
}

.admin-brand strong {
  color: var(--text-strong);
}

.admin-brand small {
  margin-top: 0.15rem;
  color: var(--text-muted);
}

.admin-nav {
  display: grid;
  gap: 0.5rem;
  margin-top: 1.6rem;
}

.admin-nav__link {
  display: flex;
  align-items: center;
  gap: 0.55rem;
  padding: 0.75rem 0.85rem;
  border-radius: 10px;
  color: var(--text-main);
  text-decoration: none;
  font-weight: 700;
}

.admin-nav__link.router-link-active {
  color: var(--brand-strong);
  background: var(--brand-soft);
}

.admin-main {
  min-width: 0;
  padding: 1.2rem;
}

.admin-topbar {
  min-height: 4.4rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 0.9rem 1.1rem;
  border: 1px solid var(--line);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.88);
}

.admin-topbar p,
.admin-topbar h1 {
  margin: 0;
}

.admin-topbar p {
  color: var(--text-muted);
  font-size: 0.82rem;
}

.admin-topbar h1 {
  margin-top: 0.16rem;
  color: var(--text-strong);
  font-size: 1.25rem;
}

.logout-button {
  height: 2.45rem;
  padding: 0 0.95rem;
  border: 1px solid var(--line-strong);
  border-radius: 10px;
  color: var(--text-strong);
  background: #fff;
  font-weight: 700;
}

@media (max-width: 760px) {
  .admin-layout {
    grid-template-columns: 1fr;
  }

  .admin-sidebar {
    border-right: 0;
    border-bottom: 1px solid var(--line);
  }

  .admin-nav {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>
