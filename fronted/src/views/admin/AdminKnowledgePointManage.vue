<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import {
  addKnowledgePoint,
  deleteKnowledgePoint,
  getCourseOptions,
  getKnowledgePoints,
  updateKnowledgePoint,
} from '../../api/admin.js'

const knowledgePoints = ref([])
const courseOptions = ref([])
const loading = ref(false)
const saving = ref(false)
const errorMessage = ref('')
const successMessage = ref('')
const showForm = ref(false)
const editingId = ref(null)

const form = reactive({
  id: '',
  courseId: '',
  name: '',
  description: '',
  difficulty: '',
  ability: '',
  source: '',
})

const formTitle = computed(() => (editingId.value ? '编辑知识点' : '新增知识点'))

onMounted(loadPageData)

async function loadPageData() {
  loading.value = true
  errorMessage.value = ''
  try {
    const [pointRes, courseRes] = await Promise.all([getKnowledgePoints(), getCourseOptions()])

    if (!isBusinessSuccess(pointRes)) {
      knowledgePoints.value = []
      errorMessage.value = getBusinessMessage(pointRes)
      return
    }
    if (!isBusinessSuccess(courseRes)) {
      courseOptions.value = []
      errorMessage.value = getBusinessMessage(courseRes)
      return
    }

    knowledgePoints.value = Array.isArray(pointRes.data) ? pointRes.data : []
    courseOptions.value = Array.isArray(courseRes.data) ? courseRes.data : []
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '知识点列表加载失败')
  } finally {
    loading.value = false
  }
}

function openCreateForm() {
  editingId.value = null
  Object.assign(form, {
    id: '',
    courseId: '',
    name: '',
    description: '',
    difficulty: '',
    ability: '',
    source: '',
  })
  showForm.value = true
}

function openEditForm(item) {
  editingId.value = item.id
  Object.assign(form, {
    id: item.id ?? '',
    courseId: item.courseId ?? '',
    name: item.name ?? '',
    description: item.description ?? '',
    difficulty: item.difficulty ?? '',
    ability: item.ability ?? '',
    source: item.source ?? '',
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
    id: editingId.value || null,
    courseId: form.courseId,
    name: form.name.trim(),
    description: form.description.trim(),
    difficulty: form.difficulty.trim(),
    ability: form.ability.trim(),
    source: form.source.trim(),
  }
}

async function saveKnowledgePoint() {
  saving.value = true
  errorMessage.value = ''
  successMessage.value = ''

  try {
    const payload = buildPayload()
    const res = editingId.value
      ? await updateKnowledgePoint(editingId.value, payload)
      : await addKnowledgePoint(payload)

    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = editingId.value ? '知识点已修改' : '知识点已新增'
    showForm.value = false
    await loadPageData()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '保存失败')
  } finally {
    saving.value = false
  }
}

async function handleDelete(item) {
  if (!window.confirm(`确认删除知识点「${item.name || item.id}」吗？`)) return

  errorMessage.value = ''
  successMessage.value = ''
  try {
    const res = await deleteKnowledgePoint(item.id)
    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = '知识点已删除'
    await loadPageData()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '删除失败')
  }
}
</script>

<template>
  <section class="knowledge-point-manage">
    <div class="manage-head">
      <div>
        <h2>知识点管理</h2>
        <p>维护 knowledge_point 表中的课程知识点、难度、能力和来源信息。</p>
      </div>
      <button type="button" class="primary-button" @click="openCreateForm">新增知识点</button>
    </div>

    <p v-if="errorMessage" class="message message--error">{{ errorMessage }}</p>
    <p v-if="successMessage" class="message message--success">{{ successMessage }}</p>

    <div class="table-panel">
      <div v-if="loading" class="empty-state">正在加载知识点...</div>
      <table v-else-if="knowledgePoints.length" class="point-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>课程</th>
            <th>知识点</th>
            <th>难度</th>
            <th>能力</th>
            <th>来源</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in knowledgePoints" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ item.courseName || item.courseId }}</td>
            <td>{{ item.name }}</td>
            <td>{{ item.difficulty || '未填写' }}</td>
            <td>{{ item.ability || '未填写' }}</td>
            <td>{{ item.source || '未填写' }}</td>
            <td>
              <div class="row-actions">
                <button type="button" @click="openEditForm(item)">编辑</button>
                <button type="button" class="danger-button" @click="handleDelete(item)">删除</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="empty-state">暂无知识点数据</div>
    </div>

    <div v-if="showForm" class="modal-mask" role="presentation" @click.self="closeForm">
      <section class="point-modal" role="dialog" aria-modal="true" :aria-label="formTitle">
        <header class="point-modal__head">
          <h3>{{ formTitle }}</h3>
          <button type="button" class="ghost-button" @click="closeForm">关闭</button>
        </header>

        <form class="point-form" @submit.prevent="saveKnowledgePoint">
          <label class="field">
            <span>ID</span>
            <input v-model="form.id" type="text" placeholder="保存后自动生成" readonly />
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
            <span>知识点名称</span>
            <input v-model="form.name" type="text" required />
          </label>

          <label class="field">
            <span>难度</span>
            <input v-model="form.difficulty" type="text" placeholder="如 基础 / 中等 / 进阶" />
          </label>

          <label class="field">
            <span>对应能力</span>
            <input v-model="form.ability" type="text" />
          </label>

          <label class="field">
            <span>来源</span>
            <input v-model="form.source" type="text" />
          </label>

          <label class="field field--wide">
            <span>说明</span>
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
.knowledge-point-manage {
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

.point-table {
  width: 100%;
  min-width: 960px;
  border-collapse: collapse;
}

.point-table th,
.point-table td {
  padding: 0.9rem;
  border-bottom: 1px solid var(--line);
  text-align: left;
  vertical-align: top;
}

.point-table th {
  color: var(--text-strong);
  background: rgba(66, 103, 154, 0.06);
  font-size: 0.9rem;
}

.point-table td {
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

.point-modal {
  width: min(100%, 760px);
  border: 1px solid var(--line);
  border-radius: 16px;
  background: #fff;
}

.point-modal__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 1rem 1.1rem;
  border-bottom: 1px solid var(--line);
}

.point-modal__head h3 {
  margin: 0;
  color: var(--text-strong);
}

.point-form {
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
.field select,
.field textarea {
  width: 100%;
  border: 1px solid var(--line-strong);
  border-radius: 10px;
  color: var(--text-strong);
  background: #fff;
  outline: none;
}

.field input,
.field select {
  height: 2.55rem;
  padding: 0 0.8rem;
}

.field textarea {
  resize: vertical;
  padding: 0.75rem 0.8rem;
}

.field input:focus,
.field select:focus,
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

  .point-form {
    grid-template-columns: 1fr;
  }
}
</style>
