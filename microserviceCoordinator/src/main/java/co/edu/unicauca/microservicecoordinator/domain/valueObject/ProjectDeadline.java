package co.edu.unicauca.microservicecoordinator.domain.valueObject;

import jakarta.persistence.Embeddable;
import java.util.Objects;

/**
 * Representa el plazo máximo de ejecución de un proyecto en meses.
 *
 * Esta clase es un objeto de valor (Value Object) que encapsula la duración máxima
 * permitida para un proyecto, con validaciones para asegurar que el plazo sea válido.
 * Está anotada con Embeddable para ser utilizada como componente embebido en entidades JPA.
 */
@Embeddable
public final class ProjectDeadline {

    /**
     * Número de meses que representa el plazo máximo del proyecto.
     */
    private final int months;

    /**
     * Constructor por defecto que inicializa el plazo en 0 meses.
     *
     * Este constructor es necesario para JPA.
     */
    public ProjectDeadline() {
        months = 0;
    }

    /**
     * Construye un nuevo ProjectDeadline con el número de meses especificado.
     *
     * @param months Número de meses del plazo. Debe ser mayor a 0 y no exceder 60 meses.
     * @throws IllegalArgumentException si months es menor o igual a 0, o mayor que 60.
     */
    public ProjectDeadline(int months) {
        if (months <= 0) {
            throw new IllegalArgumentException("El plazo debe ser mayor a 0 meses.");
        }
        if (months > 60) {
            throw new IllegalArgumentException("El plazo no puede exceder los 60 meses.");
        }
        this.months = months;
    }

    /**
     * Obtiene el número de meses que representa el plazo del proyecto.
     *
     * @return Número de meses del plazo.
     */
    public int getMonths() {
        return months;
    }

    /**
     * Compara este objeto con otro para determinar igualdad basada en el valor del plazo.
     *
     * @param o Objeto a comparar.
     * @return true si ambos objetos representan el mismo número de meses; false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectDeadline)) return false;
        ProjectDeadline that = (ProjectDeadline) o;
        return months == that.months;
    }

    /**
     * Retorna el código hash basado en el valor del plazo.
     *
     * @return Código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(months);
    }

    /**
     * Retorna una representación en cadena del plazo.
     *
     * @return Cadena que indica el número de meses, por ejemplo: "12 meses".
     */
    @Override
    public String toString() {
        return months + " meses";
    }
}

