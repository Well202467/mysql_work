<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { addCourse, deleteCourse, getCourses, updateCourse } from '../../api/admin.js'

const courses = ref([])
const loading = ref(false)
const saving = ref(false)
const errorMessage = ref('')
const successMessage = ref('')
const showForm = ref(false)
const editingId = ref('')

const form = reactive({
  id: '',
  name: '',
  description: '',
})

const formTitle = computed(() => (editingId.value ? '编辑课程' : '新增课程'))

onMounted(loadCourses)

async function loadCourses() {
  loading.value = true
  errorMessage.value = ''
  try {
    const res = await getCourses()
    if (!isBusinessSuccess(res)) {
      courses.value = []
      errorMessage.value = getBusinessMessage(res)
      return
    }
    courses.value = Array.isArray(res.data) ? res.data : []
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '课程列表加载失败')
  } finally {
    loading.value = false
  }
}

function openCreateForm() {
  editingId.value = ''
  Object.assign(form, {
    id: '',
    name: '',
    description: '',
  })
  showForm.value = true
}

function openEditForm(course) {
  editingId.value = course.id
  Object.assign(form, {
    id: course.id ?? '',
    name: course.name ?? '',
    description: course.description ?? '',
  })
  showForm.value = true
}

function closeForm() {
  showForm.value = false
}

function isBusinessSuccess(res) {
  return res?.code === 200
}

function getBusinessMessage(res) {
  return res?.message || '操作失败'
}

function getErrorMessage(error, fallback = '操作失败') {
  return error?.response?.data?.message || fallback
}

async function saveCourse() {
  saving.value = true
  errorMessage.value = ''
  successMessage.value = ''
  const payload = {
    id: form.id.trim(),
    name: form.name.trim(),
    description: form.description.trim(),
  }

  try {
    const res = editingId.value ? await updateCourse(editingId.value, payload) : await addCourse(payload)
    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = editingId.value ? '课程已修改' : '课程已新增'
    showForm.value = false
    await loadCourses()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '保存失败')
  } finally {
    saving.value = false
  }
}

async function handleDelete(course) {
  if (!window.confirm(`确认删除课程「${course.name || course.id}」吗？`)) return
  errorMessage.value = ''
  successMessage.value = ''
  try {
    const res = await deleteCourse(course.id)
    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = '课程已删除'
    await loadCourses()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '删除失败')
  }
}
</script>

<template>
  <section class="course-manage">
    <div class="manage-head">
      <div>
        <h2>课程管理</h2>
        <p>这里只维护 course 表中的课程基础信息，课程类型和学分请到专业课程关系管理中维护。</p>
      </div>
      <button type="button" class="primary-button" @click="openCreateForm">新增课程</button>
    </div>

    <p v-if="errorMessage" class="message message--error">{{ errorMessage }}</p>
    <p v-if="successMessage" class="message message--success">{{ successMessage }}</p>

    <div class="table-panel">
      <div v-if="loading" class="empty-state">正在加载课程...</div>
      <table v-else-if="courses.length" class="course-table">
        <thead>
          <tr>
            <th>课程 ID</th>
            <th>课程名称</th>
            <th>简介</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="course in courses" :key="course.id">
            <td>{{ course.id }}</td>
            <td>{{ course.name }}</td>
            <td class="course-desc">{{ course.description || '暂无简介' }}</td>
            <td>
              <div class="row-actions">
                <button type="button" @click="openEditForm(course)">编辑</button>
                <button type="button" class="danger-button" @click="handleDelete(course)">删除</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="empty-state">暂无课程数据</div>
    </div>

    <div v-if="showForm" class="modal-mask" role="presentation" @click.self="closeForm">
      <section class="course-modal" role="dialog" aria-modal="true" :aria-label="formTitle">
        <header class="course-modal__head">
          <h3>{{ formTitle }}</h3>
          <button type="button" class="ghost-button" @click="closeForm">关闭</button>
        </header>

        <form class="course-form" @submit.prevent="saveCourse">
          <label class="field">
            <span>课程 ID</span>
            <input v-model="form.id" type="text" :readonly="Boolean(editingId)" required />
          </label>
          <label class="field">
            <span>课程名称</span>
            <input v-model="form.name" type="text" required />
          </label>
          <label class="field field--wide">
            <span>简介</span>
            <textarea v-model="form.description" rows="4"></textarea>
          </label>

          <div class="form-actions">
            <button type="button" class="ghost-button" @click="closeForm">取消</button>
            <button type="submit" class="primary-button" :disabled="saving">
              {{ saving ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </section>
    </div>
  </section>
</template>

<style scoped>
.course-manage {
  display: grid;
  gap: 1rem;
  margin-top: 1rem;
}

.manage-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 1.15rem;
  border: 1px solid var(--line);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.86);
}

.manage-head h2,
.manage-head p {
  margin: 0;
}

.manage-head h2 {
  color: var(--text-strong);
  font-size: 1.28rem;
}

.manage-head p {
  margin-top: 0.42rem;
  color: var(--text-muted);
  line-height: 1.65;
}

.primary-button,
.ghost-button,
.row-actions button {
  height: 2.35rem;
  padding: 0 0.85rem;
  border-radius: 10px;
  font-weight: 700;
}

.primary-button {
  border: 0;
  color: #fff;
  background: var(--brand);
}

.primary-button:disabled {
  cursor: not-allowed;
  opacity: 0.68;
}

.ghost-button,
.row-actions button {
  border: 1px solid var(--line-strong);
  color: var(--text-strong);
  background: #fff;
}

.danger-button {
  color: #b42318 !important;
}

.message {
  margin: 0;
  padding: 0.8rem 1rem;
  border-radius: 12px;
  font-weight: 700;
}

.message--error {
  color: #b42318;
  background: rgba(180, 35, 24, 0.08);
}

.message--success {
  color: var(--success);
  background: var(--success-soft);
}

.table-panel {
  overflow: auto;
  border: 1px solid var(--line);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.9);
}

.course-table {
  width: 100%;
  min-width: 720px;
  border-collapse: collapse;
}

.course-table th,
.course-table td {
  padding: 0.9rem;
  border-bottom: 1px solid var(--line);
  text-align: left;
  vertical-align: top;
}

.course-table th {
  color: var(--text-strong);
  background: rgba(66, 103, 154, 0.06);
  font-size: 0.9rem;
}

.course-table td {
  color: var(--text-main);
  line-height: 1.55;
}

.course-desc {
  max-width: 34rem;
}

.row-actions {
  display: flex;
  gap: 0.5rem;
}

.empty-state {
  padding: 2rem;
  color: var(--text-muted);
  text-align: center;
}

.modal-mask {
  position: fixed;
  inset: 0;
  z-index: 40;
  display: grid;
  place-items: center;
  padding: 1rem;
  background: rgba(20, 38, 61, 0.28);
}

.course-modal {
  width: min(100%, 680px);
  border: 1px solid var(--line);
  border-radius: 16px;
  background: #fff;
}

.course-modal__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 1rem 1.1rem;
  border-bottom: 1px solid var(--line);
}

.course-modal__head h3 {
  margin: 0;
  color: var(--text-strong);
}

.course-form {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 1rem;
  padding: 1.1rem;
}

.field {
  display: grid;
  gap: 0.42rem;
  color: var(--text-main);
  font-weight: 700;
}

.field--wide {
  grid-column: 1 / -1;
}

.field input,
.field textarea {
  width: 100%;
  border: 1px solid var(--line-strong);
  border-radius: 10px;
  color: var(--text-strong);
  background: #fff;
  outline: none;
}

.field input {
  height: 2.55rem;
  padding: 0 0.8rem;
}

.field textarea {
  resize: vertical;
  padding: 0.75rem 0.8rem;
}

.field input:focus,
.field textarea:focus {
  border-color: rgba(66, 103, 154, 0.48);
}

.field input[readonly] {
  color: var(--text-muted);
  background: #f8f8f6;
}

.form-actions {
  grid-column: 1 / -1;
  display: flex;
  justify-content: flex-end;
  gap: 0.7rem;
}

@media (max-width: 720px) {
  .manage-head {
    align-items: stretch;
    flex-direction: column;
  }

  .course-form {
    grid-template-columns: 1fr;
  }
}
</style>
