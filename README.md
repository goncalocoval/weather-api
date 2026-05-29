# weather-api 🌤️

> just another api that checks the weather.

A simple REST API built with Java and Spring Boot that fetches current weather and 5-day forecasts using the OpenWeatherMap API. Includes Redis caching, global error handling, and Swagger documentation.

---

## Tech Stack

- **Java 17**
- **Spring Boot 4.0.6**
- **Spring Web** — REST API
- **Spring Cache + Redis** — caching layer
- **SpringDoc OpenAPI** — Swagger UI
- **Docker + Docker Compose** — containerization
- **OpenWeatherMap API** — weather data

---

## Features

- 🌡️ Current weather by city (temperature, humidity, description)
- 📅 5-day forecast grouped by day (min/max temp, precipitation probability)
- 🌍 Optional country code to avoid city name ambiguity
- ⚡ Redis cache with 5-minute TTL
- 🛡️ Global error handling with clean JSON responses
- 📖 Swagger UI for interactive API documentation

---

## Getting Started

### Prerequisites

- [Docker](https://www.docker.com/) installed and running
- [OpenWeatherMap API key](https://openweathermap.org/api) (free tier is enough)

### Installation

**1. Clone the repository**
```bash
git clone git@github.com:your-username/weather-api.git
cd weather-api
```

**2. Set up your configuration**
```bash
cp application.properties.example src/main/resources/application.properties
```

Open `src/main/resources/application.properties` and replace `YOUR_API_KEY_HERE` with your OpenWeatherMap API key.

**3. Run with Docker Compose**
```bash
docker-compose up --build
```

The API will be available at `http://localhost:8080`.

---

## API Endpoints

### `GET /weather`

Returns current weather for a given city.

| Parameter | Required | Default | Description |
|---|---|---|---|
| `city` | ✅ | — | City name |
| `country` | ❌ | — | Country code (e.g. `PT`, `ES`) |
| `units` | ❌ | `metric` | `metric` (°C) or `imperial` (°F) |
| `lang` | ❌ | `pt` | Language for description |

**Example request**
```
GET /weather?city=Lisboa&country=PT&units=metric&lang=en
```

**Example response**
```json
{
  "city": "Lisbon",
  "country": "PT",
  "temperature": 24.02,
  "description": "Few Clouds",
  "humidity": 62
}
```

---

### `GET /weather/forecast`

Returns a 5-day daily forecast for a given city.

| Parameter | Required | Default | Description |
|---|---|---|---|
| `city` | ✅ | — | City name |
| `country` | ❌ | — | Country code (e.g. `PT`, `ES`) |
| `units` | ❌ | `metric` | `metric` (°C) or `imperial` (°F) |
| `lang` | ❌ | `pt` | Language for description |

**Example request**
```
GET /weather/forecast?city=Lisboa&country=PT&units=metric&lang=en
```

**Example response**
```json
{
  "city": "Lisbon",
  "country": "PT",
  "forecast": [
    {
      "date": "2026-05-30",
      "tempMin": 17.23,
      "tempMax": 26.13,
      "description": "Scattered Clouds",
      "humidity": 40,
      "precipitationProbability": 0
    }
  ]
}
```

---

### Error Responses

| Status | Description |
|---|---|
| `404` | City not found |
| `500` | Unexpected server error |

**Example error response**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "City not found: InvalidCity"
}
```

---

## Swagger UI

Interactive API documentation is available at:

```
http://localhost:8080/swagger-ui/index.html
```

---

## Project Structure

```
src/main/java/com/example/weatherapi/
│
├── WeatherApiApplication.java
│
├── controller/
│   └── WeatherController.java
│
├── service/
│   └── WeatherService.java
│
├── model/
│   ├── WeatherResponse.java
│   ├── ForecastResponse.java
│   ├── ForecastDay.java
│   └── ErrorResponse.java
│
└── exception/
    ├── CityNotFoundException.java
    └── GlobalExceptionHandler.java
```

---

## License

Do whatever you want with it.
