# user:
|field|type|constraint|remark|
|-------|------|--------|---|
|user_id|UUID,CHAR(36)|PK, NOT NULL, UNIQUE||
|username|VARCHAR(20)|UNIQUE, NOT NULL||
|password_hash|VARCHAR(60)|NOT NULL||


# todo
|field|type|constraint|remark|
|-------|------|--------|---|
|todo_id|UUID,CHAR(36)|PK, NOT NULL, UNIQUE||
|user_id|UUID,CHAR(36)|FOREIGN KEY REFERENCES user(user_id), NOT NULL||
|title|VARCHAR(20)| NOT NULL||
|description|VARCHAR(255)| ||
|priority|INT|CHECK (priority IN (0,1,2))|0: low, 1: mid, 2: high|
|status|INT|CHECK (status IN (0,1))|0: not completed, 1: completed|
|create_time|TIMESTAMP| ||
|complete_time|TIMESTAMP| ||
|update_time|TIMESTAMP| ||
|deadline|TIMESTAMP| ||
