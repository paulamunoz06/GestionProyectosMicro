package co.edu.unicauca.microservicestudent.service;

import co.edu.unicauca.microservicestudent.entity.Project;
import co.edu.unicauca.microservicestudent.entity.Student;
import co.edu.unicauca.microservicestudent.infra.dto.StudentDto;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * Interfaz que define los servicios relacionados con la gestión de estudiantes.
 *
 * Proporciona métodos para interactuar con los estudiantes, incluyendo la postulación de un estudiante a un proyecto,
 * la consulta de un estudiante por su ID y la conversión entre objetos de tipo {@link Student} y {@link StudentDto}.
 */
public interface IStudentService {

    /**
     * Permite que un estudiante se postule a un proyecto.
     *
     * @param student El estudiante que se postula al proyecto.
     * @param project El proyecto al que el estudiante desea postularse.
     * @return El estudiante actualizado después de realizar la postulación.
     * @throws Exception Si ocurre algún error durante la postulación del estudiante al proyecto.
     */
    @Transactional
    Student studentPostulation(Student student, Project project) throws Exception;

    /**
     * Busca un estudiante en el sistema por su ID.
     *
     * @param studentId El ID del estudiante que se desea buscar.
     * @return Un {@link Optional} que contiene el estudiante si se encuentra, o está vacío si no existe.
     */
    @Transactional
    Optional<Student> findById(String studentId);

    /**
     * Convierte un objeto {@link Student} en un objeto {@link StudentDto}.
     *
     * @param student El estudiante que se va a convertir.
     * @return El objeto {@link StudentDto} que contiene los mismos datos que el estudiante original.
     */
    StudentDto studentToDto(Student student);
}

