import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { login } from '../api/auth'
import '../App.css'

function Login() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault()
    setError('')

    try {
      const result = await login(username, password)
      localStorage.setItem('token', result.token)
      navigate('/todos')
    } catch (err) {
      setError((err as Error).message)
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
          <h1>Login</h1>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Username:</label>
              <input
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </div>
            <div className="form-group">
              <label>Password:</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </div>
            {error && <p className="error-text">{error}</p>}
            <button type="submit" className="btn-primary">登录</button>
          </form>
          <p className="auth-footer">
            No account? <Link to="/register">Register</Link>
          </p>
        </div>
      </div>
  )
}

export default Login