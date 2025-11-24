-- Add priority and requires_approval columns to request_infos table

ALTER TABLE request_infos
ADD COLUMN priority VARCHAR(20) DEFAULT 'NORMAL' AFTER description;

ALTER TABLE request_infos
ADD COLUMN requires_approval BOOLEAN DEFAULT FALSE AFTER priority;

-- Update existing data: convert is_urgent to priority
UPDATE request_infos SET priority = 'URGENT' WHERE is_urgent = TRUE;
UPDATE request_infos SET priority = 'NORMAL' WHERE is_urgent = FALSE OR is_urgent IS NULL;

-- Add index for priority column
CREATE INDEX idx_priority ON request_infos(priority);
