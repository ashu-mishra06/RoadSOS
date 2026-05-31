# RoadSOS Offline Emergency Database Pipeline

This folder contains the Python pipeline used to generate the offline India emergency services database used by the RoadSOS Android app.

## Data Source

The dataset is generated from OpenStreetMap using Overpass API queries for India-focused emergency and roadside support categories.

## Categories

- Hospitals / trauma support
- Police stations
- Ambulance service query included
- Vehicle repair shops
- Puncture / tyre shops
- Vehicle showrooms and roadside support

## Pipeline

1. `01_scrape_india_hospitals.py`
   Fetches hospital/trauma-related records from OpenStreetMap.

2. `02_scrape_india_emergency_services.py`
   Fetches multiple emergency-service categories including hospitals, police stations, ambulance stations, vehicle repair, puncture shops, and showrooms.

3. `03_fix_roadsos_db.py`
   Checks final table schema, record count, category count, DB size, and indexes.


## Final DB

- File name: `roadsos_india.db`
- Size: approximately 7.46 MB
- Table: `emergency_services`
- Total records: 64,006

## Android Usage

The final DB is copied into:

`app/src/main/assets/roadsos_india.db`

The Android app loads this asset database for offline nearby emergency service lookup.