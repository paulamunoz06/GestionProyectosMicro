package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.access.MariaDB.ProjectRepositoryMariaDB;
import co.edu.unicauca.mycompany.projects.access.H2.ProjectRepositoryH2;
import java.util.HashMap;
import java.util.Map;

/**
 * Fábrica concreta para la creación de objetos que implementan la interfaz IProjectRepository.
 * 
 * Implementa el patrón Singleton para asegurar que exista una única instancia durante la ejecución.
 * 
 * Utiliza un mapa para asociar cada tipo de repositorio con su implementación correspondiente.
 */
public class ProjectRepositoryFactory implements IRepositoryFactory<IProjectRepository> {

    /**
     * Instancia única de ProjectRepositoryFactory.
     */
    private static ProjectRepositoryFactory instance;

    /**
     * Mapa que contiene las implementaciones concretas de IProjectRepository asociadas a cada tipo de repositorio.
     */
    private final Map<RepositoryType, IProjectRepository> repositoryMap;

    /**
     * Constructor privado que inicializa el mapa con las implementaciones disponibles:
     * MariaDB y H2.
     */
    private ProjectRepositoryFactory() {
        repositoryMap = new HashMap<>();
        repositoryMap.put(RepositoryType.MARIADB, new ProjectRepositoryMariaDB());
        repositoryMap.put(RepositoryType.H2, new ProjectRepositoryH2());
    }

    /**
     * Retorna la instancia única de ProjectRepositoryFactory.
     * Si aún no existe, se crea una nueva instancia.
     *
     * @return instancia única de ProjectRepositoryFactory
     */
    public static ProjectRepositoryFactory getInstance() {
        if (instance == null) {
            instance = new ProjectRepositoryFactory();
        }
        return instance;
    }

    /**
     * Devuelve una implementación concreta de IProjectRepository correspondiente al tipo de repositorio indicado.
     *
     * @param type tipo de repositorio solicitado (por ejemplo, MARIADB o H2)
     * @return instancia correspondiente de IProjectRepository
     * @throws IllegalArgumentException si el tipo de repositorio no está soportado
     */
    @Override
    public IProjectRepository getRepository(RepositoryType type) {
        IProjectRepository repo = repositoryMap.get(type);
        if (repo == null) {
            throw new IllegalArgumentException("Tipo de repositorio de compañía no soportado: " + type);
        }
        return repo;
    }
}
