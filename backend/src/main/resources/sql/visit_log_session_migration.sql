ALTER TABLE visit_log ADD COLUMN session_id VARCHAR(64) NULL COMMENT '访问会话标识' AFTER visitor_id;
CREATE INDEX idx_session_id ON visit_log(session_id);
UPDATE visit_log SET session_id = visitor_id WHERE session_id IS NULL;
