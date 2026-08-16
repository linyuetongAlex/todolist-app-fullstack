import { useState, useEffect } from 'react'
import { getTodoList, createTodo, updateTodoStatus, deleteTodo, type Todo, updateTodo } from '../api/todo'
import '../TodoList.css'
import StatsDashboard from '../components/StatsDashboard'

function TodoList() {
  const [todos, setTodos] = useState<Todo[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [page, setPage] = useState(1)
  const [total, setTotal] = useState(0)
  const pageSize = 10
  const [newDescription, setNewDescription] = useState('')
  const [newPriority, setNewPriority] = useState(0)
  const [newDeadline, setNewDeadline] = useState('')

  const [newTitle, setNewTitle] = useState('')
  const [createError, setCreateError] = useState('')

  const [selectedTodo, setSelectedTodo] = useState<Todo | null>(null)

  // 编辑相关
  const [editTitle, setEditTitle] = useState('')
  const [editDescription, setEditDescription] = useState('')
  const [editPriority, setEditPriority] = useState(0)
  const [editDeadline, setEditDeadline] = useState('')
  const [editError, setEditError] = useState('')

  const [sortBy, setSortBy] = useState<'default' | 'time' | 'priority'>('default')

  async function fetchTodos() {
    setLoading(true)
    try {
      const result = await getTodoList(page, pageSize)
      setTodos(result.list)
      setTotal(result.total)
    } catch (err) {
      setError((err as Error).message)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchTodos()
  }, [page])

  async function handleCreate(e: React.FormEvent) {
    e.preventDefault()
    setCreateError('')
    try {
      await createTodo({ title: newTitle, description:newDescription,priority: newPriority, deadline: newDeadline ? newDeadline + ':00' : undefined })
      setNewTitle('')
      setNewDescription('')
      setNewPriority(0)
      setNewDeadline('')
      await fetchTodos()
    } catch (err) {
      setCreateError((err as Error).message)
    }
  }
  async function handleToggleStatus(todo: Todo) {
    const newStatus = todo.status === 1 ? 0 : 1
    try {
      await updateTodoStatus(todo.todo_id, newStatus)
      await fetchTodos()
    } catch (err) {
      setError((err as Error).message)
    }
  }

  async function handleDelete(todoId: string) {
    if (!window.confirm('Do you want to delete this task? This action cannot be undone.')) return
    try {
      await deleteTodo(todoId)
      await fetchTodos()
    } catch (err) {
      setError((err as Error).message)
    }
  }

  // 点击任务时，除了setSelectedTodo，还要把这几个编辑state初始化成这个todo的当前值
  function handleSelectTodo(todo: Todo) {
    setSelectedTodo(todo)
    setEditTitle(todo.title)
    setEditDescription(todo.description || '')
    setEditPriority(todo.priority ?? 0)
    setEditDeadline(todo.deadline ? todo.deadline.slice(0, 16) : '')  // 去掉秒数，倒过来适配datetime-local输入框的格式
  }

  // 保存编辑
  async function handleSaveEdit() {
    if (!selectedTodo) return
    try {
      await updateTodo(selectedTodo.todo_id, {
        title: editTitle,
        description: editDescription,
        priority: editPriority,
        deadline: editDeadline ? editDeadline + ':00' : undefined,
      })
      setSelectedTodo(null)
      await fetchTodos()
    } catch (err) {
      setEditError((err as Error).message)
    }
  }


  if (loading) return <p>Loading...</p>
  if (error) return <p style={{ color: 'red' }}>{error}</p>

  const totalPages = Math.ceil(total / pageSize)
  const priorityLabel = (p: number | null) => {
  const value = p ?? 0
  return value === 0 ? 'Low' : value === 1 ? 'Medium' : 'High'
}
  
  const sortedTodos = sortBy === 'default'
    ? todos
    : [...todos].sort((a, b) => {
        if (a.status !== b.status) {
          return a.status - b.status
        }
        if (sortBy === 'priority') {
          return (b.priority ?? 0) - (a.priority ?? 0)
        } else {
          return a.create_time.localeCompare(b.create_time)
        }
    })

  function isOverdue(todo: Todo): boolean {
    if (!todo.deadline || todo.status === 1) return false
    return new Date(todo.deadline) < new Date()
}

  return (
    <div className="todo-page">
      <h1 style={{ textAlign: 'center' }}>My Todos</h1>
      <StatsDashboard />
      <form className="create-form" onSubmit={handleCreate}>
        <input
          value={newTitle}
          onChange={(e) => setNewTitle(e.target.value)}
          placeholder="New Title:"
        />
        <select value={newPriority} onChange={(e) => setNewPriority(Number(e.target.value))}>
          <option value="0">Low</option>
          <option value="1">Medium</option>
          <option value="2">High</option>
        </select>
        <input type="datetime-local" value={newDeadline} onChange={(e) => setNewDeadline(e.target.value)} />
        <textarea
          value={newDescription}
          onChange={(e) => setNewDescription(e.target.value)}
          placeholder="Task Description"
        />
        <button type="submit" className="btn-primary" style={{ width: 'auto', padding: '10px 20px' }}>
            Add
        </button>
        {createError && <p className="error-text">{createError}</p>}
      </form>

      <select className="sort-select" value={sortBy} onChange={(e) => setSortBy(e.target.value as 'default' | 'time' | 'priority')}>
        <option value="default">Default</option>
        <option value="time">By Create Time</option>
        <option value="priority">By Priority</option>
      </select>
      
      <ul className="todo-list">
        {sortedTodos.map((todo) => (
          <li key={todo.todo_id} className={`todo-card ${isOverdue(todo) ? 'overdue' : ''}`} onClick={() => handleSelectTodo(todo)}>
            <input
              type="checkbox"
              className="todo-checkbox"
              checked={todo.status === 1}
              onChange={(e) => {
                e.stopPropagation()
                handleToggleStatus(todo)
              }}
              onClick={(e) => e.stopPropagation()}
            />
            <div className="todo-content">
              <div className="todo-title-row">
                <span className={`todo-title ${todo.status === 1 ? 'completed' : ''}`}>
                  {todo.title}
                </span>
                <span className={`priority-badge priority-${todo.priority ?? 0}`}>
                  {priorityLabel(todo.priority)}
                </span>
              </div>
              {todo.description && <div className="todo-description">{todo.description}</div>}
              <div className="todo-deadline">
                {todo.deadline ? new Date(todo.deadline).toLocaleString() : '无截止时间'}
              </div>
            </div>
            <div className="todo-actions">
              <button
                className="btn-icon"
                onClick={(e) => {
                  e.stopPropagation()
                  handleDelete(todo.todo_id)
                }}
              >
                🗑
              </button>
            </div>
          </li>
        ))}
      </ul>

      <div className="pagination">
        <button disabled={page <= 1} onClick={() => setPage(page - 1)}>
          Previous Page
        </button>
        <span>
          Page {page} of {totalPages || 1}
        </span>
        <button disabled={page >= totalPages} onClick={() => setPage(page + 1)}>
          Next Page
        </button>
      </div>

      {selectedTodo && (
        <div className="modal-overlay" onClick={() => setSelectedTodo(null)}>
          <div className="modal-card" onClick={(e) => e.stopPropagation()}>
            <input value={editTitle} onChange={(e) => setEditTitle(e.target.value)} placeholder="标题" />
            <select value={editPriority} onChange={(e) => setEditPriority(Number(e.target.value))}>
              <option value="0">Low</option>
              <option value="1">Medium</option>
              <option value="2">High</option>
            </select>
            <input
              type="datetime-local"
              value={editDeadline}
              onChange={(e) => setEditDeadline(e.target.value)}
            />
            <textarea
              value={editDescription}
              onChange={(e) => setEditDescription(e.target.value)}
              placeholder="Task Description"
            />
            {editError && <p className="error-text">{editError}</p>}
            <div className="modal-actions">
              <button className="btn-primary" onClick={handleSaveEdit}>
                Save
              </button>
              <button className="btn-secondary" onClick={() => setSelectedTodo(null)}>
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default TodoList