# Databases and credentials
$DATABASES = @{
    "location_db" = "location_user location_pass location_db"
    "reservation_db" = "reservation_user reservation_pass reservation_db"
    "user_db" = "user_user user_pass user_db"
}

# Directory containing the dumps
$DUMP_DIR = Get-Location

# Iterate through each database
foreach ($DB_CONTAINER in $DATABASES.Keys) {
    $dbInfo = $DATABASES[$DB_CONTAINER] -split ' '
    $DB_USER = $dbInfo[0]
    $DB_PASS = $dbInfo[1]
    $DB_NAME = $dbInfo[2]

    $DUMP_FILE = Join-Path $DUMP_DIR "base_${DB_NAME}.dump"

    if (-not (Test-Path $DUMP_FILE)) {
        Write-Host "Dump file $DUMP_FILE not found! Skipping $DB_NAME."
        continue
    }

    Write-Host "Restoring database $DB_NAME in container $DB_CONTAINER..."

    # Copy the dump file into the container
    docker cp $DUMP_FILE "${DB_CONTAINER}:/tmp/${DB_NAME}.dump"

    # Restore the database from the dump
    docker exec $DB_CONTAINER sh -c "PGPASSWORD='$DB_PASS' pg_restore -U $DB_USER -d $DB_NAME -F c /tmp/${DB_NAME}.dump"

    # Remove the dump file from the container
    docker exec $DB_CONTAINER sh -c "rm /tmp/${DB_NAME}.dump"

    Write-Host "Database $DB_NAME restored successfully!"
}

Write-Host "All databases have been restored!"
