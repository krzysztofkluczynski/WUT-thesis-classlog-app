CREATE TABLE role (
    role_id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL
);

CREATE TABLE classlog_user (
    user_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role_id BIGINT NOT NULL REFERENCES role(role_id) ON DELETE RESTRICT
);

CREATE TABLE class (
    class_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    code VARCHAR(255) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE user_class (
    class_id BIGINT REFERENCES class(class_id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES classlog_user(user_id),
    PRIMARY KEY (class_id, user_id)
);

 CREATE TABLE grade (
    grade_id BIGSERIAL PRIMARY KEY,
    class_id BIGINT REFERENCES class(class_id) NOT NULL ,
    student_id BIGINT REFERENCES classlog_user(user_id) NOT NULL ,
    teacher_id BIGINT REFERENCES classlog_user(user_id) NOT NULL ,
    grade INT NOT NULL,
    wage INT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE file (
    file_id BIGSERIAL PRIMARY KEY,
    class_id BIGINT REFERENCES class(class_id),
    user_id BIGINT REFERENCES classlog_user(user_id),
    file_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE post (
    post_id BIGSERIAL PRIMARY KEY,
    class_id BIGINT NOT NULL REFERENCES class(class_id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES classlog_user(user_id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE comment (
    comment_id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL REFERENCES post(post_id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES classlog_user(user_id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE question_type (
    question_type_id BIGSERIAL PRIMARY KEY,
    type_name VARCHAR(255) NOT NULL
);

CREATE TABLE lesson (
    lesson_id BIGSERIAL PRIMARY KEY,
    created_by BIGINT NOT NULL REFERENCES classlog_user(user_id),
    class_id BIGINT NOT NULL REFERENCES class(class_id) ON DELETE CASCADE,
    lesson_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    subject VARCHAR(255) NOT NULL,
    content TEXT NOT NULL
);



-- Recreate the TASK table
CREATE TABLE task (
    task_id BIGSERIAL PRIMARY KEY,
    created_by BIGINT REFERENCES classlog_user(user_id) ON DELETE SET NULL,
    task_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP,
    description TEXT,
    score INT
);

CREATE TABLE question (
    question_id BIGSERIAL PRIMARY KEY,
    question_type_id BIGINT REFERENCES question_type(question_type_id) ON DELETE SET NULL, 
    edited_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    points INT NOT NULL,
    content TEXT NOT NULL,
    file_id BIGINT REFERENCES file(file_id) ON DELETE SET NULL 
);


CREATE TABLE task_question (
    task_question_id BIGSERIAL PRIMARY KEY,
    task_id BIGINT REFERENCES task(task_id) ON DELETE CASCADE, 
    question_id BIGINT REFERENCES question(question_id) ON DELETE CASCADE 
);

CREATE TABLE answer (
    answer_id BIGSERIAL PRIMARY KEY,
    question_id BIGINT REFERENCES question(question_id) ON DELETE CASCADE, 
    is_correct BOOLEAN,
    content TEXT NOT NULL
);

CREATE TABLE submitted_answer (
    submitted_answer_id BIGSERIAL PRIMARY KEY,
    task_question_id BIGINT NOT NULL REFERENCES task_question(task_question_id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES classlog_user(user_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    content TEXT NOT NULL
);


CREATE TABLE user_task (
    user_task_id BIGSERIAL PRIMARY KEY, 
    task_id BIGINT NOT NULL REFERENCES task(task_id) ON DELETE CASCADE, 
    user_id BIGINT NOT NULL REFERENCES classlog_user(user_id) ON DELETE CASCADE, 
    score INT DEFAULT NULL 
);


CREATE TABLE presence (
    presence_id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES classlog_user(user_id) ON DELETE CASCADE, 
    lesson_id BIGINT NOT NULL REFERENCES lesson(lesson_id) ON DELETE CASCADE     
);