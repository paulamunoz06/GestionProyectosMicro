package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.access.MariaDB.CoordinatorRepositoryMariaDB;
import co.edu.unicauca.mycompany.projects.access.H2.CoordinatorRepositoryH2;
import java.util.HashMap;
import java.util.Map;

public class CoordinatorRepositoryFactory  implements IRepositoryFactory<ICoordinatorRepository> {

    private static CoordinatorRepositoryFactory instance;

    private final Map<RepositoryType, ICoordinatorRepository> repositoryMap;

    private CoordinatorRepositoryFactory() {
        repositoryMap = new HashMap<>();
        repositoryMap.put(RepositoryType.MARIADB, new CoordinatorRepositoryMariaDB());
        repositoryMap.put(RepositoryType.H2, new CoordinatorRepositoryH2());
    }

    public static CoordinatorRepositoryFactory getInstance() {
        if (instance == null) {
            instance = new CoordinatorRepositoryFactory();
        }
        return instance;
    }

    @Override
    public ICoordinatorRepository getRepository(RepositoryType type) {
        ICoordinatorRepository repo = repositoryMap.get(type);
        if (repo == null) {
            throw new IllegalArgumentException("Tipo de repositorio de compañía no soportado: " + type);
        }
        return repo;
    }
}