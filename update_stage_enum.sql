-- Update tests table stage column to include RESULT_APPROVAL
ALTER TABLE tests
MODIFY COLUMN stage ENUM('REQUEST', 'RECEIPT', 'RECEIPT_APPROVAL', 'RESULT_INPUT', 'RESULT_APPROVAL', 'COMPLETED') NOT NULL DEFAULT 'REQUEST';

-- Update approval_signs table stage column to include RESULT_APPROVAL
ALTER TABLE approval_signs
MODIFY COLUMN stage ENUM('REQUEST', 'RECEIPT', 'RECEIPT_APPROVAL', 'RESULT_INPUT', 'RESULT_APPROVAL', 'COMPLETED') NOT NULL;

-- Insert migration record to prevent re-running
INSERT INTO flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, execution_time, success)
VALUES (
    (SELECT COALESCE(MAX(installed_rank), 0) + 1 FROM flyway_schema_history),
    '17',
    'Add Result Approval Stage',
    'SQL',
    'V17__Add_Result_Approval_Stage.sql',
    0,
    'lims_user',
    0,
    1
);
