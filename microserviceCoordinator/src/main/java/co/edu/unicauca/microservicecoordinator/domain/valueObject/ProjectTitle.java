package co.edu.unicauca.microservicecoordinator.domain.valueObject;

import jakarta.persistence.Embeddable;
import java.util.Objects;

/**
 * Representa el título de un proyecto como un objeto de valor.
 *
 * Esta clase garantiza que el título no sea nulo, ni vacío, y que su longitud no exceda
 * los 100 caracteres. Está anotada con Embeddable para su uso como componente
 * embebido en entidades JPA.
 */
@Embeddable
public final class ProjectTitle {

    /**
     * Valor del título del proyecto.
     */
    private final String value;

    /**
     * Constructor por defecto que inicializa el título con una cadena vacía.
     *
     * Este constructor es requerido por JPA.
     */
    public ProjectTitle() {
        value = "";
    }

    /**
     * Construye una nueva instancia de ProjectTitle con el valor especificado.
     *
     * @param value Título del proyecto. No puede ser nulo, vacío, ni superar los 100 caracteres.
     * @throws IllegalArgumentException si el título es nulo, vacío o excede los 100 caracteres.
     */
    public ProjectTitle(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El título del proyecto no puede estar vacío.");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("El título no puede tener más de 100 caracteres.");
        }
        this.value = value.trim();
    }

    /**
     * Obtiene el valor del título del proyecto.
     *
     * @return Título del proyecto.
     */
    public String getValue() {
        return value;
    }

    /**
     * Compara esta instancia con otra para determinar igualdad basada en el valor del título.
     *
     * @param o Objeto a comparar.
     * @return true si ambas instancias tienen el mismo valor; false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectTitle)) return false;
        ProjectTitle that = (ProjectTitle) o;
        return Objects.equals(value, that.value);
    }

    /**
     * Retorna el código hash basado en el valor del título.
     *
     * @return Código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    /**
     * Retorna una representación en cadena del título.
     *
     * @return Cadena con el título del proyecto.
     */
    @Override
    public String toString() {
        return value;
    }
}