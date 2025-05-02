package co.edu.unicauca.microservicecoordinator.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa un coordinador dentro del sistema, que es un tipo de usuario con
 * la capacidad de gestionar proyectos.
 */
@Entity
@Getter
@Setter
public class Coordinator {

    /**
     * Identificador único del coordinador.
     */
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String coordinatorId;

    /**
     * Correo electrónico del coordinador.
     */
    @Column(nullable = false, unique = true)
    private String coordinatorEmail;


    /**
     * Nombre completo del coordinador.
     */
    @Column(nullable = false)
    private String coordinatorName;
}
