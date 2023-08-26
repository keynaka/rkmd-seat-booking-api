# Changelog

### 2023.08.26
- feat: Se agrega endpoint para envío de e-mails. (76920b3)
- feat: Se implementa TransportMailSenderImpl para envío de e-mails utilizando dependencias del paquete jakarta.mail. (3d7c245)
- feat: Se agrega a archivo pom.xml dependencia para envío de e-mails. (b5cd483)
- feat: Se agrega a application.yaml y application-prod.yaml campos para envío de e-mails. (2410f15)
- feat: Se crea interfaz IMailingService para envío de e-mails. (e893c18)
- feat: Se crea la clase EmailDto para enviar e-mails. (d4f59b9)

### 2023.08.24
- fix: Asientos faltantes en Platea
- fix: Agregar response headers para CORS
- feat: Agregar getAllSeats
- feat: Agregar getSectorSeatsByRow
- fix: Agregar logger
- fix: Recommendation arreglado + Sacar logs
- refactor: Sacar PALCOS + Actualizar mejores asientos de PLATEA a row 14

### 2023.08.22
- refactor: Se modifica validación de estado de Seat (a2d1c28)
- refactor: Se agrega anotación @Transactional en el cambio de estado de Payment, Booking y Seat (15c849a)
- refactor: Se modifica implementación en el método que actualiza el estado de Payment, Seats y Booking (66c4bbc)
- refactor: Se modifica la relación entre entidades Payment y Booking (d3c2c40)
- refactor: quito simbolos al generar el bookingCode, ya que traen conflictos al ser utilizados en una petición GET (356177d)
- fix: Se corrige error al obtener los datos de una reserva (fb8d30e)
- feat: Se agrega método para validar un BookingCode + Dni (8f2ad9d)

### 2023.08.21
- refactor: Se modifica método de creación de Bookings para evitar NullPointer y errores de duplicidad. Se agrega anotación @Transactional para evitar persistencia de datos ante un error (5420646)
- feat: Se agrega método que asocia la entidad Booking con Seat y modifica el 'status' de Seat según corresponda (9807672)
- refactor: Se quitan algunos simbolos en la generación del BookingCode debido a errores que se pueden presentar al colocar el bookingCode en la URI de un método del tipo GET (324d972)
- refactor: Se modifica el método de creación de un Contacto. En caso de que "NO EXISTA" se crea un nuevo contacto y en caso de que "EXISTA", se modifican los datos existentes (5b30b9c)
- fix: Se corrige error de NullPointer al crear la respuesta de una reserva y se corrige error al intentar persistir un Seat ya existente. (3ab3296)
- fix: Se quita nullable en la propiedad last_updated de Booking (9d4c8ba)
- fix: se crean envs RESERVATION_MERCADOPAGO_EXPIRATION_TIME y RESERVATION_CASH_EXPIRATION_TIME para manejo del tiempo de expiración de las reservar. (c94554d)
- fix: archivo pom.xml para poder buildear el binario .jar con mvn install. (811f317)

### 2023.08.19
- feat: Se agregan clases DTO para generar una reserva (4662364)
- feat: Se agrego implementación para reservar butacas (1c60892)
- feat: Se agrega documentación de BookingController (408f168)
- feat: Se agrega constructor a clases Booking y Contact (9fbc239)
- feat: Se agrega método (en Tools) para generar el código de reserva de forma aleatoria (148f094)
- feat: Se agrega endpoint e implementación para obtener los datos de una reserva (115cc4a)
- feat: Se agregan clases DTO para obtener los datos de una reserva (047a7b2)
- refactor: Se agregan anotaciones de Swagger en enums (2a42dc5)
- feat: Se agrega enum PhoneType para determinar que tipo de telefono ingresa el usuario (9ef7301)

### 2023.08.18
- refactor: Se quita método de '.getBalance()' del controller Payment para migrarlo a otro controller (391b772)
- feat: Se agrega implementación del cambio de estado de un pago, reserva y butaca (72a4983)

### 2023.08.17
- feat: Se agrega implementación para el cambio de estado de un pago y de una reserva (f4ae7b6)
- build: Se agrega dependencia para PostgreSQL (28c6ea5)
- feat: Se agrega nuevo controlador Payment con su respectiva documentación y servicio (6bf1281)
- feat: Se agregan las clases DTO para la entidad Payment (aeba1e9)
- feat: Se agrega servicio gestiona los pagos (969474a)
- refactor: Se agregan nuevas variables de configuración que definen el tiempo limite de pago según la forma de pago elegida por el usuario. (790f5a8)
- feat: agrego clase Tools que contiene método que retorna la fecha y hora actual (e81ab4d)
- refactor: Se agrega la interfaz de PaymentRepository (5df6b32)
- refactor: Se agrega la relación de la entidad Booking con la entidad Payment (fff1292)
- feat: Se agrega nueva entidad Payment y enums donde se define "la forma de pago" y "el estado del pago" (646bb82)

### 2023.08.16
- docs: Se actualiza el archivo README y se agrega nueva variable de entorno 'SPRING_PROFILES_ACTIVE' para el manejo de perfiles (8cc2e8b)
- feat: Se agrega tres nuevos application.yml (h2, postgresql, prod) para el uso de perfiles (9d64dbd)

### 2023.08.15
- feat: Se agrega enpoints para la asignación de precios por sector o por fila (edfeda2)
- feat: Se agrega DTO para la asignación de precios por sector y se agrega Constantes para la asignación de precios por fila (9f48922)
- refactor: Se agrega el atributo 'price' en la entidad Seat para fijar el precio de las butacas (53c05de)

- feat: Se agrega la interfaz de UserController.java que contiene las anotaciones para la documentación de Swagger. Y se agregan dos nuevos endpoints para la creación del usuario y la obtención de datos del usuario. (6376e3c)
- feat: Se agregan nuevos métodos en UserService.java. Método de creación de usuario, método para obtener datos del usuario, validaciones de userName, password y role. (7642390)
- feat: Se agrega el servicio de autenticación para el hashing de contraseñas (c2cc60e)
- feat: Se agregan clases DTO de User para las request y response a nivel Controller (10d6ef0)
- feat: Se agregan dependencias de Lombok y ModelMapper al pom.xml (bde96dc)
- feat: Se agrega la configuración inicial de Swagger y se agrega una nueva variable de entorno: SWAGGER_ENABLED (2b993c6)
- build: Se modifica la dependencia de SpringDoc OpenApi por incompatibilidad con la versión de Spring (31ccef6)
- build: Se agrega la dependencia de SpringDoc OpenApi para generar la documentación del servicio (c0f16af)
- docs: Se actualiza archivo .gitignore (f4d167d)
- docs: Se elimina la carpeta .idea (01216a1)
- docs: Se crea archivo CHANGELOG.md (a944886)
