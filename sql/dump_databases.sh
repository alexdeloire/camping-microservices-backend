#!/bin/bash

# Databases and credentials
declare -A DATABASES=(
  ["location_db"]="location_user location_pass location_db"
  ["reservation_db"]="reservation_user reservation_pass reservation_db"
  ["user_db"]="user_user user_pass user_db"
)

# Output directory
OUTPUT_DIR=$(pwd)

# Iterate through each database
for DB_CONTAINER in "${!DATABASES[@]}"; do
  IFS=' ' read -r DB_USER DB_PASS DB_NAME <<< "${DATABASES[$DB_CONTAINER]}"

  echo "Creating dump for database $DB_NAME in container $DB_CONTAINER..."

  # Generate a dump inside the container
  docker exec "$DB_CONTAINER" sh -c "PGPASSWORD='$DB_PASS' pg_dump -U $DB_USER -d $DB_NAME -F c -f /tmp/${DB_NAME}.dump"

  # Copy the dump to the host machine
  docker cp "$DB_CONTAINER:/tmp/${DB_NAME}.dump" "$OUTPUT_DIR"

  # Remove the dump from the container
  docker exec "$DB_CONTAINER" sh -c "rm /tmp/${DB_NAME}.dump"

  echo "Dump for $DB_NAME saved to $OUTPUT_DIR/${DB_NAME}.dump"
done

echo "All database dumps completed successfully!"
