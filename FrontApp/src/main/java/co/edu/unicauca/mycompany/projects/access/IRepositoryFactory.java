package co.edu.unicauca.mycompany.projects.access;

/**
 * Interfaz genérica para una fábrica de repositorios.
 */
public interface IRepositoryFactory<T> {
    /**
     * Retorna una implementación del repositorio de acuerdo al tipo indicado.
     *
     * @param type Tipo de repositorio.
     * @return Implementación del repositorio.
     */
    T getRepository(RepositoryType type);
}