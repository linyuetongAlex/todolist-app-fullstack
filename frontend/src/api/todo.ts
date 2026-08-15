import { apiRequest } from './request'

export interface Todo {
  todo_id: string
  title: string
  description: string | null
  priority: number | null
  status: number
  deadline: string | null
  create_time: string
  update_time: string
  complete_time: string | null
}

interface PageResponse<T> {
  list: T[]
  total: number
  page: number
  pageSize: number
}

export function getTodoList(page: number, pageSize: number): Promise<PageResponse<Todo>> {
  return apiRequest<PageResponse<Todo>>(`/api/todos?page=${page}&pageSize=${pageSize}`, {
    method: 'GET',
  })
}

interface TodoRequest {
  title: string
  description?: string
  priority?: number
  deadline?: string
}

export function createTodo(data: TodoRequest): Promise<Todo> {
  return apiRequest<Todo>('/api/todos', {
    method: 'POST',
    body: JSON.stringify(data),
  })
}

export function updateTodoStatus(todoId: string, status: number): Promise<Todo> {
  return apiRequest<Todo>(`/api/todos/${todoId}`, {
    method: 'PATCH',
    body: JSON.stringify({ status }),
  })
}

export function deleteTodo(todoId: string): Promise<{ todo_id: string }> {
  return apiRequest<{ todo_id: string }>(`/api/todos/${todoId}`, {
    method: 'DELETE',
  })
}

interface UpdateTodoData {
  title?: string
  description?: string
  priority?: number
  deadline?: string
  status?: number
}

export function updateTodo(todoId: string, data: UpdateTodoData): Promise<Todo> {
  return apiRequest<Todo>(`/api/todos/${todoId}`, {
    method: 'PATCH',
    body: JSON.stringify(data),
  })
}