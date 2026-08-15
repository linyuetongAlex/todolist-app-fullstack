import { apiRequest } from './request'

export interface DailyPoint {
  date: string
  count: number
}

export function getDailyStats(): Promise<number> {
  return apiRequest<number>('/api/todos/stats/daily', { method: 'GET' })
}

export function getWeeklyStats(): Promise<DailyPoint[]> {
  return apiRequest<DailyPoint[]>('/api/todos/stats/weekly', { method: 'GET' })
}

export function getMonthlyStats(): Promise<DailyPoint[]> {
  return apiRequest<DailyPoint[]>('/api/todos/stats/monthly', { method: 'GET' })
}