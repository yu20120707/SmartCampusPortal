import request from '@/utils/request'

export function getStudentProfile() {
  return request({
    url: '/campus/academic/profile/student',
    method: 'get'
  })
}

export function listMyCourses() {
  return request({
    url: '/campus/academic/courses/my',
    method: 'get'
  })
}

export function listMySchedule() {
  return request({
    url: '/campus/academic/schedule/my',
    method: 'get'
  })
}

export function listMyGrades() {
  return request({
    url: '/campus/academic/grades/my',
    method: 'get'
  })
}

export function listMyExams() {
  return request({
    url: '/campus/academic/exams/my',
    method: 'get'
  })
}

export function listAvailableElectives() {
  return request({
    url: '/campus/academic/electives/available',
    method: 'get'
  })
}

export function enrollElective(sectionId) {
  return request({
    url: `/campus/academic/electives/${sectionId}/enroll`,
    method: 'post'
  })
}

export function dropElective(sectionId) {
  return request({
    url: `/campus/academic/electives/${sectionId}`,
    method: 'delete'
  })
}

export function getTeacherProfile() {
  return request({
    url: '/campus/academic/teacher/profile',
    method: 'get'
  })
}

export function listTeachingCourses() {
  return request({
    url: '/campus/academic/teacher/courses',
    method: 'get'
  })
}

export function listTeachingSchedule() {
  return request({
    url: '/campus/academic/teacher/schedule',
    method: 'get'
  })
}

export function listTeachingExams() {
  return request({
    url: '/campus/academic/teacher/exams',
    method: 'get'
  })
}

export function listTeachingScores(sectionId) {
  return request({
    url: `/campus/academic/teacher/sections/${sectionId}/scores`,
    method: 'get'
  })
}

export function saveTeachingScore(sectionId, studentId, data) {
  return request({
    url: `/campus/academic/teacher/sections/${sectionId}/students/${studentId}/score`,
    method: 'put',
    data
  })
}
