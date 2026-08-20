# Equ — Android App for Psychotherapists: MVP Architecture & Build Plan

Companion doc to the feasibility discussion. Scope: Android app for therapists to (1)
publish a simple shareable landing page, (2) let clients book via Calendly/cal.com,
(3) surface reminders and session recaps, (4) maintain client profiles with notes and
images.

## 1. Recommended stack

| Layer | Choice | Why |
|---|---|---|
| Mobile app | Kotlin + Jetpack Compose (native Android) | Scope is Android-only for now; native avoids cross-platform overhead until iOS is actually needed. |
| Backend | Supabase (Postgres + Auth + Storage + Row Level Security) | Ships auth, DB, file storage and RLS out of the box; Postgres is a defensible long-term choice for clinical-adjacent data; BAA available on paid tier if/when HIPAA applies. Firebase is a reasonable alternative if the team prefers it. |
| Landing pages | Small Next.js (or plain static) site, one route/slug per therapist, deployed on Vercel/Cloudflare Pages, content pulled from the backend | Keeps the "page builder" to a form (photo, bio, services, contact, booking link) rather than a real drag-and-drop editor — that's the difference between a 2-week feature and a 2-month one. |
| Scheduling | Calendly API/OAuth + embedded scheduling widget, **or** self-hosted cal.com | Don't build a booking engine. Calendly = fastest to integrate. cal.com (self-hosted) = more control and data residency, relevant if HIPAA scope grows — worth deciding early, see §5. |
| Push notifications | Firebase Cloud Messaging | Standard choice, works regardless of backend. |
| File storage (client photos/attachments) | Supabase Storage or S3, server-side encryption, short-lived signed URLs, access scoped per therapist via RLS | Attachments are the most sensitive payload in the system — never store them world-readable or with permanent public URLs. |

## 2. High-level flow

```
Therapist (Android app)
  │
  ├─ Onboards → creates landing page config → published to hosted slug (equ.app/t/<slug>)
  │                                              │
  │                                              └─ Client opens link → sees bio + "Book a session"
  │                                                    → redirected to Calendly/cal.com widget (therapist's own account)
  │
  ├─ Connects Calendly/cal.com account (OAuth) once
  │       └─ Booking webhook (new/changed/cancelled event) → backend
  │                                                              ├─ schedules FCM reminder push (therapist + optionally client)
  │                                                              └─ creates "appointment" record → shows in app timeline
  │
  └─ Maintains client profiles in-app
          └─ notes + photos → backend (encrypted at rest, per-therapist row-level access only)
```

## 3. Data model sketch

- `therapists` — id, auth_id, display_name, bio, contact, landing_page_slug, calendly_or_calcom_credentials
- `landing_pages` — id, therapist_id, slug, template_config (photo, bio, services), published (bool)
- `clients` — id, therapist_id, name, contact_info, status
- `client_notes` — id, client_id, therapist_id, body, created_at, updated_at
- `attachments` — id, note_id, storage_path, mime_type
- `appointments` — id, client_id, therapist_id, external_booking_id, start_time, status, reminder_sent_at, recap_text

All client-scoped tables carry `therapist_id` with Postgres RLS policies so a therapist can only ever query their own rows — this is the single most important guardrail in the whole system.

## 4. Phased build plan (MVP, 1–2 developers)

| Phase | Scope | Estimate |
|---|---|---|
| 0 — Foundations | Project setup, auth, therapist onboarding, pick Calendly vs. cal.com (see §5) | 1–2 wks |
| 1 — Landing page + booking | Form-based landing page builder, hosted page generation, OAuth connect to Calendly/cal.com, embedded booking, shareable link/QR | 2–3 wks |
| 2 — Reminders & recaps | Webhook receiver for booking events, FCM reminder pushes, in-app recap screen | 2 wks |
| 3 — Client profiles | Client CRUD, notes, image upload, search/filter | 2–3 wks |
| 4 — Security/compliance hardening | Encryption at rest/in transit, RLS audit, access logging, BAAs with every vendor touching data, retention/deletion policy, consent flows | 2–4 wks — **run in parallel starting Phase 0, not bolted on at the end** |
| 5 — Beta polish | Play internal testing track, feedback loop, bug fixes | 1–2 wks |

**Total: ~10–14 weeks** to a real MVP, assuming compliance work runs alongside feature work rather than after it.

## 5. Open decisions that materially change scope

1. **Calendly vs. self-hosted cal.com.** Calendly is faster to integrate; cal.com (self-hosted) gives you control over where booking data lives, which matters a lot once client bookings are tied to real health-adjacent data.
2. **Will beta/testing use real client data, or synthetic data only?** This single decision determines whether HIPAA/GDPR obligations apply from day one or can be deferred past the prototype stage.
3. **Vendor BAAs.** Whichever backend, storage, and push-notification vendors are chosen, each one touching real client data needs a signed Business Associate Agreement (US/HIPAA) before it goes live with real clients.
4. **iOS later?** Native Android now is fine, but if iOS is likely within a year, it's worth revisiting Flutter/React Native before Phase 1 locks in native-only UI code.
