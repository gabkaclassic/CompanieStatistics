ALTER TABLE stocks
ADD COLUMN IF NOT EXISTS delta_volume bigint DEFAULT 0;

