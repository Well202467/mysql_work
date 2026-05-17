import request from './request.js'

export function adminLogin(data) {
  return request.post('/api/admin/login', data)
}

export async function getCourses() {
  try {
    const response = await request.get('/api/admin/courses')
    return normalizeResponseData(response)
  } catch (error) {
    if (!isNotFound(error)) {
      throw error
    }
    return getCoursesFromMajorApis()
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

function normalizeResponseData(response) {
  return Array.isArray(response.data?.data) ? response.data.data : []
}

function isNotFound(error) {
  return error?.response?.status === 404
}

async function getCoursesFromMajorApis() {
  const majorsResponse = await request.get('/api/majors')
  const majors = Array.isArray(majorsResponse.data?.data) ? majorsResponse.data.data : []
  const courseGroups = await Promise.all(
    majors.map(async (major) => {
      const response = await request.get(`/api/majors/${encodeURIComponent(major.id)}/courses`)
      return Array.isArray(response.data?.data) ? response.data.data : []
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
