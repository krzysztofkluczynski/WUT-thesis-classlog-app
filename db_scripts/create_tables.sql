-- Creating the ROLE table
CREATE TABLE role (
    role_id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL
);

-- Creating the CLASSLOG_USER table
CREATE TABLE classlog_user (
    user_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role_id BIGINT REFERENCES role(role_id) ON DELETE RESTRICT
);

-- Creating the CLASS table
CREATE TABLE class (
    class_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    code VARCHAR(255) UNIQUE,  -- Will be generated later
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Creating the USER_CLASS table
CREATE TABLE user_class (
    class_id BIGINT REFERENCES class(class_id) ON DELETE CASCADE, -- Cascade delete
    user_id BIGINT REFERENCES classlog_user(user_id),
    PRIMARY KEY (class_id, user_id)
);

 CREATE TABLE grade (
    grade_id BIGSERIAL PRIMARY KEY,
    class_id BIGINT REFERENCES class(class_id),
    student_id BIGINT REFERENCES classlog_user(user_id),
    teacher_id BIGINT REFERENCES classlog_user(user_id),
    grade INT,
    wage INT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Creating the FILE table
CREATE TABLE file (
    file_id BIGSERIAL PRIMARY KEY,
    class_id BIGINT REFERENCES class(class_id),
    user_id BIGINT REFERENCES classlog_user(user_id),
    file_path VARCHAR(255), -- Full file path
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Creating the POST table
CREATE TABLE post (
    post_id BIGSERIAL PRIMARY KEY,
    class_id BIGINT REFERENCES class(class_id) ON DELETE CASCADE, -- Cascade delete for class
    user_id BIGINT REFERENCES classlog_user(user_id) ON DELETE CASCADE, -- Cascade delete for user
    title VARCHAR(255),
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Creating the COMMENT table
CREATE TABLE comment (
    comment_id BIGSERIAL PRIMARY KEY,
    post_id BIGINT REFERENCES post(post_id) ON DELETE CASCADE, -- Cascade delete for post
    user_id BIGINT REFERENCES classlog_user(user_id) ON DELETE CASCADE, -- Cascade delete for user
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Creating the QUESTION_TYPE table
CREATE TABLE question_type (
    question_type_id BIGSERIAL PRIMARY KEY,
    type_name VARCHAR(255) NOT NULL
);

-- Creating the LESSON table
CREATE TABLE lesson (
    lesson_id BIGSERIAL PRIMARY KEY,
    created_by BIGINT REFERENCES classlog_user(user_id),
    class_id BIGINT REFERENCES class(class_id),
    lesson_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    subject VARCHAR(255),
    content TEXT
);


-- Recreate the TASK table
CREATE TABLE task (
    task_id BIGSERIAL PRIMARY KEY,
    created_by BIGINT REFERENCES classlog_user(user_id) ON DELETE SET NULL, -- If the user is deleted, retain tasks with null creator
    task_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP,
    description TEXT,
    score INT
);

CREATE TABLE question (
    question_id BIGSERIAL PRIMARY KEY,
    question_type_id BIGINT REFERENCES question_type(question_type_id) ON DELETE SET NULL, -- Retain questions with null type
    edited_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    points INT,
    content TEXT NOT NULL,
    file_id BIGINT REFERENCES file(file_id) ON DELETE SET NULL -- If the file is deleted, retain the question but set file_id to NULL
);


-- Recreate the TASK_QUESTION table
CREATE TABLE task_question (
    task_question_id BIGSERIAL PRIMARY KEY,
    task_id BIGINT REFERENCES task(task_id) ON DELETE CASCADE, -- If the task is deleted, remove associated task-question links
    question_id BIGINT REFERENCES question(question_id) ON DELETE CASCADE -- If the question is deleted, remove associated task-question links
);

-- Recreate the ANSWER table
CREATE TABLE answer (
    answer_id BIGSERIAL PRIMARY KEY,
    question_id BIGINT REFERENCES question(question_id) ON DELETE CASCADE, -- If the question is deleted, remove its answers
    is_correct BOOLEAN,
    content TEXT
);

-- Recreate the SUBMITTED_ANSWER table
CREATE TABLE submitted_answer (
    submitted_answer_id BIGSERIAL PRIMARY KEY,
    answer_id BIGINT REFERENCES answer(answer_id) ON DELETE CASCADE, -- If the answer is deleted, remove submitted answers
    user_id BIGINT REFERENCES classlog_user(user_id) ON DELETE CASCADE, -- If the user is deleted, remove their submissions
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    content TEXT
);

-- Recreate the TASK_USER table
CREATE TABLE user_task (
    user_task_id BIGSERIAL PRIMARY KEY, -- Surrogate primary key for unique identification
    task_id BIGINT REFERENCES task(task_id) ON DELETE CASCADE, -- If the task is deleted, remove user-task assignments
    user_id BIGINT REFERENCES classlog_user(user_id) ON DELETE CASCADE, -- If the user is deleted, remove task-user assignments
    score INT DEFAULT NULL -- Allow NULL values for score
);


-- Creating the PRESENCE table
CREATE TABLE presence (
    presence_id BIGSERIAL PRIMARY KEY,
    student_id BIGINT REFERENCES classlog_user(user_id) ON DELETE CASCADE, -- Cascade delete for user
    lesson_id BIGINT REFERENCES lesson(lesson_id) ON DELETE CASCADE        -- Cascade delete for lesson
);
