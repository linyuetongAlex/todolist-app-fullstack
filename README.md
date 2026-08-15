# Todo List Full-Stack App

A full-stack todo list application with separate frontend and backend. Users can register, log in, and create, view, edit, complete, and delete their own tasks, with support for priority levels and deadlines.

## Project Status

MVP (Minimum Viable Product) version. All P0 and P1 features from the PRD are implemented. P2 and P3 features are not implemented.

## Tech Stack

### Backend

| Technology | Purpose |
|---|---|
| Spring Boot 4.1.0 | Application framework |
| Spring Data JPA / Hibernate | ORM, database access |
| Spring Security | API authorization |
| MySQL | Database |
| JWT (jjwt) | Stateless auth/session handling |
| BCrypt | Password hashing |
| Lombok | Boilerplate reduction |
| Maven | Dependency and build management |

### Frontend

| Technology | Purpose |
|---|---|
| React 18 | UI framework |
| TypeScript | Type system |
| Vite | Build tool and dev server |
| React Router | Client-side routing |
| Native fetch | HTTP requests |
| Plain CSS (CSS variables + Flexbox) | Styling, no third-party UI library |

## Features

- User registration (username/password, password strength validation, BCrypt hashing)
- User login (JWT issuance, 7-day expiry)
- Persistent login (token stored in localStorage, survives page refresh)
- Route guards (unauthenticated users are redirected to login; expired tokens are cleared and redirect to login)
- Create tasks (title, description, priority, deadline)
- Paginated task list
- Edit tasks (click a task to open a modal, edit any field)
- Toggle task status (checkbox marks complete/incomplete, auto-sets/clears completion time)
- Delete tasks (with confirmation)
- Consistent purple, rounded-card visual design

