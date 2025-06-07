# Sistema de Gestión de Proyectos Académicos

El sistema está compuesto por múltiples microservicios independientes que colaboran entre sí para permitir la gestión de proyectos académicos entre estudiantes, empresas y coordinadores.

## Estado del Proyecto
Este proyecto es una tercera iteración de un sistema funcional, desarrollado como proyecto de clase de Ingeniería de Software II. [cite_start]Se encuentra en un estado funcional con prototipos de interfaz, pruebas de usabilidad y documentación de arquitectura. 

## Arquitectura

El sistema sigue una arquitectura **basada en microservicios** con comunicación entre ellos a través de **REST** y **mensajería asincrónica**. Cada microservicio se implementa con **Spring Boot**, emplea **JPA** para persistencia, y está organizado en paquetes por responsabilidad (Controller, Service, Repository, Entity, DTO, etc.). [cite_start]La arquitectura es de tipo híbrida, combinando microservicios con un diseño interno de Arquitectura Hexagonal para cada microservicio. 

### Microservicios del sistema

- **Login Microservice**
  [cite_start]Se encarga de todo lo relacionado con el inicio de sesión, autenticación y autorización de usuarios. 
  El sistema utiliza Keycloak como sistema proveedor de identidad para la gestión de usuarios, roles, grupos y la emisión de tokens de seguridad. [cite_start]Un API Gateway se encarga de interceptar las solicitudes entrantes para realizar la validación de autenticación y autorización inicial. 

- **Company Microservice**
  Maneja las necesidades de las empresas:
  - [cite_start]Registro de empresas. 
  - [cite_start]Publicación y gestión de proyectos. 

- **Student Microservice**
  Maneja la lógica relacionada con los estudiantes:
  - [cite_start]Consulta de proyectos disponibles. 
  - [cite_start]Postulación a proyectos. 
  - [cite_start]Visualización del estado de postulaciones. 

- **Coordinator Microservice**
  Da soporte a los coordinadores:
  - [cite_start]Visualización de todos los proyectos. 
  - [cite_start]Evaluación de postulaciones y aprobación/rechazo. 
  - [cite_start]Notificación a empresas sobre el estado de los proyectos. 
  - [cite_start]Visualización de gráficas estadísticas en barras y pastel sobre la cantidad de proyectos postulados, aprobados, rechazados y terminados en un período académico específico. 

### Patrones de Diseño

[cite_start]Se implementaron seis patrones de diseño como Observer, Singleton, Builder, Factory, State y Template para optimizar la interacción entre los módulos, garantizando modularidad y facilidad de mantenimiento. 

### Despliegue

[cite_start]La aplicación está dockerizada con Docker-Compose.  [cite_start]Se ha configurado un volumen para la persistencia del microservicio del coordinador mediante Postgres. 

## Tecnologías Utilizadas

* **Backend:** Java 21, Spring Boot, JPA/Hibernate
* [cite_start]**Bases de Datos:** PostgreSQL (para el microservicio Coordinator), H2 o SQLite (para otros microservicios en memoria) 
* [cite_start]**Autenticación y Autorización:** Keycloak, JWT, API Gateway 
* [cite_start]**Contenedorización:** Docker, Docker-Compose 
* [cite_start]**Mensajería Asíncrona:** RabbitMQ 
* [cite_start]**Front-end:** Java 21 

## Requisitos
### Funcionales
1.  [cite_start]Registro de empresas con información detallada (NIT, nombre, email, sector, teléfono, nombres y apellidos del contacto, cargo del contacto). 
2.  [cite_start]Publicación de proyectos por parte de las empresas (nombre, resumen, objetivos, descripción, tiempo máximo en meses, presupuesto, fecha).  [cite_start]El estado inicial de un proyecto es "recibido". 
3.  [cite_start]Evaluación y gestión de proyectos por parte del coordinador, incluyendo listado de proyectos, visualización de detalles, cambio de estado (Recibido, Aceptado, Rechazado) y adición de comentarios. 
4.  [cite_start]Notificación por correo electrónico a la empresa al cambiar el estado del proyecto. 
5.  [cite_start]Visualización y postulación de proyectos por los estudiantes, con listado de proyectos (No, fecha, nombre empresa, nombre proyecto, resumen) y detalles de la empresa y el proyecto. 
6.  [cite_start]Inicio de sesión para los distintos usuarios (empresa, coordinador, estudiante) para consultar las novedades de los proyectos. 
7.  [cite_start]Visualización de gráficas estadísticas en barras y pastel sobre cantidad de proyectos postulados, aprobados, rechazados y terminados en un período académico para el coordinador. 

### No Funcionales
-   [cite_start]**Escalabilidad:** Diseñado para soportar una alta concurrencia de usuarios (500 estudiantes accediendo simultáneamente para postularse) y con capacidad de expansión a un producto nacional que involucre a estudiantes y empresas de todo el país. 
-   **Rendimiento:** Tiempos de respuesta rápidos para operaciones críticas. [cite_start]El sistema debe procesar cada postulación en menos de 1.5 segundos, soportando 500 postulaciones simultáneas sin degradación del rendimiento.  [cite_start]El rendimiento del sistema no debe degradarse con la concurrencia, manteniendo tiempos de respuesta óptimos y evitando sobrecargas en la base de datos o el servidor.  [cite_start]Las acciones dentro del sistema deben procesarse en menos de 2 segundos. 
-   [cite_start]**Integridad de Datos:** Garantizar la integridad y consistencia de los datos, evitando postulaciones duplicadas o errores en la actualización del estado del estudiante. 
-   [cite_start]**Mantenimiento:** Código bien documentado y modular para facilitar futuras mejoras y mantenimiento. 
-   **Usabilidad:** Interfaz intuitiva y fácil de usar. [cite_start]Diseño responsive y accesible desde dispositivos móviles y computadoras. 
-   [cite_start]**Seguridad:** Implementación de autenticación y autorización seguras basadas en tokens JWT, gestionados mediante Keycloak y un API Gateway.  Protección de datos personales y académicos, control de acceso y autorización. Implementación de HTTPS y cifrado de datos sensibles. [cite_start]Protección contra ataques comunes (SQL Injection, XSS, CSRF, etc.). 
-   [cite_start]**Fiabilidad:** Consistencia y fiabilidad en el ciclo de vida del desarrollo. 

## Prototipos y Diseño
-   [cite_start][Prototipos de Interfaz Gráfica](https://www.figma.com/design/5V1ec7uQPEZeMPGu4EzIRq/Gesti%C3%B3n-De-Proyectos-Acad%C3%A9micos?node-id=450-259&p=f&t=W0WP2pIvaqkZJR3k-0) 
-   [cite_start][Diagrama Entidad-Relación](https://drive.google.com/file/d/1I6pi0R7gYwSiqrWNn6MVyKbS7FnPlVaR/view?usp=sharing) 
-   [cite_start][Modelo C4](https://drive.google.com/file/d/1I6pi0R7gYwSiqrWNn6MVyKbS7FnPlVaR/view?usp=sharing) 
    * [cite_start]Diagrama de Contexto 
    * [cite_start]Modelo de Contenedores 
    * [cite_start]Diagrama de Componentes 
    * [cite_start]Diagrama de Clases – Entidades 
-   [cite_start][Bounded Context Diagram](https://documentacion.docx#page=5) 
-   [cite_start][Modelo de Dominio y Arquitectura Hexagonal](https://documentacion.docx#page=6) 
-   [cite_start][Levantamiento de Requisitos (Historias de Usuario)](https://docs.google.com/spreadsheets/d/1hG2GuJDQpcxUXRv70Yiytuf38SZNCaiGbclfKXcRKBk/edit?usp=sharing) 
-   [cite_start][Test de Usabilidad](https://docs.google.com/spreadsheets/d/1pmMF3Gd32J0-KrMs3bDB2ZYqA62sIhG9D5hBbXWHq5U/edit?gid=946668092#gid=946668092) 

## Repositorio del Proyecto
[cite_start][https://github.com/paulamunoz06/GestionProyectosMicro.git](https://github.com/paulamunoz06/GestionProyectosMicro.git) 

## Tablero de Tareas y Progreso
[cite_start]Se siguió la metodología Scrum para la gestión de tareas, con un tablero de tareas para el Sprint 3 y gráficas de progreso que muestran el 100% de finalización. 

## Video de Sustentación
[cite_start](). 

## Créditos
**Desarrollado por:**
-   [cite_start]Ana Sofía Arango Yanza 
-   [cite_start]Juan Diego Gómez Garcés 
-   [cite_start]Paula Andrea Muñoz Delgado 
-   [cite_start]Cristhian Camilo Unas Ocaña 
-   [cite_start]Juan David Vela Coronado 

[cite_start]**Profesor:** Nombres y apellidos 
