import sqlite3
import os

INPUT_DB = r"C:\Users\ASHU\Desktop\hackathon\Research\database\roadsos_india.db"
OUTPUT_DB = r"C:\Users\ASHU\Desktop\hackathon\Research\database\roadsos_india_fixed.db"

if not os.path.exists(INPUT_DB):
    raise FileNotFoundError(f"Input DB not found: {INPUT_DB}")

src = sqlite3.connect(INPUT_DB)
src_cur = src.cursor()

tables = [
    row[0] for row in src_cur.execute(
        "SELECT name FROM sqlite_master WHERE type='table';"
    ).fetchall()
]

print("Tables found:", tables)

if "emergency_services" not in tables:
    src.close()
    raise Exception(
        "emergency_services table not found. You are opening the wrong DB file."
    )

if os.path.exists(OUTPUT_DB):
    os.remove(OUTPUT_DB)

dst = sqlite3.connect(OUTPUT_DB)
dst_cur = dst.cursor()

dst_cur.execute("""
CREATE TABLE emergency_services (
    id INTEGER NOT NULL PRIMARY KEY,
    name TEXT NOT NULL,
    category TEXT NOT NULL,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL,
    is_verified INTEGER NOT NULL
)
""")

rows = src_cur.execute("""
SELECT 
    rowid,
    COALESCE(name, 'Unknown'),
    COALESCE(category, 'unknown'),
    latitude,
    longitude,
    COALESCE(is_verified, 0)
FROM emergency_services
WHERE latitude IS NOT NULL
AND longitude IS NOT NULL
""").fetchall()

dst_cur.executemany("""
INSERT INTO emergency_services (
    id,
    name,
    category,
    latitude,
    longitude,
    is_verified
)
VALUES (?, ?, ?, ?, ?, ?)
""", rows)

dst_cur.execute("""
CREATE INDEX idx_category
ON emergency_services(category)
""")

dst_cur.execute("""
CREATE INDEX idx_location
ON emergency_services(latitude, longitude)
""")

dst.commit()

count = dst_cur.execute(
    "SELECT COUNT(*) FROM emergency_services"
).fetchone()[0]

categories = dst_cur.execute(
    "SELECT DISTINCT category FROM emergency_services"
).fetchall()

src.close()
dst.close()

print("Fixed DB created:", OUTPUT_DB)
print("Rows:", count)
print("Categories:", categories)
