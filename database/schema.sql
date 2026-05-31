-- RoadSOS Offline Emergency Services Database Schema
-- Final DB file: roadsos_india.db
-- Android asset path: app/src/main/assets/roadsos_india.db

PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS emergency_services;

CREATE TABLE emergency_services (
    id INTEGER NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    category TEXT NOT NULL,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL,
    is_verified INTEGER NOT NULL DEFAULT 0,

    CHECK (length(trim(name)) > 0),
    CHECK (length(trim(category)) > 0),
    CHECK (latitude BETWEEN -90.0 AND 90.0),
    CHECK (longitude BETWEEN -180.0 AND 180.0),
    CHECK (is_verified IN (0, 1))
);

CREATE INDEX IF NOT EXISTS idx_emergency_services_category
ON emergency_services(category);

CREATE INDEX IF NOT EXISTS idx_emergency_services_location
ON emergency_services(latitude, longitude);

CREATE INDEX IF NOT EXISTS idx_emergency_services_category_location
ON emergency_services(category, latitude, longitude);

-- Optional metadata table for documenting DB build details.
CREATE TABLE IF NOT EXISTS database_metadata (
    key TEXT NOT NULL PRIMARY KEY,
    value TEXT NOT NULL
);

INSERT OR REPLACE INTO database_metadata (key, value)
VALUES
    ('database_name', 'roadsos_india.db'),
    ('table_name', 'emergency_services'),
    ('data_source', 'OpenStreetMap via Overpass API'),
    ('project', 'RoadSOS'),
    ('usage', 'Offline nearby emergency and roadside support lookup'),
    ('prototype_status', 'Hackathon prototype; not certified emergency dispatch');

-- Useful validation queries:
-- SELECT COUNT(*) FROM emergency_services;
-- SELECT category, COUNT(*) FROM emergency_services GROUP BY category ORDER BY COUNT(*) DESC;
-- SELECT * FROM emergency_services LIMIT 10;