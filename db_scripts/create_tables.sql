-- Creating the ROLE table
CREATE TABLE role (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL
);

-- Creating the CLASSLOG_USER table
CREATE TABLE classlog_user (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    role_id INT REFERENCES role(role_id)
);

-- Creating the CLASS table
CREATE TABLE class (
    class_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    code VARCHAR(255) UNIQUE,  -- Will be generated later
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Creating the USER_CLASS table
CREATE TABLE user_class (
    class_id INT REFERENCES class(class_id),
    user_id INT REFERENCES classlog_user(user_id),
    PRIMARY KEY (class_id, user_id)
);

-- Creating the GRADE table
CREATE TABLE grade (
    grade_id SERIAL PRIMARY KEY,
    class_id INT REFERENCES class(class_id),
    student_id INT REFERENCES classlog_user(user_id),
    teacher_id INT REFERENCES classlog_user(user_id),
    grade VARCHAR(10),
    wage INT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Creating the FILE table
CREATE TABLE file (
    file_id SERIAL PRIMARY KEY,
    class_id INT REFERENCES class(class_id),
    user_id INT REFERENCES classlog_user(user_id),
    filename VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Creating the POST table
CREATE TABLE post (
    post_id SERIAL PRIMARY KEY,
    class_id INT REFERENCES class(class_id),
    user_id INT REFERENCES classlog_user(user_id),
    title VARCHAR(255),
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Creating the COMMENT table
CREATE TABLE comment (
    comment_id SERIAL PRIMARY KEY,
    post_id INT REFERENCES post(post_id),
    user_id INT REFERENCES classlog_user(user_id),
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Creating the QUESTION_TYPE table
CREATE TABLE question_type (
    question_type_id SERIAL PRIMARY KEY,
    type_name VARCHAR(255) NOT NULL
);

-- Creating the LESSON table
CREATE TABLE lesson (
    lesson_id SERIAL PRIMARY KEY,
    created_by INT REFERENCES classlog_user(user_id),
    class_id INT REFERENCES class(class_id),
    lesson_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    subject VARCHAR(255),
    content TEXT
);


-- Creating the TASK table
CREATE TABLE task (
    task_id SERIAL PRIMARY KEY,
    created_by INT REFERENCES classlog_user(user_id),
    lesson_id INT REFERENCES lesson(lesson_id),
    task_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP,
    description TEXT
);

-- Creating the QUESTION table
CREATE TABLE question (
    question_id SERIAL PRIMARY KEY,
    task_id INT REFERENCES task(task_id),
    question_type_id INT REFERENCES question_type(question_type_id),
    edited_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    filename VARCHAR(255),
    points INT,
    content TEXT NOT NULL
);

-- Creating the ANSWER table
CREATE TABLE answer (
    answer_id SERIAL PRIMARY KEY,
    question_id INT REFERENCES question(question_id),
    is_correct BOOLEAN,
    content TEXT
);

-- Creating the SUBMITTED_ANSWER table
CREATE TABLE submitted_answer (
    submitted_answer_id SERIAL PRIMARY KEY,
    answer_id INT REFERENCES answer(answer_id),
    user_id INT REFERENCES classlog_user(user_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    content TEXT
);


-- Creating the TASK_USER table
CREATE TABLE task_user (
    task_id INT REFERENCES task(task_id),
    user_id INT REFERENCES classlog_user(user_id),
    PRIMARY KEY (task_id, user_id)
);

-- Creating the PRESENCE table
CREATE TABLE presence (
    presence_id SERIAL PRIMARY KEY,
    student_id INT REFERENCES classlog_user(user_id),
    lesson_id INT REFERENCES lesson(lesson_id)
);
