package co.edu.unicauca.microserviceCompany.service;

import org.springframework.transaction.annotation.Transactional;

/**
 * Clase abstracta que define la estructura del proceso de registro de entidades
 * usando el patrón Template Method.
 *
 * @param <E> Tipo de entidad (Company, Project, etc.)
 * @param <D> Tipo de DTO (CompanyDto, ProjectDto, etc.)
 */
public abstract class AbstractEntityRegistrationService<E, D> {

    /**
     * Método plantilla que define el flujo general del proceso de registro
     *
     * @param dto DTO con los datos de la entidad a registrar
     * @return La entidad registrada
     * @throws Exception Si ocurre algún error durante el registro
     */
    @Transactional // Buena práctica tenerlo aquí si todas las implementaciones son transaccionales
    public final E registerEntity(D dto) throws Exception {
        // Validación inicial
        validateData(dto);

        // Verificar si la entidad ya existe
        checkEntityExists(dto);

        // Realizar operaciones previas al registro (como notificar a otros servicios)
        preRegisterOperations(dto);

        // Convertir DTO a entidad
        E entity = dtoToEntity(dto);

        // Guardar la entidad
        E savedEntity = saveEntity(entity);

        // Realizar operaciones posteriores al registro
        postRegisterOperations(savedEntity);

        return savedEntity;
    }

    /**
     * Valida los datos de entrada del DTO
     *
     * @param dto DTO a validar
     * @throws IllegalArgumentException Si los datos son inválidos
     */
    protected abstract void validateData(D dto) throws IllegalArgumentException;

    /**
     * Verifica si la entidad ya existe
     *
     * @param dto DTO con los datos de la entidad
     * @throws IllegalArgumentException Si la entidad ya existe
     */
    protected abstract void checkEntityExists(D dto) throws IllegalArgumentException;

    /**
     * Realiza operaciones previas al registro
     *
     * @param dto DTO con los datos de la entidad
     * @throws Exception Si ocurre algún error
     */
    protected abstract void preRegisterOperations(D dto) throws Exception;

    /**
     * Convierte el DTO a la entidad correspondiente
     *
     * @param dto DTO con los datos de la entidad
     * @return La entidad convertida
     */
    protected abstract E dtoToEntity(D dto);

    /**
     * Guarda la entidad en la base de datos
     *
     * @param entity Entidad a guardar
     * @return La entidad guardada
     */
    protected abstract E saveEntity(E entity);

    /**
     * Realiza operaciones posteriores al registro
     *
     * @param entity Entidad guardada
     * @throws Exception Si ocurre algún error
     */
    protected abstract void postRegisterOperations(E entity) throws Exception;
}