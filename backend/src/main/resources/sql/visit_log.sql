CREATE TABLE IF NOT EXISTS visit_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    visitor_id VARCHAR(64),
    session_id VARCHAR(64),
    path VARCHAR(255),
    ip VARCHAR(64),
    user_agent TEXT,
    referer VARCHAR(500),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_visit_log_created_at (created_at),
    INDEX idx_visit_log_visitor_created_at (visitor_id, created_at),
    INDEX idx_session_id (session_id),
    INDEX idx_visit_log_path_created_at (path, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
