import request from '@/utils/request'

export function getCurrentPortal() {
  return request({
    url: '/campus/portal/current',
    method: 'get'
  })
}
