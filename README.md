# RKMD Seat Booking API

API for seat booking at RKMD Toki no Nagare

- [RKMD Seat Booking API](#rkmd-seat-booking-api)
  - [Contact Endpoints](#contact-endpoints)
    - [GET (retrieve all contacts)](#get-retrieve-all-contacts)
    - [POST (create a new contact)](#post-create-a-new-contact)
  - [User Endpoints](#user-endpoints)
    - [GET (retrieve user by ID)](#get-retrieve-user-by-id)
    - [POST (create a new user)](#post-create-a-new-user)
  - [Seat Endpoints](#seat-endpoints)
    - [GET seat](#get-seat)
    - [GET recommendations](#get-recommendations)
    - [PUT (to change the seat's status)](#put-to-change-the-seats-status)
    - [POST (WARNING: This endpoint goal is JUST to bootstrap the theater seats)](#post-warning-this-endpoint-goal-is-just-to-bootstrap-the-theater-seats)
  - [Local Usage](#local-usage)
  - [Consultas](#consultas)

## Contact Endpoints

### GET (retrieve all contacts)

Retrieve contact information by ID.

```bash
curl --location 'localhost:8080/contact/35345'
```

### POST (create a new contact)

```bash
curl --location 'localhost:8080/contact' \
--header 'Content-Type: application/json' \
--data '{
    "name": "noe",
    "last_name": "shokita",
    "dni": "35345",
    "username": "noeshoki",
    "password": "1234",
    "role": "admin"
}'
```

## User Endpoints

### GET (retrieve user by ID)

```bash
curl --location 'localhost:8080/user/1'
```

### POST (create a new user)

```bash
curl --location 'localhost:8080/user' \
--header 'Content-Type: application/json' \
--data '{
    "username": "yu.nakasone",
    "password": "1234",
    "role": "viewer"
}'
```

## Seat Endpoints

### GET seat

```bash
curl --location 'localhost:8080/seat?sector=pullman&row=1&column=27'
```

### GET recommendations

```bash
curl --location 'localhost:8080/seat/recommendation?seat_count=5&sector=platea&row=20'
```

### PUT (to change the seat's status)

```bash
curl --location --request PUT 'localhost:8080/seat/platea/21/27/reserved' \
--header 'Content-Type: application/json' \
--data '{
"sector": "pullman",
"row": "1",
"column": "27",
"status": "reserved"
}'
```

### POST (WARNING: This endpoint goal is JUST to bootstrap the theater seats)

```bash
curl --location --request POST 'localhost:8080/seat/bootstrap'
```

## Local Usage

Running locally the application from Main will use H2 to set the DB at memory.

You can enter this URL to check the DB in a console like MySqlWorkbench

```bash
http://localhost:8080/h2-console
```

If it does not work, check:

```bash
- Saved Settings & Setting Name: Generic H2 (Embedded)
- Driver Class: "org.h2.Driver"
- JDBC URL: "jdbc:h2:mem:testdb"
- User Name: "sa"
- Password: ""  (empty)
```

## Envs
~~~conf
APP_PORT=5000
DB_DDL_AUTO=update
DB_HOST=rkmd-seat-booking.cvc7mgpitmse.us-east-2.rds.amazonaws.com
DB_NAME=
DB_PORT=3306
DB_USERNAME=root
DB_PASSWORD=Rkmd4dm1n2023
~~~

## Consultas

- Que pasa unos asientos que estaban reservados expiran? Van a quedar lugares libres en el medio
