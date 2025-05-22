package co.edu.unicauca.microservicecoordinator.domain.valueObject;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public final class ProjectTitle {
    private final String value;

    public ProjectTitle() {
        value = "";
    }

    public ProjectTitle(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El título del proyecto no puede estar vacío.");
        }
        if (value.length() > 100) {
            throw new IllegalArgumentException("El título no puede tener más de 100 caracteres.");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectTitle)) return false;
        ProjectTitle that = (ProjectTitle) o;
        return Objects.equals(value, that.value);
    }

    @Override public int hashCode() {
        return Objects.hash(value);
    }

    @Override public String toString() {
        return value;
    }
}
