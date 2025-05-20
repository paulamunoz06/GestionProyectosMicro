package co.edu.unicauca.microservicecoordinator.domain.valueObject;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public final class ProjectRegistrationDate {
    private final LocalDate date;

    public ProjectRegistrationDate() {
        date = LocalDate.now();
    }

    public ProjectRegistrationDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula.");
        }
        if (date.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser futura.");
        }
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectRegistrationDate)) return false;
        ProjectRegistrationDate that = (ProjectRegistrationDate) o;
        return date.equals(that.date);
    }

    @Override public int hashCode() {
        return Objects.hash(date);
    }

    @Override public String toString() {
        return date.toString();
    }
}
