# Movies Management Application

## Table of Contents
- [Requirements](#requirements)
- [Running the application](#running-the-application)
    - [Docker](#run-with-docker)
- [Database](#database)
- [Usage](#usage)

## Requirements
For building and running the application you need:

- [JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

## Running the application

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `com.goncalo.movies.MoviesApplication` class from your IDE.

### Run with Docker

* Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)
* Create the necessary Docker Images and Containers by running the following command in the project's root folder:
```
docker compose up --build -d
```

## Database
* In-memory database available at `http://localhost:8080/h2-console/`
  - JDBC URL: `jdbc:h2:mem:moviesdb`
  - User Name: `sa`

## Usage
It is recommended to use tools like Postman to interact with the API.

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
```
GET /movies/filter?startDate={start_date}&endDate={end_date}

Response: HTTP 200
Content: Filtered movies
```

#### Update a Movie
To update a movie, include only the values to be updated in the request body.
```
PUT /movies/{movieId}

{
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
