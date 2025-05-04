package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.access.MariaDB.CoordinatorRepositoryMariaDB;
import co.edu.unicauca.mycompany.projects.access.H2.CoordinatorRepositoryH2;
import java.util.HashMap;
import java.util.Map;

/**
 * Fábrica concreta que implementa el patrón Singleton y la interfaz IRepositoryFactory
 * para proporcionar instancias de ICoordinatorRepository según el tipo de almacenamiento especificado.
 */
public class CoordinatorRepositoryFactory implements IRepositoryFactory<ICoordinatorRepository> {

    /**
     * Instancia única de la clase CoordinatorRepositoryFactory.
     */
    private static CoordinatorRepositoryFactory instance;

    /**
     * Mapa que asocia cada tipo de repositorio con su implementación concreta de ICoordinatorRepository.
     */
    private final Map<RepositoryType, ICoordinatorRepository> repositoryMap;

    /**
     * Constructor privado para evitar la creación de múltiples instancias.
     * Inicializa el mapa con las implementaciones disponibles para MariaDB y H2.
     */
    private CoordinatorRepositoryFactory() {
        repositoryMap = new HashMap<>();
        repositoryMap.put(RepositoryType.MARIADB, new CoordinatorRepositoryMariaDB());
        repositoryMap.put(RepositoryType.H2, new CoordinatorRepositoryH2());
    }

    /**
     * Retorna la instancia única de CoordinatorRepositoryFactory.
     * Si no existe, la crea.
     *
     * @return instancia singleton de CoordinatorRepositoryFactory
     */
    public static CoordinatorRepositoryFactory getInstance() {
        if (instance == null) {
            instance = new CoordinatorRepositoryFactory();
        }
        return instance;
    }

    /**
     * Devuelve una implementación concreta de ICoordinatorRepository según el tipo especificado.
     *
     * @param type tipo de repositorio (ej. MARIADB, H2)
     * @return implementación correspondiente de ICoordinatorRepository
     * @throws IllegalArgumentException si el tipo no está soportado
     */
    @Override
    public ICoordinatorRepository getRepository(RepositoryType type) {
        ICoordinatorRepository repo = repositoryMap.get(type);
        if (repo == null) {
            throw new IllegalArgumentException("Tipo de repositorio de compañía no soportado: " + type);
        }
        return repo;
    }
}
