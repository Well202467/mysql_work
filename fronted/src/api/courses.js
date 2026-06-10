import request from './request.js'

export function getCourseKnowledgePoints(courseId) {
  return request.get(`/api/courses/${encodeURIComponent(courseId)}/knowledge-points`)
}

export function getCourseCareers(courseId) {
  return request.get(`/api/courses/${encodeURIComponent(courseId)}/careers`)
}
