import axios from 'axios'

const request = axios.create({
  baseURL: 'http://localhost:8086',
  timeout: 10000,
})

request.interceptors.request.use((config) => {
  const token = localStorage.getItem('adminToken')
  if (token) {
    config.headers = config.headers || {}
    config.headers.Authorization = token
  }
  return config
})

request.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (!error.response) {
      error.response = { data: { message: '操作失败' } }
    } else if (!error.response.data?.message) {
      error.response.data = {
        ...(error.response.data || {}),
        message: '操作失败',
      }
    }
    return Promise.reject(error)
  },
)

export default request
