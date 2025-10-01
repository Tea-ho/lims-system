@echo off
echo Running database migration to add RESULT_APPROVAL stage...

mysql -u lims_user -plims1234 lims_db < update_stage_enum.sql

if %errorlevel% == 0 (
    echo Migration completed successfully!
) else (
    echo Migration failed. Please check MySQL installation and credentials.
)

pause
