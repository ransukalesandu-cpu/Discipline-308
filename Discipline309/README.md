# 309 Day Discipline — Android app

Features included:
- 309-day countdown target (Aug 1, 2027)
- Daily 6-item checklist, completion percentage, streak and best streak
- Local data storage; no login required
- Daily alarms using Android AlarmManager with exact/Doze-friendly scheduling
- Notification + Sinhala TTS attempt when an alarm fires, including screen-off/locked use
- Re-scheduling hook on reboot/time changes
- Strict motivational coaching messages
- Built-in offline discipline assistant (Sinhala/English)
- Settings guidance for notification/exact-alarm permissions

## Build
Open this folder in Android Studio and let it sync Gradle. Then Build > Build APK(s).

## Voice
The app requests Sinhala (`si-LK`) TTS. Android's installed TTS engine/voice determines whether a natural female Sinhala voice is available. If not available, install/select a Sinhala female voice in Android Text-to-Speech settings.

## Cloud AI
No private API key is bundled. A real cloud AI chat should use a secure backend or user-supplied provider credentials; do not hard-code a secret into the APK.
