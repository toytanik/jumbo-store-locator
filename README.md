# Jumbo Store Locator

REST API that returns the Jumbo stores closest to a given position, ranking all 587
stores from the provided `stores.json` by great-circle distance.

```bash
curl "http://localhost:8080/api/v1/stores/nearest?latitude=52.0907&longitude=5.1214"
```

```json
{
  "origin": { "latitude": 52.0907, "longitude": 5.1214 },
  "stores": [
    {
      "id": "sWwKYx4XRAsAAAFIJoIYwKxK",
      "name": "Jumbo Utrecht Biltstraat",
      "city": "Utrecht",
      "postalCode": "3572 BG",
      "address": "Biltstraat 74",
      "latitude": 52.095381,
      "longitude": 5.129767,
      "distanceKm": 0.77
    }
  ]
}
```

## Running it

```bash
./mvnw spring-boot:run
```

Or with Docker:

```bash
./mvnw package
docker build -t jumbo-store-locator .
docker run -p 8080:8080 jumbo-store-locator
```

API at `http://localhost:8080` — Swagger UI at `/swagger-ui.html`, health at
`/actuator/health`.

## API

`GET /api/v1/stores/nearest`

| Param       | Required | Rules                          |
|-------------|----------|---------------------------------|
| `latitude`  | yes      | double, -90..90                |
| `longitude` | yes      | double, -180..180              |
| `limit`     | no       | int, default 5, min 1, max 50  |

Invalid, missing, or out-of-range parameters return `400` with an RFC 7807
`ProblemDetail` body naming the offending parameter.

## Testing

```bash
./mvnw test     # unit + slice tests, no Docker required
./mvnw verify   # also builds the real Docker image and hits it over HTTP (needs Docker)
```

## Notes

- No database — 587 static rows fit comfortably in memory and load once at startup.
  `StoreRepository` is an interface, so a real data source could replace it later.
- Brute-force distance search,fast enough at this scale.
  `NearestStoreService` hides the implementation, so that can change later too.
- Haversine distance, not flat-Earth, since it stays accurate away from the equator.
- No auth, rate limiting, pagination, or store-type/opening-hours filtering — out of
  scope for the brief. `todayOpen`/`todayClose` in the source data is a single-day
  snapshot, so it can't support a correct "open now" filter.

## AI usage
I designed the project and actively used ai to create tests.

## Time spent
I spent 3 hours 
