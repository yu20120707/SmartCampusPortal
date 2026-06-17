import request from '@/utils/request'

export function listAvailableAssets() {
  return request({
    url: '/campus/asset/assets/available',
    method: 'get'
  })
}

export function listMyAssetBorrows() {
  return request({
    url: '/campus/asset/borrows/my',
    method: 'get'
  })
}

export function applyAssetBorrow(data) {
  return request({
    url: '/campus/asset/borrows',
    method: 'post',
    data: data
  })
}

export function listTodoAssetBorrows() {
  return request({
    url: '/campus/asset/borrows/todo',
    method: 'get'
  })
}

export function approveAssetBorrow(borrowId, data) {
  return request({
    url: '/campus/asset/borrows/' + borrowId + '/approve',
    method: 'put',
    data: data
  })
}

export function rejectAssetBorrow(borrowId, data) {
  return request({
    url: '/campus/asset/borrows/' + borrowId + '/reject',
    method: 'put',
    data: data
  })
}
