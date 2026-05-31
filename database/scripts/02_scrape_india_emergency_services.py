import asyncio
import aiohttp
import pandas as pd
from tqdm import tqdm

OVERPASS_URL = "https://overpass-api.de/api/interpreter"

# --------------------------------
# CATEGORY QUERIES
# --------------------------------

queries = {

    "hospital": """
    [out:json][timeout:120];
    area["ISO3166-1"="IN"][admin_level=2]->.searchArea;

    (
      node["amenity"="hospital"](area.searchArea);
      way["amenity"="hospital"](area.searchArea);
      relation["amenity"="hospital"](area.searchArea);
    );

    out center tags;
    """,

    "police_station": """
    [out:json][timeout:120];
    area["ISO3166-1"="IN"][admin_level=2]->.searchArea;

    (
      node["amenity"="police"](area.searchArea);
      way["amenity"="police"](area.searchArea);
      relation["amenity"="police"](area.searchArea);
    );

    out center tags;
    """,

    "ambulance_service": """
    [out:json][timeout:120];
    area["ISO3166-1"="IN"][admin_level=2]->.searchArea;

    (
      node["amenity"="ambulance_station"](area.searchArea);
      way["amenity"="ambulance_station"](area.searchArea);
      relation["amenity"="ambulance_station"](area.searchArea);
    );

    out center tags;
    """,

    "vehicle_repair": """
    [out:json][timeout:120];
    area["ISO3166-1"="IN"][admin_level=2]->.searchArea;

    (
      node["shop"="car_repair"](area.searchArea);
      node["amenity"="car_repair"](area.searchArea);

      way["shop"="car_repair"](area.searchArea);
      way["amenity"="car_repair"](area.searchArea);
    );

    out center tags;
    """,

    "puncture_shop": """
    [out:json][timeout:120];
    area["ISO3166-1"="IN"][admin_level=2]->.searchArea;

    (
      node["shop"="tyres"](area.searchArea);
      way["shop"="tyres"](area.searchArea);
    );

    out center tags;
    """,

    "vehicle_showroom": """
    [out:json][timeout:120];
    area["ISO3166-1"="IN"][admin_level=2]->.searchArea;

    (
      node["shop"="car"](area.searchArea);
      node["shop"="motorcycle"](area.searchArea);

      way["shop"="car"](area.searchArea);
      way["shop"="motorcycle"](area.searchArea);
    );

    out center tags;
    """
}

# --------------------------------
# FETCH FUNCTION
# --------------------------------

async def fetch_category(session, category, query):

    try:

        print(f"\nFetching {category}...")

        async with session.post(
            OVERPASS_URL,
            data=query
        ) as response:

            data = await response.json()

            return category, data["elements"]

    except Exception as e:

        print(f"Error in {category}: {e}")

        return category, []

# --------------------------------
# PROCESS FUNCTION
# --------------------------------

def process_element(item, category):

    tags = item.get("tags", {})

    name = tags.get("name")

    if not name:
        return None

    lat = item.get("lat")
    lon = item.get("lon")

    if not lat:

        center = item.get("center", {})

        lat = center.get("lat")
        lon = center.get("lon")

    # --------------------------------
    # HOSPITAL SUBCLASSIFICATION
    # --------------------------------

    if category == "hospital":

        lower = name.lower()

        if (
            "aiims" in lower
            or "trauma" in lower
            or "apex" in lower
        ):

            category = "trauma_l1"

        elif (
            "medical college" in lower
            or "emergency" in lower
        ):

            category = "trauma_l2"

        else:

            category = "district_emergency"

    return {
        "name": name,
        "category": category,
        "latitude": lat,
        "longitude": lon,
        "is_verified": 1
    }

# --------------------------------
# MAIN
# --------------------------------

async def main():

    timeout = aiohttp.ClientTimeout(total=300)

    async with aiohttp.ClientSession(
        timeout=timeout
    ) as session:

        tasks = []

        for category, query in queries.items():

            tasks.append(
                fetch_category(
                    session,
                    category,
                    query
                )
            )

        results = await asyncio.gather(*tasks)

    all_data = []

    # --------------------------------
    # PROCESS ALL RESULTS
    # --------------------------------

    for category, elements in results:

        print(
            f"{category}: {len(elements)} records"
        )

        for item in tqdm(elements):

            processed = process_element(
                item,
                category
            )

            if processed:
                all_data.append(processed)

    # --------------------------------
    # DATAFRAME
    # --------------------------------

    df = pd.DataFrame(all_data)

    # Remove duplicates
    df = df.drop_duplicates(
        subset=["name", "latitude", "longitude"]
    )

    # Garbage filtering
    blacklist = [
        "dental",
        "pathology",
        "skin",
        "ivf"
    ]

    for word in blacklist:

        df = df[
            ~df["name"]
            .str.lower()
            .str.contains(word, na=False)
        ]

    # Add ID
    df.reset_index(drop=True, inplace=True)

    df.index += 1

    df.insert(0, "id", df.index)

    # Save
    df.to_csv(
        "india_emergency_services.csv",
        index=False
    )

    print("\nCSV Saved Successfully")
    print(df.head())

# --------------------------------
# RUN
# --------------------------------

asyncio.run(main())