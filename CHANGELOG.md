# Changelog

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
