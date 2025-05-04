package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.access.MariaDB.ProjectRepositoryMariaDB;
import co.edu.unicauca.mycompany.projects.access.H2.ProjectRepositoryH2;
import java.util.HashMap;
import java.util.Map;

public class ProjectRepositoryFactory implements IRepositoryFactory<IProjectRepository> {

    private static ProjectRepositoryFactory instance;

    private final Map<RepositoryType, IProjectRepository> repositoryMap;

    private ProjectRepositoryFactory() {
        repositoryMap = new HashMap<>();
        repositoryMap.put(RepositoryType.MARIADB, new ProjectRepositoryMariaDB());
        repositoryMap.put(RepositoryType.H2, new ProjectRepositoryH2());
    }

    public static ProjectRepositoryFactory getInstance() {
        if (instance == null) {
            instance = new ProjectRepositoryFactory();
        }
        return instance;
    }

    @Override
    public IProjectRepository getRepository(RepositoryType type) {
        IProjectRepository repo = repositoryMap.get(type);
        if (repo == null) {
            throw new IllegalArgumentException("Tipo de repositorio de compañía no soportado: " + type);
        }
        return repo;
    }
}
