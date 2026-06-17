import request from '@/utils/request'

export function getMyStudentAffairsProfile() {
  return request({
    url: '/campus/student/profile/my',
    method: 'get'
  })
}

export function listMyStudentAffairsRecords() {
  return request({
    url: '/campus/student/records/my',
    method: 'get'
  })
}

export function getStudentAffairsOverview() {
  return request({
    url: '/campus/student/overview',
    method: 'get'
  })
}
