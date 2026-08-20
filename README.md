# Equ — MVP

Android app for psychotherapists: shareable landing page, client bookings,
reminders/recaps, and client profiles. Native Android (Kotlin + Jetpack
Compose). See `ARCHITECTURE.md` for the full design and phased plan.

## What works right now (no API keys needed)

- **Clients tab** — full local CRUD: add/edit/delete client profiles with
  name, contact info, notes, and a photo, stored on-device (Room database).
- **Landing Page tab** — draft and save your page content (name, bio,
  services) locally.

## What's stubbed as "Coming soon"

Anything that needs a third-party API key or a backend that isn't wired up
yet shows a "Coming soon" banner in the app instead of a broken feature:

- **Publishing** the landing page to a real shareable link/QR — needs the
  hosting backend.
- **Bookings tab** — needs a Calendly or cal.com API key (OAuth connect).
- **Reminders & Recaps tab** — needs Firebase Cloud Messaging plus booking
  webhooks from Calendly/cal.com.

As those pieces get connected (see `ARCHITECTURE.md` §5 for the open
decisions), each banner gets replaced with the real feature — no rewrite
needed, the screens are already there.

## Getting an installable APK

This project was scaffolded in a sandbox with no access to the Android SDK
or `services.gradle.org`, so no `gradlew`/Gradle wrapper jar is committed
and no APK has been built here yet. Two ways to get one:

1. **GitHub Actions (recommended)** — `.github/workflows/android-build.yml`
   builds a debug APK on every push. After a push, open the **Actions** tab
   on GitHub, open the latest run, and download the `equ-debug-apk`
   artifact. Unzip it, copy `app-debug.apk` to your phone, and install it
   (enable "Install unknown apps" for whichever app you use to open it —
   one-time setting).
2. **Android Studio** — open this folder in Android Studio; it will offer
   to regenerate the Gradle wrapper automatically, then Run ▸ on a
   connected device or emulator.

## Project layout

```
app/src/main/java/com/equ/app/
  MainActivity.kt
  ui/navigation/     bottom-nav + NavHost wiring the four tabs
  ui/screens/         one file per tab + client detail/edit screen
  ui/components/      ComingSoonBanner (shared placeholder for API-gated features)
  ui/theme/
  data/local/         Room (clients) + DataStore (landing page draft)
  data/repository/
```
