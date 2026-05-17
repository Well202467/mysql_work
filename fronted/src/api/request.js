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

export default request
