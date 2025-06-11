package co.edu.unicauca.microserviceCompany.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
// Quitamos @NoArgsConstructor y @AllArgsConstructor si forzamos el builder.
// Si necesitas un constructor sin args para JPA/Frameworks, puedes añadirlo explícitamente o mantener @NoArgsConstructor.
// Para este ejemplo, lo mantendré por simplicidad con JPA, pero el constructor principal será privado.
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter // Puedes restringir setters si quieres inmutabilidad post-construcción
@NoArgsConstructor // Para JPA y otras librerías de framework
@Table(name = "project")
public class Project {

    @Id
    @Column(name = "proid", nullable = false, unique = true)
    private String proId;

    @NotBlank(message = "El título del proyecto no puede estar vacío")
    @Size(min = 2, max = 100)
    @Column(name = "protitle", nullable = false)
    private String proTitle;

    @NotBlank(message = "La descripción del proyecto no puede estar vacía")
    @Size(min = 10, max = 1000)
    @Column(name = "prodescription", nullable = false)
    private String proDescription;

    @NotBlank(message = "El resumen no puede estar vacío")
    @Column(name = "proabstract", nullable = false)
    private String proAbstract;

    @NotBlank(message = "Los objetivos no pueden estar vacíos")
    @Column(name = "progoals", nullable = false)
    private String proGoals;

    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "prodate", nullable = false)
    private LocalDate proDate;

    @NotNull(message = "El tiempo máximo en meses no puede estar vacío")
    @Column(name = "prodeadline", nullable = false)
    private int proDeadLine;

    @Column(name = "probudget", nullable = true)
    private Double proBudget;

    @Enumerated(EnumType.STRING)
    @Column(name = "prostate", nullable = false)
    private EnumProjectState proState;

    @Column(name = "idcompany", nullable = true)
    private String idcompany;

    @Column(name = "idcoordinator", nullable = true)
    private String proCoordinator;

    // Constructor privado: solo el Builder puede llamarlo
    private Project(ProjectBuilder builder) {
        this.proId = builder.proId;
        this.proTitle = builder.proTitle;
        this.proDescription = builder.proDescription;
        this.proAbstract = builder.proAbstract;
        this.proGoals = builder.proGoals;
        this.proDate = builder.proDate;
        this.proDeadLine = builder.proDeadLine;
        this.proBudget = builder.proBudget;
        this.proState = builder.proState;
        this.idcompany = builder.idcompany;
        this.proCoordinator = builder.proCoordinator;
    }

    // Método estático para obtener una instancia del Builder
    public static ProjectBuilder builder(String proId, String proTitle) {
        return new ProjectBuilder(proId, proTitle);
    }

    // Clase Builder interna estática
    public static class ProjectBuilder {
        // Campos requeridos (ejemplo, podrían ser más o menos)
        private final String proId;
        private final String proTitle;

        // Campos opcionales con valores por defecto si aplica
        private String proDescription = "";
        private String proAbstract = "";
        private String proGoals = "";
        private LocalDate proDate = LocalDate.now(); // Valor por defecto
        private int proDeadLine;
        private Double proBudget;
        private EnumProjectState proState = EnumProjectState.RECIBIDO; // Valor por defecto
        private String idcompany;
        private String proCoordinator;

        // Constructor del Builder con los campos requeridos
        public ProjectBuilder(String proId, String proTitle) {
            if (proId == null || proId.trim().isEmpty()) {
                throw new IllegalArgumentException("Project ID (proId) no puede ser nulo o vacío.");
            }
            if (proTitle == null || proTitle.trim().isEmpty()) {
                throw new IllegalArgumentException("Project Title (proTitle) no puede ser nulo o vacío.");
            }
            this.proId = proId;
            this.proTitle = proTitle;
        }

        // Métodos "setter" fluidos para los campos opcionales
        public ProjectBuilder description(String proDescription) {
            this.proDescription = proDescription;
            return this;
        }

        public ProjectBuilder proAbstract(String proAbstract) {
            this.proAbstract = proAbstract;
            return this;
        }

        public ProjectBuilder goals(String proGoals) {
            this.proGoals = proGoals;
            return this;
        }

        public ProjectBuilder date(LocalDate proDate) {
            if (proDate != null) { // Permitir sobreescribir el valor por defecto solo si no es null
                this.proDate = proDate;
            }
            return this;
        }

        public ProjectBuilder deadLine(int proDeadLine) {
            this.proDeadLine = proDeadLine;
            return this;
        }

        public ProjectBuilder budget(Double proBudget) {
            this.proBudget = proBudget;
            return this;
        }

        public ProjectBuilder state(EnumProjectState proState) {
            if (proState != null) { // Permitir sobreescribir el valor por defecto solo si no es null
                this.proState = proState;
            }
            return this;
        }

        public ProjectBuilder companyId(String idcompany) {
            this.idcompany = idcompany;
            return this;
        }

        public ProjectBuilder coordinatorId(String proCoordinator) {
            this.proCoordinator = proCoordinator;
            return this;
        }

        // Método build() que crea la instancia de Project
        public Project build() {
            // Aquí podrías añadir validaciones más complejas de los campos del builder si es necesario
            // antes de crear el objeto Project.
            if (this.proDeadLine <= 0 && this.proState != EnumProjectState.RECHAZADO) { // Ejemplo de validación
                // Podrías lanzar una excepción o ajustar el valor
                // System.err.println("Advertencia: proDeadLine debería ser positivo.");
            }
            return new Project(this);
        }
    }
}