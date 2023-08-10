#!/bin/bash
set -e

# Wait for PostgreSQL to be ready
until pg_isready -h psql-db -p 5432 -U artemisadmin; do
   echo "Waiting for PostgreSQL to become available..."
   sleep 2
done

# Restore the database using psql
psql -h psql-db -U artemisadmin -d agendaCraft < /docker-entrypoint-initdb.d/agendaCraft.sql

echo "Database restoration complete."