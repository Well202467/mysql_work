<script setup>
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { adminLogin } from '../../api/admin.js'

const router = useRouter()
const route = useRoute()

const form = reactive({
  username: '',
  password: '',
})

const errorMessage = ref('')
const loading = ref(false)

async function handleLogin() {
  errorMessage.value = ''
  loading.value = true
  try {
    const res = await adminLogin({
      username: form.username.trim(),
      password: form.password,
    })
    const token = res?.data?.token
    if (res?.code === 200 && token) {
      localStorage.setItem('adminToken', token)
      await router.push(String(route.query.redirect || '/admin/dashboard'))
      return
    }
    errorMessage.value = res?.message || '登录失败'
  } catch (error) {
    errorMessage.value = error?.response?.data?.message || '用户名或密码错误'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <main class="admin-login">
    <section class="login-panel">
      <div class="login-panel__head">
        <p>管理员入口</p>
        <h1>管理员登录</h1>
      </div>

      <form class="login-form" @submit.prevent="handleLogin">
        <label class="field">
          <span>用户名</span>
          <input v-model="form.username" type="text" autocomplete="username" placeholder="请输入用户名" />
        </label>

        <label class="field">
          <span>密码</span>
          <input v-model="form.password" type="password" autocomplete="current-password" placeholder="请输入密码" />
        </label>

        <p v-if="errorMessage" class="form-error">{{ errorMessage }}</p>

        <button class="login-button" type="submit" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>
    </section>
  </main>
</template>

<style scoped>
.admin-login {
  min-height: 100vh;
  display: grid;
  place-items: center;
  padding: 1.2rem;
  background: linear-gradient(180deg, #f7f8f5 0%, #eef2f0 100%);
}

.login-panel {
  width: min(100%, 420px);
  padding: 2rem;
  border: 1px solid var(--line);
  border-radius: var(--radius-xl);
  background: rgba(255, 255, 255, 0.92);
}

.login-panel__head {
  display: grid;
  gap: 0.45rem;
  margin-bottom: 1.4rem;
}

.login-panel__head p,
.login-panel__head h1 {
  margin: 0;
}

.login-panel__head p {
  color: var(--brand-strong);
  font-size: 0.82rem;
  font-weight: 700;
}

.login-panel__head h1 {
  color: var(--text-strong);
  font-size: 1.65rem;
}

.login-form {
  display: grid;
  gap: 1rem;
}

.field {
  display: grid;
  gap: 0.45rem;
  color: var(--text-main);
  font-weight: 700;
}

.field input {
  width: 100%;
  height: 2.75rem;
  padding: 0 0.9rem;
  border: 1px solid var(--line-strong);
  border-radius: 10px;
  color: var(--text-strong);
  background: #fff;
  outline: none;
}

.field input:focus {
  border-color: rgba(66, 103, 154, 0.48);
}

.form-error {
  margin: 0;
  color: #b42318;
  font-size: 0.9rem;
}

.login-button {
  height: 2.8rem;
  border: 0;
  border-radius: 10px;
  background: var(--brand);
  color: #fff;
  font-weight: 800;
}

.login-button:disabled {
  cursor: not-allowed;
  opacity: 0.68;
}
</style>
