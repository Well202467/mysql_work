<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import {
  addCareerCourse,
  deleteCareerCourse,
  getCareerAbilities,
  getCareerCourses,
  getCareerOptions,
  getCourseOptions,
  getKnowledgePointOptions,
  updateCareerCourse,
} from '../../api/admin.js'

const importanceOptions = ['高', '中', '低']
const learningStageOptions = ['大一', '大二', '大三', '大四', '基础阶段', '提升阶段', '实战阶段']

const careerCourses = ref([])
const careerAbilityOptions = ref([])
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
  courseRole: '',
  relatedAbility: '',
  selectedAbilities: [],
  relatedPoints: '',
  selectedPoints: [],
  importance: '',
  isCore: '',
  learningStage: '',
})

const formTitle = computed(() => (editingId.value !== null ? '编辑职业课程关系' : '新增职业课程关系'))
const currentCareerAbilities = computed(() =>
  careerAbilityOptions.value.filter((item) => getCareerId(item) === form.careerId),
)
const displayedAbilityOptions = computed(() => {
  const optionNames = currentCareerAbilities.value.map((item) => item.name).filter(Boolean)
  const missingNames = form.selectedAbilities.filter((name) => name && !optionNames.includes(name))

  return [
    ...currentCareerAbilities.value.map((item) => ({ name: item.name, missing: false })),
    ...missingNames.map((name) => ({ name, missing: true })),
  ]
})
const displayedPointOptions = computed(() => {
  const optionNames = knowledgePointOptions.value.map((item) => getOptionLabel(item)).filter(Boolean)
  const missingNames = form.selectedPoints.filter((name) => name && !optionNames.includes(name))

  return [
    ...knowledgePointOptions.value.map((item) => ({ name: getOptionLabel(item), missing: false })),
    ...missingNames.map((name) => ({ name, missing: true })),
  ]
})

onMounted(loadPageData)

async function loadPageData() {
  loading.value = true
  errorMessage.value = ''
  try {
    const [relationRes, careerRes, courseRes, abilityRes] = await Promise.all([
      getCareerCourses(),
      getCareerOptions(),
      getCourseOptions(),
      getCareerAbilities(),
    ])

    if (!isBusinessSuccess(relationRes)) {
      careerCourses.value = []
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
    if (!isBusinessSuccess(abilityRes)) {
      careerAbilityOptions.value = []
      errorMessage.value = getBusinessMessage(abilityRes)
      return
    }

    careerCourses.value = Array.isArray(relationRes.data) ? relationRes.data : []
    careerOptions.value = Array.isArray(careerRes.data) ? careerRes.data : []
    courseOptions.value = Array.isArray(courseRes.data) ? courseRes.data : []
    careerAbilityOptions.value = Array.isArray(abilityRes.data) ? abilityRes.data : []
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '职业课程关系列表加载失败')
  } finally {
    loading.value = false
  }
}

function openCreateForm() {
  editingId.value = null
  Object.assign(form, {
    careerId: '',
    courseId: '',
    courseRole: '',
    relatedAbility: '',
    selectedAbilities: [],
    relatedPoints: '',
    selectedPoints: [],
    importance: '',
    isCore: '',
    learningStage: '',
  })
  showForm.value = true
}

function openEditForm(item) {
  editingId.value = item.id
  Object.assign(form, {
    careerId: getCareerId(item),
    courseId: getCourseId(item),
    courseRole: getCourseRole(item),
    relatedAbility: getRelatedAbility(item),
    selectedAbilities: splitRelatedAbility(getRelatedAbility(item)),
    relatedPoints: getRelatedPoints(item),
    selectedPoints: splitTextList(getRelatedPoints(item)),
    importance: item.importance ?? '',
    isCore: normalizeIsCore(getIsCore(item)),
    learningStage: getLearningStage(item),
  })
  loadKnowledgePointOptions(form.courseId)
  showForm.value = true
}

function closeForm() {
  showForm.value = false
}

function handleCareerChange() {
  form.relatedAbility = ''
  form.selectedAbilities = []
}

async function handleCourseChange() {
  form.relatedPoints = ''
  form.selectedPoints = []
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

function getCourseRole(item) {
  return item?.courseRole ?? item?.course_role ?? ''
}

function getRelatedAbility(item) {
  return item?.relatedAbility ?? item?.related_ability ?? ''
}

function getRelatedPoints(item) {
  return item?.relatedPoints ?? item?.related_points ?? ''
}

function getIsCore(item) {
  return item?.isCore ?? item?.is_core ?? ''
}

function getLearningStage(item) {
  return item?.learningStage ?? item?.learning_stage ?? ''
}

function splitRelatedAbility(value) {
  return splitTextList(value)
}

function splitTextList(value) {
  return Array.from(
    new Set(
      String(value || '')
        .split(/[、,，]/)
        .map((item) => item.trim())
        .filter(Boolean),
    ),
  )
}

function normalizeIsCore(value) {
  if (value === true || value === 1 || value === '1') return '1'
  if (value === false || value === 0 || value === '0') return '0'
  return ''
}

function formatIsCore(item) {
  const value = normalizeIsCore(getIsCore(item))
  if (value === '1') return '是'
  if (value === '0') return '否'
  return '未填写'
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
  return true
}

function buildPayload() {
  return {
    careerId: form.careerId,
    courseId: form.courseId,
    courseRole: form.courseRole.trim(),
    relatedAbility: form.selectedAbilities.join('、'),
    relatedPoints: form.selectedPoints.join('、'),
    importance: form.importance,
    isCore: form.isCore === '' ? null : Number(form.isCore),
    learningStage: form.learningStage,
  }
}

async function saveCareerCourse() {
  errorMessage.value = ''
  successMessage.value = ''
  if (!validateForm()) return

  saving.value = true
  try {
    const payload = buildPayload()
    const res =
      editingId.value !== null
        ? await updateCareerCourse(editingId.value, payload)
        : await addCareerCourse(payload)

    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = editingId.value !== null ? '职业课程关系已修改' : '职业课程关系已新增'
    showForm.value = false
    await loadPageData()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '保存失败')
  } finally {
    saving.value = false
  }
}

async function handleDelete(item) {
  if (!window.confirm(`确认删除「${getCareerName(item)} - ${getCourseName(item)}」这条职业课程关系吗？`)) {
    return
  }

  errorMessage.value = ''
  successMessage.value = ''
  try {
    const res = await deleteCareerCourse(item.id)
    if (!isBusinessSuccess(res)) {
      errorMessage.value = getBusinessMessage(res)
      return
    }

    successMessage.value = '职业课程关系已删除'
    await loadPageData()
  } catch (error) {
    errorMessage.value = getErrorMessage(error, '删除失败')
  }
}
</script>

<template>
  <section class="career-course-manage">
    <div class="manage-head">
      <div>
        <h2>职业课程关系</h2>
        <p>维护 career_course 表中的职业、课程、关联能力、核心课程和学习阶段。</p>
      </div>
      <button type="button" class="primary-button" @click="openCreateForm">新增关系</button>
    </div>

    <p v-if="errorMessage" class="message message--error">{{ errorMessage }}</p>
    <p v-if="successMessage" class="message message--success">{{ successMessage }}</p>

    <div class="table-panel">
      <div v-if="loading" class="empty-state">正在加载职业课程关系...</div>
      <table v-else-if="careerCourses.length" class="career-course-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>职业</th>
            <th>课程</th>
            <th>课程作用</th>
            <th>重要程度</th>
            <th>核心课程</th>
            <th>学习阶段</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in careerCourses" :key="item.id">
            <td>{{ item.id }}</td>
            <td>{{ getCareerName(item) }}</td>
            <td>{{ getCourseName(item) }}</td>
            <td>{{ getCourseRole(item) || '未填写' }}</td>
            <td>{{ item.importance || '未填写' }}</td>
            <td>{{ formatIsCore(item) }}</td>
            <td>{{ getLearningStage(item) || '未填写' }}</td>
            <td>
              <div class="row-actions">
                <button type="button" @click="openEditForm(item)">编辑</button>
                <button type="button" class="danger-button" @click="handleDelete(item)">删除</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="empty-state">暂无职业课程关系数据</div>
    </div>

    <div v-if="showForm" class="modal-mask" role="presentation" @click.self="closeForm">
      <section class="career-course-modal" role="dialog" aria-modal="true" :aria-label="formTitle">
        <header class="career-course-modal__head">
          <h3>{{ formTitle }}</h3>
          <button type="button" class="ghost-button" @click="closeForm">关闭</button>
        </header>

        <form class="career-course-form" @submit.prevent="saveCareerCourse">
          <label class="field">
            <span>职业</span>
            <select v-model="form.careerId" required @change="handleCareerChange">
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
            <span>课程作用</span>
            <input v-model="form.courseRole" type="text" />
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

          <label class="field">
            <span>核心课程</span>
            <select v-model="form.isCore">
              <option value="">请选择是否核心</option>
              <option value="1">是</option>
              <option value="0">否</option>
            </select>
          </label>

          <label class="field">
            <span>学习阶段</span>
            <select v-model="form.learningStage">
              <option value="">请选择学习阶段</option>
              <option v-for="item in learningStageOptions" :key="item" :value="item">
                {{ item }}
              </option>
            </select>
          </label>

          <div class="field field--wide">
            <span>关联能力</span>
            <div v-if="!form.careerId" class="ability-empty">请先选择职业</div>
            <div v-else-if="displayedAbilityOptions.length" class="ability-options">
              <label v-for="item in displayedAbilityOptions" :key="item.name" class="ability-option">
                <input v-model="form.selectedAbilities" type="checkbox" :value="item.name" />
                <span>{{ item.name }}{{ item.missing ? '（原有关联）' : '' }}</span>
              </label>
            </div>
            <div v-else class="ability-empty">当前职业暂无能力数据，可先到“职业能力管理”中添加。</div>
          </div>

          <div class="field field--wide">
            <span>关联知识点</span>
            <div v-if="!form.courseId" class="option-empty">请先选择课程</div>
            <div v-else-if="pointLoading" class="option-empty">正在加载知识点...</div>
            <div v-else-if="displayedPointOptions.length" class="option-list">
              <label v-for="item in displayedPointOptions" :key="item.name" class="option-item">
                <input v-model="form.selectedPoints" type="checkbox" :value="item.name" />
                <span>{{ item.name }}{{ item.missing ? '（原有关联）' : '' }}</span>
              </label>
            </div>
            <div v-else class="option-empty">当前课程暂无知识点，可先到“知识点管理”中添加。</div>
          </div>

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
.career-course-manage {
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

.career-course-table {
  width: 100%;
  min-width: 1040px;
  border-collapse: collapse;
}

.career-course-table th,
.career-course-table td {
  padding: 0.9rem;
  border-bottom: 1px solid var(--line);
  text-align: left;
  vertical-align: top;
}

.career-course-table th {
  color: var(--text-strong);
  background: rgba(66, 103, 154, 0.06);
  font-size: 0.9rem;
}

.career-course-table td {
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

.career-course-modal {
  width: min(100%, 780px);
  border: 1px solid var(--line);
  border-radius: 16px;
  background: #fff;
}

.career-course-modal__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 1rem 1.1rem;
  border-bottom: 1px solid var(--line);
}

.career-course-modal__head h3 {
  margin: 0;
  color: var(--text-strong);
}

.career-course-form {
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

.ability-options,
.option-list {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 0.55rem;
  padding: 0.75rem;
  border: 1px solid var(--line-strong);
  border-radius: 10px;
  background: #fff;
}

.ability-option,
.option-item {
  display: flex;
  align-items: center;
  gap: 0.45rem;
  min-width: 0;
  color: var(--text-main);
  font-weight: 600;
  line-height: 1.4;
}

.ability-option input,
.option-item input {
  width: auto;
  height: auto;
  flex: none;
}

.ability-empty,
.option-empty {
  padding: 0.75rem 0.8rem;
  border: 1px dashed var(--line-strong);
  border-radius: 10px;
  color: var(--text-muted);
  background: #fff;
  font-weight: 600;
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

  .career-course-form {
    grid-template-columns: 1fr;
  }

  .ability-options,
  .option-list {
    grid-template-columns: 1fr;
  }
}
</style>
