import request from './request.js'

export function getCareerById(id) {
  return request.get(`/api/careers/${encodeURIComponent(id)}`)
}

export function getCareerCourses(id) {
  return request.get(`/api/careers/${encodeURIComponent(id)}/courses`)
}

export function getCareerAbilities(id) {
  return request.get(`/api/careers/${encodeURIComponent(id)}/abilities`)
}

export function getCareerKnowledgePoints(id) {
  return request.get(`/api/careers/${encodeURIComponent(id)}/knowledge-points`)
}
