package co.edu.unicauca.microservicecoordinator.domain.model;

/**
 * Representa a un coordinador dentro del sistema.
 *
 * Esta clase contiene los datos básicos asociados a un coordinador, tales como su identificador,
 * correo electrónico y contraseña. Puede formar parte del modelo de dominio o servir como entidad
 * para autenticación o administración de usuarios con rol de coordinador.
 */
public class Coordinator {

    /**
     * Identificador único del coordinador.
     */
    private String coordinatorId;

    /**
     * Correo electrónico del coordinador.
     */
    private String coordinatorEmail;

    /**
     * Contraseña del coordinador.
     */
    private String coordinatorPassword;

    /**
     * Constructor vacío requerido para frameworks de persistencia o serialización.
     */
    public Coordinator() {
    }

    /**
     * Constructor que inicializa todos los atributos del coordinador.
     *
     * @param coordinatorId        Identificador único del coordinador.
     * @param coordinatorEmail     Correo electrónico del coordinador.
     * @param coordinatorPassword  Contraseña del coordinador.
     */
    public Coordinator(String coordinatorId, String coordinatorEmail, String coordinatorPassword) {
        this.coordinatorId = coordinatorId;
        this.coordinatorEmail = coordinatorEmail;
        this.coordinatorPassword = coordinatorPassword;
    }

    /**
     * Obtiene el identificador único del coordinador.
     *
     * @return Identificador del coordinador.
     */
    public String getCoordinatorId() {
        return coordinatorId;
    }

    /**
     * Obtiene el correo electrónico del coordinador.
     *
     * @return Correo electrónico del coordinador.
     */
    public String getCoordinatorEmail() {
        return coordinatorEmail;
    }

    /**
     * Obtiene la contraseña del coordinador.
     *
     * @return Contraseña del coordinador.
     */
    public String getCoordinatorPassword() {
        return coordinatorPassword;
    }
}
