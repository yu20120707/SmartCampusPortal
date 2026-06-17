import request from '@/utils/request'

export function getCardAccount() {
  return request({
    url: '/campus/card/account',
    method: 'get'
  })
}

export function listCardTransactions() {
  return request({
    url: '/campus/card/transactions/my',
    method: 'get'
  })
}

export function rechargeCard(data) {
  return request({
    url: '/campus/card/recharge',
    method: 'post',
    data: data
  })
}
