#!/bin/bash
set -e

# Get the IP address of the psql-db container
PSQL_DB_IP=$(docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' psql-db)

# Wait for PostgreSQL to be ready
until pg_isready -h $PSQL_DB_IP -p 5432 -U artemisadmin; do
   echo "Waiting for PostgreSQL to become available..."
   sleep 2
done

# Restore the database using psql
psql -h $PSQL_DB_IP -U artemisadmin -d agendaCraft < /docker-entrypoint-initdb.d/agendaCraft.sql

echo "Database restoration complete."