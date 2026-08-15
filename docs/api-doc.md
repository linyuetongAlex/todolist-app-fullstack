## POST /api/todos
### 1. api description    
create a todo task

### 2. request url  
`{apiAddress}/api/todos`  

### 3. request method  
**POST**  

### 4. request parameters  
#### 4.1 Header parameters  

| Parameter name| Required | Type      | description         |
| ------------ | ---- | ---------------- | ------------ |
| Content-Type | Yes   | application/json | 请求体的格式 |
| Authorization | Yes   | string | Bearer {token},登录后获取的 JWT 令牌 |
#### 4.2 Body parameters  

| Parameter name    | Required | Type   | constraint      | description |
| --------- | ---- | ------ | --------------- | -------- |
| title   | yes   | string | length: 1-20 | task title |
| description  | no   | string | length: 0-255 | task description |
| priority | no   | int |  | task priority |
| deadline | no   | timestamp |  | task deadline |
   
### 5. response example  
```json
{
    "code": 0,
    "msg": "success",
    "data": {
        "title": "...",
        "description": "...",
        "priority": 0,
        "deadline": "...",
        "todo_id": "...",
        "status": 0,
        "create_time": "...",
        "update_time": "...",
        "complete_time": null
    }
}
```
```json
{
    "code": 40001,
    "msg": "title不能为空",
    "data": null
}
```
### 6. remark  
- code=0: 创建成功
- code=40001: title 为空
- code=40002: title 超过20字符
- code=40101: 未登录或 token 失效


## GET /api/todos
### 1. api description   
get todo task lists

### 2. request url  
`{apiAddress}/api/todos` 

### 3. request method  
**GET**  

### 4. request parameters  
#### 4.1 Header parameters  
| Parameter name| Required | Type      | description         |
| ------------ | ---- | ---------------- | ------------ |
| Content-Type | Yes   | application/json | 请求体的格式 |
| Authorization | Yes   | string | Bearer {token},登录后获取的 JWT 令牌 |  
#### 4.2 Query parameters
| Parameter name | Required | Type | default | description |
|---|---|---|---|---|
| page | no | int | 1 | 页码,从1开始 |
| pageSize | no | int | 10 | 每页条数 |


### 5. response example  
```json
{
    "code": 0,
    "msg": "success",
    "data": {
        "list": [
            { "todo_id": "...", "title": "...", ... },
            { "todo_id": "...", "title": "...", ... }
        ],
        "total": 42,
        "page": 1,
        "pageSize": 10
}
}
```

### 6. remark  
- code=0: 获取成功
- code=40101: 未登录或 token 失效


## PATCH /api/todos/{todo_id}
### 1. api description    
update a todo task

### 2. request url  
`{apiAddress}/api/todos/{todo_id}`  

### 3. request method  
**PATCH**  

### 4. request parameters  
#### 4.1 Header parameters  
| Parameter name| Required | Type      | description         |
| ------------ | ---- | ---------------- | ------------ |
| Content-Type | Yes   | application/json | 请求体的格式 |
| Authorization | Yes   | string | Bearer {token},登录后获取的 JWT 令牌 |

#### 4.2 Body parameters  
| Parameter name    | Required | Type   | constraint      | description |
| --------- | ---- | ------ | --------------- | -------- |
| title   | no   | string | length: 1-20 | task title |
| description  | no   | string | length: 0-255 | task description |
| priority | no   | int |  | task priority |
| deadline | no   | timestamp |  | task deadline |
| status | no   | int | CHECK (status IN (0,1)) | task status |
   
### 5. response example  
```json
{
    "code": 0,
    "msg": "success",
    "data": {
        "todo_id": "...",
        "title": "...",
        "description": "...",
        "deadline": "...",
        "priority": 0,
        "status": 0,
        "create_time": "...",
        "update_time": "...",
        "complete_time": "..." //complete time的逻辑怎么决定
    }
}
```

### 6. remark  
- code=0: 更新成功
- code=40001: title 为空
- code=40002: title 超过20字符
- code=40003: body 为空,没有任何字段被更新
- code=40101: 未登录或 token 失效
- code=40401: todo_id 不存在或无权限访问
- code=40003:body 为空,没有任何字段被更新



## DELETE /api/todos/{todo_id}
### 1. api description   
delte a todo task 

### 2. request url  
`{apiAddress}/api/todos/{todo_id}` 

### 3. request method  
**DELETE**  

### 4. request parameters  
#### 4.1 Header parameters  
| Parameter name| Required | Type      | description         |
| ------------ | ---- | ---------------- | ------------ |
| Content-Type | Yes   | application/json | 请求体的格式 |
| Authorization | Yes   | string | Bearer {token},登录后获取的 JWT 令牌 |  

### 5. response example  
```json
{
    "code": 0,
    "msg": "success",
    "data": {
        "todo_id": "..."
    }
}
```

### 6. remark  
- code=0: 删除成功
- code=40101: 未登录或 token 失效
- code=40401: todo_id 不存在或无权限访问

## POST /api/auth/register
### 1. api description    
user register

### 2. request url  
`{apiAddress}/api/auth/register`  

### 3. request method  
**POST**  

### 4. request parameters  
#### 4.1 Header parameters  
| Parameter name| Required | Type      | description         |
| ------------ | ---- | ---------------- | ------------ |
| Content-Type | Yes   | application/json | 请求体的格式 |
#### 4.2 Body parameters  
| Parameter name    | Required | Type   | constraint      | description |
| --------- | ---- | ------ | --------------- | -------- |
| username   | yes   | string | length: 1-20 | username |
| password  | yes   | string | length: 8-30 | password |
   
### 5. response example  
```json
{
    "code": 0,
    "msg": "success",
    "data": {
        "user_id": "...",
        "username": "..."
    }
}
```
### 6. remark  
- code=0: 注册成功
- code=40001: username 为空
- code=40002: username 超过20字符
- code=40004: password 为空
- code=40005: password 长度不合法
- code=40006: username 已存在
- code=40007: password 不不符合规则（必须包含字母、数字和特殊字符）


## POST /api/auth/login
### 1. api description    
user login

### 2. request url  
`{apiAddress}/api/auth/login`  

### 3. request method  
**POST**  

### 4. request parameters  
#### 4.1 Header parameters  
| Parameter name| Required | Type      | description         |
| ------------ | ---- | ---------------- | ------------ |
| Content-Type | Yes   | application/json | 请求体的格式 |
#### 4.2 Body parameters  
| Parameter name    | Required | Type   | constraint      | description |
| --------- | ---- | ------ | --------------- | -------- |
| username   | yes   | string | length: 1-20 | username |
| password  | yes   | string | length: 8-30 | password |
   
### 5. response example  
```json
{
    "code": 0,
    "msg": "success",
    "data": {
        "user_id": "...",
        "username": "...",
        "token": "..." //用户登录成功后,后端要发一个JWT token给前端,前端之后每次请求把这个token放进Authorization header里
    }
}
```
### 6. remark  
- code=0: 登录成功
- code=40001: username 为空
- code=40004: password 为空
- code=40010: 用户名或密码错误
