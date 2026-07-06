$timestamp = Get-Date -Format "yyyyMMddHHmmss"
$description = Read-Host "Migration name"

New-Item "src/main/resources/db/migrations/V${timestamp}__${description}.sql"