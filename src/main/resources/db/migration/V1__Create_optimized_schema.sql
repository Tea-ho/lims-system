-- Database schema and index optimization for LIMS Study

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    authorities VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_authorities (authorities),
    INDEX idx_created_at (created_at)
);

-- Products table
CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_created_at (created_at),
    FULLTEXT idx_fulltext_search (name, description)
);

-- Request info table
CREATE TABLE IF NOT EXISTS request_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_details TEXT,
    request_date DATE,
    priority ENUM('LOW', 'NORMAL', 'HIGH', 'URGENT') DEFAULT 'NORMAL',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_request_date (request_date),
    INDEX idx_priority (priority),
    INDEX idx_created_at (created_at)
);

-- Receipt info table
CREATE TABLE IF NOT EXISTS receipt_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    receipt_details TEXT,
    receipt_date DATE,
    received_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_receipt_date (receipt_date),
    INDEX idx_received_by (received_by),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (received_by) REFERENCES users(id)
);

-- Result info table
CREATE TABLE IF NOT EXISTS result_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    result_details TEXT,
    result_value DECIMAL(10,3),
    result_unit VARCHAR(20),
    result_status ENUM('PASS', 'FAIL', 'PENDING') DEFAULT 'PENDING',
    tested_by BIGINT,
    test_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_result_status (result_status),
    INDEX idx_tested_by (tested_by),
    INDEX idx_test_date (test_date),
    INDEX idx_result_value (result_value),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (tested_by) REFERENCES users(id)
);

-- Completion info table
CREATE TABLE IF NOT EXISTS completion_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    completion_details TEXT,
    completion_date DATE,
    completed_by BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_completion_date (completion_date),
    INDEX idx_completed_by (completed_by),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (completed_by) REFERENCES users(id)
);

-- Tests table (main table)
CREATE TABLE IF NOT EXISTS tests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_info_id BIGINT,
    receipt_info_id BIGINT,
    result_info_id BIGINT,
    completion_info_id BIGINT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    stage ENUM('REQUEST', 'RECEIPT', 'RECEIPT_APPROVAL', 'RESULT_INPUT', 'COMPLETED') DEFAULT 'REQUEST',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Primary indexes
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_stage (stage),
    INDEX idx_created_at (created_at),
    INDEX idx_updated_at (updated_at),
    
    -- Composite indexes for common queries
    INDEX idx_user_stage (user_id, stage),
    INDEX idx_product_stage (product_id, stage),
    INDEX idx_stage_created (stage, created_at),
    INDEX idx_user_created (user_id, created_at),
    
    -- Foreign key constraints
    FOREIGN KEY (request_info_id) REFERENCES request_info(id),
    FOREIGN KEY (receipt_info_id) REFERENCES receipt_info(id),
    FOREIGN KEY (result_info_id) REFERENCES result_info(id),
    FOREIGN KEY (completion_info_id) REFERENCES completion_info(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Approval requests table
CREATE TABLE IF NOT EXISTS approval_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    test_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    request_message TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_test_id (test_id),
    INDEX idx_requester_id (requester_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    INDEX idx_status_created (status, created_at),
    
    FOREIGN KEY (test_id) REFERENCES tests(id),
    FOREIGN KEY (requester_id) REFERENCES users(id)
);

-- Approvals table
CREATE TABLE IF NOT EXISTS approvals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    approval_request_id BIGINT NOT NULL,
    approver_id BIGINT NOT NULL,
    approval_order INT NOT NULL DEFAULT 1,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    comments TEXT,
    processed_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_approval_request_id (approval_request_id),
    INDEX idx_approver_id (approver_id),
    INDEX idx_status (status),
    INDEX idx_approval_order (approval_order),
    INDEX idx_processed_at (processed_at),
    INDEX idx_request_order (approval_request_id, approval_order),
    
    FOREIGN KEY (approval_request_id) REFERENCES approval_requests(id),
    FOREIGN KEY (approver_id) REFERENCES users(id)
);

-- Approval signs table
CREATE TABLE IF NOT EXISTS approval_signs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    approval_id BIGINT NOT NULL,
    signer_id BIGINT NOT NULL,
    signature_data TEXT,
    signed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_approval_id (approval_id),
    INDEX idx_signer_id (signer_id),
    INDEX idx_signed_at (signed_at),
    
    FOREIGN KEY (approval_id) REFERENCES approvals(id),
    FOREIGN KEY (signer_id) REFERENCES users(id)
);

-- Audit logs table for tracking all changes
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    old_values JSON,
    new_values JSON,
    user_id BIGINT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_action (action),
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at),
    INDEX idx_entity_created (entity_type, entity_id, created_at),
    
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Performance monitoring views
CREATE OR REPLACE VIEW test_statistics AS
SELECT 
    stage,
    COUNT(*) as test_count,
    AVG(TIMESTAMPDIFF(HOUR, created_at, updated_at)) as avg_processing_hours
FROM tests 
GROUP BY stage;

CREATE OR REPLACE VIEW user_workload AS
SELECT 
    u.id,
    u.username,
    COUNT(t.id) as active_tests,
    COUNT(CASE WHEN t.stage = 'REQUEST' THEN 1 END) as pending_requests,
    COUNT(CASE WHEN t.stage = 'COMPLETED' THEN 1 END) as completed_tests
FROM users u
LEFT JOIN tests t ON u.id = t.user_id
GROUP BY u.id, u.username;
