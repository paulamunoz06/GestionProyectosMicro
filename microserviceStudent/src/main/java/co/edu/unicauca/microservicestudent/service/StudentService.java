package co.edu.unicauca.microservicestudent.service;

import co.edu.unicauca.microservicestudent.entity.EnumProjectState;
import co.edu.unicauca.microservicestudent.entity.Project;
import co.edu.unicauca.microservicestudent.entity.Student;
import co.edu.unicauca.microservicestudent.infra.dto.StudentDto;
import co.edu.unicauca.microservicestudent.repository.IProjectRepository;
import co.edu.unicauca.microservicestudent.repository.IStudentRepository;
import co.edu.unicauca.microservicestudent.infra.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los estudiantes.
 *
 * Implementa los métodos definidos en la interfaz {@link IStudentService} para manejar la postulación de estudiantes a proyectos,
 * así como la conversión de entidades de estudiantes a objetos DTO.
 */
@Service
public class StudentService implements IStudentService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private IProjectRepository projectRepository;

    /**
     * Permite a un estudiante postularse a un proyecto, verificando las condiciones necesarias.
     *
     * @param student El estudiante que realiza la postulación.
     * @param project El proyecto al cual el estudiante desea postularse.
     * @return El estudiante actualizado con la postulación realizada.
     * @throws Exception Si el proyecto no está disponible para postulaciones o si el estudiante ya se postuló o fue aprobado en el proyecto.
     */
    @Override
    @Transactional
    public Student studentPostulation(Student student, Project project) throws Exception {
        try {
            // Verifica que el proyecto esté en estado "ACEPTADO" para recibir postulaciones.
            if (project.getProState() != EnumProjectState.ACEPTADO) {
                throw new Exception("El proyecto no recibe postulaciones");
            }
            // Verifica que el estudiante no se haya postulado previamente al proyecto.
            if (student.getPostulated().contains(project)) {
                throw new Exception("El estudiante ya se postuló al proyecto");
            }
            // Verifica que el estudiante no haya sido aprobado en el proyecto.
            if (student.getApproved().contains(project)) {
                throw new Exception("El estudiante ya es parte del proyecto");
            }

            // Realiza la postulación del estudiante al proyecto.
            student.getPostulated().add(project);
            project.getPostulated().add(student);

            // Guarda los cambios en las entidades de estudiante y proyecto.
            studentRepository.save(student);
            projectRepository.save(project);

            // Convierte el estudiante a DTO y lo envía a la cola RabbitMQ para actualizar el estado de la postulación.
            StudentDto studentDto = studentToDto(student);
            rabbitTemplate.convertAndSend(RabbitMQConfig.STUDENTPOSTULATION_QUEUE, studentDto);

            return student;

        } catch (Exception e) {
            // Lanza la excepción si ocurre algún error en el proceso de postulación.
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Busca un estudiante por su ID.
     *
     * @param studentId El ID del estudiante a buscar.
     * @return Un {@link Optional} que contiene el estudiante si existe, o está vacío si no se encuentra.
     */
    @Override
    @Transactional
    public Optional<Student> findById(String studentId) {
        return studentRepository.findById(studentId);
    }

    /**
     * Convierte un objeto {@link Student} en un objeto {@link StudentDto}.
     *
     * @param student El estudiante que se desea convertir.
     * @return El objeto {@link StudentDto} que representa al estudiante.
     */
    @Override
    public StudentDto studentToDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setEmail(student.getEmail());
        studentDto.setPassword(student.getPassword());
        studentDto.setPostulatedIds(student.getPostulated().stream().map(Project::getProId).collect(Collectors.toList()));
        studentDto.setApprovedIds(student.getApproved().stream().map(Project::getProId).collect(Collectors.toList()));
        return studentDto;
    }
}