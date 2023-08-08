# Changelog

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
