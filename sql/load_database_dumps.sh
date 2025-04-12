#!/bin/bash

# Databases and credentials
declare -A DATABASES=(
  ["location_db"]="location_user location_pass location_db"
  ["reservation_db"]="reservation_user reservation_pass reservation_db"
  ["user_db"]="user_user user_pass user_db"
)

# Directory containing the dumps
DUMP_DIR=$(pwd)

# Iterate through each database
for DB_CONTAINER in "${!DATABASES[@]}"; do
  IFS=' ' read -r DB_USER DB_PASS DB_NAME <<< "${DATABASES[$DB_CONTAINER]}"

  DUMP_FILE="${DUMP_DIR}/base_${DB_NAME}.dump"

  if [[ ! -f "$DUMP_FILE" ]]; then
    echo "Dump file $DUMP_FILE not found! Skipping $DB_NAME."
    continue
  fi

  echo "Restoring database $DB_NAME in container $DB_CONTAINER..."

  # Copy the dump file into the container
  docker cp "$DUMP_FILE" "$DB_CONTAINER:/tmp/${DB_NAME}.dump"

  # Restore the database from the dump
  docker exec "$DB_CONTAINER" sh -c "PGPASSWORD='$DB_PASS' pg_restore -U $DB_USER -d $DB_NAME -F c /tmp/${DB_NAME}.dump"

  # Remove the dump file from the container
  docker exec "$DB_CONTAINER" sh -c "rm /tmp/${DB_NAME}.dump"

  echo "Database $DB_NAME restored successfully!"
done

echo "All databases have been restored!"
