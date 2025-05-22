package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.domain.entities.Student;

/**
 * Interfaz que define el contrato para el repositorio de estudiantes.
 */
public interface IStudentRepository extends IsetToken{
    /**
     * Obtiene la información detallada de un estudiante a partir de su identificador.
     * 
     * @param id Identificador único del estudiante.
     * @return Objeto Student con la información del estudiante, o null si no se encuentra.
     */
    Student getStudent(String id);
}
