# Sistema de Gestión de Proyectos

## Descripción
Sistema de gestión de proyectos educativos implementado con arquitectura hexagonal y microservicios, utilizando Spring Boot y Docker para el despliegue.

## Arquitectura

### Estructura de Microservicios
El sistema está compuesto por los siguientes microservicios:

1. **microserviceCompany**
   - Gestión de empresas
   - Manejo de información empresarial
   - Integración con la base de datos

2. **microserviceCoordinator**
   - Gestión de coordinadores
   - Administración de usuarios y roles
   - Control de proyectos

3. **microserviceLogin**
   - Autenticación y autorización
   - Gestión de sesiones
   - Seguridad del sistema

4. **microserviceStudent**
   - Gestión de estudiantes
   - Manejo de perfiles estudiantiles
   - Asignación de proyectos

5. **api-gateway**
   - Punto de entrada único para la aplicación
   - Manejo de rutas y enrutamiento
   - Balanceo de carga

## Tecnologías y Dependencias

### Backend
- **Spring Boot**: 3.4.4
- **Java**: 21
- **Spring Cloud**: Para la gestión de microservicios
- **Spring Security**: Para autenticación y autorización
- **Spring Web**: Para endpoints REST
- **Spring Data JPA**: Para acceso a datos
- **Docker**: Para contenerización
- **Maven**: 3.9.6 (versión recomendada)

### Frontend
- **Angular**: Framework principal para el frontend
- **TypeScript**: 5.3.3
- **Bootstrap**: 5.3.2
- **Angular Material**: 17.3.1
- **RxJS**: 7.8.1

## Requisitos del Sistema

### Software Requerido
- Java 21 JDK
- Maven 3.9.6
- Docker y Docker Compose
- Node.js y npm (para el frontend)

### Bases de Datos
- PostgreSQL
- Redis (para caché)

## Instalación y Configuración

### Backend
1. Clonar el repositorio
2. Configurar las variables de entorno en el archivo `application.properties`
3. Ejecutar los microservicios usando:
   ```bash
   mvn spring-boot:run
   ```

### Frontend
1. Navegar al directorio FrontApp
2. Instalar dependencias:
   ```bash
   npm install
   ```
3. Ejecutar la aplicación:
   ```bash
   ng serve
   ```

## Estructura del Proyecto

```
GestionProyectosMicro-master/
├── FrontApp/              # Aplicación frontend Angular
├── api-gateway/           # Gateway de servicios
├── microserviceCompany/   # Microservicio de empresas
├── microserviceCoordinator/ # Microservicio de coordinadores
├── microserviceLogin/     # Microservicio de autenticación
├── microserviceStudent/   # Microservicio de estudiantes
├── pom.xml               # Archivo de configuración Maven
└── src/                 # Código fuente principal
```

## Funcionalidades Principales

### Gestión de Usuarios
- Registro y autenticación de usuarios
- Roles y permisos
- Gestión de perfiles

### Gestión de Proyectos
- Creación y asignación de proyectos
- Seguimiento de progreso
- Evaluación y calificación

### Gestión de Empresas
- Registro de empresas
- Gestión de convenios
- Asignación de proyectos

### Gestión de Estudiantes
- Perfil de estudiantes
- Asignación a proyectos
- Seguimiento académico

## Documentación

### API
- Documentación Swagger disponible en `/swagger-ui.html`
- Endpoints REST bien documentados
- Ejemplos de request/response

### Base de Datos
- Diagrama ER
- Scripts de creación
- Documentación de modelos

## Contribución

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## Licencia
Este proyecto está bajo la licencia MIT - ver el archivo LICENSE para más detalles.
