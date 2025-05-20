package co.edu.unicauca.microservicecoordinator.domain.model;

public class Coordinator {

    // Atributos

    private String coordinatorId;

    private String coordinatorEmail;

    private String coordinatorPassword;

    // Constructores

    public Coordinator() {
    }

    public Coordinator(String coordinatorId, String coordinatorEmail, String coordinatorPassword) {
        this.coordinatorId = coordinatorId;
        this.coordinatorEmail = coordinatorEmail;
        this.coordinatorPassword = coordinatorPassword;
    }

    // Getters

    public String getCoordinatorId() {
        return coordinatorId;
    }

    public String getCoordinatorEmail() {
        return coordinatorEmail;
    }

    public String getCoordinatorPassword() {
        return coordinatorPassword;
    }

}
