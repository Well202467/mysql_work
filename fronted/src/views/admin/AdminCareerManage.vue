<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { addCareer, deleteCareer, getCareers, updateCareer } from '../../api/admin.js'

const careers = ref([])
const loading = ref(false)
const saving = ref(false)
const errorMessage = ref('')
const successMessage = ref('')
const showForm = ref(false)
const editingId = ref(null)

const form = reactive({
  id: '',
  name: '',
  category: '',
  sortOrder: '',
})

const formTitle = computed(() => (editingId.value !== null ? '编辑职业' : '新增职业'))

onMounted(loadCareers)

async function loadCareers() {
  loading.value = true
  errorMessage.value = ''
  try {
    const res = await getCareers()
    if (!isBusinessSuccess(res)) {
      careers.value = []
      errorMessage.value = getBusinessMessage(res)
      return
    }

    careers.value = Array.isArray(res.data) ? res.data : []
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '职业列表加载失败')
  } finally {
    loading.value = false
  }
}

function openCreateForm() {
  editingId.value = null
  Object.assign(form, {
    id: '',
    name: '',
    category: '',
    sortOrder: '',
  })
  showForm.value = true
}

function openEditForm(career) {
  editingId.value = career.id
  Object.assign(form, {
    id: career.id ?? '',
    name: career.name ?? '',
    category: career.category ?? '',
    sortOrder: getSortOrder(career),
  })
  showForm.value = true
}

function closeForm() {
  showForm.value = false
}

function getSortOrder(career) {
  return career?.sortOrder ?? career?.sort_order ?? ''
}

function formatSortOrder(career) {
  const sortOrder = getSortOrder(career)
  return sortOrder === '' || sortOrder === null ? '-' : sortOrder
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

function validateForm() {
  if (!form.id.trim()) {
    errorMessage.value = '职业 ID 不能为空'
    return false
  }
  if (!form.name.trim()) {
    errorMessage.value = '职业名称不能为空'
    return false
  }
  if (!form.category.trim()) {
    errorMessage.value = '职业类别不能为空'
    return false
  }
  return true
}

function buildPayload() {
  return {
    id: form.id.trim(),
    name: form.name.trim(),
    category: form.category.trim(),
    sortOrder: form.sortOrder === '' ? null : Number(form.sortOrder),
  }
}

async function saveCareer() {
  errorMessage.value = ''
  successMessage.value = ''
  if (!validateForm()) return

  saving.value = true
  try {
    const payload = buildPayload()
    const res =
      editingId.value !== null ? await updateCareer(editingId.value, payload) : await addCareer(payload)

    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = editingId.value !== null ? '职业已修改' : '职业已新增'
    showForm.value = false
    await loadCareers()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '保存失败')
  } finally {
    saving.value = false
  }
}

async function handleDelete(career) {
  if (!window.confirm(`确认删除职业「${career.name || career.id}」吗？`)) return

  errorMessage.value = ''
  successMessage.value = ''
  try {
    const res = await deleteCareer(career.id)
    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = '职业已删除'
    await loadCareers()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '删除失败')
  }
}
</script>

<template>
  <section class="career-manage">
    <div class="manage-head">
      <div>
        <h2>职业管理</h2>
        <p>维护 career 表中的职业名称、类别和排序信息。</p>
      </div>
      <button type="button" class="primary-button" @click="openCreateForm">新增职业</button>
    </div>

    <p v-if="errorMessage" class="message message--error">{{ errorMessage }}</p>
    <p v-if="successMessage" class="message message--success">{{ successMessage }}</p>

    <div class="table-panel">
      <div v-if="loading" class="empty-state">正在加载职业...</div>
      <table v-else-if="careers.length" class="career-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>职业名称</th>
            <th>类别</th>
            <th>排序</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="career in careers" :key="career.id">
            <td>{{ career.id }}</td>
            <td>{{ career.name }}</td>
            <td>{{ career.category || '未填写' }}</td>
            <td>{{ formatSortOrder(career) }}</td>
            <td>
              <div class="row-actions">
                <button type="button" @click="openEditForm(career)">编辑</button>
                <button type="button" class="danger-button" @click="handleDelete(career)">删除</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="empty-state">暂无职业数据</div>
    </div>

    <div v-if="showForm" class="modal-mask" role="presentation" @click.self="closeForm">
      <section class="career-modal" role="dialog" aria-modal="true" :aria-label="formTitle">
        <header class="career-modal__head">
          <h3>{{ formTitle }}</h3>
          <button type="button" class="ghost-button" @click="closeForm">关闭</button>
        </header>

        <form class="career-form" @submit.prevent="saveCareer">
          <label class="field">
            <span>ID</span>
            <input
              v-model="form.id"
              type="text"
              placeholder="请输入职业ID，如 java-backend-engineer"
              :disabled="editingId !== null"
            />
          </label>

          <label class="field">
            <span>职业名称</span>
            <input v-model="form.name" type="text" />
          </label>

          <label class="field">
            <span>类别</span>
            <input v-model="form.category" type="text" />
          </label>

          <label class="field">
            <span>排序</span>
            <input v-model="form.sortOrder" type="number" min="0" step="1" />
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
.career-manage {
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

.career-table {
  width: 100%;
  min-width: 760px;
  border-collapse: collapse;
}

.career-table th,
.career-table td {
  padding: 0.9rem;
  border-bottom: 1px solid var(--line);
  text-align: left;
  vertical-align: top;
}

.career-table th {
  color: var(--text-strong);
  background: rgba(66, 103, 154, 0.06);
  font-size: 0.9rem;
}

.career-table td {
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

.career-modal {
  width: min(100%, 680px);
  border: 1px solid var(--line);
  border-radius: 16px;
  background: #fff;
}

.career-modal__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 1rem 1.1rem;
  border-bottom: 1px solid var(--line);
}

.career-modal__head h3 {
  margin: 0;
  color: var(--text-strong);
}

.career-form {
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

.field input {
  width: 100%;
  height: 2.55rem;
  padding: 0 0.8rem;
  border: 1px solid var(--line-strong);
  border-radius: 10px;
  color: var(--text-strong);
  background: #fff;
  outline: none;
}

.field input:focus {
  border-color: rgba(66, 103, 154, 0.48);
}

.field input[readonly],
.field input[disabled] {
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

  .career-form {
    grid-template-columns: 1fr;
  }
}
</style>
