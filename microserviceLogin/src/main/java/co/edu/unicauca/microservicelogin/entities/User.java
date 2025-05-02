package co.edu.unicauca.microservicelogin.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
/**
 * Representa un usuario de la aplicación.
 *
 * Esta entidad define la estructura de la tabla 'app_user' en la base de datos
 * y contiene información básica sobre un usuario, como su identificador único,
 * contraseña (que debe ser manejada con seguridad), correo electrónico y rol.
 *
 * Utiliza anotaciones de Jakarta Persistence para el mapeo objeto-relacional (ORM)
 * y anotaciones de Lombok para la generación automática de métodos getter y setter.
 */
@Entity
@Table(name = "app_user")  // Evita el conflicto con 'user'
public class User {
    /**
     * Identificador único del usuario.
     *
     * Esta propiedad está anotada con {@code @Id}, indicando que es la clave primaria
     * de la entidad en la base de datos.
     */
    @Getter
    @Setter
    @Id
    private String id;

    /**
     * Contraseña del usuario.
     *
     * Esta propiedad almacena la contraseña del usuario. Es crucial implementar
     * medidas de seguridad adecuadas, como el hashing, para proteger las contraseñas.
     * El metodo getter para esta propiedad no se genera explícitamente con Lombok
     * por consideraciones de seguridad; sin embargo, se incluye un setter.
     */
    @Setter
    @Getter
    private String password;

    /**
     * Correo electrónico del usuario.
     *
     * Esta propiedad almacena la dirección de correo electrónico del usuario.
     * Está anotada con {@code @Getter} y {@code @Setter} para la generación automática
     * de los métodos de acceso.
     */
    @Setter
    @Getter
    private String email;

    /**
     * Rol del usuario dentro de la aplicación.
     *
     * Esta propiedad representa el rol o los permisos del usuario. El valor puede
     * ser un código numérico o una referencia a una tabla de roles más compleja.
     * Está anotada con {@code @Getter} y {@code @Setter} para la generación automática
     * de los métodos de acceso.
     * 1=Estudiante, 2=Coordinador, 3=Empresa
     */
    @Getter
    @Setter
    private int role;

}
