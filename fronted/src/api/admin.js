import request from './request.js'

export function adminLogin(data) {
  return request.post('/api/admin/login', data)
}

export async function getCourses() {
  try {
    return await request.get('/api/admin/courses')
  } catch (error) {
    if (!isNotFound(error)) {
      throw error
    }
    const courses = await getCoursesFromMajorApis()
    return {
      code: 200,
      message: 'success',
      data: courses,
    }
  }
}

export function addCourse(data) {
  return request.post('/api/admin/courses', data)
}

export function updateCourse(id, data) {
  return request.put(`/api/admin/courses/${encodeURIComponent(id)}`, data)
}

export function deleteCourse(id) {
  return request.delete(`/api/admin/courses/${encodeURIComponent(id)}`)
}

export function getMajors() {
  return request.get('/api/admin/majors')
}

export function addMajor(data) {
  return request.post('/api/admin/majors', data)
}

export function updateMajor(id, data) {
  return request.put(`/api/admin/majors/${encodeURIComponent(id)}`, data)
}

export function deleteMajor(id) {
  return request.delete(`/api/admin/majors/${encodeURIComponent(id)}`)
}

export function getMajorCourses() {
  return request.get('/api/admin/major-courses')
}

export function addMajorCourse(data) {
  return request.post('/api/admin/major-courses', data)
}

export function updateMajorCourse(id, data) {
  return request.put(`/api/admin/major-courses/${encodeURIComponent(id)}`, data)
}

export function deleteMajorCourse(id) {
  return request.delete(`/api/admin/major-courses/${encodeURIComponent(id)}`)
}

export function getKnowledgePoints() {
  return request.get('/api/admin/knowledge-points')
}

export function addKnowledgePoint(data) {
  return request.post('/api/admin/knowledge-points', data)
}

export function updateKnowledgePoint(id, data) {
  return request.put(`/api/admin/knowledge-points/${encodeURIComponent(id)}`, data)
}

export function deleteKnowledgePoint(id) {
  return request.delete(`/api/admin/knowledge-points/${encodeURIComponent(id)}`)
}

export function getCareers() {
  return request.get('/api/admin/careers')
}

export function addCareer(data) {
  return request.post('/api/admin/careers', data)
}

export function updateCareer(id, data) {
  return request.put(`/api/admin/careers/${encodeURIComponent(id)}`, data)
}

export function deleteCareer(id) {
  return request.delete(`/api/admin/careers/${encodeURIComponent(id)}`)
}

export function getCareerProfiles() {
  return request.get('/api/admin/career-profiles')
}

export function addCareerProfile(data) {
  return request.post('/api/admin/career-profiles', data)
}

export function updateCareerProfile(id, data) {
  return request.put(`/api/admin/career-profiles/${encodeURIComponent(id)}`, data)
}

export function deleteCareerProfile(id) {
  return request.delete(`/api/admin/career-profiles/${encodeURIComponent(id)}`)
}

export function getCareerAbilities() {
  return request.get('/api/admin/career-abilities')
}

export function addCareerAbility(data) {
  return request.post('/api/admin/career-abilities', data)
}

export function updateCareerAbility(id, data) {
  return request.put(`/api/admin/career-abilities/${encodeURIComponent(id)}`, data)
}

export function deleteCareerAbility(id) {
  return request.delete(`/api/admin/career-abilities/${encodeURIComponent(id)}`)
}

export function getCareerCourses() {
  return request.get('/api/admin/career-courses')
}

export function addCareerCourse(data) {
  return request.post('/api/admin/career-courses', data)
}

export function updateCareerCourse(id, data) {
  return request.put(`/api/admin/career-courses/${encodeURIComponent(id)}`, data)
}

export function deleteCareerCourse(id) {
  return request.delete(`/api/admin/career-courses/${encodeURIComponent(id)}`)
}

export function getCareerKnowledgePoints() {
  return request.get('/api/admin/career-knowledge-points')
}

export function addCareerKnowledgePoint(data) {
  return request.post('/api/admin/career-knowledge-points', data)
}

export function updateCareerKnowledgePoint(id, data) {
  return request.put(`/api/admin/career-knowledge-points/${encodeURIComponent(id)}`, data)
}

export function deleteCareerKnowledgePoint(id) {
  return request.delete(`/api/admin/career-knowledge-points/${encodeURIComponent(id)}`)
}

export function getMajorOptions() {
  return request.get('/api/admin/options/majors')
}

export function getCourseOptions() {
  return request.get('/api/admin/options/courses')
}

export function getCareerOptions() {
  return request.get('/api/admin/options/careers')
}

export function getKnowledgePointOptions(courseId) {
  const query = courseId ? `?courseId=${encodeURIComponent(courseId)}` : ''
  return request.get(`/api/admin/options/knowledge-points${query}`)
}

export function getTodayVisitStats() {
  return request.get('/api/admin/stats/today')
}

export function getVisitStatsHistory(params) {
  if (!params || Object.keys(params).length === 0) {
    return request.get('/api/admin/stats/history')
  }
  return request.get('/api/admin/stats/history', { params })
}

export function getVisitHistoryStats(params) {
  return getVisitStatsHistory(params)
}

function normalizeResponseData(response) {
  return Array.isArray(response?.data) ? response.data : []
}

function isNotFound(error) {
  return error?.response?.status === 404
}

async function getCoursesFromMajorApis() {
  const majorsResponse = await request.get('/api/majors')
  const majors = normalizeResponseData(majorsResponse)
  const courseGroups = await Promise.all(
    majors.map(async (major) => {
      const response = await request.get(`/api/majors/${encodeURIComponent(major.id)}/courses`)
      return normalizeResponseData(response)
    }),
  )

  const courseMap = new Map()
  courseGroups.flat().forEach((course) => {
    const id = course.courseId ?? course.id
    if (!id || courseMap.has(id)) return
    courseMap.set(id, {
      id,
      name: course.courseName ?? course.name ?? '',
      description: course.courseDescription ?? course.description ?? '',
      type: course.type ?? course.courseType ?? '',
      credits: course.credits ?? '',
    })
  })

  return Array.from(courseMap.values())
}
