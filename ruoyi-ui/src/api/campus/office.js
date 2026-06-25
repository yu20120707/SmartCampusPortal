import request from '@/utils/request'

export function listMyApplications() {
  return request({
    url: '/campus/office/applications/my',
    method: 'get'
  })
}

export function addApplication(data) {
  return request({
    url: '/campus/office/applications',
    method: 'post',
    data: data
  })
}

export function listTodoApplications() {
  return request({
    url: '/campus/office/applications/todo',
    method: 'get'
  })
}

export function approveApplication(applicationId, data) {
  return request({
    url: '/campus/office/applications/' + applicationId + '/approve',
    method: 'put',
    data: data
  })
}

export function rejectApplication(applicationId, data) {
  return request({
    url: '/campus/office/applications/' + applicationId + '/reject',
    method: 'put',
    data: data
  })
}

export function getLeaders() {
  return request({
    url: '/campus/office/applications/leaders',
    method: 'get'
  })
}
