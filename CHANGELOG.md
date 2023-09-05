# Changelog

### 2023.09.05
- refactor: Se remueve endpoint de consulta de reservas de BookingController a ReportController (176a3f4)
- fix: Se quita validación de bookingCode con BCrypt cuando se consulta el estado de la reserva (2e42c1f)
- feat: Se agrega configuración de logback para mejorar la visualización del log del servicio (7761ec5)
- build: Se agrega dependencias de logback para mejorar el log del servicio (e7e73ae)
- fix: Se agrega env para referenciar ubicación del archivo .png para el header el e-mail. (8233291)
- fix: Agrego metodos de equals() y hashCode() para sacar warning de composite-id

### 2023.09.04
- fix: Infinite serialization for json

### 2023.09.02
- feat: Se agrega a AbstractMailingService método abstracto para enviar e-mail en formato texto. (a9446c2)
- test: Se agregan test de carga de templates html para confirmacion y expiracion. (1dbad13)
- fix: Se remueven templates de e-mails en formato .txt. (dd32310)
- test: se corrigen test en TransportMailSenderImplTest. (b7ada32)
- fix: Se remueve implementacion JavaMailSenderImpl. (c5e1ae3)
- feat: Agrego ExpirationServices + Factory
- feat: Agrego ExpirationJob
- test: Agrego tests para Cash y MP ExpirationServices
- feat: Se agrega envío de e-mail al finalizar una reserva provisoria. (a6f1b97)
- feat: Se agrega a TransportMailSenderImpl uso de perfiles para envío de e-mails. (11ee407)
- fix: Se agrega constructor vacío a SeatDto. (4f31cce)
- feat: Se agrega manejo de perfil en envío de e-mails para diferencias producción y testing. (59953a5)
- fix: Se agrega app.profile a application-prod.yml para manejo de entorno. (93189bc)
- fix: Se agrega archivo application-testing.yaml. (bab00f2)
- fix: Se corrige archivo reservation-mp-template.html. (4a7eaa2)
- feat: Se implementa notificación por e-mail para confirmar un pago. (66a738b)
- fix: Se agrega env DB_CONNECTION_PARAMS para setear parámetros en el string de conexión a la db. (c75ff4d)

### 2023.08.31
- feat: Agrego endpoint de PUT /prereserve
- refactor: Modifico endpoint de POST /v2/bookings para que valide no solo que sea vacante sino tambien que este prereservado.
- feat: Implementacion de toda la logica de prereserve de 5 minutos
- fix: Se agrega endpoint para testing de e-mails de expiración de reserva. (c9e97b8)
- feat: Se agrega método para notificar expiración de reserva por e-mail. (fe6e6aa)
- feat: Se agrega template html para envío de e-mail de expiración de reserva. (9e089e3)

### 2023.08.30
- feat: Agrego endpoint de GET /v1/report/booking/{code_id} que devuelve todo lo relacionado al booking para los admin
- feat: Agrego endpoint de GET /v3/bookings/{code_id} que devuelve solo el status
- refactor: Limpio un par de endpoints y metodos legacy
- refactor: Modifico PUT /v1/payments para que solo necesite el bookingCode y el paymentStatus para poder actualizarlo

### 2023.08.29
- refactor: Se agrega try-catch para controlar las excepciones de duplicidad de códigos de reserva (82927b4)
- refactor: Se agrega unique-constraint en la entidad Booking. Sobre el atributo 'hashedBookingCode' y también una
unique-constraint compuesta sobre los atributos 'hashedBookingCode' y 'clientId'. Para que no existan códigos de reserva
duplicados y a su vez, no existan códigos de reserva duplicados para un mismo usuario. (354f238)
- refactor: Se modifica la implementación para generar códigos de reserva (aeb9055)

### 2023.08.28
- refactor: Se elimina el enum PhoneType de la entidad Contact (f11ba11)
- fix: Se separa env eventDateTime en dos envs eventDate y eventTime. (e1a1884)
- feat: Se agrega a AbstractMailingService constante CONFIRMATION_SUBJECT. (e1a1884)
- fix: Se agrega a application*.yaml envs EVENT_DATE y EVENT_TIME. (21a6ab3)
- feat: Se agrega a EmailDto el uso de mapa para carga de imagenes al template html. (47e4bbc)
- feat: Se implementa carga de imagenes al template html. (36f1e06)
- feat: Se implementa en TransportMailSenderImpl método abstracto notifyConfirmation para envío de e-mails de confirmación de pago. (57c3511)
- fix: Se formatean templates html para reserva provisoria. (c6cc12b)
- feat: Se agregan templates html para confirmación de pago. (200500f)
- fix: Se agrega imagen .png para ser enviado como header en los e-mails de reserva y confirmación de pago. (7a10217)
- fix: Se agrega endpoint para testing de e-mails de confirmación de pago. (3771b96)

### 2023.08.27
- fix: Se agrega a ContactController el envío a demanda de e-mails para testing. (0d194c1)
- fix: Se agrega a deployment-prod.yaml env PAYMENT_MP_ACCOUNT para informar por e-mail la cuenta de Mercado Pago. (fbd7d18)
- fix: Se realizan mejoras en la notificación por e-mail de reservas provisorias. (5b91714)
- fix: Se elimina IMailingService y se reemplaza por AbstractMailingService. (a342300)
- fix: Se modificaa clase ContactController para el test de envío a demanda de e-mails con formato html. (95e4782)
- feat: Se agregan templaes html para emails de reserva provisoria. (360f939)
- fix: Se agregan test unitarios para obtención de templates html para emails de reserva provisoria. (b6cca74)
- fix: se mejora el cuerpo html del email de reserva provisoria. (71848fa)
- docs: Se modifica la descripción de atributos en la clase JavaMailSenderImpl. (8bd59c5)
- fix: Se agrega uso de envs con datos del evento en AbstractMailingService. (7afe00a)
- fix: Se agregan envs EVENT_ADDRESS, EVENT_DATE_TIME, EVENT_NAME y EVENT_PLACE para cargar los datos del evento. (3a40929)
- feat: Se agregan templates para envío de emails en formato texto. (a4b8cdd)
- fix: Se agrega uso de envs MAIL_USERNAME y MAIL_PASSWORD. (b738613)

### 2023.08.26
- feat: Se implementa clase JavaMailSenderImpl para envío de e-mails simples con solo texto. (4dec238)
- feat: Se agrega endpoint para envío de e-mails. (76920b3)
- feat: Se implementa TransportMailSenderImpl para envío de e-mails utilizando dependencias del paquete jakarta.mail. (3d7c245)
- feat: Se agrega a archivo pom.xml dependencia para envío de e-mails. (b5cd483)
- feat: Se agrega a application.yaml y application-prod.yaml campos para envío de e-mails. (2410f15)
- feat: Se crea interfaz IMailingService para envío de e-mails. (e893c18)
- feat: Se crea la clase EmailDto para enviar e-mails. (d4f59b9)
- fix: Se utiliza JavaMailSenderImpl para endpoint de test de envío de e-mails. (8254b1f)

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
