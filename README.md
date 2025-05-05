# Sistema de Gestión de Proyectos Académicos

Este sistema está compuesto por múltiples microservicios independientes que colaboran entre sí para permitir la gestión de proyectos académicos entre estudiantes, empresas y coordinadores.

## Arquitectura

El sistema sigue una arquitectura **basada en microservicios** con comunicación entre ellos a través de **REST** y/o **mensajería asincrónica**. Cada microservicio se implementa con **Spring Boot**, emplea **JPA/Hibernate** para persistencia, y está organizado en paquetes por responsabilidad (Controller, Service, Repository, Entity, DTO, etc.).

### Microservicios del sistema

- **Login Microservice**  
  Se encarga de todo lo relacionado con el inicio de sesión, autenticación y autorización de usuarios.

- **Company Microservice**  
  Maneja las necesidades de las empresas:
  - Registro de empresas.
  - Publicación y gestión de proyectos.

- **Student Microservice**  
  Maneja la lógica relacionada con los estudiantes:
  - Consulta de proyectos disponibles.
  - Postulación a proyectos.
  - Visualización del historial y estado de postulaciones.

- **Coordinator Microservice**  
  Da soporte a los coordinadores:
  - Visualización de todos los proyectos.
  - Evaluación de postulaciones y aprobación/rechazo.
  - Notificación a empresas sobre el estado de los proyectos.

-**Coordinator Microservice**: se encarga de manejar las necesidades de los coordinadores, como ver todos los proyectos, evaluarlos, y notificar a las empresas de los cambios realizados.
## Requisitos
### Funcionales
1. Registro de empresas con información detallada.
2. Publicación de proyectos por parte de las empresas.
3. Evaluación y gestión de proyectos por parte del coordinador.
4. Visualización y postulación de proyectos por los estudiantes.
5. Inicio de sesión para los distintos usuarios.

### No Funcionales
- **Escalabilidad:** Diseñado para soportar múltiples empresas y proyectos.

## Prototipos y Diseño
- [Prototipos de Interfaz Gráfica](https://www.figma.com/design/5V1ec7uQPEZeMPGu4EzIRq/Gestión-De-Proyectos-Académicos?node-id=450-259&p=f&t=W0WP2pIvaqkZJR3k-0)
- [Diagrama Entidad-Relación](https://drive.google.com/file/d/1I6pi0R7gYwSiqrWNn6MVyKbS7FnPlVaR/view?usp=sharing)
- [Modelo C4](https://drive.google.com/file/d/1I6pi0R7gYwSiqrWNn6MVyKbS7FnPlVaR/view?usp=sharing)
- [Levantamiento de Requisitos](https://docs.google.com/spreadsheets/d/1hG2GuJDQpcxUXRv70Yiytuf38SZNCaiGbclfKXcRKBk/edit?usp=sharing)
- [Test de Usabilidad](https://docs.google.com/spreadsheets/d/1pmMF3Gd32J0-KrMs3bDB2ZYqA62sIhG9D5hBbXWHq5U/edit?gid=946668092#gid=946668092)

## Créditos
**Desarrollado por:**
- Ana Sofia Arango Yanza
- Juan Diego Gómez Garcés
- Paula Andrea Muñoz Delgado
- Cristhian Camilo Unas Ocaña
- Juan David Vela Coronado

**Profesor:** Libardo Pantoja
