<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import {
  addMajorCourse,
  deleteMajorCourse,
  getCourseOptions,
  getMajorCourses,
  getMajorOptions,
  updateMajorCourse,
} from '../../api/admin.js'

const majorCourses = ref([])
const majorOptions = ref([])
const courseOptions = ref([])
const loading = ref(false)
const saving = ref(false)
const errorMessage = ref('')
const successMessage = ref('')
const showForm = ref(false)
const editingId = ref(null)

const form = reactive({
  majorId: '',
  courseId: '',
  type: '',
  credits: '',
})

const formTitle = computed(() => (editingId.value ? '编辑专业课程关系' : '新增专业课程关系'))

onMounted(loadPageData)

async function loadPageData() {
  loading.value = true
  errorMessage.value = ''
  try {
    const [relationRes, majorRes, courseRes] = await Promise.all([
      getMajorCourses(),
      getMajorOptions(),
      getCourseOptions(),
    ])

    if (!isBusinessSuccess(relationRes)) {
      majorCourses.value = []
      errorMessage.value = getBusinessMessage(relationRes)
      return
    }
    if (!isBusinessSuccess(majorRes)) {
      majorOptions.value = []
      errorMessage.value = getBusinessMessage(majorRes)
      return
    }
    if (!isBusinessSuccess(courseRes)) {
      courseOptions.value = []
      errorMessage.value = getBusinessMessage(courseRes)
      return
    }

    majorCourses.value = Array.isArray(relationRes.data) ? relationRes.data : []
    majorOptions.value = Array.isArray(majorRes.data) ? majorRes.data : []
    courseOptions.value = Array.isArray(courseRes.data) ? courseRes.data : []
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '专业课程关系列表加载失败')
  } finally {
    loading.value = false
  }
}

function openCreateForm() {
  editingId.value = null
  Object.assign(form, {
    majorId: '',
    courseId: '',
    type: '',
    credits: '',
  })
  showForm.value = true
}

function openEditForm(item) {
  editingId.value = item.id
  Object.assign(form, {
    majorId: item.majorId ?? '',
    courseId: item.courseId ?? '',
    type: item.type ?? '',
    credits: item.credits ?? '',
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

function buildPayload() {
  return {
    majorId: form.majorId,
    courseId: form.courseId,
    type: form.type.trim(),
    credits: form.credits === '' ? null : Number(form.credits),
  }
}

async function saveMajorCourse() {
  saving.value = true
  errorMessage.value = ''
  successMessage.value = ''

  try {
    const payload = buildPayload()
    const res = editingId.value
      ? await updateMajorCourse(editingId.value, payload)
      : await addMajorCourse(payload)

    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = editingId.value ? '专业课程关系已修改' : '专业课程关系已新增'
    showForm.value = false
    await loadPageData()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '保存失败')
  } finally {
    saving.value = false
  }
}

async function handleDelete(item) {
  if (!window.confirm(`确认删除「${item.majorName || item.majorId} - ${item.courseName || item.courseId}」这条关系吗？`)) {
    return
  }

  errorMessage.value = ''
  successMessage.value = ''
  try {
    const res = await deleteMajorCourse(item.id)
    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = '专业课程关系已删除'
    await loadPageData()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '删除失败')
  }
}
</script>

<template>
  <section class="major-course-manage">
    <div class="manage-head">
      <div>
        <h2>专业课程关系管理</h2>
        <p>这里维护 major_course 表中的专业、课程、类型和学分关系。</p>
      </div>
      <button type="button" class="primary-button" @click="openCreateForm">新增关系</button>
    </div>

    <p v-if="errorMessage" class="message message--error">{{ errorMessage }}</p>
    <p v-if="successMessage" class="message message--success">{{ successMessage }}</p>

    <div class="table-panel">
      <div v-if="loading" class="empty-state">正在加载专业课程关系...</div>
      <table v-else-if="majorCourses.length" class="relation-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>专业</th>
            <th>课程</th>
            <th>类型</th>
            <th>学分</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in majorCourses" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.majorName || item.majorId }}</td>
            <td>{{ item.courseName || item.courseId }}</td>
            <td>{{ item.type || '未填写' }}</td>
            <td>{{ item.credits ?? '-' }}</td>
            <td>
              <div class="row-actions">
                <button type="button" @click="openEditForm(item)">编辑</button>
                <button type="button" class="danger-button" @click="handleDelete(item)">删除</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="empty-state">暂无专业课程关系数据</div>
    </div>

    <div v-if="showForm" class="modal-mask" role="presentation" @click.self="closeForm">
      <section class="relation-modal" role="dialog" aria-modal="true" :aria-label="formTitle">
        <header class="relation-modal__head">
          <h3>{{ formTitle }}</h3>
          <button type="button" class="ghost-button" @click="closeForm">关闭</button>
        </header>

        <form class="relation-form" @submit.prevent="saveMajorCourse">
          <label class="field">
            <span>专业</span>
            <select v-model="form.majorId" required>
              <option value="" disabled>请选择专业</option>
              <option v-for="item in majorOptions" :key="item.value" :value="item.value">
                {{ item.label }}
              </option>
            </select>
          </label>

          <label class="field">
            <span>课程</span>
            <select v-model="form.courseId" required>
              <option value="" disabled>请选择课程</option>
              <option v-for="item in courseOptions" :key="item.value" :value="item.value">
                {{ item.label }}
              </option>
            </select>
          </label>

          <label class="field">
            <span>类型</span>
            <input v-model="form.type" type="text" placeholder="如 专业必修 / 专业选修" />
          </label>

          <label class="field">
            <span>学分</span>
            <input v-model="form.credits" type="number" min="0" step="0.5" placeholder="如 3.5" />
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
.major-course-manage {
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

.relation-table {
  width: 100%;
  min-width: 820px;
  border-collapse: collapse;
}

.relation-table th,
.relation-table td {
  padding: 0.9rem;
  border-bottom: 1px solid var(--line);
  text-align: left;
  vertical-align: top;
}

.relation-table th {
  color: var(--text-strong);
  background: rgba(66, 103, 154, 0.06);
  font-size: 0.9rem;
}

.relation-table td {
  color: var(--text-main);
  line-height: 1.55;
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

.relation-modal {
  width: min(100%, 720px);
  border: 1px solid var(--line);
  border-radius: 16px;
  background: #fff;
}

.relation-modal__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 1rem 1.1rem;
  border-bottom: 1px solid var(--line);
}

.relation-modal__head h3 {
  margin: 0;
  color: var(--text-strong);
}

.relation-form {
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

.field input,
.field select {
  width: 100%;
  height: 2.55rem;
  padding: 0 0.8rem;
  border: 1px solid var(--line-strong);
  border-radius: 10px;
  color: var(--text-strong);
  background: #fff;
  outline: none;
}

.field input:focus,
.field select:focus {
  border-color: rgba(66, 103, 154, 0.48);
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

  .relation-form {
    grid-template-columns: 1fr;
  }
}
</style>
