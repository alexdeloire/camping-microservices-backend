$filesAndDatabases = @(
    @{ File = "user.sql"; Container = "user_db"; Database = "user_db"; User = "user_user" },
    @{ File = "location.sql"; Container = "location_db"; Database = "location_db"; User = "location_user" },
    @{ File = "reservation.sql"; Container = "reservation_db"; Database = "reservation_db"; User = "reservation_user" }
)

foreach ($item in $filesAndDatabases) {
    $filePath = $item["File"]
    $containerName = $item["Container"]
    $databaseName = $item["Database"]
    $dbUser = $item["User"]

    if (!(Test-Path -Path $filePath)) {
        Write-Host "The file $filePath does not exist. Please check the path." -ForegroundColor Red
        continue
    }

    Write-Host "Copying $filePath to container $containerName..." -ForegroundColor Cyan
    docker cp $filePath "${containerName}:/tmp/$filePath"

    Write-Host "Executing $filePath in database $databaseName on container $containerName..." -ForegroundColor Cyan
    docker exec -it $containerName psql -U $dbUser -d $databaseName -f "/tmp/$filePath"

    Write-Host "Deleting $filePath from container $containerName..." -ForegroundColor Yellow
    docker exec -it $containerName rm "/tmp/$filePath"

    Write-Host "SQL commands from $filePath have been successfully executed in $databaseName." -ForegroundColor Green
}

Write-Host "All operations are complete."

