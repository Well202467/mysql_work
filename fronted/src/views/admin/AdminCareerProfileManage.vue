<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import {
  addCareerProfile,
  deleteCareerProfile,
  getCareerOptions,
  getCareerProfiles,
  updateCareerProfile,
} from '../../api/admin.js'

const careerProfiles = ref([])
const careerOptions = ref([])
const loading = ref(false)
const saving = ref(false)
const errorMessage = ref('')
const successMessage = ref('')
const showForm = ref(false)
const editingId = ref(null)

const form = reactive({
  careerId: '',
  intro: '',
  workContent: '',
  requirement: '',
  commonTech: '',
  suitableMajor: '',
  developmentPath: '',
  source: '',
})

const formTitle = computed(() => (editingId.value !== null ? '编辑职业画像' : '新增职业画像'))

onMounted(loadPageData)

async function loadPageData() {
  loading.value = true
  errorMessage.value = ''
  try {
    const [profileRes, careerRes] = await Promise.all([getCareerProfiles(), getCareerOptions()])

    if (!isBusinessSuccess(profileRes)) {
      careerProfiles.value = []
      errorMessage.value = getBusinessMessage(profileRes)
      return
    }
    if (!isBusinessSuccess(careerRes)) {
      careerOptions.value = []
      errorMessage.value = getBusinessMessage(careerRes)
      return
    }

    careerProfiles.value = Array.isArray(profileRes.data) ? profileRes.data : []
    careerOptions.value = Array.isArray(careerRes.data) ? careerRes.data : []
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '职业画像列表加载失败')
  } finally {
    loading.value = false
  }
}

function openCreateForm() {
  editingId.value = null
  Object.assign(form, {
    careerId: '',
    intro: '',
    workContent: '',
    requirement: '',
    commonTech: '',
    suitableMajor: '',
    developmentPath: '',
    source: '',
  })
  showForm.value = true
}

function openEditForm(item) {
  editingId.value = item.id
  Object.assign(form, {
    careerId: getCareerId(item),
    intro: item.intro ?? '',
    workContent: getWorkContent(item),
    requirement: item.requirement ?? '',
    commonTech: getCommonTech(item),
    suitableMajor: getSuitableMajor(item),
    developmentPath: getDevelopmentPath(item),
    source: item.source ?? '',
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

function getWorkContent(item) {
  return item?.workContent ?? item?.work_content ?? ''
}

function getCommonTech(item) {
  return item?.commonTech ?? item?.common_tech ?? ''
}

function getSuitableMajor(item) {
  return item?.suitableMajor ?? item?.suitable_major ?? ''
}

function getDevelopmentPath(item) {
  return item?.developmentPath ?? item?.development_path ?? ''
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
  return true
}

function buildPayload() {
  return {
    careerId: form.careerId,
    intro: form.intro.trim(),
    workContent: form.workContent.trim(),
    requirement: form.requirement.trim(),
    commonTech: form.commonTech.trim(),
    suitableMajor: form.suitableMajor.trim(),
    developmentPath: form.developmentPath.trim(),
    source: form.source.trim(),
  }
}

async function saveCareerProfile() {
  errorMessage.value = ''
  successMessage.value = ''
  if (!validateForm()) return

  saving.value = true
  try {
    const payload = buildPayload()
    const res =
      editingId.value !== null
        ? await updateCareerProfile(editingId.value, payload)
        : await addCareerProfile(payload)

    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = editingId.value !== null ? '职业画像已修改' : '职业画像已新增'
    showForm.value = false
    await loadPageData()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '保存失败')
  } finally {
    saving.value = false
  }
}

async function handleDelete(item) {
  if (!window.confirm(`确认删除「${getCareerName(item) || item.id}」的职业画像吗？`)) return

  errorMessage.value = ''
  successMessage.value = ''
  try {
    const res = await deleteCareerProfile(item.id)
    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = '职业画像已删除'
    await loadPageData()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '删除失败')
  }
}
</script>

<template>
  <section class="profile-manage">
    <div class="manage-head">
      <div>
        <h2>职业画像管理</h2>
        <p>维护 career_profile 表中的职业介绍、工作内容、能力要求和发展路径。</p>
      </div>
      <button type="button" class="primary-button" @click="openCreateForm">新增职业画像</button>
    </div>

    <p v-if="errorMessage" class="message message--error">{{ errorMessage }}</p>
    <p v-if="successMessage" class="message message--success">{{ successMessage }}</p>

    <div class="table-panel">
      <div v-if="loading" class="empty-state">正在加载职业画像...</div>
      <table v-else-if="careerProfiles.length" class="profile-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>职业</th>
            <th>简介</th>
            <th>常用技术</th>
            <th>适合专业</th>
            <th>来源</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in careerProfiles" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ getCareerName(item) }}</td>
            <td class="text-cell">{{ item.intro || '未填写' }}</td>
            <td class="text-cell">{{ getCommonTech(item) || '未填写' }}</td>
            <td>{{ getSuitableMajor(item) || '未填写' }}</td>
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
      <div v-else class="empty-state">暂无职业画像数据</div>
    </div>

    <div v-if="showForm" class="modal-mask" role="presentation" @click.self="closeForm">
      <section class="profile-modal" role="dialog" aria-modal="true" :aria-label="formTitle">
        <header class="profile-modal__head">
          <h3>{{ formTitle }}</h3>
          <button type="button" class="ghost-button" @click="closeForm">关闭</button>
        </header>

        <form class="profile-form" @submit.prevent="saveCareerProfile">
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
            <span>来源</span>
            <input v-model="form.source" type="text" />
          </label>

          <label class="field field--wide">
            <span>简介</span>
            <textarea v-model="form.intro" rows="3"></textarea>
          </label>

          <label class="field field--wide">
            <span>工作内容</span>
            <textarea v-model="form.workContent" rows="3"></textarea>
          </label>

          <label class="field field--wide">
            <span>任职要求</span>
            <textarea v-model="form.requirement" rows="3"></textarea>
          </label>

          <label class="field">
            <span>常用技术</span>
            <textarea v-model="form.commonTech" rows="3"></textarea>
          </label>

          <label class="field">
            <span>适合专业</span>
            <textarea v-model="form.suitableMajor" rows="3"></textarea>
          </label>

          <label class="field field--wide">
            <span>发展路径</span>
            <textarea v-model="form.developmentPath" rows="3"></textarea>
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
.profile-manage {
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
  overflow-x: auto;
  border: 1px solid var(--line);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.9);
}

.profile-table {
  width: 100%;
  max-width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}

.profile-table th,
.profile-table td {
  padding: 12px 10px;
  border-bottom: 1px solid var(--line);
  font-size: 0.92rem;
  text-align: left;
  vertical-align: top;
  overflow-wrap: anywhere;
  white-space: normal;
  word-break: break-word;
}

.profile-table th {
  color: var(--text-strong);
  background: rgba(66, 103, 154, 0.06);
  font-size: 0.9rem;
}

.profile-table td {
  color: var(--text-main);
  line-height: 1.55;
}

.profile-table th:nth-child(1),
.profile-table td:nth-child(1) {
  width: 5%;
}

.profile-table th:nth-child(2),
.profile-table td:nth-child(2) {
  width: 13%;
}

.profile-table th:nth-child(3),
.profile-table td:nth-child(3) {
  width: 22%;
}

.profile-table th:nth-child(4),
.profile-table td:nth-child(4) {
  width: 22%;
}

.profile-table th:nth-child(5),
.profile-table td:nth-child(5) {
  width: 12%;
}

.profile-table th:nth-child(6),
.profile-table td:nth-child(6) {
  width: 8%;
}

.text-cell {
  line-height: 1.6;
}

.profile-table th:last-child,
.profile-table td:last-child {
  width: 145px;
  min-width: 145px;
}

.row-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  justify-content: flex-start;
  white-space: nowrap;
}

.row-actions button {
  min-width: 56px;
  padding: 8px 12px;
  flex-shrink: 0;
  text-align: center;
  white-space: nowrap;
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

.profile-modal {
  width: min(100%, 860px);
  max-height: calc(100vh - 2rem);
  overflow: auto;
  border: 1px solid var(--line);
  border-radius: 16px;
  background: #fff;
}

.profile-modal__head {
  position: sticky;
  top: 0;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 1rem 1.1rem;
  border-bottom: 1px solid var(--line);
  background: #fff;
}

.profile-modal__head h3 {
  margin: 0;
  color: var(--text-strong);
}

.profile-form {
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

  .profile-form {
    grid-template-columns: 1fr;
  }
}
</style>
