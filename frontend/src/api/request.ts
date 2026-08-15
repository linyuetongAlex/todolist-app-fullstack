const BASE_URL = 'http://localhost:8080'

interface ApiResponse<T> {
  code: number
  msg: string
  data: T
}

export async function apiRequest<T>(
  path: string,
  options: RequestInit = {}
): Promise<T> {
  const token = localStorage.getItem('token')

  const response = await fetch(BASE_URL + path, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...options.headers,
    },
  })

  if (response.status === 401 || response.status === 403) {
    localStorage.removeItem('token')
    window.location.href = '/login'
    throw new Error('登录已失效，请重新登录')
  }

  const result: ApiResponse<T> = await response.json()

  if (result.code !== 0) {
    throw new Error(result.msg)
  }

  return result.data
}