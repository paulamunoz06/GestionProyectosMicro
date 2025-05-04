package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.access.MariaDB.CompanyRepositoryMariaDB;
import co.edu.unicauca.mycompany.projects.access.H2.CompanyRepositoryH2;
import java.util.HashMap;
import java.util.Map;

/**
 * Fábrica concreta que implementa el patrón Singleton y la interfaz IRepositoryFactory
 * para proporcionar instancias de ICompanyRepository según el tipo de almacenamiento especificado.
 */
public class CompanyRepositoryFactory implements IRepositoryFactory<ICompanyRepository> {

    /**
     * Instancia única de la clase CompanyRepositoryFactory.
     */
    private static CompanyRepositoryFactory instance;

    /**
     * Mapa que asocia cada tipo de repositorio con su implementación concreta de ICompanyRepository.
     */
    private final Map<RepositoryType, ICompanyRepository> repositoryMap;

    /**
     * Constructor privado para evitar la creación de múltiples instancias.
     * Inicializa el mapa con las implementaciones disponibles para MariaDB y H2.
     */
    private CompanyRepositoryFactory() {
        repositoryMap = new HashMap<>();
        repositoryMap.put(RepositoryType.MARIADB, new CompanyRepositoryMariaDB());
        repositoryMap.put(RepositoryType.H2, new CompanyRepositoryH2());
    }

    /**
     * Retorna la instancia única de CompanyRepositoryFactory.
     * Si no existe, la crea.
     *
     * @return instancia singleton de CompanyRepositoryFactory
     */
    public static CompanyRepositoryFactory getInstance() {
        if (instance == null) {
            instance = new CompanyRepositoryFactory();
        }
        return instance;
    }

    /**
     * Devuelve una implementación concreta de ICompanyRepository según el tipo especificado.
     *
     * @param type tipo de repositorio (ej. MARIADB, H2)
     * @return implementación correspondiente de ICompanyRepository
     * @throws IllegalArgumentException si el tipo no está soportado
     */
    @Override
    public ICompanyRepository getRepository(RepositoryType type) {
        ICompanyRepository repo = repositoryMap.get(type);
        if (repo == null) {
            throw new IllegalArgumentException("Tipo de repositorio de compañía no soportado: " + type);
        }
        return repo;
    }
}
