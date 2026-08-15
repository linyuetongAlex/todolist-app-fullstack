# Todo List 后端接口测试文档

## 测试环境

- Base URL: `http://localhost:8080`
- 测试工具：Postman
- 前置条件：项目已启动，数据库已连接

## 通用说明

- 除 `/api/auth/register`、`/api/auth/login` 外，其余接口均需在 Header 中携带：
  `Authorization: Bearer <登录接口返回的token>`
- 所有请求 Header 需带：`Content-Type: application/json`
- 表格中"结果"一栏测试时手动填写 通过 / 不通过

---

## 一、POST /api/auth/register 用户注册

| # | 用例描述 | 请求Body | 预期code | 结果 |
|---|---|---|---|---|
| 1 | 正常注册 | `{"username":"testuser","password":"abc12345!"}` | 0 | |
| 2 | username为空 | `{"username":"","password":"abc12345!"}` | 40001 | |
| 3 | username超过20字符 | `{"username":"aaaaaaaaaaaaaaaaaaaaa","password":"abc12345!"}` | 40002 | |
| 4 | password为空 | `{"username":"testuser2","password":""}` | 40004 | |
| 5 | password长度不合法（<8） | `{"username":"testuser2","password":"abc12!"}` | 40005 | |
| 6 | password长度不合法（>30） | `{"username":"testuser2","password":"a1!aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"}` | 40005 | |
| 7 | password不含特殊字符 | `{"username":"testuser2","password":"abcd1234"}` | 40007 | |
| 8 | password不含数字 | `{"username":"testuser2","password":"abcdefg!"}` | 40007 | |
| 9 | password不含字母 | `{"username":"testuser2","password":"12345678!"}` | 40007 | |
| 10 | username已存在（用例1注册过的testuser） | `{"username":"testuser","password":"abc12345!"}` | 40006 | |

**用例1验证点**：
- 返回 `data.user_id`、`data.username`（字段为下划线格式）
- 数据库 `user` 表新增一条记录，`password_hash` 为加密字符串，不是明文

---

## 二、POST /api/auth/login 用户登录

前置：先执行注册用例1，得到用户 `testuser` / `abc12345!`

| # | 用例描述 | 请求Body | 预期code | 结果 |
|---|---|---|---|---|
| 1 | 正常登录 | `{"username":"testuser","password":"abc12345!"}` | 0 | |
| 2 | username为空 | `{"username":"","password":"abc12345!"}` | 40001 | |
| 3 | password为空 | `{"username":"testuser","password":""}` | 40004 | |
| 4 | 密码错误 | `{"username":"testuser","password":"wrongpass1!"}` | 40010 | |
| 5 | 用户名不存在 | `{"username":"notexist","password":"abc12345!"}` | 40010 | |

**用例1验证点**：
- 返回 `data.user_id`、`data.username`、`data.token`
- 用例4、5 返回**相同的错误码和提示文案**（不应区分"用户不存在"和"密码错误"，防止账号枚举）

---

## 三、POST /api/todos 创建任务

前置：使用登录返回的 token，Header携带 `Authorization: Bearer <token>`

| # | 用例描述 | 请求Body | 预期code | 结果 |
|---|---|---|---|---|
| 1 | 正常创建（全字段） | `{"title":"买菜","description":"西红柿、鸡蛋","priority":1,"deadline":"2026-08-20T18:00:00"}` | 0 | |
| 2 | 正常创建（仅title） | `{"title":"洗车"}` | 0 | |
| 3 | title为空 | `{"title":""}` | 40001 | |
| 4 | title超过20字符 | `{"title":"aaaaaaaaaaaaaaaaaaaaa"}` | 40002 | |
| 5 | 不带Authorization header | 同用例1 | 401/403 | |
| 6 | token失效/伪造token | 同用例1，Header传一个乱写的字符串 | 401/403 | |

**用例1验证点**：
- 返回字段：`todo_id`、`title`、`description`、`priority`、`deadline`、`status`（应为0）、`create_time`、`update_time`、`complete_time`（应为null），均为下划线命名
- 数据库 `todo` 表新增记录，`user_id` 正确关联到当前登录用户

---

## 四、GET /api/todos 查询任务列表（分页）

前置：已创建多条Todo（建议至少4条，用于验证分页）

| # | 用例描述 | 请求 | 预期结果 | 结果 |
|---|---|---|---|---|
| 1 | 默认分页（不传参数） | `GET /api/todos` | code=0，page=1，pageSize=10 | |
| 2 | 指定分页 | `GET /api/todos?page=1&pageSize=2` | list长度=2 | |
| 3 | 查询第2页 | `GET /api/todos?page=2&pageSize=2` | 返回剩余数据 | |
| 4 | 数据隔离验证 | 用另一个用户B的token查询 | 只能看到B自己创建的todo，看不到用户A的 | |
| 5 | 不带Authorization | `GET /api/todos` | 401/403 | |

**验证点**：`data.total` 与实际创建总数一致

---

## 五、PATCH /api/todos/{todo_id} 更新任务

前置：使用用例三中创建的某个 `todo_id`

| # | 用例描述 | 请求Body | 预期code | 结果 |
|---|---|---|---|---|
| 1 | 仅更新title | `{"title":"买菜（已改）"}` | 0 | |
| 2 | title超过20字符 | `{"title":"aaaaaaaaaaaaaaaaaaaaa"}` | 40002 | |
| 3 | 标记完成 | `{"status":1}` | 0 | |
| 4 | 撤销完成 | `{"status":0}` | 0 | |
| 5 | body为空 | `{}` | 40003 | |
| 6 | todo_id不存在 | 路径传一个随机UUID | 40401 | |
| 7 | 更新他人的todo | 用用户B的token更新用户A的todo_id | 40401 | |
| 8 | 不带Authorization | 同用例1 | 401/403 | |

**用例3验证点**：返回的 `complete_time` 被设置为当前时间
**用例4验证点**：返回的 `complete_time` 变回 `null`

---

## 六、DELETE /api/todos/{todo_id} 删除任务

| # | 用例描述 | 请求 | 预期code | 结果 |
|---|---|---|---|---|
| 1 | 正常删除自己的任务 | `DELETE /api/todos/{存在的todo_id}` | 0 | |
| 2 | todo_id不存在 | `DELETE /api/todos/{随机UUID}` | 40401 | |
| 3 | 删除他人的任务 | 用用户B的token删除用户A的todo_id | 40401 | |
| 4 | 不带Authorization | 同用例1 | 401/403 | |

**用例1验证点**：
- 返回 `data.todo_id`
- 数据库中该条记录已被物理删除（`SELECT * FROM todo WHERE todo_id = '...'` 查不到）

---

## 已知待办 / 暂不覆盖范围

- [ ] 未登录/token过期时，当前返回的是Spring Security默认的401/403响应，尚未统一成 `{code, msg, data}` 格式（需引入 `AuthenticationEntryPoint`，属于后续优化项）
- [ ] 暂未编写自动化单元测试（JUnit），当前均为手动Postman测试