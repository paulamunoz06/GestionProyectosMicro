package co.edu.unicauca.microservicecoordinator.domain.valueObject;

import jakarta.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ProjectId{

    private final String value;

    public ProjectId() {
        value = "";
    }

    public ProjectId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El ID del proyecto no puede estar vacío");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectId)) return false;
        ProjectId that = (ProjectId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

