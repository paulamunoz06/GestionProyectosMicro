package co.edu.unicauca.mycompany.projects.access;

/**
 * Enumeración que representa los tipos de repositorios disponibles
 * para acceder a los datos en la aplicación.
 * 
 * MARIADB corresponde a una implementación basada en una base de datos MariaDB.
 * H2 corresponde a una implementación basada en la base de datos en memoria H2.
 */
public enum RepositoryType {
    MARIADB,
    H2
}
