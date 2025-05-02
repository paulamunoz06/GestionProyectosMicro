package co.edu.unicauca.mycompany.projects.domain.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un estudiante dentro del sistema.
 * 
 * Un estudiante es un usuario que puede participar en proyectos y postularse a ellos.
 * Esta clase extiende la clase User, heredando sus propiedades y funcionalidades.
 */
public class Student extends User {
    
    /** Lista de proyectos en los que el estudiante está participando activamente. */
    private List<String> approvedIds;
    
    /** Lista de proyectos a los que el estudiante se ha postulado. */
    private List<String> postulatedIds;
    
    private String name;
    /**
     * Constructor de la clase Student.
     * 
     * @param userId Identificador único del estudiante.
     * @param userEmail Correo electrónico del estudiante.
     * @param userPassword Contraseña del estudiante.
     */
    public Student(String userId, String userEmail, String userPassword) {
        super(userId, userEmail, userPassword);
        this.approvedIds = new ArrayList<>();
        this.postulatedIds = new ArrayList<>();
    }

    public Student(String id, String name, String email, String password,List<String> approvedIds, List<String> postulatedIds) {
        super(id, email, password);
        this.approvedIds = approvedIds;
        this.postulatedIds = postulatedIds;
        this.name = name;
    }

    public Student() {
    }

    /**
     * Obtiene la lista de proyectos en los que el estudiante participa.
     * 
     * @return Lista de proyectos activos del estudiante.
     */
    public List<String> getMyProjects() {
        return approvedIds;
    }

    /**
     * Establece la lista de proyectos en los que el estudiante participa.
     * 
     * @param myProjects Lista de proyectos en los que el estudiante está participando.
     */
    public void setMyProjects(List<String> myProjects) {
        this.approvedIds = myProjects;
    }

    /**
     * Obtiene la lista de proyectos a los que el estudiante se ha postulado.
     * 
     * @return Lista de proyectos en los que el estudiante ha enviado una postulación.
     */
    public List<String> getMyPostulations() {
        return postulatedIds;
    }

    /**
     * Establece la lista de postulaciones del estudiante.
     * 
     * @param myPostulations Lista de proyectos a los que el estudiante se ha postulado.
     */
    public void setMyPostulations(List<String> myPostulations) {
        this.postulatedIds = myPostulations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
