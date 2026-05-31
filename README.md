# RoadSOS — Offline Crash Emergency Assistant

<div align="center">

<img src="https://readme-typing-svg.herokuapp.com?font=Fira+Code&weight=700&size=26&duration=2800&pause=700&color=FF1744&center=true&vCenter=true&width=950&lines=Crash+Emergency+Prototype;Smart+SOS+Countdown;Last-Known+Location+Fallback;Offline+Emergency+Lookup;Built+for+Road+Safety" alt="RoadSOS Typing Animation" />

<br/>

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge\&logo=android\&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-Jetpack%20Compose-7F52FF?style=for-the-badge\&logo=kotlin\&logoColor=white)
![Offline](https://img.shields.io/badge/Offline--First-Local%20Emergency%20Flow-00C853?style=for-the-badge)
![SQLite](https://img.shields.io/badge/Database-SQLite-2563EB?style=for-the-badge)
![Prototype](https://img.shields.io/badge/Status-Prototype%201-FF9800?style=for-the-badge)

</div>

---

## Hackathon Snapshot

<pre>
Problem Statement: 1.3 RoadSoS — Emergency Response Optimization  
        
Team: Fuzeppers  
        
Project Type: Android offline-first emergency assistant  
        
Core Claim: On-device crash-like sound detection + false-alarm countdown + emergency SMS/call + offline nearby service lookup  
        
Prototype Status: Working Prototype 1  
        
Final DB Size: 7.46 MB  
        
TFLite Model Size: 1.1 MB  
        
APK Size: ~40 MB
        
</pre>

```text
This prototype is designed for hackathon demonstration and first-response support, not as a certified emergency dispatch system.
```
---

## The Idea

**RoadSOS** is an Android emergency-response prototype built to reduce the delay between a possible road accident and the first human response.

It listens for crash-like events, starts a false-alarm countdown, and if the user does not cancel, it moves into emergency mode.

From there, RoadSOS can send SOS messages to saved contacts, attach current or last-known location, optionally attempt an emergency call, show nearby offline emergency services, and save the event locally.

```text
Detect → Countdown → Alert → Locate → Assist → Record
```

This is **Prototype 1**: a working emergency workflow prototype, not a certified crash-response system.

---

## Why This Matters

After an accident, the victim may be unconscious, injured, shocked, or unable to unlock the phone. In many cases, help is delayed not because help is unavailable, but because no one knows the accident happened quickly enough.

RoadSOS focuses on that first-response gap.

It is designed around three practical questions:

```text
Can the phone notice something unusual?
Can the user cancel if it is a false alarm?
Can the phone still send useful emergency context if the user cannot respond?
```

---

## Core Flow

<div align="center">

<pre>
Crash-like sound / Manual SOS
        ↓
Countdown starts
        ↓
User can cancel
        ↓
Emergency mode
        ↓
SMS to emergency contacts
        ↓
Current or last-known location
        ↓
Optional emergency call
        ↓
Offline nearby services
        ↓
Local emergency history
</pre>

</div>

---

## What Works in Prototype 1

| Module               | Implementation                                  |
| -------------------- | ----------------------------------------------- |
| Crash-like detection | Audio monitoring service with ML helper         |
| Manual SOS           | Large emergency button with countdown           |
| False alarm handling | User can cancel before emergency action         |
| Emergency contacts   | Multiple saved contacts supported               |
| SMS alert            | Automatic SMS attempt to saved contacts         |
| Auto-call            | Optional emergency call setting                 |
| Location fallback    | Current location or saved last-known location   |
| Offline help         | SQLite-based emergency service lookup           |
| History              | Local emergency event log                       |
| Profile              | Medical profile with editable details           |
| UI                   | Jetpack Compose with dark/light mode            |
| Language             | Multilingual support for major Indian languages |

---

## System Architecture

<div align="center">

<pre>
┌──────────────────────────────┐
│        Jetpack Compose UI     │
└───────────────┬──────────────┘
                ↓
┌──────────────────────────────┐
│          ViewModels           │
│ Emergency / Location / Map    │
│ Profile / Settings / History  │
└───────────────┬──────────────┘
                ↓
┌──────────────────────────────┐
│        Local Data Layer       │
│ DataStore + SQLite Assets DB  │
└───────────────┬──────────────┘
                ↓
┌──────────────────────────────┐
│       Emergency Managers      │
│ SMS / Call / Status / History │
└───────────────┬──────────────┘
                ↓
┌──────────────────────────────┐
│     Background Monitoring     │
│ Audio Service + ML Helper     │
└──────────────────────────────┘
</pre>

</div>

---

## Crash Detection Pipeline

<div align="center">

<pre>
AudioMonitoringService
        ↓
AudioRecorderManager
        ↓
Audio chunks
        ↓
TensorFlow helper
        ↓
Crash-like score
        ↓
EmergencyEventManager
        ↓
Emergency countdown
</pre>

</div>

The detection layer is intentionally described as **crash-like sound detection**, not certified crash detection. This keeps the prototype honest and technically grounded.

---

## Location Strategy

Location is one of the most important parts of an emergency alert.

RoadSOS does not silently track users when location is off. Instead, it uses a fallback strategy:

<div align="center">

<pre>
Current location available
        ↓
Use current location

Current location unavailable
        ↓
Use saved last-known location

No saved location
        ↓
Send alert with location unavailable warning
</pre>

</div>

Emergency messages clearly label whether the location is current, recent last-known, old last-known, or unavailable.

---

## Emergency SMS

RoadSOS sends emergency SMS alerts to all saved emergency contacts.

Example SMS:

```text
RoadSOS EMERGENCY ALERT

Possible accident detected.

Name: Ashutosh
Blood Group: B+

Location Type: Recent last-known location
Location: https://maps.google.com/?q=23.xxxx,77.xxxx
Last updated: 6 minutes ago
Warning: location may be slightly old.

Please call the user immediately and contact emergency services if unreachable.
```

The goal is simple: give contacts enough information to act quickly.

---

## Offline Emergency Lookup

RoadSOS includes an offline SQLite database stored inside app assets.

The Map screen can show nearby emergency services using current or last-known location.

Supported emergency categories include:

```text
Hospitals
Police stations
Vehicle repair services
Tow / puncture support
Other emergency-related services
```

This makes the app useful even when internet-based search is unavailable.

---

## Emergency History

Every completed emergency is stored locally.

RoadSOS records:

| Field             | Meaning                             |
| ----------------- | ----------------------------------- |
| Time              | When the emergency happened         |
| Trigger           | Manual SOS or automatic detection   |
| Location type     | Current, last-known, or unavailable |
| Coordinates       | Location used during emergency      |
| SMS status        | SMS attempt result                  |
| Call status       | Auto-call result                    |
| Auto-call setting | Whether auto-call was enabled       |

This creates a small audit trail for testing, debugging, and demonstration.

---

## App Modules

```text
RoadSOS
│
├── Home
│   ├── SOS button
│   ├── Countdown overlay
│   ├── Protection status
│   └── SMS / call / location status
│
├── Map
│   ├── Emergency radar
│   ├── Offline service lookup
│   └── Navigation intents
│
├── Profile
│   ├── Medical details
│   ├── Multiple emergency contacts
│   └── Language selection
│
├── Settings
│   ├── Auto-call toggle
│   ├── Permission health
│   ├── Theme toggle
│   └── Emergency history access
│
└── History
    ├── SOS event logs
    ├── Trigger source
    └── Emergency action status
```

---

## Tech Stack

| Layer           | Technology                      |
| --------------- | ------------------------------- |
| Language        | Kotlin                          |
| UI              | Jetpack Compose                 |
| Architecture    | ViewModel-based MVVM style      |
| Local Storage   | DataStore                       |
| Offline DB      | SQLite asset database           |
| Audio Capture   | AudioRecord                     |
| ML Helper       | TensorFlow Lite helper          |
| Background Work | Foreground service              |
| Navigation      | Navigation Compose              |
| Location        | Android Location APIs           |
| SMS             | SmsManager                      |
| Call            | Android call intent             |
| Maps            | External map/navigation intents |

---

## Simplified Project Structure

```text
RoadSOS
│
├── app/src/main
│   ├── AndroidManifest.xml
│   ├── assets
│   │   ├── crash_model.tflite
│   │   ├── labels.csv
│   │   └── roadsos_india.db
│   │
│   └── java/com/example/roadsos
│       ├── MainActivity.kt
│       ├── navigation
│       ├── ui
│       │   ├── components
│       │   └── screens
│       │       ├── home
│       │       ├── map
│       │       ├── profile
│       │       ├── settings
│       │       ├── history
│       │       └── onboarding
│       │
│       ├── viewmodel
│       ├── data
│       │   ├── local
│       │   ├── profile
│       │   ├── settings
│       │   ├── location
│       │   ├── history
│       │   └── repository
│       │
│       ├── service
│       ├── ml
│       ├── audio
│       ├── sms
│       └── utils
```

---

## Required Permissions

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.SEND_SMS" />
<uses-permission android:name="android.permission.CALL_PHONE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_MICROPHONE" />
<uses-permission android:name="android.permission.VIBRATE" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

---

## Run Locally

```bash
git clone https://github.com/ashu-mishra06/RoadSOS.git
cd RoadSOS
```

Open the project in Android Studio.

```text
Sync Gradle → Build Project → Run App
```

Debug APK:

```bash
./gradlew assembleDebug
```

APK output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

---

## Testing Checklist

```text
First Launch
→ Fill profile
→ Add contacts
→ Grant permissions
→ Verify Settings permission card

Manual SOS
→ Press SOS
→ Cancel countdown once
→ Trigger again
→ Let emergency activate
→ Check SMS/call status
→ Check Emergency History

Crash-like Detection
→ Keep monitoring active
→ Play crash-like sound
→ Wait for countdown
→ Verify emergency flow

Location Fallback
→ Turn location ON once
→ Let app save location
→ Turn location OFF
→ Trigger SOS
→ Verify last-known location in SMS/map

Offline Check
→ Turn internet OFF
→ Keep location ON
→ Verify offline DB lookup
```

---

## Prototype Boundaries

RoadSOS Prototype 1 is functional, but it is not production-certified.

Known boundaries:

```text
Audio-only detection may cause false positives.
Some real crashes may not produce detectable sound.
SMS status means send attempt, not delivery confirmation.
Android background restrictions may affect monitoring on some phones.
Auto-calling must be handled carefully.
Location cannot be fetched if location is off and no last-known location exists.
Offline service quality depends on database quality.
```

---

## Responsible Design Choices

RoadSOS is built around safety-first assumptions:

```text
Do not alert instantly
→ Use countdown

Do not pretend old location is live
→ Label last-known location clearly

Do not depend fully on internet
→ Use local database and local storage

Do not silently track users
→ Respect location permissions

Do not rely on only one contact
→ Support multiple emergency contacts
```

---

## Demo Line

```text
RoadSOS is an offline-first Android crash emergency prototype that detects crash-like events locally, starts a false-alarm countdown, sends SOS alerts with current or last-known location, optionally attempts emergency calling, and stores a local emergency history.
```

---

## Team Fuzeppers

| Member                | Role                                            |
| --------------------- | ----------------------------------------------- |
| Ashutosh Mishra       | DB, Frontend, App Integration and Documentation |
| Satvik Jain           | ML and Presentation                             |
| Arpit Singh Bhadoriya | UI Integration                                  |
| Vivek Jangela         | Frontend and App Integration                    |

---

## Disclaimer

RoadSOS is a hackathon prototype built for learning, demonstration, and research.

It is **not a certified emergency response system** and should not be used as a replacement for official emergency services without real-world validation, legal review, regulatory approval, and safety testing.

---

<div align="center">

### RoadSOS

**Because every second after a crash matters.**

<img src="https://readme-typing-svg.herokuapp.com?font=Fira+Code&weight=600&size=20&duration=2500&pause=600&color=22C55E&center=true&vCenter=true&width=760&lines=Detect.;Countdown.;Alert.;Locate.;Record." alt="RoadSOS Animation" />

</div>
