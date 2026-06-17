import request from '@/utils/request'

export function listPendingPaymentItems() {
  return request({
    url: '/campus/payment/items/pending',
    method: 'get'
  })
}

export function listPaymentRecords() {
  return request({
    url: '/campus/payment/records/my',
    method: 'get'
  })
}

export function demoPayPaymentItem(itemId) {
  return request({
    url: '/campus/payment/items/' + itemId + '/demo-pay',
    method: 'post'
  })
}
