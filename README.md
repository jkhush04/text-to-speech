# TTS Studio — Text-to-Speech Web App

A full-stack Text-to-Speech application built with **React** (frontend) and **Java Spring Boot** (backend), using **ElevenLabs** for multilingual speech generation and **PostgreSQL** for user accounts and speech history.

## Features

- Convert text to natural-sounding speech in 7 languages: English, Hindi, Gujarati, Marathi, Spanish, French, German
- User authentication (JWT-based) — register, login
- Per-user speech history
- Audio playback and download in-browser
- Full error handling with proper HTTP status codes

## Tech Stack

**Frontend:** React (Vite), Tailwind CSS, Axios
**Backend:** Java 17, Spring Boot 3, Spring Security, Spring Data JPA, Maven
**Database:** PostgreSQL
**TTS Provider:** ElevenLabs API (`eleven_v3` model)
**Auth:** JWT (JJWT library), BCrypt password hashing

## Project Structure

text-to-speech/
├── frontend/ # React app
└── backend/ # Spring Boot app


## Prerequisites

- Java 17+
- Node.js 18+
- PostgreSQL (running locally, or a connection string to a hosted instance)
- An ElevenLabs account (free tier) — [elevenlabs.io](https://elevenlabs.io)

## Setup

### 1. Database

```sql
CREATE DATABASE tts_db;
```

### 2. Backend

```bash
cd backend
cp .env.example .env   # then fill in real values — see below on how env vars are actually loaded
```

> **Note:** Spring Boot does not read `.env` files natively. Set these as actual environment variables in your terminal or IDE run configuration:

| Variable | Description |
|---|---|
| `ELEVENLABS_API_KEY` | Your ElevenLabs API key (Profile → API Keys) |
| `DB_USERNAME` | PostgreSQL username |
| `DB_PASSWORD` | PostgreSQL password |
| `JWT_SECRET` | A random secret, 256+ bits — generate with `openssl rand -hex 64` |

Run:
```bash
./mvnw spring-boot:run
```
Backend runs on `http://localhost:8080`.

### 3. Frontend

```bash
cd frontend
cp .env.example .env   # Vite loads this automatically — no changes needed for local dev
npm install
npm run dev
```
Frontend runs on `http://localhost:5173`.

## API Endpoints

| Method | Endpoint | Auth required | Description |
|---|---|---|---|
| POST | `/api/auth/register` | No | Create an account |
| POST | `/api/auth/login` | No | Log in, returns JWT |
| GET | `/api/voices` | No | List available voices/languages |
| GET | `/api/health` | No | Health check |
| POST | `/api/tts` | **Yes** | Generate speech from text |
| GET | `/audio/{filename}` | **Yes** | Retrieve generated audio |
| GET | `/api/history` | **Yes** | Get logged-in user's past generations |

## Important Notes

- ElevenLabs' free tier does not allow API access to general "Voice Library" voices — only voices already in your account's own voice list (`GET /v1/voices`) work via the API.
- Speech generation uses the `eleven_v3` model specifically, since `eleven_multilingual_v2` does not support Gujarati or Marathi.

## Future Improvements

- Deploy frontend (Vercel) and backend (Render/Railway)
- Auto-refresh history after a new generation
- Optional file upload (PDF/DOCX/TXT) to convert existing documents to speech