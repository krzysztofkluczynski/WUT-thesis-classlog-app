-- Disable foreign key checks to avoid issues during deletion
SET session_replication_role = 'replica';

-- Dropping all dependent tables
DROP TABLE IF EXISTS submitted_answer CASCADE;
DROP TABLE IF EXISTS answer CASCADE;
DROP TABLE IF EXISTS question CASCADE;
DROP TABLE IF EXISTS task_user CASCADE;
DROP TABLE IF EXISTS task CASCADE;
DROP TABLE IF EXISTS lesson CASCADE;
DROP TABLE IF EXISTS question_type CASCADE;
DROP TABLE IF EXISTS comment CASCADE;
DROP TABLE IF EXISTS post CASCADE;
DROP TABLE IF EXISTS file CASCADE;
DROP TABLE IF EXISTS grade CASCADE;
DROP TABLE IF EXISTS presence CASCADE;
DROP TABLE IF EXISTS user_class CASCADE;
DROP TABLE IF EXISTS class CASCADE;
DROP TABLE IF EXISTS classlog_user CASCADE;
DROP TABLE IF EXISTS role CASCADE;

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