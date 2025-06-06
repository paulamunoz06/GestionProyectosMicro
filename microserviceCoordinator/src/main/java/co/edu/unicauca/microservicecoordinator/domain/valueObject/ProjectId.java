package co.edu.unicauca.microservicecoordinator.domain.valueObject;

import jakarta.persistence.Embeddable;
import java.util.Objects;

/**
 * Representa el identificador único de un proyecto.
 *
 * Esta clase es un objeto de valor (Value Object) que encapsula el ID del proyecto,
 * garantizando que el valor no sea nulo ni vacío. Está anotada con Embeddable
 * para ser utilizada como componente embebido en entidades JPA.
 */
@Embeddable
public class ProjectId {

    /**
     * Valor del identificador del proyecto.
     */
    private final String value;

    /**
     * Constructor por defecto que inicializa el valor con una cadena vacía.
     *
     * Este constructor es necesario para JPA.
     */
    public ProjectId() {
        value = "";
    }

    /**
     * Construye un nuevo ProjectId con el valor especificado.
     *
     * @param value Identificador único del proyecto. No puede ser nulo, vacío o contener solo espacios en blanco.
     * @throws IllegalArgumentException si value es nulo, vacío o solo espacios en blanco.
     */
    public ProjectId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El ID del proyecto no puede estar vacío");
        }
        this.value = value;
    }

    /**
     * Obtiene el valor del identificador del proyecto.
     *
     * @return Valor del ID del proyecto.
     */
    public String getValue() {
        return value;
    }

    /**
     * Compara este objeto con otro para determinar igualdad basada en el valor del ID.
     *
     * @param o Objeto a comparar.
     * @return true si ambos objetos tienen el mismo valor; false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectId)) return false;
        ProjectId that = (ProjectId) o;
        return Objects.equals(value, that.value);
    }

    /**
     * Retorna el código hash basado en el valor del ID.
     *
     * @return Código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Retorna una representación en cadena del identificador.
     *
     * @return Cadena con el valor del ID del proyecto.
     */
    @Override
    public String toString() {
        return value;
    }
}