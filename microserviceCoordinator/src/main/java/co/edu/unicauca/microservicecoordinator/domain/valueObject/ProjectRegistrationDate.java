package co.edu.unicauca.microservicecoordinator.domain.valueObject;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Representa la fecha de registro de un proyecto.
 *
 * Esta clase es un objeto de valor (Value Object) que encapsula una fecha de registro,
 * asegurando que la fecha no sea nula ni futura. Está anotada con Embeddable
 * para ser utilizada como componente embebido en entidades JPA.
 */
@Embeddable
public final class ProjectRegistrationDate {

    /**
     * Fecha de registro del proyecto.
     */
    private final LocalDate date;

    /**
     * Constructor por defecto que inicializa la fecha con la fecha actual del sistema.
     *
     * Este constructor es necesario para JPA.
     */
    public ProjectRegistrationDate() {
        date = LocalDate.now();
    }

    /**
     * Construye una nueva instancia de ProjectRegistrationDate con la fecha especificada.
     *
     * @param date Fecha de registro. No puede ser nula ni una fecha futura.
     * @throws IllegalArgumentException si date es nula o es una fecha posterior a la fecha actual.
     */
    public ProjectRegistrationDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser futura.");
        }
        this.date = date;
    }

    /**
     * Obtiene la fecha de registro.
     *
     * @return Fecha de registro.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Compara esta instancia con otra para determinar igualdad basada en la fecha.
     *
     * @param o Objeto a comparar.
     * @return {@code true} si ambas instancias tienen la misma fecha; false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectRegistrationDate)) return false;
        ProjectRegistrationDate that = (ProjectRegistrationDate) o;
        return date.equals(that.date);
    }

    /**
     * Retorna el código hash basado en la fecha.
     *
     * @return Código hash del objeto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    /**
     * Retorna una representación en cadena de la fecha.
     *
     * @return Cadena con la fecha en formato ISO-8601.
     */
    @Override
    public String toString() {
        return date.toString();
    }
}