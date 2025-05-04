package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.access.MariaDB.UserRepositoryMariaDB;
import co.edu.unicauca.mycompany.projects.access.H2.UserRepositoryH2;
import java.util.HashMap;
import java.util.Map;

/**
 * Fábrica concreta para la creación de objetos que implementan la interfaz IUserRepository.
 * 
 * Utiliza el patrón Singleton para asegurar una única instancia de la fábrica durante la ejecución.
 * 
 * Permite obtener implementaciones concretas del repositorio de usuarios según el tipo de almacenamiento.
 */
public class UserRepositoryFactory implements IRepositoryFactory<IUserRepository> {

    /**
     * Instancia única de UserRepositoryFactory.
     */
    private static UserRepositoryFactory instance;

    /**
     * Mapa que asocia cada tipo de repositorio con su implementación concreta de IUserRepository.
     */
    private final Map<RepositoryType, IUserRepository> repositoryMap;

    /**
     * Constructor privado que inicializa el mapa con las implementaciones disponibles:
     * MariaDB y H2.
     */
    private UserRepositoryFactory() {
        repositoryMap = new HashMap<>();
        repositoryMap.put(RepositoryType.MARIADB, new UserRepositoryMariaDB());
        repositoryMap.put(RepositoryType.H2, new UserRepositoryH2());
    }

    /**
     * Devuelve la instancia única de UserRepositoryFactory.
     * Si aún no ha sido creada, se inicializa una nueva instancia.
     *
     * @return instancia única de UserRepositoryFactory
     */
    public static UserRepositoryFactory getInstance() {
        if (instance == null) {
            instance = new UserRepositoryFactory();
        }
        return instance;
    }

    /**
     * Obtiene la implementación concreta de IUserRepository correspondiente al tipo de repositorio solicitado.
     *
     * @param type tipo de repositorio requerido (por ejemplo, MARIADB o H2)
     * @return instancia correspondiente de IUserRepository
     * @throws IllegalArgumentException si el tipo de repositorio no está soportado
     */
    @Override
    public IUserRepository getRepository(RepositoryType type) {
        IUserRepository repo = repositoryMap.get(type);
        if (repo == null) {
            throw new IllegalArgumentException("Tipo de repositorio de compañía no soportado: " + type);
        }
        return repo;
    }
}

