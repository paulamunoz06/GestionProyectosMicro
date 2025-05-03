package co.edu.unicauca.microservicestudent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un estudiante dentro del sistema académico.
 *
 * Esta entidad almacena información básica de autenticación del estudiante, como su identificador,
 * correo electrónico y contraseña. También mantiene las relaciones con los proyectos a los que el estudiante
 * se ha postulado y en los que ha sido aprobado.
 */
@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
public class Student {

    /**
     * Identificador único del estudiante.
     *
     * Este campo es obligatorio y se utiliza como clave primaria en la base de datos.
     */
    @Id
    @NotNull(message = "El ID no puede estar vacío")
    @Column(name = "STUDENTID", nullable = false, unique = true)
    private String id;

    /**
     * Correo electrónico institucional o personal del estudiante.
     *
     * Debe ser un formato válido de correo electrónico y no puede estar vacío.
     */
    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Debe ser un correo electrónico válido")
    @Column(name = "STUDENTEMAIL", nullable = false)
    private String email;

    /**
     * Contraseña de acceso del estudiante al sistema.
     *
     * Debe tener al menos 8 caracteres de longitud y no puede estar vacía.
     */
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(name = "STUDENTPASSWORD", nullable = false)
    private String password;

    /**
     * Lista de proyectos a los que el estudiante se ha postulado.
     *
     * Relación muchos-a-muchos gestionada desde la clase {@link Project}.
     */
    @ManyToMany(mappedBy = "postulated")
    private List<Project> postulated = new ArrayList<>();

    /**
     * Lista de proyectos en los que el estudiante ha sido aprobado para participar.
     *
     * Relación muchos-a-muchos gestionada desde la clase {@link Project}.
     */
    @ManyToMany(mappedBy = "approved")
    private List<Project> approved = new ArrayList<>();

    /**
     * Constructor para crear un estudiante con los atributos básicos.
     *
     * @param id Identificador único del estudiante.
     * @param email Correo electrónico del estudiante.
     * @param password Contraseña del estudiante.
     */
    public Student(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
