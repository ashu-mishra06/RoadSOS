package com.example.roadsos.utils

object AppText {

    const val EN = "en"
    const val HI = "hi"
    const val BN = "bn"
    const val TA = "ta"
    const val TE = "te"
    const val MR = "mr"

    fun t(
        language: String,
        key: String
    ): String {
        return when (language) {

            HI -> hindi[key]
            BN -> bengali[key]
            TA -> tamil[key]
            TE -> telugu[key]
            MR -> marathi[key]

            else -> english[key]
        } ?: english[key] ?: key
    }

    private val english =
        mapOf(
            // -------------------------
            // COMMON
            // -------------------------
            "app_name" to "RoadSOS",
            "home" to "Home",
            "map" to "Map",
            "settings" to "Settings",
            "profile" to "Profile",
            "language" to "Language",
            "change" to "Change",
            "save" to "Save",
            "cancel" to "Cancel",
            "edit" to "Edit",
            "not_saved" to "Not saved",

            // -------------------------
            // HOME SCREEN
            // -------------------------
            "home_subtitle" to "Offline AI Crash Detection",
            "emergency_response_active" to "Emergency response active",
            "crash_countdown_running" to "Crash countdown running",
            "protection_status" to "Protection Status",
            "active" to "Active",
            "permission_needed" to "Permission Needed",
            "all_systems_working" to "All systems working properly",
            "open_settings_grant_access" to "Open settings to grant access",
            "current_location" to "Current Location",
            "fetching_location" to "Fetching GPS location...",
            "crash_detection" to "Crash Detection",
            "no_crash_detected" to "No crash detected",
            "crash_detected" to "Crash Detected",
            "emergency_sos_triggered" to "Emergency SOS Triggered",
            "listening_ai" to "RoadSOS is listening locally using on-device AI.",
            "countdown_warning" to "Safety countdown is running. Cancel if this is a false alarm.",
            "emergency_alert_active" to "Emergency alert flow is active. Nearest services can be checked on the map.",
            "emergency_actions_started" to "Emergency Actions Started",
            "emergency_actions_desc" to "RoadSOS has prepared your emergency alert, current location and nearest emergency services.",
            "emergency_sos" to "EMERGENCY SOS",
            "cancel_countdown" to "CANCEL COUNTDOWN",
            "return_normal" to "RETURN TO NORMAL",
            "tap_reset" to "Tap to reset emergency mode",
            "tap_cancel" to "Tap to cancel safety countdown",
            "roadsos_ready" to "Hold steady. RoadSOS is ready.",

            // -------------------------
            // PROFILE
            // -------------------------
            "medical_profile" to "Medical Profile",
            "saved_emergency_info" to "Saved emergency information for RoadSOS.",
            "name" to "Name",
            "blood_group" to "Blood Group",
            "emergency_contact" to "Emergency Contact",
            "weight" to "Weight",
            "height" to "Height",
            "age" to "Age",
            "choose_language" to "Choose Language",
            "setup_profile" to "Setup RoadSOS Profile",
            "medical_details_help" to "Your medical details help during emergency response.",
            "full_name" to "Full Name",
            "save_continue" to "Save & Continue",

            // -------------------------
            // SETTINGS
            // -------------------------
            "dark_mode" to "Dark Mode",
            "light_mode" to "Light Mode",
            "appearance_control" to "Smooth minimal appearance control",
            "manage_safety_profile" to "Manage your RoadSOS safety profile and app access.",
            "settings_manage" to "Settings Manage",
            "all_permissions_granted" to "All required permissions are granted",
            "some_permissions_missing" to "Some permissions are missing",
            "logout" to "Logout",
            "erase_profile_data" to "Erase saved user profile data from RoadSOS",
            "clear_data" to "Clear Data",

            // -------------------------
            // MAP SCREEN
            // -------------------------
            "emergency_map" to "Emergency Map",
            "map_subtitle" to "Offline service radar with external navigation",
            "live_emergency_radar" to "Live Emergency Radar",
            "radar_desc" to "Center is your accident location. Dots are nearby emergency services.",
            "open_accident_location" to "Open Accident Location",
            "nearest_emergency_services" to "Nearest Emergency Services",
            "top_services_desc" to "Minimal top 3 services from each category",
            "loading_services" to "Loading emergency services from offline database...",
            "nearby" to "nearby",
            "you" to "YOU",
            "accident_point" to "Accident Point",
            "trauma" to "Trauma",
            "police" to "Police",
            "vehicle" to "Vehicle",
            "km_away" to "km away",
            "verified" to "Verified",
            "unverified" to "Unverified",
            "route" to "Route"
        )

    private val hindi =
        mapOf(
            // -------------------------
            // COMMON
            // -------------------------
            "app_name" to "RoadSOS",
            "home" to "होम",
            "map" to "मैप",
            "settings" to "सेटिंग्स",
            "profile" to "प्रोफाइल",
            "language" to "भाषा",
            "change" to "बदलें",
            "save" to "सेव",
            "cancel" to "कैंसल",
            "edit" to "एडिट",
            "not_saved" to "सेव नहीं है",

            // -------------------------
            // HOME SCREEN
            // -------------------------
            "home_subtitle" to "ऑफलाइन AI क्रैश डिटेक्शन",
            "emergency_response_active" to "इमरजेंसी रिस्पॉन्स सक्रिय है",
            "crash_countdown_running" to "क्रैश काउंटडाउन चल रहा है",
            "protection_status" to "सुरक्षा स्थिति",
            "active" to "सक्रिय",
            "permission_needed" to "अनुमति चाहिए",
            "all_systems_working" to "सभी सिस्टम सही काम कर रहे हैं",
            "open_settings_grant_access" to "अनुमति देने के लिए सेटिंग्स खोलें",
            "current_location" to "वर्तमान स्थान",
            "fetching_location" to "GPS स्थान लिया जा रहा है...",
            "crash_detection" to "क्रैश डिटेक्शन",
            "no_crash_detected" to "कोई क्रैश नहीं मिला",
            "crash_detected" to "क्रैश मिला",
            "emergency_sos_triggered" to "इमरजेंसी SOS शुरू हुआ",
            "listening_ai" to "RoadSOS डिवाइस पर AI से सुन रहा है।",
            "countdown_warning" to "सुरक्षा काउंटडाउन चल रहा है। गलत अलार्म हो तो कैंसल करें।",
            "emergency_alert_active" to "इमरजेंसी अलर्ट सक्रिय है। नजदीकी सेवाएं मैप पर देखें।",
            "emergency_actions_started" to "इमरजेंसी एक्शन शुरू",
            "emergency_actions_desc" to "RoadSOS ने अलर्ट, लोकेशन और नजदीकी सेवाएं तैयार कर दी हैं।",
            "emergency_sos" to "इमरजेंसी SOS",
            "cancel_countdown" to "काउंटडाउन कैंसल करें",
            "return_normal" to "नॉर्मल मोड",
            "tap_reset" to "इमरजेंसी मोड रीसेट करें",
            "tap_cancel" to "काउंटडाउन कैंसल करें",
            "roadsos_ready" to "RoadSOS तैयार है।",

            // -------------------------
            // PROFILE
            // -------------------------
            "medical_profile" to "मेडिकल प्रोफाइल",
            "saved_emergency_info" to "RoadSOS के लिए सेव की गई इमरजेंसी जानकारी।",
            "name" to "नाम",
            "blood_group" to "ब्लड ग्रुप",
            "emergency_contact" to "इमरजेंसी संपर्क",
            "weight" to "वजन",
            "height" to "ऊंचाई",
            "age" to "उम्र",
            "choose_language" to "भाषा चुनें",
            "setup_profile" to "RoadSOS प्रोफाइल सेटअप",
            "medical_details_help" to "आपकी मेडिकल जानकारी इमरजेंसी में मदद करती है।",
            "full_name" to "पूरा नाम",
            "save_continue" to "सेव करें और आगे बढ़ें",

            // -------------------------
            // SETTINGS
            // -------------------------
            "dark_mode" to "डार्क मोड",
            "light_mode" to "लाइट मोड",
            "appearance_control" to "स्मूथ और मिनिमल थीम कंट्रोल",
            "manage_safety_profile" to "RoadSOS सुरक्षा प्रोफाइल और ऐप एक्सेस मैनेज करें।",
            "settings_manage" to "सेटिंग्स मैनेज",
            "all_permissions_granted" to "सभी जरूरी अनुमतियां मिली हुई हैं",
            "some_permissions_missing" to "कुछ अनुमतियां गायब हैं",
            "logout" to "लॉगआउट",
            "erase_profile_data" to "RoadSOS से सेव प्रोफाइल डेटा हटाएं",
            "clear_data" to "डेटा हटाएं",

            // -------------------------
            // MAP SCREEN
            // -------------------------
            "emergency_map" to "इमरजेंसी मैप",
            "map_subtitle" to "ऑफलाइन सर्विस रडार और बाहरी नेविगेशन",
            "live_emergency_radar" to "लाइव इमरजेंसी रडार",
            "radar_desc" to "बीच में आपकी दुर्घटना लोकेशन है। डॉट्स नजदीकी इमरजेंसी सेवाएं हैं।",
            "open_accident_location" to "दुर्घटना लोकेशन खोलें",
            "nearest_emergency_services" to "नजदीकी इमरजेंसी सेवाएं",
            "top_services_desc" to "हर कैटेगरी से टॉप 3 सेवाएं",
            "loading_services" to "ऑफलाइन डेटाबेस से इमरजेंसी सेवाएं लोड हो रही हैं...",
            "nearby" to "नजदीक",
            "you" to "आप",
            "accident_point" to "दुर्घटना स्थान",
            "trauma" to "ट्रॉमा",
            "police" to "पुलिस",
            "vehicle" to "वाहन",
            "km_away" to "किमी दूर",
            "verified" to "वेरिफाइड",
            "unverified" to "अनवेरिफाइड",
            "route" to "रूट"
        )

    private val bengali =
        mapOf(
            // -------------------------
            // COMMON
            // -------------------------
            "app_name" to "RoadSOS",
            "home" to "হোম",
            "map" to "ম্যাপ",
            "settings" to "সেটিংস",
            "profile" to "প্রোফাইল",
            "language" to "ভাষা",
            "change" to "পরিবর্তন",
            "save" to "সংরক্ষণ",
            "cancel" to "বাতিল",
            "edit" to "এডিট",
            "not_saved" to "সংরক্ষিত নয়",

            // -------------------------
            // HOME SCREEN
            // -------------------------
            "home_subtitle" to "অফলাইন AI ক্র্যাশ ডিটেকশন",
            "emergency_response_active" to "ইমার্জেন্সি রেসপন্স সক্রিয়",
            "crash_countdown_running" to "ক্র্যাশ কাউন্টডাউন চলছে",
            "protection_status" to "সুরক্ষা অবস্থা",
            "active" to "সক্রিয়",
            "permission_needed" to "অনুমতি দরকার",
            "all_systems_working" to "সব সিস্টেম ঠিকভাবে চলছে",
            "open_settings_grant_access" to "অনুমতি দিতে সেটিংস খুলুন",
            "current_location" to "বর্তমান লোকেশন",
            "fetching_location" to "GPS লোকেশন নেওয়া হচ্ছে...",
            "crash_detection" to "ক্র্যাশ ডিটেকশন",
            "no_crash_detected" to "কোনো ক্র্যাশ পাওয়া যায়নি",
            "crash_detected" to "ক্র্যাশ পাওয়া গেছে",
            "emergency_sos_triggered" to "ইমার্জেন্সি SOS চালু হয়েছে",
            "listening_ai" to "RoadSOS ডিভাইসের AI দিয়ে শুনছে।",
            "countdown_warning" to "সেফটি কাউন্টডাউন চলছে। ভুল অ্যালার্ম হলে বাতিল করুন।",
            "emergency_alert_active" to "ইমার্জেন্সি অ্যালার্ট সক্রিয়। নিকটবর্তী সেবা ম্যাপে দেখুন।",
            "emergency_actions_started" to "ইমার্জেন্সি অ্যাকশন শুরু",
            "emergency_actions_desc" to "RoadSOS অ্যালার্ট, লোকেশন এবং নিকটবর্তী সেবা প্রস্তুত করেছে।",
            "emergency_sos" to "ইমার্জেন্সি SOS",
            "cancel_countdown" to "কাউন্টডাউন বাতিল",
            "return_normal" to "নরমাল মোড",
            "tap_reset" to "ইমার্জেন্সি মোড রিসেট করুন",
            "tap_cancel" to "কাউন্টডাউন বাতিল করুন",
            "roadsos_ready" to "RoadSOS প্রস্তুত।",

            // -------------------------
            // PROFILE
            // -------------------------
            "medical_profile" to "মেডিক্যাল প্রোফাইল",
            "saved_emergency_info" to "RoadSOS-এর জন্য সংরক্ষিত জরুরি তথ্য।",
            "name" to "নাম",
            "blood_group" to "রক্তের গ্রুপ",
            "emergency_contact" to "জরুরি যোগাযোগ",
            "weight" to "ওজন",
            "height" to "উচ্চতা",
            "age" to "বয়স",
            "choose_language" to "ভাষা নির্বাচন করুন",
            "setup_profile" to "RoadSOS প্রোফাইল সেটআপ",
            "medical_details_help" to "আপনার মেডিক্যাল তথ্য জরুরি অবস্থায় সাহায্য করে।",
            "full_name" to "পুরো নাম",
            "save_continue" to "সংরক্ষণ করে এগিয়ে যান",

            // -------------------------
            // SETTINGS
            // -------------------------
            "dark_mode" to "ডার্ক মোড",
            "light_mode" to "লাইট মোড",
            "appearance_control" to "স্মুথ মিনিমাল থিম কন্ট্রোল",
            "manage_safety_profile" to "RoadSOS সেফটি প্রোফাইল এবং অ্যাপ অ্যাক্সেস ম্যানেজ করুন।",
            "settings_manage" to "সেটিংস ম্যানেজ",
            "all_permissions_granted" to "সব প্রয়োজনীয় অনুমতি দেওয়া হয়েছে",
            "some_permissions_missing" to "কিছু অনুমতি নেই",
            "logout" to "লগআউট",
            "erase_profile_data" to "RoadSOS থেকে সংরক্ষিত প্রোফাইল ডেটা মুছুন",
            "clear_data" to "ডেটা মুছুন",

            // -------------------------
            // MAP SCREEN
            // -------------------------
            "emergency_map" to "ইমার্জেন্সি ম্যাপ",
            "map_subtitle" to "অফলাইন সার্ভিস রাডার এবং নেভিগেশন",
            "live_emergency_radar" to "লাইভ ইমার্জেন্সি রাডার",
            "radar_desc" to "মাঝখানে আপনার দুর্ঘটনার লোকেশন। ডটগুলো নিকটবর্তী জরুরি সেবা।",
            "open_accident_location" to "দুর্ঘটনার লোকেশন খুলুন",
            "nearest_emergency_services" to "নিকটবর্তী জরুরি সেবা",
            "top_services_desc" to "প্রতিটি ক্যাটাগরি থেকে সেরা ৩টি সেবা",
            "loading_services" to "অফলাইন ডাটাবেস থেকে জরুরি সেবা লোড হচ্ছে...",
            "nearby" to "নিকটে",
            "you" to "আপনি",
            "accident_point" to "দুর্ঘটনা স্থান",
            "trauma" to "ট্রমা",
            "police" to "পুলিশ",
            "vehicle" to "যানবাহন",
            "km_away" to "কিমি দূরে",
            "verified" to "যাচাইকৃত",
            "unverified" to "যাচাই হয়নি",
            "route" to "রুট"
        )

    private val tamil =
        mapOf(
            // -------------------------
            // COMMON
            // -------------------------
            "app_name" to "RoadSOS",
            "home" to "ஹோம்",
            "map" to "மேப்",
            "settings" to "அமைப்புகள்",
            "profile" to "சுயவிவரம்",
            "language" to "மொழி",
            "change" to "மாற்று",
            "save" to "சேமி",
            "cancel" to "ரத்து",
            "edit" to "திருத்து",
            "not_saved" to "சேமிக்கப்படவில்லை",

            // -------------------------
            // HOME SCREEN
            // -------------------------
            "home_subtitle" to "ஆஃப்லைன் AI விபத்து கண்டறிதல்",
            "emergency_response_active" to "அவசர பதில் செயல்பாட்டில் உள்ளது",
            "crash_countdown_running" to "விபத்து கவுண்டவுன் இயங்குகிறது",
            "protection_status" to "பாதுகாப்பு நிலை",
            "active" to "செயலில்",
            "permission_needed" to "அனுமதி தேவை",
            "all_systems_working" to "அனைத்து அமைப்புகளும் சரியாக இயங்குகின்றன",
            "open_settings_grant_access" to "அனுமதி வழங்க அமைப்புகளைத் திறக்கவும்",
            "current_location" to "தற்போதைய இருப்பிடம்",
            "fetching_location" to "GPS இருப்பிடம் பெறப்படுகிறது...",
            "crash_detection" to "விபத்து கண்டறிதல்",
            "no_crash_detected" to "விபத்து கண்டறியப்படவில்லை",
            "crash_detected" to "விபத்து கண்டறியப்பட்டது",
            "emergency_sos_triggered" to "அவசர SOS தொடங்கப்பட்டது",
            "listening_ai" to "RoadSOS சாதனத்திலேயே AI மூலம் கேட்கிறது.",
            "countdown_warning" to "பாதுகாப்பு கவுண்டவுன் இயங்குகிறது. தவறான அலாரம் என்றால் ரத்து செய்யவும்.",
            "emergency_alert_active" to "அவசர அலர்ட் செயல்பாட்டில் உள்ளது. அருகிலுள்ள சேவைகளை மேப்பில் பார்க்கவும்.",
            "emergency_actions_started" to "அவசர நடவடிக்கைகள் தொடங்கின",
            "emergency_actions_desc" to "RoadSOS உங்கள் அலர்ட், இருப்பிடம் மற்றும் அருகிலுள்ள சேவைகளைத் தயாரித்துள்ளது.",
            "emergency_sos" to "அவசர SOS",
            "cancel_countdown" to "கவுண்டவுன் ரத்து",
            "return_normal" to "சாதாரண நிலை",
            "tap_reset" to "அவசர நிலையை மீட்டமைக்கவும்",
            "tap_cancel" to "கவுண்டவுனை ரத்து செய்யவும்",
            "roadsos_ready" to "RoadSOS தயார்.",

            // -------------------------
            // PROFILE
            // -------------------------
            "medical_profile" to "மருத்துவ சுயவிவரம்",
            "saved_emergency_info" to "RoadSOS க்கான சேமிக்கப்பட்ட அவசர தகவல்.",
            "name" to "பெயர்",
            "blood_group" to "இரத்த வகை",
            "emergency_contact" to "அவசர தொடர்பு",
            "weight" to "எடை",
            "height" to "உயரம்",
            "age" to "வயது",
            "choose_language" to "மொழியைத் தேர்வு செய்யவும்",
            "setup_profile" to "RoadSOS சுயவிவரம் அமைப்பு",
            "medical_details_help" to "உங்கள் மருத்துவ தகவல் அவசரத்தில் உதவும்.",
            "full_name" to "முழு பெயர்",
            "save_continue" to "சேமித்து தொடரவும்",

            // -------------------------
            // SETTINGS
            // -------------------------
            "dark_mode" to "டார்க் மோடு",
            "light_mode" to "லைட் மோடு",
            "appearance_control" to "மென்மையான குறைந்தபட்ச தோற்ற கட்டுப்பாடு",
            "manage_safety_profile" to "RoadSOS பாதுகாப்பு சுயவிவரம் மற்றும் பயன்பாட்டு அணுகலை நிர்வகிக்கவும்.",
            "settings_manage" to "அமைப்புகள் நிர்வகிப்பு",
            "all_permissions_granted" to "அனைத்து அனுமதிகளும் வழங்கப்பட்டுள்ளன",
            "some_permissions_missing" to "சில அனுமதிகள் இல்லை",
            "logout" to "வெளியேறு",
            "erase_profile_data" to "RoadSOS இல் சேமிக்கப்பட்ட சுயவிவரத்தை அழிக்கவும்",
            "clear_data" to "தரவை அழிக்கவும்",

            // -------------------------
            // MAP SCREEN
            // -------------------------
            "emergency_map" to "அவசர மேப்",
            "map_subtitle" to "ஆஃப்லைன் சேவை ரடார் மற்றும் வெளி வழிநடத்தல்",
            "live_emergency_radar" to "நேரடி அவசர ரடார்",
            "radar_desc" to "மையம் உங்கள் விபத்து இடம். புள்ளிகள் அருகிலுள்ள அவசர சேவைகள்.",
            "open_accident_location" to "விபத்து இடத்தைத் திறக்கவும்",
            "nearest_emergency_services" to "அருகிலுள்ள அவசர சேவைகள்",
            "top_services_desc" to "ஒவ்வொரு வகையிலும் முதல் 3 சேவைகள்",
            "loading_services" to "ஆஃப்லைன் தரவுத்தளத்தில் இருந்து அவசர சேவைகள் ஏற்றப்படுகின்றன...",
            "nearby" to "அருகில்",
            "you" to "நீங்கள்",
            "accident_point" to "விபத்து இடம்",
            "trauma" to "டிராமா",
            "police" to "போலீஸ்",
            "vehicle" to "வாகனம்",
            "km_away" to "கிமீ தூரம்",
            "verified" to "சரிபார்க்கப்பட்டது",
            "unverified" to "சரிபார்க்கப்படவில்லை",
            "route" to "வழி"
        )

    private val telugu =
        mapOf(
            // -------------------------
            // COMMON
            // -------------------------
            "app_name" to "RoadSOS",
            "home" to "హోమ్",
            "map" to "మ్యాప్",
            "settings" to "సెట్టింగ్స్",
            "profile" to "ప్రొఫైల్",
            "language" to "భాష",
            "change" to "మార్చు",
            "save" to "సేవ్",
            "cancel" to "రద్దు",
            "edit" to "ఎడిట్",
            "not_saved" to "సేవ్ కాలేదు",

            // -------------------------
            // HOME SCREEN
            // -------------------------
            "home_subtitle" to "ఆఫ్‌లైన్ AI క్రాష్ గుర్తింపు",
            "emergency_response_active" to "ఎమర్జెన్సీ స్పందన సక్రియంగా ఉంది",
            "crash_countdown_running" to "క్రాష్ కౌంట్‌డౌన్ నడుస్తోంది",
            "protection_status" to "రక్షణ స్థితి",
            "active" to "సక్రియం",
            "permission_needed" to "అనుమతి అవసరం",
            "all_systems_working" to "అన్ని సిస్టమ్స్ సరిగా పనిచేస్తున్నాయి",
            "open_settings_grant_access" to "అనుమతి ఇవ్వడానికి సెట్టింగ్స్ తెరవండి",
            "current_location" to "ప్రస్తుత స్థానం",
            "fetching_location" to "GPS స్థానం తీసుకుంటోంది...",
            "crash_detection" to "క్రాష్ గుర్తింపు",
            "no_crash_detected" to "క్రాష్ గుర్తించబడలేదు",
            "crash_detected" to "క్రాష్ గుర్తించబడింది",
            "emergency_sos_triggered" to "ఎమర్జెన్సీ SOS ప్రారంభమైంది",
            "listening_ai" to "RoadSOS డివైస్‌లోని AIతో వినుతోంది.",
            "countdown_warning" to "సేఫ్టీ కౌంట్‌డౌన్ నడుస్తోంది. తప్పు అలారం అయితే రద్దు చేయండి.",
            "emergency_alert_active" to "ఎమర్జెన్సీ అలర్ట్ సక్రియంగా ఉంది. సమీప సేవలను మ్యాప్‌లో చూడండి.",
            "emergency_actions_started" to "ఎమర్జెన్సీ చర్యలు ప్రారంభమయ్యాయి",
            "emergency_actions_desc" to "RoadSOS అలర్ట్, స్థానం మరియు సమీప సేవలను సిద్ధం చేసింది.",
            "emergency_sos" to "ఎమర్జెన్సీ SOS",
            "cancel_countdown" to "కౌంట్‌డౌన్ రద్దు",
            "return_normal" to "సాధారణ స్థితి",
            "tap_reset" to "ఎమర్జెన్సీ మోడ్ రీసెట్ చేయండి",
            "tap_cancel" to "కౌంట్‌డౌన్ రద్దు చేయండి",
            "roadsos_ready" to "RoadSOS సిద్ధంగా ఉంది.",

            // -------------------------
            // PROFILE
            // -------------------------
            "medical_profile" to "మెడికల్ ప్రొఫైల్",
            "saved_emergency_info" to "RoadSOS కోసం సేవ్ చేసిన అత్యవసర సమాచారం.",
            "name" to "పేరు",
            "blood_group" to "రక్త గ్రూప్",
            "emergency_contact" to "ఎమర్జెన్సీ కాంటాక్ట్",
            "weight" to "బరువు",
            "height" to "ఎత్తు",
            "age" to "వయస్సు",
            "choose_language" to "భాషను ఎంచుకోండి",
            "setup_profile" to "RoadSOS ప్రొఫైల్ సెటప్",
            "medical_details_help" to "మీ మెడికల్ సమాచారం ఎమర్జెన్సీలో సహాయపడుతుంది.",
            "full_name" to "పూర్తి పేరు",
            "save_continue" to "సేవ్ చేసి కొనసాగండి",

            // -------------------------
            // SETTINGS
            // -------------------------
            "dark_mode" to "డార్క్ మోడ్",
            "light_mode" to "లైట్ మోడ్",
            "appearance_control" to "స్మూత్ మినిమల్ థీమ్ కంట్రోల్",
            "manage_safety_profile" to "RoadSOS సేఫ్టీ ప్రొఫైల్ మరియు యాప్ యాక్సెస్ నిర్వహించండి.",
            "settings_manage" to "సెట్టింగ్స్ మేనేజ్",
            "all_permissions_granted" to "అన్ని అవసరమైన అనుమతులు ఇచ్చారు",
            "some_permissions_missing" to "కొన్ని అనుమతులు లేవు",
            "logout" to "లాగ్అవుట్",
            "erase_profile_data" to "RoadSOS నుండి సేవ్ చేసిన ప్రొఫైల్ డేటాను తొలగించండి",
            "clear_data" to "డేటా తొలగించండి",

            // -------------------------
            // MAP SCREEN
            // -------------------------
            "emergency_map" to "ఎమర్జెన్సీ మ్యాప్",
            "map_subtitle" to "ఆఫ్‌లైన్ సర్వీస్ రాడార్ మరియు నావిగేషన్",
            "live_emergency_radar" to "లైవ్ ఎమర్జెన్సీ రాడార్",
            "radar_desc" to "మధ్యలో మీ ప్రమాద స్థానం ఉంటుంది. డాట్స్ సమీప ఎమర్జెన్సీ సేవలు.",
            "open_accident_location" to "ప్రమాద స్థానం తెరవండి",
            "nearest_emergency_services" to "సమీప ఎమర్జెన్సీ సేవలు",
            "top_services_desc" to "ప్రతి కేటగిరీ నుండి టాప్ 3 సేవలు",
            "loading_services" to "ఆఫ్‌లైన్ డేటాబేస్ నుండి ఎమర్జెన్సీ సేవలు లోడ్ అవుతున్నాయి...",
            "nearby" to "సమీపంలో",
            "you" to "మీరు",
            "accident_point" to "ప్రమాద స్థానం",
            "trauma" to "ట్రామా",
            "police" to "పోలీస్",
            "vehicle" to "వాహనం",
            "km_away" to "కిమీ దూరం",
            "verified" to "ధృవీకరించబడింది",
            "unverified" to "ధృవీకరించబడలేదు",
            "route" to "రూట్"
        )

    private val marathi =
        mapOf(
            // -------------------------
            // COMMON
            // -------------------------
            "app_name" to "RoadSOS",
            "home" to "होम",
            "map" to "मॅप",
            "settings" to "सेटिंग्स",
            "profile" to "प्रोफाइल",
            "language" to "भाषा",
            "change" to "बदला",
            "save" to "सेव्ह",
            "cancel" to "रद्द",
            "edit" to "एडिट",
            "not_saved" to "सेव्ह नाही",

            // -------------------------
            // HOME SCREEN
            // -------------------------
            "home_subtitle" to "ऑफलाइन AI क्रॅश डिटेक्शन",
            "emergency_response_active" to "इमर्जन्सी प्रतिसाद सक्रिय आहे",
            "crash_countdown_running" to "क्रॅश काउंटडाउन सुरू आहे",
            "protection_status" to "सुरक्षा स्थिती",
            "active" to "सक्रिय",
            "permission_needed" to "परवानगी आवश्यक",
            "all_systems_working" to "सर्व सिस्टम योग्य काम करत आहेत",
            "open_settings_grant_access" to "परवानगीसाठी सेटिंग्स उघडा",
            "current_location" to "सध्याचे स्थान",
            "fetching_location" to "GPS स्थान घेत आहे...",
            "crash_detection" to "क्रॅश डिटेक्शन",
            "no_crash_detected" to "क्रॅश आढळला नाही",
            "crash_detected" to "क्रॅश आढळला",
            "emergency_sos_triggered" to "इमर्जन्सी SOS सुरू झाले",
            "listening_ai" to "RoadSOS डिव्हाइसवरील AI ने ऐकत आहे.",
            "countdown_warning" to "सेफ्टी काउंटडाउन सुरू आहे. चुकीचा अलार्म असल्यास रद्द करा.",
            "emergency_alert_active" to "इमर्जन्सी अलर्ट सक्रिय आहे. जवळच्या सेवा मॅपवर पहा.",
            "emergency_actions_started" to "इमर्जन्सी कृती सुरू",
            "emergency_actions_desc" to "RoadSOS ने अलर्ट, लोकेशन आणि जवळच्या सेवा तयार केल्या आहेत.",
            "emergency_sos" to "इमर्जन्सी SOS",
            "cancel_countdown" to "काउंटडाउन रद्द",
            "return_normal" to "नॉर्मल मोड",
            "tap_reset" to "इमर्जन्सी मोड रीसेट करा",
            "tap_cancel" to "काउंटडाउन रद्द करा",
            "roadsos_ready" to "RoadSOS तयार आहे.",

            // -------------------------
            // PROFILE
            // -------------------------
            "medical_profile" to "मेडिकल प्रोफाइल",
            "saved_emergency_info" to "RoadSOS साठी सेव केलेली इमर्जन्सी माहिती.",
            "name" to "नाव",
            "blood_group" to "ब्लड ग्रुप",
            "emergency_contact" to "इमर्जन्सी संपर्क",
            "weight" to "वजन",
            "height" to "उंची",
            "age" to "वय",
            "choose_language" to "भाषा निवडा",
            "setup_profile" to "RoadSOS प्रोफाइल सेटअप",
            "medical_details_help" to "तुमची मेडिकल माहिती इमर्जन्सीत मदत करते.",
            "full_name" to "पूर्ण नाव",
            "save_continue" to "सेव्ह करा आणि पुढे जा",

            // -------------------------
            // SETTINGS
            // -------------------------
            "dark_mode" to "डार्क मोड",
            "light_mode" to "लाईट मोड",
            "appearance_control" to "स्मूथ मिनिमल थीम कंट्रोल",
            "manage_safety_profile" to "RoadSOS सुरक्षा प्रोफाइल आणि अॅप अ‍ॅक्सेस व्यवस्थापित करा.",
            "settings_manage" to "सेटिंग्स मॅनेज",
            "all_permissions_granted" to "सर्व आवश्यक परवानग्या दिल्या आहेत",
            "some_permissions_missing" to "काही परवानग्या नाहीत",
            "logout" to "लॉगआउट",
            "erase_profile_data" to "RoadSOS मधील सेव प्रोफाइल डेटा हटवा",
            "clear_data" to "डेटा हटवा",

            // -------------------------
            // MAP SCREEN
            // -------------------------
            "emergency_map" to "इमर्जन्सी मॅप",
            "map_subtitle" to "ऑफलाइन सर्विस रडार आणि बाह्य नेव्हिगेशन",
            "live_emergency_radar" to "लाईव्ह इमर्जन्सी रडार",
            "radar_desc" to "मध्यभागी तुमचे अपघात स्थान आहे. डॉट्स जवळच्या इमर्जन्सी सेवा आहेत.",
            "open_accident_location" to "अपघात स्थान उघडा",
            "nearest_emergency_services" to "जवळच्या इमर्जन्सी सेवा",
            "top_services_desc" to "प्रत्येक कॅटेगरीतील टॉप 3 सेवा",
            "loading_services" to "ऑफलाइन डेटाबेसमधून इमर्जन्सी सेवा लोड होत आहेत...",
            "nearby" to "जवळ",
            "you" to "तुम्ही",
            "accident_point" to "अपघात स्थान",
            "trauma" to "ट्रॉमा",
            "police" to "पोलीस",
            "vehicle" to "वाहन",
            "km_away" to "किमी दूर",
            "verified" to "वेरिफाइड",
            "unverified" to "अनवेरिफाइड",
            "route" to "रूट"
        )
}