# seat-booking-api
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
