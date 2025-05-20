package co.edu.unicauca.microservicecoordinator.domain.valueObject;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public final class ProjectDeadline {

    private final int months;

    public ProjectDeadline() {
        months = 0;
    }

    public ProjectDeadline(int months) {
        if (months <= 0) {
            throw new IllegalArgumentException("El plazo debe ser mayor a 0 meses.");
        }
        if (months > 60) {
            throw new IllegalArgumentException("El plazo no puede exceder los 60 meses.");
        }
        this.months = months;
    }

    public int getMonths() {
        return months;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectDeadline)) return false;
        ProjectDeadline that = (ProjectDeadline) o;
        return months == that.months;
    }

    @Override
    public int hashCode() {
        return Objects.hash(months);
    }

    @Override
    public String toString() {
        return months + " meses";
    }
}

