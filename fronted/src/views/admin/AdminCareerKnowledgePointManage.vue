<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import {
  addCareerKnowledgePoint,
  deleteCareerKnowledgePoint,
  getCareerKnowledgePoints,
  getCareerOptions,
  getCourseOptions,
  getKnowledgePointOptions,
  updateCareerKnowledgePoint,
} from '../../api/admin.js'

const importanceOptions = ['高', '中', '低']

const relations = ref([])
const careerOptions = ref([])
const courseOptions = ref([])
const knowledgePointOptions = ref([])
const loading = ref(false)
const pointLoading = ref(false)
const saving = ref(false)
const errorMessage = ref('')
const successMessage = ref('')
const showForm = ref(false)
const editingId = ref(null)

const form = reactive({
  careerId: '',
  courseId: '',
  knowledgePointId: '',
  useScene: '',
  importance: '',
  learningAdvice: '',
  source: '',
})

const formTitle = computed(() => (editingId.value !== null ? '编辑职业知识点关系' : '新增职业知识点关系'))

onMounted(loadPageData)

async function loadPageData() {
  loading.value = true
  errorMessage.value = ''
  try {
    const [relationRes, careerRes, courseRes] = await Promise.all([
      getCareerKnowledgePoints(),
      getCareerOptions(),
      getCourseOptions(),
    ])

    if (!isBusinessSuccess(relationRes)) {
      relations.value = []
      errorMessage.value = getBusinessMessage(relationRes)
      return
    }
    if (!isBusinessSuccess(careerRes)) {
      careerOptions.value = []
      errorMessage.value = getBusinessMessage(careerRes)
      return
    }
    if (!isBusinessSuccess(courseRes)) {
      courseOptions.value = []
      errorMessage.value = getBusinessMessage(courseRes)
      return
    }

    relations.value = Array.isArray(relationRes.data) ? relationRes.data : []
    careerOptions.value = Array.isArray(careerRes.data) ? careerRes.data : []
    courseOptions.value = Array.isArray(courseRes.data) ? courseRes.data : []
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '职业知识点关系列表加载失败')
  } finally {
    loading.value = false
  }
}

function openCreateForm() {
  editingId.value = null
  knowledgePointOptions.value = []
  Object.assign(form, {
    careerId: '',
    courseId: '',
    knowledgePointId: '',
    useScene: '',
    importance: '',
    learningAdvice: '',
    source: '',
  })
  showForm.value = true
}

async function openEditForm(item) {
  editingId.value = item.id
  Object.assign(form, {
    careerId: getCareerId(item),
    courseId: getCourseId(item),
    knowledgePointId: '',
    useScene: getUseScene(item),
    importance: item.importance ?? '',
    learningAdvice: getLearningAdvice(item),
    source: item.source ?? '',
  })
  await loadKnowledgePointOptions(form.courseId)
  form.knowledgePointId = getKnowledgePointId(item)
  showForm.value = true
}

function closeForm() {
  showForm.value = false
}

async function handleCourseChange() {
  form.knowledgePointId = ''
  await loadKnowledgePointOptions(form.courseId)
}

async function loadKnowledgePointOptions(courseId) {
  knowledgePointOptions.value = []
  if (!courseId) return

  pointLoading.value = true
  try {
    const res = await getKnowledgePointOptions(courseId)
    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }
    knowledgePointOptions.value = Array.isArray(res.data) ? res.data : []
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '知识点选项加载失败')
  } finally {
    pointLoading.value = false
  }
}

function getCareerId(item) {
  return item?.careerId ?? item?.career_id ?? ''
}

function getCareerName(item) {
  return item?.careerName ?? item?.career_name ?? getCareerId(item)
}

function getCourseId(item) {
  return item?.courseId ?? item?.course_id ?? ''
}

function getCourseName(item) {
  return item?.courseName ?? item?.course_name ?? getCourseId(item)
}

function getKnowledgePointId(item) {
  return item?.knowledgePointId ?? item?.knowledge_point_id ?? ''
}

function getKnowledgePointName(item) {
  return item?.knowledgePointName ?? item?.knowledge_point_name ?? getKnowledgePointId(item)
}

function getUseScene(item) {
  return item?.useScene ?? item?.use_scene ?? ''
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
  if (!form.courseId) {
    errorMessage.value = '请选择课程'
    return false
  }
  if (!form.knowledgePointId) {
    errorMessage.value = '请选择知识点'
    return false
  }
  return true
}

function buildPayload() {
  return {
    careerId: form.careerId,
    courseId: form.courseId,
    knowledgePointId: Number(form.knowledgePointId),
    useScene: form.useScene.trim(),
    importance: form.importance,
    learningAdvice: form.learningAdvice.trim(),
    source: form.source.trim(),
  }
}

async function saveRelation() {
  errorMessage.value = ''
  successMessage.value = ''
  if (!validateForm()) return

  saving.value = true
  try {
    const payload = buildPayload()
    const res =
      editingId.value !== null
        ? await updateCareerKnowledgePoint(editingId.value, payload)
        : await addCareerKnowledgePoint(payload)

    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = editingId.value !== null ? '职业知识点关系已修改' : '职业知识点关系已新增'
    showForm.value = false
    await loadPageData()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '保存失败')
  } finally {
    saving.value = false
  }
}

async function handleDelete(item) {
  if (!window.confirm(`确认删除「${getCareerName(item)} - ${getKnowledgePointName(item)}」这条职业知识点关系吗？`)) {
    return
  }

  errorMessage.value = ''
  successMessage.value = ''
  try {
    const res = await deleteCareerKnowledgePoint(item.id)
    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = '职业知识点关系已删除'
    await loadPageData()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '删除失败')
  }
}
</script>

<template>
  <section class="career-point-manage">
    <div class="manage-head">
      <div>
        <h2>职业知识点关系</h2>
        <p>维护 career_knowledge_point 表中的职业、课程、知识点、使用场景和学习建议。</p>
      </div>
      <button type="button" class="primary-button" @click="openCreateForm">新增关系</button>
    </div>

    <p v-if="errorMessage" class="message message--error">{{ errorMessage }}</p>
    <p v-if="successMessage" class="message message--success">{{ successMessage }}</p>

    <div class="table-panel">
      <div v-if="loading" class="empty-state">正在加载职业知识点关系...</div>
      <table v-else-if="relations.length" class="career-point-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>职业</th>
            <th>课程</th>
            <th>知识点</th>
            <th>使用场景</th>
            <th>重要程度</th>
            <th>学习建议</th>
            <th>来源</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in relations" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ getCareerName(item) }}</td>
            <td>{{ getCourseName(item) }}</td>
            <td>{{ getKnowledgePointName(item) }}</td>
            <td class="text-cell">{{ getUseScene(item) || '未填写' }}</td>
            <td>{{ item.importance || '未填写' }}</td>
            <td class="text-cell">{{ getLearningAdvice(item) || '未填写' }}</td>
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
      <div v-else class="empty-state">暂无职业知识点关系数据</div>
    </div>

    <div v-if="showForm" class="modal-mask" role="presentation" @click.self="closeForm">
      <section class="career-point-modal" role="dialog" aria-modal="true" :aria-label="formTitle">
        <header class="career-point-modal__head">
          <h3>{{ formTitle }}</h3>
          <button type="button" class="ghost-button" @click="closeForm">关闭</button>
        </header>

        <form class="career-point-form" @submit.prevent="saveRelation">
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
            <span>课程</span>
            <select v-model="form.courseId" required @change="handleCourseChange">
              <option value="" disabled>请选择课程</option>
              <option v-for="item in courseOptions" :key="getOptionValue(item)" :value="getOptionValue(item)">
                {{ getOptionLabel(item) }}
              </option>
            </select>
          </label>

          <label class="field">
            <span>知识点</span>
            <select v-model="form.knowledgePointId" required :disabled="!form.courseId || pointLoading">
              <option value="" disabled>
                {{ pointLoading ? '正在加载知识点...' : '请选择知识点' }}
              </option>
              <option v-for="item in knowledgePointOptions" :key="getOptionValue(item)" :value="getOptionValue(item)">
                {{ getOptionLabel(item) }}
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

          <p v-if="form.courseId && !pointLoading && !knowledgePointOptions.length" class="field-hint">
            当前课程暂无知识点，可先到“知识点管理”中添加。
          </p>

          <label class="field">
            <span>来源</span>
            <input v-model="form.source" type="text" />
          </label>

          <label class="field field--wide">
            <span>使用场景</span>
            <textarea v-model="form.useScene" rows="4"></textarea>
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
.career-point-manage {
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

.career-point-table {
  width: 100%;
  min-width: 1180px;
  border-collapse: collapse;
}

.career-point-table th,
.career-point-table td {
  padding: 0.9rem;
  border-bottom: 1px solid var(--line);
  text-align: left;
  vertical-align: top;
}

.career-point-table th {
  color: var(--text-strong);
  background: rgba(66, 103, 154, 0.06);
  font-size: 0.9rem;
}

.career-point-table td {
  color: var(--text-main);
  line-height: 1.55;
}

.text-cell {
  max-width: 18rem;
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

.career-point-modal {
  width: min(100%, 820px);
  border: 1px solid var(--line);
  border-radius: 16px;
  background: #fff;
}

.career-point-modal__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 1rem 1.1rem;
  border-bottom: 1px solid var(--line);
}

.career-point-modal__head h3 {
  margin: 0;
  color: var(--text-strong);
}

.career-point-form {
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

.field--wide,
.field-hint {
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

.field select:disabled {
  color: var(--text-muted);
  background: #f8f8f6;
}

.field input:focus,
.field select:focus,
.field textarea:focus {
  border-color: rgba(66, 103, 154, 0.48);
}

.field-hint {
  margin: 0;
  padding: 0.75rem 0.8rem;
  border: 1px dashed var(--line-strong);
  border-radius: 10px;
  color: var(--text-muted);
  background: #fff;
  font-weight: 700;
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

  .career-point-form {
    grid-template-columns: 1fr;
  }
}
</style>
