# Movies Management Application

## Table of Contents
- [Requirements](#requirements)
- [Running the application](#running-the-application)
    - [Docker](#run-with-docker)
- [Database](#database)
- [Usage](#usage)
- [Tests](#tests)
## Requirements
For building and running the application you need:

- [JDK 17 or newer](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

## Running the application

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.goncalo.movies.MoviesApplication` class from your IDE.

### Run with Docker

* Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)
* Create the necessary Docker Images and Containers by running the following command in the project's root folder:

```shell
docker compose up --build -d
```

## Database
* In-memory database available at `http://localhost:8080/h2-console/`
  - JDBC URL: `jdbc:h2:mem:moviesdb`
  - User Name: `sa`

## Usage
It is recommended to use tools like [Postman](https://www.postman.com/) to interact with the API. The API is available at `http://localhost:8080/api`

#### Create a movie
```
POST /movies

{
  "title":"Interstellar",
  "launchDate":"2014-11-06",
  "rank":10,
  "revenue":200000000
}

Response: HTTP 201
Content: Created movie
```

#### List movies

```
GET /movies

Response: HTTP 200
Content: All movies stored
```

To retrieve a movie by its ID:

```
GET /movies/{movieId}

Response: HTTP 200
Content: Movie requested
```

Filter movies by their launch date within a specified range. The date format should be YYYY-MM-DD.
For example, to get the movies between 2021-01-01 and 2023-12-31: `GET /movies/filter?startDate=2021-01-01&endDate=2023-12-31`

```
GET /movies/filter?startDate={start_date}&endDate={end_date}

Response: HTTP 200
Content: Filtered movies
```

#### Update a Movie
```
PUT /movies/{movieId}

{
  "title":"Interstellar",
  "launchDate":"2014-11-06",
  "rank":9,
  "revenue":500000000
}

Response: HTTP 200
Content: Updated movie
```

#### Delete a movie
```
DELETE /movies/{movieId}

Response: HTTP 200
```

## Tests

To view the collective test results, utilize Docker Compose:

`````shell
docker compose run movie-tests
`````

Alternatively, you can individually view and execute the tests using your preferred IDE by running them from the `test` package.
