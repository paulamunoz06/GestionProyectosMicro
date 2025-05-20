package co.edu.unicauca.microservicecoordinator.domain.model;

import co.edu.unicauca.microservicecoordinator.domain.model.state.*;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.*;
import java.time.LocalDate;

public class Project {

    /**
     * Identificador único del proyecto.
     */
    private String proId;

    /**
     * Título del proyecto.
     */
    private String proTitle;

    /**
     * Descripción detallada del proyecto.
     */
    private String proDescription;

    /**
     * Resumen del proyecto.
     */
    private String proAbstract;

    /**
     * Objetivos del proyecto.
     */
    private String proGoals;

    /**
     * Fecha de registro del proyecto.
     */
    private LocalDate proDate;

    /**
     * Tiempo máximo de ejecución del proyecto en meses.
     */
    private int proDeadLine;

    /**
     * Presupuesto asignado al proyecto.
     */
    private Double proBudget;

    /**
     * Estado actual del proyecto como enumeracion.
     */
    private EnumProjectState proState;

    /**
     * Estado actual del proyecto como estado.
     */
    private ProjectState currentState;

    /**
     * Identificador único de la compañia.
     */
    private String idcompany;

    /**
     * Identificador único del coordinador.
     */
    private String proCoordinator;

    // Constructores

    public Project(ProjectId proId, ProjectTitle proTitle, String proDescription, String proAbstract, String proGoals, ProjectRegistrationDate proDate, ProjectDeadline proDeadLine, Double proBudget, EnumProjectState proState, String idcompany, String proCoordinator) {
        this.proId = proId.getValue();
        this.proTitle = proTitle.getValue();
        this.proDescription = proDescription;
        this.proAbstract = proAbstract;
        this.proGoals = proGoals;
        this.proDate = proDate.getDate();
        this.proDeadLine = proDeadLine.getMonths();
        this.proBudget = (proBudget != null) ? proBudget : 0.0;
        this.proState = proState != null ? proState : EnumProjectState.RECIBIDO;
        this.idcompany = idcompany != null ? idcompany : "";
        this.proCoordinator = proCoordinator != null ? proCoordinator : "";
        syncState();
    }

    // Getters

    public ProjectId getProId() {
        return new ProjectId(this.proId);
    }

    public ProjectTitle getProTitle() {
        return new ProjectTitle(this.proTitle);
    }

    public String getProDescription() {
        return proDescription;
    }

    public String getProAbstract() {
        return proAbstract;
    }

    public String getProGoals() {
        return proGoals;
    }

    public ProjectRegistrationDate getProDate() {
        return new ProjectRegistrationDate(this.proDate);
    }

    public ProjectDeadline getProDeadLine() {
        return new ProjectDeadline(this.proDeadLine);
    }

    public Double getProBudget() {
        return proBudget;
    }

    public EnumProjectState getProState() {
        return proState;
    }

    public String getIdcompany() {
        return idcompany;
    }

    public String getProCoordinator() {
        return proCoordinator;
    }

    public ProjectState getCurrentState() {
        return currentState;
    }

    // Metodos adicionales

    public void syncState() {
        switch (this.proState) {
            case ACEPTADO -> this.currentState = new AcceptedState();
            case RECHAZADO -> this.currentState = new RejectState();
            case EJECUCION -> this.currentState = new ExecutedState();
            case CERRADO -> this.currentState = new ClosedState();
            case RECIBIDO -> this.currentState = new ReceivedState();
        }
    }

    public void changeState(EnumProjectState newState) {
        this.proState = newState;
        syncState();
    }
}