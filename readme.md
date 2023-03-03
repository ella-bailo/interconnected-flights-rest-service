# Interconnected Flights Rest Service

A reactive springboot rest service that consumes two ryanair APIs to produce a list of interconnected flights:

`https://services-api.ryanair.com/locate/3/routes`

&

`https://services-api.ryanair.com/timtbl/3/schedules/MAD/MAN/years/2023/months/11`

The application responses to the following uri:

`http://<HOST>/api/interconnections?departure={departure}&arrival=
{arrival}&departureDateTime={departureDateTime}&arrivalDateTime={arrivalDateTime}`

An example output can be seen below:

```
[
    {
        "stops": 0,
        "legs": [
    {
        "departureAirport": "DUB",
        "arrivalAirport": "WRO",
        "departureDateTime": "2018-03-01T12:40",
        arrivalDateTime": "2018-03-01T16:40"
    }
]
    },
    {
        "stops": 1,
        "legs": [
    {
        "departureAirport": "DUB",
        "arrivalAirport": "STN",
        "departureDateTime": "2018-03-01T06:25",
        "arrivalDateTime": "2018-03-01T07:35"
    },
    {
        "departureAirport": "STN",
        "arrivalAirport": "WRO",
        "departureDateTime": "2018-03-01T09:50",
        "arrivalDateTime": "2018-03-01T13:20"
    }
]
```

Running:

```
`mvn spring-boot:run`
```

The api is available on port `8080` e.g:

```
http://localhost:8080/api/interconnections?departure=MAN&arrival=AGP&departureDateTime=2023-03-01T06:00&arrivalDateTime=2023-03-01T19:00
```

### Testing

This is my first java, and spring boot project and as such have had to investigate each step in getting the project up and running and has meant that I have into some issues for the first time whilst familarising myself with the language and framework. Whilst setting up the tests I ran into an issue which I have not as of yet been able to resolve and unfortunately have not had the time to debug. I will therefore outline here what and how I had planed to test this app.

My main focus for testing would have been in creating unit tests for the services. In order to do this I had planned to use mockserver, and the mockserver expectation initilaizer plug-in order to mock the response from the ryanair APIs. In the test folder I have created the expectation initilaizer with mock data which automatically initialises expectations before each test.

I then would have used independency injection for each service in its respective test file to be able to call it, and created a mock client via the `@MockBean` annotation in order to allow the required clients to be callable from the services. This is unfortunately where I ran into problems, with the calls from the mock clients seemingly not being picked up/matching expectations (The responses from the clients returned null flux/monos instead of ones populated by the mock data).

Tests I would have written for each service:

**RyanairRoutesService**

- Returns a Flux of a list of correct routes (with one leg) when max legs is 1 (would have checked the presence of data in the blocked result);
- Returns a Flux of a list of correct routes (with up to two legs) when max legs is 2.
- Returns a Flux of a list of correct routes (with up to three legs) when max legs is 3.
- Returns an empty Flux if max legs 0.
- Returns an empty Flux if no routes found.
- All legs in Route are unique.

**RyanairSchedulesService**

- Returns a Flux of flights for a given date with correct data (would have checked the presence of data in the blocked result);
- Returns an empty flux if no routes found.

**RyanairInterconnectedFlightsService**

- Throws `BAD_REQUEST` (400) response status exception if departure and arrival are on the same day.
- Throws `BAD_REQUEST` (400) response status exception if arrival and departure airport are the same.
- Throws `BAD_REQUEST` (400) response status exception if arrival date time is before departure date time.
- Returns a Flux of correct `InterconnectedFlightDto`s for 1 leg.
- Returns a Flux of correct `InterconnectedFlightDto`s for 2 legs.
- Returns a Flux of correct `InterconnectedFlightDto`s for 3 legs.
- Returns an empty Flux if no interconnecting flights found.

Each check of the data in the InterconnectingFlightDto should check that:

- “stops” = `legs.length - 1`
- `legs.length` is no larger than maxLegs
- First leg departure airport matches departure airport
- Last leg arrival airport matches departure airport
- First leg departure time is not before `minDepatureTime`
- Last legs arrival time is not after `maxArrivalTime`
- Each legs departure time is not before the arrival time of the previous leg + `minTransferMinutes`.

I would have also written integration tests for each client against the actual APIs (most importantly to check filtering of `connectingAirport` & `operator`)

### What I would do next

1. Solve known issues (see below)
2. Validation around query param data being in the correct format (currently incorrect format will cause a 500 response rather than a 400) e.g. `“2023-03-01T06:00000000”` instead of `“2023-03-01T06:01”`;
3. Introduce logging
4. In memory caching
5. Dockerise build (docker & docker compose file)
6. Add commit hooks.
7. Ordering of `interconnectedFlightDto`s in the returned list.

**Known issues:**

- Tests as discussed above.
- Remove hardcoded data (`baseUrl`, uris, `maxLegs` & `minTransferMinutes`). I tried to do this but encountered a bug where getting the values using `@Value` annotations from the application.properties caused an error to be thrown. This would require some debugging.
