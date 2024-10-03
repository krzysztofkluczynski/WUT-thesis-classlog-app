-- Disable foreign key checks to avoid issues during deletion
SET session_replication_role = 'replica';

-- Dropping all dependent tables
DROP TABLE IF EXISTS submitted_answer;
DROP TABLE IF EXISTS answer;
DROP TABLE IF EXISTS question;
DROP TABLE IF EXISTS task_user;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS lesson;
DROP TABLE IF EXISTS question_type;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS file;
DROP TABLE IF EXISTS grade;
DROP TABLE IF EXISTS presence;
DROP TABLE IF EXISTS user_class;
DROP TABLE IF EXISTS class;
DROP TABLE IF EXISTS classlog_user;
DROP TABLE IF EXISTS role;

-- Re-enable foreign key checks
SET session_replication_role = 'origin';

-- Resetting all sequences
DO $$
DECLARE
    seq RECORD;
BEGIN
    -- Loop over all sequences and reset them
    FOR seq IN
        SELECT sequence_name FROM information_schema.sequences
        WHERE sequence_schema = 'public'
    LOOP
        EXECUTE 'ALTER SEQUENCE ' || seq.sequence_name || ' RESTART WITH 1';
    END LOOP;
END $$;