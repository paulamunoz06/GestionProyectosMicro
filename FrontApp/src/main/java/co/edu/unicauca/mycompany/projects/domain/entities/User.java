package co.edu.unicauca.mycompany.projects.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la respuesta de login de microservicio de usuarios.
 * 
 * Un usuario tiene un identificador, un correo electrónico y una contraseña.
 * Esta clase sirve como base para otros tipos de usuarios, como estudiantes y coordinadores.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    
    /** Identificador único del usuario. */
    @JsonProperty("id")
    private String id;
    
    /** Contraseña del usuario. */
    @JsonProperty("password")
    private String password;

    /** Correo electrónico del usuario. */
    @JsonProperty("email")
    private String email;

    /** Rol del usuario como un entero, 1=Estudiante, 2=Coordinador, 3=Empresa*/
    @JsonProperty("role")
    private int role;

    /**
     * Constructor de la clase User.
     * 
     * @param id Identificador único del usuario.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     */
    public User(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

}
