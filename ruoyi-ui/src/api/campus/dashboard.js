import request from '@/utils/request'

export function getLeaderDashboard() {
  return request({
    url: '/campus/dashboard/leader',
    method: 'get'
  })
}
