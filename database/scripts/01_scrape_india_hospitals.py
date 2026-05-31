import asyncio
import aiohttp
import pandas as pd
from tqdm import tqdm

# --------------------------------
# OVERPASS API CONFIG
# --------------------------------

OVERPASS_URL = "https://overpass-api.de/api/interpreter"

MAX_RETRIES = 5
RETRY_DELAY = 5

# --------------------------------
# QUERY
# --------------------------------

query = """
[out:json][timeout:120];

area["ISO3166-1"="IN"][admin_level=2]->.searchArea;

(
  node["amenity"="hospital"](area.searchArea);
  way["amenity"="hospital"](area.searchArea);
  relation["amenity"="hospital"](area.searchArea);
);

out center tags;
"""

# --------------------------------
# FETCH FUNCTION WITH RETRIES
# --------------------------------

async def fetch_hospital_data():

    timeout = aiohttp.ClientTimeout(total=300)

    async with aiohttp.ClientSession(
        timeout=timeout
    ) as session:

        for attempt in range(1, MAX_RETRIES + 1):

            try:

                print(
                    f"\nAttempt {attempt} → Fetching data..."
                )

                async with session.post(
                    OVERPASS_URL,
                    data=query
                ) as response:

                    # -------------------------
                    # RATE LIMIT HANDLING
                    # -------------------------

                    if response.status == 429:

                        wait_time = (
                            RETRY_DELAY * attempt
                        )

                        print(
                            f"429 Rate Limited. "
                            f"Waiting {wait_time}s..."
                        )

                        await asyncio.sleep(wait_time)

                        continue

                    # -------------------------
                    # SERVER OVERLOAD
                    # -------------------------

                    elif response.status >= 500:

                        wait_time = (
                            RETRY_DELAY * attempt
                        )

                        print(
                            f"Server overload "
                            f"({response.status}) "
                            f"Retrying in {wait_time}s..."
                        )

                        await asyncio.sleep(wait_time)

                        continue

                    # -------------------------
                    # OTHER ERRORS
                    # -------------------------

                    elif response.status != 200:

                        raise Exception(
                            f"Unexpected API Error: "
                            f"{response.status}"
                        )

                    # -------------------------
                    # SUCCESS
                    # -------------------------

                    data = await response.json()

                    print(
                        "\nSuccessfully fetched data"
                    )

                    return data

            # --------------------------------
            # TIMEOUT ERROR
            # --------------------------------

            except asyncio.TimeoutError:

                wait_time = RETRY_DELAY * attempt

                print(
                    f"Timeout Error. "
                    f"Retrying in {wait_time}s..."
                )

                await asyncio.sleep(wait_time)

            # --------------------------------
            # NETWORK ERRORS
            # --------------------------------

            except aiohttp.ClientError as e:

                wait_time = RETRY_DELAY * attempt

                print(
                    f"Network Error: {e}"
                )

                print(
                    f"Retrying in {wait_time}s..."
                )

                await asyncio.sleep(wait_time)

            # --------------------------------
            # UNKNOWN ERRORS
            # --------------------------------

            except Exception as e:

                print(f"Unexpected Error: {e}")

                wait_time = RETRY_DELAY * attempt

                print(
                    f"Retrying in {wait_time}s..."
                )

                await asyncio.sleep(wait_time)

        # --------------------------------
        # FAILED AFTER ALL RETRIES
        # --------------------------------

        raise Exception(
            "Failed after maximum retries"
        )

# --------------------------------
# PROCESS DATA
# --------------------------------

def process_data(data):

    hospitals = []

    print(
        f"\nTotal records fetched: "
        f"{len(data['elements'])}"
    )

    for item in tqdm(data["elements"]):

        tags = item.get("tags", {})

        name = tags.get("name")

        if not name:
            continue

        # -------------------------
        # LAT/LON
        # -------------------------

        lat = item.get("lat")
        lon = item.get("lon")

        if not lat:

            center = item.get("center", {})

            lat = center.get("lat")
            lon = center.get("lon")

        # -------------------------
        # CATEGORY DETECTION
        # -------------------------

        name_lower = name.lower()

        if (
            "aiims" in name_lower
            or "apex" in name_lower
            or "trauma" in name_lower
            or "super speciality" in name_lower
        ):

            category = "trauma_l1"

        elif (
            "medical college" in name_lower
            or "government medical" in name_lower
            or "emergency" in name_lower
        ):

            category = "trauma_l2"

        else:

            category = "district_emergency"

        # -------------------------
        # VERIFIED
        # -------------------------

        is_verified = 1

        # -------------------------
        # SAVE
        # -------------------------

        hospitals.append({
            "name": name,
            "category": category,
            "latitude": lat,
            "longitude": lon,
            "is_verified": is_verified
        })

    return hospitals

# --------------------------------
# CLEAN DATAFRAME
# --------------------------------

def clean_dataframe(df):

    # Remove duplicates
    df = df.drop_duplicates(
        subset=["name", "latitude", "longitude"]
    )

    # Garbage filter
    blacklist = [
        "dental",
        "clinic",
        "diagnostic",
        "pathology",
        "skin",
        "eye"
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

    return df

# --------------------------------
# MAIN
# --------------------------------

async def main():

    try:

        # Fetch API Data
        data = await fetch_hospital_data()

        # Process
        hospitals = process_data(data)

        # DataFrame
        df = pd.DataFrame(hospitals)

        # Clean
        df = clean_dataframe(df)

        # Save CSV
        df.to_csv(
            "india_trauma_centers.csv",
            index=False
        )

        print(
            "\nCSV saved successfully"
        )

        print(df.head())

    except Exception as e:

        print(f"\nFINAL ERROR: {e}")

# --------------------------------
# RUN
# --------------------------------

if __name__ == "__main__":

    asyncio.run(main())