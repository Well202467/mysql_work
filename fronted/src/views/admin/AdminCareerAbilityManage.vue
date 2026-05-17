<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import {
  addCareerAbility,
  deleteCareerAbility,
  getCareerAbilities,
  getCareerOptions,
  updateCareerAbility,
} from '../../api/admin.js'

const abilityTypeOptions = [
  '编程能力',
  '数据库能力',
  '前端开发能力',
  '后端开发能力',
  '算法能力',
  '数据分析能力',
  '人工智能能力',
  '网络安全能力',
  '测试能力',
  '系统设计能力',
  '项目协作能力',
  '文档表达能力',
]

const importanceOptions = ['高', '中', '低']

const careerAbilities = ref([])
const careerOptions = ref([])
const loading = ref(false)
const saving = ref(false)
const errorMessage = ref('')
const successMessage = ref('')
const showForm = ref(false)
const editingId = ref(null)

const form = reactive({
  careerId: '',
  name: '',
  description: '',
  abilityType: '',
  importance: '',
  learningAdvice: '',
})

const formTitle = computed(() => (editingId.value !== null ? '编辑职业能力' : '新增职业能力'))

onMounted(loadPageData)

async function loadPageData() {
  loading.value = true
  errorMessage.value = ''
  try {
    const [abilityRes, careerRes] = await Promise.all([getCareerAbilities(), getCareerOptions()])

    if (!isBusinessSuccess(abilityRes)) {
      careerAbilities.value = []
      errorMessage.value = getBusinessMessage(abilityRes)
      return
    }
    if (!isBusinessSuccess(careerRes)) {
      careerOptions.value = []
      errorMessage.value = getBusinessMessage(careerRes)
      return
    }

    careerAbilities.value = Array.isArray(abilityRes.data) ? abilityRes.data : []
    careerOptions.value = Array.isArray(careerRes.data) ? careerRes.data : []
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '职业能力列表加载失败')
  } finally {
    loading.value = false
  }
}

function openCreateForm() {
  editingId.value = null
  Object.assign(form, {
    careerId: '',
    name: '',
    description: '',
    abilityType: '',
    importance: '',
    learningAdvice: '',
  })
  showForm.value = true
}

function openEditForm(item) {
  editingId.value = item.id
  Object.assign(form, {
    careerId: getCareerId(item),
    name: item.name ?? '',
    description: item.description ?? '',
    abilityType: getAbilityType(item),
    importance: item.importance ?? '',
    learningAdvice: getLearningAdvice(item),
  })
  showForm.value = true
}

function closeForm() {
  showForm.value = false
}

function getCareerId(item) {
  return item?.careerId ?? item?.career_id ?? ''
}

function getCareerName(item) {
  return item?.careerName ?? item?.career_name ?? getCareerId(item)
}

function getAbilityType(item) {
  return item?.abilityType ?? item?.ability_type ?? ''
}

function getLearningAdvice(item) {
  return item?.learningAdvice ?? item?.learning_advice ?? ''
}

function getOptionValue(item) {
  return item?.value ?? item?.id ?? ''
}

function getOptionLabel(item) {
  return item?.label ?? item?.name ?? getOptionValue(item)
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
  if (!form.careerId) {
    errorMessage.value = '请选择职业'
    return false
  }
  if (!form.name.trim()) {
    errorMessage.value = '能力名称不能为空'
    return false
  }
  return true
}

function buildPayload() {
  return {
    careerId: form.careerId,
    name: form.name.trim(),
    description: form.description.trim(),
    abilityType: form.abilityType,
    importance: form.importance,
    learningAdvice: form.learningAdvice.trim(),
  }
}

async function saveCareerAbility() {
  errorMessage.value = ''
  successMessage.value = ''
  if (!validateForm()) return

  saving.value = true
  try {
    const payload = buildPayload()
    const res =
      editingId.value !== null
        ? await updateCareerAbility(editingId.value, payload)
        : await addCareerAbility(payload)

    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = editingId.value !== null ? '职业能力已修改' : '职业能力已新增'
    showForm.value = false
    await loadPageData()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '保存失败')
  } finally {
    saving.value = false
  }
}

async function handleDelete(item) {
  if (!window.confirm(`确认删除职业能力「${item.name || item.id}」吗？`)) return

  errorMessage.value = ''
  successMessage.value = ''
  try {
    const res = await deleteCareerAbility(item.id)
    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = '职业能力已删除'
    await loadPageData()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '删除失败')
  }
}
</script>

<template>
  <section class="ability-manage">
    <div class="manage-head">
      <div>
        <h2>职业能力管理</h2>
        <p>维护 career_ability 表中的职业能力、能力类型、重要程度和学习建议。</p>
      </div>
      <button type="button" class="primary-button" @click="openCreateForm">新增职业能力</button>
    </div>

    <p v-if="errorMessage" class="message message--error">{{ errorMessage }}</p>
    <p v-if="successMessage" class="message message--success">{{ successMessage }}</p>

    <div class="table-panel">
      <div v-if="loading" class="empty-state">正在加载职业能力...</div>
      <table v-else-if="careerAbilities.length" class="ability-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>职业</th>
            <th>能力名称</th>
            <th>能力类型</th>
            <th>重要程度</th>
            <th>学习建议</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in careerAbilities" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ getCareerName(item) }}</td>
            <td>{{ item.name }}</td>
            <td>{{ getAbilityType(item) || '未填写' }}</td>
            <td>{{ item.importance || '未填写' }}</td>
            <td class="text-cell">{{ getLearningAdvice(item) || '未填写' }}</td>
            <td>
              <div class="row-actions">
                <button type="button" @click="openEditForm(item)">编辑</button>
                <button type="button" class="danger-button" @click="handleDelete(item)">删除</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="empty-state">暂无职业能力数据</div>
    </div>

    <div v-if="showForm" class="modal-mask" role="presentation" @click.self="closeForm">
      <section class="ability-modal" role="dialog" aria-modal="true" :aria-label="formTitle">
        <header class="ability-modal__head">
          <h3>{{ formTitle }}</h3>
          <button type="button" class="ghost-button" @click="closeForm">关闭</button>
        </header>

        <form class="ability-form" @submit.prevent="saveCareerAbility">
          <label class="field">
            <span>职业</span>
            <select v-model="form.careerId" required>
              <option value="" disabled>请选择职业</option>
              <option v-for="item in careerOptions" :key="getOptionValue(item)" :value="getOptionValue(item)">
                {{ getOptionLabel(item) }}
              </option>
            </select>
          </label>

          <label class="field">
            <span>能力名称</span>
            <input v-model="form.name" type="text" />
          </label>

          <label class="field">
            <span>能力类型</span>
            <select v-model="form.abilityType">
              <option value="">请选择能力类型</option>
              <option v-for="item in abilityTypeOptions" :key="item" :value="item">
                {{ item }}
              </option>
            </select>
          </label>

          <label class="field">
            <span>重要程度</span>
            <select v-model="form.importance">
              <option value="">请选择重要程度</option>
              <option v-for="item in importanceOptions" :key="item" :value="item">
                {{ item }}
              </option>
            </select>
          </label>

          <label class="field field--wide">
            <span>能力说明</span>
            <textarea v-model="form.description" rows="4"></textarea>
          </label>

          <label class="field field--wide">
            <span>学习建议</span>
            <textarea v-model="form.learningAdvice" rows="4"></textarea>
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
.ability-manage {
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

.ability-table {
  width: 100%;
  min-width: 1040px;
  border-collapse: collapse;
}

.ability-table th,
.ability-table td {
  padding: 0.9rem;
  border-bottom: 1px solid var(--line);
  text-align: left;
  vertical-align: top;
}

.ability-table th {
  color: var(--text-strong);
  background: rgba(66, 103, 154, 0.06);
  font-size: 0.9rem;
}

.ability-table td {
  color: var(--text-main);
  line-height: 1.55;
}

.text-cell {
  max-width: 22rem;
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

.ability-modal {
  width: min(100%, 760px);
  border: 1px solid var(--line);
  border-radius: 16px;
  background: #fff;
}

.ability-modal__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 1rem 1.1rem;
  border-bottom: 1px solid var(--line);
}

.ability-modal__head h3 {
  margin: 0;
  color: var(--text-strong);
}

.ability-form {
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

  .ability-form {
    grid-template-columns: 1fr;
  }
}
</style>
