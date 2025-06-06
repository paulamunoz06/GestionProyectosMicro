package co.edu.unicauca.microservicecoordinator.domain.model;

import co.edu.unicauca.microservicecoordinator.domain.model.state.*;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.*;
import java.time.LocalDate;

/**
 * Representa un proyecto dentro del sistema.
 *
 * Esta clase pertenece al modelo de dominio y encapsula todos los atributos y comportamientos
 * asociados a un proyecto. Implementa el patrón de máquina de estados para gestionar
 * su ciclo de vida y su transición entre estados.
 */
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

    /**
     * Construye un nuevo proyecto con todos los datos necesarios.
     *
     * @param proId           Identificador único del proyecto (Value Object).
     * @param proTitle        Título del proyecto (Value Object).
     * @param proDescription  Descripción detallada del proyecto.
     * @param proAbstract     Resumen del proyecto.
     * @param proGoals        Objetivos del proyecto.
     * @param proDate         Fecha de registro del proyecto (Value Object).
     * @param proDeadLine     Plazo máximo del proyecto en meses (Value Object).
     * @param proBudget       Presupuesto asignado al proyecto.
     * @param proState        Estado inicial del proyecto.
     * @param idcompany       Identificador de la empresa asociada.
     * @param proCoordinator  Identificador del coordinador responsable.
     */
    public Project(ProjectId proId, ProjectTitle proTitle, String proDescription, String proAbstract, String proGoals,
                   ProjectRegistrationDate proDate, ProjectDeadline proDeadLine, Double proBudget,
                   EnumProjectState proState, String idcompany, String proCoordinator) {

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

    /**
     * Obtiene el identificador del proyecto.
     *
     * @return Identificador como objeto de valor.
     */
    public ProjectId getProId() {
        return new ProjectId(this.proId);
    }

    /**
     * Obtiene el título del proyecto.
     *
     * @return Título como objeto de valor.
     */
    public ProjectTitle getProTitle() {
        return new ProjectTitle(this.proTitle);
    }

    /**
     * Obtiene la descripción del proyecto.
     *
     * @return Descripción detallada.
     */
    public String getProDescription() {
        return proDescription;
    }

    /**
     * Obtiene el resumen del proyecto.
     *
     * @return Resumen breve.
     */
    public String getProAbstract() {
        return proAbstract;
    }

    /**
     * Obtiene los objetivos del proyecto.
     *
     * @return Objetivos definidos.
     */
    public String getProGoals() {
        return proGoals;
    }

    /**
     * Obtiene la fecha de registro del proyecto.
     *
     * @return Fecha como objeto de valor.
     */
    public ProjectRegistrationDate getProDate() {
        return new ProjectRegistrationDate(this.proDate);
    }

    /**
     * Obtiene el plazo máximo del proyecto.
     *
     * @return Plazo en meses como objeto de valor.
     */
    public ProjectDeadline getProDeadLine() {
        return new ProjectDeadline(this.proDeadLine);
    }

    /**
     * Obtiene el presupuesto asignado al proyecto.
     *
     * @return Monto del presupuesto.
     */
    public Double getProBudget() {
        return proBudget;
    }

    /**
     * Obtiene el estado actual del proyecto (como enumeración).
     *
     * @return Estado del proyecto.
     */
    public EnumProjectState getProState() {
        return proState;
    }

    /**
     * Obtiene el identificador de la empresa asociada.
     *
     * @return ID de la empresa.
     */
    public String getIdcompany() {
        return idcompany;
    }

    /**
     * Obtiene el identificador del coordinador asignado.
     *
     * @return ID del coordinador.
     */
    public String getProCoordinator() {
        return proCoordinator;
    }

    /**
     * Obtiene el estado actual del proyecto como objeto de comportamiento.
     *
     * @return Estado actual implementado mediante el patrón State.
     */
    public ProjectState getCurrentState() {
        return currentState;
    }

    /**
     * Sincroniza el estado actual del proyecto con base en su enumeración {@code proState}.
     * Este método instancia la clase correspondiente al estado actual usando el patrón State.
     */
    public void syncState() {
        switch (this.proState) {
            case ACEPTADO -> this.currentState = new AcceptedState();
            case RECHAZADO -> this.currentState = new RejectState();
            case EJECUCION -> this.currentState = new ExecutedState();
            case CERRADO -> this.currentState = new ClosedState();
            case RECIBIDO -> this.currentState = new ReceivedState();
        }
    }

    /**
     * Cambia el estado del proyecto y actualiza su comportamiento interno.
     *
     * @param newState Nuevo estado a asignar.
     */
    public void changeState(EnumProjectState newState) {
        this.proState = newState;
        syncState();
    }
}