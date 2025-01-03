CREATE TABLE IF NOT EXISTS role (
    role_id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS classlog_user (
    user_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role_id BIGINT NOT NULL REFERENCES role(role_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS class (
    class_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    code VARCHAR(255) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_class (
    class_id BIGINT REFERENCES class(class_id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES classlog_user(user_id),
    PRIMARY KEY (class_id, user_id)
);

 CREATE TABLE IF NOT EXISTS grade (
    grade_id BIGSERIAL PRIMARY KEY,
    class_id BIGINT REFERENCES class(class_id) NOT NULL ,
    student_id BIGINT REFERENCES classlog_user(user_id) NOT NULL ,
    teacher_id BIGINT REFERENCES classlog_user(user_id) NOT NULL ,
    grade INT NOT NULL,
    wage INT NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS file (
    file_id BIGSERIAL PRIMARY KEY,
    class_id BIGINT REFERENCES class(class_id),
    user_id BIGINT REFERENCES classlog_user(user_id),
    file_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS post (
    post_id BIGSERIAL PRIMARY KEY,
    class_id BIGINT NOT NULL REFERENCES class(class_id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES classlog_user(user_id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE TABLE IF NOT EXISTS comment (
    comment_id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL REFERENCES post(post_id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES classlog_user(user_id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS question_type (
    question_type_id BIGSERIAL PRIMARY KEY,
    type_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS lesson (
    lesson_id BIGSERIAL PRIMARY KEY,
    created_by BIGINT NOT NULL REFERENCES classlog_user(user_id),
    class_id BIGINT NOT NULL REFERENCES class(class_id) ON DELETE CASCADE,
    lesson_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    subject VARCHAR(255) NOT NULL,
    content TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS task (
    task_id BIGSERIAL PRIMARY KEY,
    created_by BIGINT REFERENCES classlog_user(user_id) ON DELETE SET NULL,
    task_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    due_date TIMESTAMP,
    description TEXT,
    score INT
);

CREATE TABLE IF NOT EXISTS question (
    question_id BIGSERIAL PRIMARY KEY,
    question_type_id BIGINT REFERENCES question_type(question_type_id) ON DELETE SET NULL, 
    edited_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    points INT NOT NULL,
    content TEXT NOT NULL,
    file_id BIGINT REFERENCES file(file_id) ON DELETE SET NULL 
);


CREATE TABLE IF NOT EXISTS task_question (
    task_question_id BIGSERIAL PRIMARY KEY,
    task_id BIGINT REFERENCES task(task_id) ON DELETE CASCADE, 
    question_id BIGINT REFERENCES question(question_id) ON DELETE CASCADE 
);

CREATE TABLE IF NOT EXISTS answer (
    answer_id BIGSERIAL PRIMARY KEY,
    question_id BIGINT REFERENCES question(question_id) ON DELETE CASCADE,
    is_correct BOOLEAN,
    content TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS submitted_answer (
    submitted_answer_id BIGSERIAL PRIMARY KEY,
    task_question_id BIGINT NOT NULL REFERENCES task_question(task_question_id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES classlog_user(user_id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    content TEXT NOT NULL
);


CREATE TABLE IF NOT EXISTS user_task (
    user_task_id BIGSERIAL PRIMARY KEY, 
    task_id BIGINT NOT NULL REFERENCES task(task_id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES classlog_user(user_id) ON DELETE CASCADE, 
    score INT DEFAULT NULL
);


CREATE TABLE IF NOT EXISTS presence (
    presence_id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES classlog_user(user_id) ON DELETE CASCADE, 
    lesson_id BIGINT NOT NULL REFERENCES lesson(lesson_id) ON DELETE CASCADE     
);

CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE OR REPLACE FUNCTION generate_class_code()
RETURNS TRIGGER AS $$
BEGIN
    NEW.code := SUBSTRING(gen_random_uuid()::text, 1, 10);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER set_class_code
BEFORE INSERT ON class
FOR EACH ROW
WHEN (NEW.code IS NULL)
EXECUTE FUNCTION generate_class_code();

INSERT INTO role (role_name)
VALUES
    ('Teacher'),
    ('Student'),
    ('Admin'),
    ('Unknown')
ON CONFLICT (role_name) DO NOTHING;

INSERT INTO question_type (type_name)
VALUES
    ('Closed Question'),
    ('Open Question')
ON CONFLICT (type_name) DO NOTHING;

-- Insert or update admin user
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM classlog_user WHERE email = '${ADMIN_EMAIL}') THEN
        INSERT INTO classlog_user (name, surname, email, password, role_id)
        VALUES ('Admin', 'User', '${ADMIN_EMAIL}', '${ADMIN_PASSWORD_ENCODED}', 
                (SELECT role_id FROM role WHERE role_name = 'Admin'));
    ELSE
        UPDATE classlog_user
        SET password = '${ADMIN_PASSWORD_ENCODED}'
        WHERE email = '${ADMIN_EMAIL}';
    END IF;
END $$;
