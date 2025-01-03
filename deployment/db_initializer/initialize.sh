#!/bin/bash

set -e

# Wait for the database to be ready
echo "Waiting for the database to be ready..."
until pg_isready -h db -p 5432 -U "$POSTGRES_USER"; do
  sleep 1
done

# Encode the admin password
echo "Encoding admin password..."
ENCODED_PASSWORD=$(python3 /scripts/password_encoder.py "$ADMIN_PASSWORD")

if [ -z "$ENCODED_PASSWORD" ]; then
  echo "Failed to encode the password!"
  exit 1
fi

# Replace placeholders in the SQL template
echo "Preparing SQL script..."
sed "s|\${ADMIN_PASSWORD_ENCODED}|$ENCODED_PASSWORD|g; s|\${ADMIN_EMAIL}|$ADMIN_EMAIL|g" /scripts/init_db.sql > /scripts/init.sql

# Execute the SQL script
echo "Initializing the database..."
PGPASSWORD="$POSTGRES_PASSWORD" psql -h db -U "$POSTGRES_USER" -d "$POSTGRES_DB" -f /scripts/init.sql

echo "Database initialized successfully!"
