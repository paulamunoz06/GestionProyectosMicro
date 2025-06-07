# Sistema de Gestión de Proyectos Académicos

El sistema está compuesto por múltiples microservicios independientes que colaboran entre sí para permitir la gestión de proyectos académicos entre estudiantes, empresas y coordinadores.

## Estado del Proyecto
Este proyecto es una tercera iteración de un sistema funcional, desarrollado como proyecto de clase de Ingeniería de Software II. Se encuentra en un estado funcional con prototipos de interfaz, pruebas de usabilidad y documentación de arquitectura. 

## Arquitectura

El sistema sigue una arquitectura **basada en microservicios** con comunicación entre ellos a través de **REST** y **mensajería asincrónica**. Cada microservicio se implementa con **Spring Boot**, emplea **JPA** para persistencia, y está organizado en paquetes por responsabilidad (Controller, Service, Repository, Entity, DTO, etc.). La arquitectura es de tipo híbrida, combinando microservicios con un diseño interno de Arquitectura Hexagonal para cada microservicio. 

### Microservicios del sistema

- **Login Microservice**
  Se encarga de todo lo relacionado con el inicio de sesión, autenticación y autorización de usuarios. 
  El sistema utiliza Keycloak como sistema proveedor de identidad para la gestión de usuarios, roles, grupos y la emisión de tokens de seguridad. Un API Gateway se encarga de interceptar las solicitudes entrantes para realizar la validación de autenticación y autorización inicial. 

- **Company Microservice**
  Maneja las necesidades de las empresas:
  - Registro de empresas. 
  - Publicación y gestión de proyectos. 

- **Student Microservice**
  Maneja la lógica relacionada con los estudiantes:
  - Consulta de proyectos disponibles. 
  - Postulación a proyectos. 
  - Visualización del estado de postulaciones. 

- **Coordinator Microservice**
  Da soporte a los coordinadores:
  - Visualización de todos los proyectos. 
  - Evaluación de postulaciones y aprobación/rechazo. 
  - Notificación a empresas sobre el estado de los proyectos. 
  - Visualización de gráficas estadísticas en barras y pastel sobre la cantidad de proyectos postulados, aprobados, rechazados y terminados en un período académico específico. 

### Patrones de Diseño

Se implementaron seis patrones de diseño como Observer, Singleton, Builder, Factory, State y Template para optimizar la interacción entre los módulos, garantizando modularidad y facilidad de mantenimiento. 

### Despliegue

La aplicación está dockerizada con Docker-Compose. Se ha configurado un volumen para la persistencia del microservicio del coordinador mediante Postgres. 

## Tecnologías Utilizadas

* **Backend:** Java 21, Spring Boot, JPA/Hibernate
* **Bases de Datos:** PostgreSQL (para el microservicio Coordinator), H2 o SQLite (para otros microservicios en memoria) 
* **Autenticación y Autorización:** Keycloak, JWT, API Gateway 
* **Contenedorización:** Docker, Docker-Compose 
* **Mensajería Asíncrona:** RabbitMQ 
* **Front-end:** Java 21 

## Requisitos
### Funcionales
1.  Registro de empresas con información detallada (NIT, nombre, email, sector, teléfono, nombres y apellidos del contacto, cargo del contacto). 
2.  Publicación de proyectos por parte de las empresas (nombre, resumen, objetivos, descripción, tiempo máximo en meses, presupuesto, fecha). El estado inicial de un proyecto es "recibido". 
3.  Evaluación y gestión de proyectos por parte del coordinador, incluyendo listado de proyectos, visualización de detalles, cambio de estado (Recibido, Aceptado, Rechazado) y adición de comentarios. 
4.  Notificación por correo electrónico a la empresa al cambiar el estado del proyecto. 
5.  Visualización y postulación de proyectos por los estudiantes, con listado de proyectos (No, fecha, nombre empresa, nombre proyecto, resumen) y detalles de la empresa y el proyecto. 
6.  Inicio de sesión para los distintos usuarios (empresa, coordinador, estudiante) para consultar las novedades de los proyectos. 
7.  Visualización de gráficas estadísticas en barras y pastel sobre cantidad de proyectos postulados, aprobados, rechazados y terminados en un período académico para el coordinador. 

### No Funcionales
-   **Escalabilidad:** Diseñado para soportar una alta concurrencia de usuarios (500 estudiantes accediendo simultáneamente para postularse) y con capacidad de expansión a un producto nacional que involucre a estudiantes y empresas de todo el país. 
-   **Rendimiento:** Tiempos de respuesta rápidos para operaciones críticas. El sistema debe procesar cada postulación en menos de 1.5 segundos, soportando 500 postulaciones simultáneas sin degradación del rendimiento. El rendimiento del sistema no debe degradarse con la concurrencia, manteniendo tiempos de respuesta óptimos y evitando sobrecargas en la base de datos o el servidor. Las acciones dentro del sistema deben procesarse en menos de 2 segundos. 
-   **Integridad de Datos:** Garantizar la integridad y consistencia de los datos, evitando postulaciones duplicadas o errores en la actualización del estado del estudiante. 
-   **Mantenimiento:** Código bien documentado y modular para facilitar futuras mejoras y mantenimiento. 
-   **Usabilidad:** Interfaz intuitiva y fácil de usar. Diseño responsive y accesible desde dispositivos móviles y computadoras. 
-   **Seguridad:** Implementación de autenticación y autorización seguras basadas en tokens JWT, gestionados mediante Keycloak y un API Gateway.  Protección de datos personales y académicos, control de acceso y autorización. Implementación de HTTPS y cifrado de datos sensibles. Protección contra ataques comunes (SQL Injection, XSS, CSRF, etc.). 
-   **Fiabilidad:** Consistencia y fiabilidad en el ciclo de vida del desarrollo. 

## Prototipos y Diseño
-   [Prototipos de Interfaz Gráfica](https://www.figma.com/design/5V1ec7uQPEZeMPGu4EzIRq/Gesti%C3%B3n-De-Proyectos-Acad%C3%A9micos?node-id=450-259&p=f&t=W0WP2pIvaqkZJR3k-0) 
-   [Diagrama Entidad-Relación](https://drive.google.com/file/d/1I6pi0R7gYwSiqrWNn6MVyKbS7FnPlVaR/view?usp=sharing) 
-   [Modelo C4](https://drive.google.com/file/d/1I6pi0R7gYwSiqrWNn6MVyKbS7FnPlVaR/view?usp=sharing) 
    * Diagrama de Contexto 
    * Modelo de Contenedores 
    * Diagrama de Componentes 
    * Diagrama de Clases – Entidades 
-   [Bounded Context Diagram](https://drive.google.com/file/d/1I6pi0R7gYwSiqrWNn6MVyKbS7FnPlVaR/view?usp=sharing)
-   [Modelo de Dominio y Arquitectura Hexagonal](https://drive.google.com/file/d/1I6pi0R7gYwSiqrWNn6MVyKbS7FnPlVaR/view?usp=sharing) 
-   [Levantamiento de Requisitos (Historias de Usuario)](https://docs.google.com/spreadsheets/d/1hG2GuJDQpcxUXRv70Yiytuf38SZNCaiGbclfKXcRKBk/edit?usp=sharing) 
-   [Test de Usabilidad](https://docs.google.com/spreadsheets/d/1pmMF3Gd32J0-KrMs3bDB2ZYqA62sIhG9D5hBbXWHq5U/edit?gid=946668092#gid=946668092) 

## Repositorio del Proyecto
[https://github.com/paulamunoz06/GestionProyectosMicro.git](https://github.com/paulamunoz06/GestionProyectosMicro.git) 

## Video sustentación

## Créditos
**Desarrollado por:**
-   Ana Sofía Arango Yanza 
-   Juan Diego Gómez Garcés 
-   Paula Andrea Muñoz Delgado 
-   Cristhian Camilo Unas Ocaña 
-   [cite_start]Juan David Vela Coronado 

**Profesor:** Nombres y apellidos 
