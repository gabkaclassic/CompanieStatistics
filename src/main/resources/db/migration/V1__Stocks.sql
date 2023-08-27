CREATE TABLE IF NOT EXISTS stocks (
    id VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
    company_name VARCHAR(128),
    volume bigint,
    latest_volume bigint
)