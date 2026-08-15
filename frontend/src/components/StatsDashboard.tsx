import { useState, useEffect } from 'react'
import { BarChart, Bar, LineChart, Line, XAxis, YAxis, Tooltip, ResponsiveContainer } from 'recharts'
import { getDailyStats, getWeeklyStats, getMonthlyStats, type DailyPoint } from '../api/stats'

function StatsDashboard() {
  const [error, setError] = useState('')
  const [daily, setDaily] = useState(0)
  const [weekly, setWeekly] = useState<DailyPoint[]>([])
  const [monthly, setMonthly] = useState<DailyPoint[]>([])

  useEffect(() => {
    async function fetchStats() {
        try {
        const [dailyData, weeklyData, monthlyData] = await Promise.all([
            getDailyStats(),
            getWeeklyStats(),
            getMonthlyStats(),
        ])
        setDaily(dailyData)
        setWeekly(weeklyData)
        setMonthly(monthlyData)
        } catch (err) {
        setError((err as Error).message)
        }
    }
    fetchStats()
}, [])

  return (
    <div>
        <div className="stats-dashboard">
            <h1>Task Complete Today: {daily}</h1>
            <ResponsiveContainer width="100%" height={200}>
                <BarChart data={weekly}>
                    <XAxis dataKey="date" />
                    <YAxis allowDecimals={false} />
                    <Tooltip />
                    <Bar dataKey="count" fill="#7c3aed" />
                </BarChart>
            </ResponsiveContainer>
            <ResponsiveContainer width="100%" height={200}>
                <LineChart data={monthly}>
                    <XAxis dataKey="date" />
                    <YAxis allowDecimals={false} />
                    <Tooltip />
                    <Line type="monotone" dataKey="count" stroke="#7c3aed" strokeWidth={2} />
                </LineChart>
            </ResponsiveContainer>
            {error && <p className="text-red-500">{error}</p>}
        </div>
    </div>
  )
}

export default StatsDashboard