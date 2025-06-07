package co.edu.unicauca.microservicecoordinator.adapter.out.persistence;

import co.edu.unicauca.microservicecoordinator.application.port.out.ProjectRepositoryPort;
import co.edu.unicauca.microservicecoordinator.domain.model.EnumProjectState;
import co.edu.unicauca.microservicecoordinator.domain.model.Project;
import co.edu.unicauca.microservicecoordinator.domain.valueObject.ProjectId;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.entity.JpaProjectEntity;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.repository.JpaProjectRepository;
import co.edu.unicauca.microservicecoordinator.infraestructure.persistence.repository.ProjectJpaMapper;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDto;
import co.edu.unicauca.microservicecoordinator.presentation.dto.ProjectDtoMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del puerto de salida ProjectRepositoryPort para el acceso a la persistencia de proyectos.
 *
 * Esta clase actúa como un adaptador en la arquitectura hexagonal, delegando las operaciones de almacenamiento
 * y recuperación a un repositorio JPA específico (JpaProjectRepository) y realizando las transformaciones
 * necesarias entre entidades JPA y objetos del dominio.
 */
@Repository
@Primary
public class ProjectRepositoryImpl implements ProjectRepositoryPort {

    /**
     * Repositorio JPA que interactúa directamente con la base de datos.
     * Este componente está centrado en el modelo de datos y las entidades persistentes.
     */
    private final JpaProjectRepository jpaRepository;

    /**
     * Constructor que permite la inyección del repositorio JPA.
     *
     * @param jpaRepository Repositorio JPA especializado en la persistencia de proyectos.
     */
    public ProjectRepositoryImpl(JpaProjectRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    /**
     * Guarda un proyecto del dominio en la base de datos.
     * La entidad de dominio se transforma a una entidad JPA antes de persistirla,
     * y luego se transforma nuevamente a dominio tras la operación.
     *
     * @param project Proyecto a guardar.
     * @return Proyecto guardado, representado como entidad de dominio.
     */
    @Override
    public Project save(Project project) {
        JpaProjectEntity jpaEntity = ProjectJpaMapper.toJpaEntity(project);
        return ProjectJpaMapper.toDomainEntity(jpaRepository.save(jpaEntity));
    }

    /**
     * Busca un proyecto por su identificador y lo devuelve como un DTO.
     * Aplica una transformación desde entidad JPA a entidad de dominio y luego a DTO.
     *
     * @param id Identificador del proyecto.
     * @return Optional que contiene el proyecto si fue encontrado.
     */
    @Override
    public Optional<ProjectDto> findById(String id) {
        return jpaRepository.findById(new ProjectId(id))
                .map(ProjectJpaMapper::toDomainEntity)
                .map(ProjectDtoMapper::projectToDto);
    }

    /**
     * Recupera todos los proyectos almacenados en la base de datos,
     * transformando cada uno desde entidad JPA a DTO.
     *
     * @return Lista de todos los proyectos disponibles en forma de DTOs.
     */
    @Override
    public List<ProjectDto> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(ProjectJpaMapper::toDomainEntity)
                .map(ProjectDtoMapper::projectToDto)
                .collect(Collectors.toList());
    }

    /**
     * Cuenta la cantidad de proyectos que se encuentran en un estado específico.
     *
     * @param enumProjectState Estado del proyecto, representado como enumeración del dominio.
     * @return Número de proyectos con el estado especificado.
     */
    @Override
    public Long countByProState(EnumProjectState enumProjectState) {
        return jpaRepository.countByProState(enumProjectState);
    }

    /**
     * Cuenta el número total de proyectos almacenados en el sistema.
     *
     * @return Número total de proyectos.
     */
    @Override
    public int count() {
        return (int) jpaRepository.count();
    }
}