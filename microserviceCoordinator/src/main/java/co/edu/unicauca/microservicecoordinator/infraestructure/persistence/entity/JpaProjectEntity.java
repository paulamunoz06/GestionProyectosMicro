package co.edu.unicauca.microservicecoordinator.infraestructure.persistence.entity;

import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.state.*;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA que representa un proyecto en la base de datos.
 *
 * Esta clase forma parte de la capa de infraestructura en la arquitectura hexagonal,
 * y se encarga del mapeo del objeto dominio Project a la base de datos relacional.
 *
 * Los atributos incluyen tanto tipos embebidos para valores de objeto de valor (Value Objects)
 * como atributos simples y enumeraciones.
 *
 * Validaciones básicas se aplican a los campos requeridos mediante anotaciones de Bean Validation.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
public class JpaProjectEntity {

    /**
     * Identificador único del proyecto, representado como objeto de valor ProjectId.
     */
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "proId"))
    private ProjectId proId;

    /**
     * Título del proyecto como objeto de valor ProjectTitle.
     */
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "proTitle", nullable = false))
    private ProjectTitle proTitle;

    /**
     * Descripción detallada del proyecto. No puede estar vacía.
     */
    @NotBlank(message = "La descripción del proyecto no puede estar vacía")
    @Column(nullable = false)
    private String proDescription;

    /**
     * Resumen del proyecto. No puede estar vacío.
     */
    @NotBlank(message = "El resumen no puede estar vacío")
    @Column(nullable = false)
    private String proAbstract;

    /**
     * Objetivos del proyecto. No pueden estar vacíos.
     */
    @NotBlank(message = "Los objetivos no pueden estar vacíos")
    @Column(nullable = false)
    private String proGoals;

    /**
     * Fecha de registro del proyecto, representada como objeto de valor ProjectRegistrationDate.
     * Se almacena solo la fecha (sin hora).
     */
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Embedded
    @AttributeOverride(name = "date", column = @Column(name = "proDate", nullable = false))
    private ProjectRegistrationDate proDate;

    /**
     * Plazo máximo para la ejecución del proyecto, representado como objeto de valor ProjectDeadline.
     */
    @Embedded
    @AttributeOverride(name = "months", column = @Column(name = "proDeadLine", nullable = false))
    private ProjectDeadline proDeadLine;

    /**
     * Presupuesto asignado al proyecto. Puede ser nulo.
     */
    @Column(nullable = true)
    private Double proBudget;

    /**
     * Estado actual del proyecto como enumeración EnumProjectState.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumProjectState proState;

    /**
     * Identificador de la compañía asociada al proyecto. Puede ser nulo.
     */
    @Column(nullable = true)
    private String idcompany;

    /**
     * Identificador del coordinador asignado al proyecto. Puede ser nulo.
     */
    @Column(nullable = true)
    private String proCoordinator;

    /**
     * Constructor sin argumentos requerido por JPA.
     */
    public JpaProjectEntity() {
    }
}