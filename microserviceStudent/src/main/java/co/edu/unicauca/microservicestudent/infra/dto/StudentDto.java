package co.edu.unicauca.microservicestudent.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de transferencia de datos (DTO) que representa un estudiante.
 *
 * Esta clase permite transportar datos del estudiante entre capas del sistema
 * sin exponer directamente la entidad del modelo.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    /**
     * Identificador único del estudiante.
     */
    private String id;

    /**
     * Correo electrónico del estudiante.
     */
    private String email;

    /**
     * Contraseña del estudiante.
     *
     * Se recomienda no exponer este campo en respuestas públicas por motivos de seguridad.
     */
    private String password;

    /**
     * Lista de identificadores de proyectos a los que el estudiante se ha postulado.
     */
    private List<String> postulatedIds = new ArrayList<>();

    /**
     * Lista de identificadores de proyectos en los que el estudiante ha sido aprobado.
     */
    private List<String> approvedIds = new ArrayList<>();

    /**
     * Constructor que permite crear un estudiante DTO sin relaciones.
     *
     * @param id       Identificador del estudiante
     * @param email    Correo electrónico del estudiante
     * @param password Contraseña del estudiante
     */
    public StudentDto(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
