#!/bin/bash

declare -A filesAndDatabases=(
    [user]="user.sql user_db user_db user_user"
    [location]="location.sql location_db location_db location_user"
    [reservation]="reservation.sql reservation_db reservation_db reservation_user"
)

for key in "${!filesAndDatabases[@]}"; do

    IFS=' ' read -r filePath containerName databaseName dbUser <<< "${filesAndDatabases[$key]}"

    if [[ ! -f "$filePath" ]]; then
        echo "The file $filePath does not exist. Please check the path." >&2
        continue
    fi

    echo "Copying $filePath to container $containerName..."
    docker cp "$filePath" "${containerName}:/tmp/$filePath"

    echo "Executing $filePath in database $databaseName on container $containerName..."
    docker exec -i "$containerName" psql -U "$dbUser" -d "$databaseName" -f "/tmp/$filePath"

    echo "Deleting $filePath from container $containerName..."
    docker exec -i "$containerName" rm "/tmp/$filePath"

    echo "SQL commands from $filePath have been successfully executed in $databaseName."
done

echo "All operations are complete."
