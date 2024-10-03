CREATE OR REPLACE FUNCTION generate_class_code()
RETURNS TRIGGER AS $$
BEGIN
    -- Generate a random 10-character code and assign it to the "code" field
    NEW.code := SUBSTRING(MD5(random()::text), 1, 10);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_class_code
BEFORE INSERT ON class
FOR EACH ROW
WHEN (NEW.code IS NULL)  -- Only generate if the code is not provided
EXECUTE FUNCTION generate_class_code();