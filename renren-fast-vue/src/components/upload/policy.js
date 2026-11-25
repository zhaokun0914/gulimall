import http from '@/utils/httpRequest'

/**
 * 获取阿里云 OSS 直传签名
 * 网关路由：/api/thirdparty/** -> gulimall-third-party，重写后请求 /oss/policy
 * 返回：{ accessId, policy, signature, dir, host, expire }
 */
export function policy () {
  return http({
    url: http.adornUrl('/thirdparty/oss/policy'),
    method: 'get',
    params: http.adornParams({})
  }).then(({ data }) => data)
}
