package co.edu.unicauca.microservicecoordinator.infraestructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad JPA que representa un coordinador en la base de datos.
 *
 * Esta clase corresponde a la capa de infraestructura dentro de la arquitectura hexagonal,
 * y es usada para mapear los datos del coordinador en la base de datos relacional.
 *
 * Atributos:
 * - coordinatorId: Identificador único del coordinador.
 * - coordinatorEmail: Correo electrónico del coordinador, debe ser único.
 * - coordinatorPassword: Contraseña cifrada del coordinador.
 *
 * Usa Lombok para generar los métodos getters, setters y constructor con todos los atributos.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "jpa_coordinator_entity")
public class JpaCoordinatorEntity {

    /**
     * Identificador único del coordinador.
     */
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String coordinatorId;

    /**
     * Correo electrónico único del coordinador.
     */
    @Column(name = "coordinator_email",nullable = false, unique = true)
    private String coordinatorEmail;

    /**
     * Contraseña del coordinador.
     */
    @Column(name = "coordinator_password",nullable = false)
    private String coordinatorPassword;

    /**
     * Constructor sin argumentos requerido por JPA.
     */
    public JpaCoordinatorEntity() {
    }
}