# rkmd-seat-booking-api
Seat booking api for RKMD Toki no Nagare

# Contact Endpoints

## GET
curl --location 'localhost:8080/contact/35345'

## POST
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

# User Endpoints

## GET
curl --location 'localhost:8080/user/1'

## POST
curl --location 'localhost:8080/user' \
--header 'Content-Type: application/json' \
--data '{
    "username": "yu.nakasone",
    "password": "1234",
    "role": "viewer"
}'

# Seat Endpoints

## GET
curl --location 'localhost:8080/seat/A/27'

## POST
curl --location 'localhost:8080/seat' \
--header 'Content-Type: application/json' \
--data '{
"row": "A",
"column": "27",
"status": "reserved"
}'

# Local Usage
Running locally the application from Main will use H2 to set the DB at memory.

You can enter this URL to check the DB in a console like MySqlWorkbench
http://localhost:8080/h2-console

If it does not work, check:
- Saved Settings & Setting Name: Generic H2 (Embedded)
- Driver Class: "org.h2.Driver"
- JDBC URL: "jdbc:h2:mem:testdb"
- User Name: "sa"
- Password: ""  (empty)



