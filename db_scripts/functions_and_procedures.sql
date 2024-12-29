CREATE OR REPLACE FUNCTION generate_class_code()
RETURNS TRIGGER AS $$
BEGIN
    NEW.code := SUBSTRING(MD5(random()::text), 1, 10);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_class_code
BEFORE INSERT ON class
FOR EACH ROW
WHEN (NEW.code IS NULL)
EXECUTE FUNCTION generate_class_code();