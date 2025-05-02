package co.edu.unicauca.microserviceCompany.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
/**
 * Representa un usuario de la aplicación.
 *
 * Solo se usa para el envio a la cola de rabbitMQ por lo que no se guarda en la base de datos
 */

@Setter
@Getter
public class User {
    /**
     * Identificador único del usuario.
     */
    private String id;

    /**
     * Contraseña del usuario.
     *
     * Esta propiedad almacena la contraseña del usuario. Es crucial implementar
     * medidas de seguridad adecuadas, como el hashing, para proteger las contraseñas.
     * El metodo getter para esta propiedad no se genera explícitamente con Lombok
     * por consideraciones de seguridad; sin embargo, se incluye un setter.
     */
    private String password;

    /**
     * Correo electrónico del usuario.
     *
     * Esta propiedad almacena la dirección de correo electrónico del usuario.
     * Está anotada con {@code @Getter} y {@code @Setter} para la generación automática
     * de los métodos de acceso.
     */
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
    private int role;

}
