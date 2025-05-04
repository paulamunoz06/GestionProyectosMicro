package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.access.MariaDB.CompanyRepositoryMariaDB;
import co.edu.unicauca.mycompany.projects.access.H2.CompanyRepositoryH2;
import java.util.HashMap;
import java.util.Map;

public class CompanyRepositoryFactory implements IRepositoryFactory<ICompanyRepository> {

    private static CompanyRepositoryFactory instance;

    private final Map<RepositoryType, ICompanyRepository> repositoryMap;

    private CompanyRepositoryFactory() {
        repositoryMap = new HashMap<>();
        repositoryMap.put(RepositoryType.MARIADB, new CompanyRepositoryMariaDB());
        repositoryMap.put(RepositoryType.H2, new CompanyRepositoryH2());
    }

    public static CompanyRepositoryFactory getInstance() {
        if (instance == null) {
            instance = new CompanyRepositoryFactory();
        }
        return instance;
    }

    @Override
    public ICompanyRepository getRepository(RepositoryType type) {
        ICompanyRepository repo = repositoryMap.get(type);
        if (repo == null) {
            throw new IllegalArgumentException("Tipo de repositorio de compañía no soportado: " + type);
        }
        return repo;
    }
}
