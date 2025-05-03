package co.edu.unicauca.microserviceCompany.entity;

/**
 * Enumeración que define los sectores disponibles para las empresas.
 *
 * La clase {@link EnumSector} representa los diferentes sectores a los que puede pertenecer una empresa.
 * Estos sectores se utilizan en la clase {@link Company} para categorizar a las empresas según su área de operación.
 *
 */
public enum EnumSector {
    /**
     * Sector de tecnología.
     */
    TECHNOLOGY,

    /**
     * Sector de salud.
     */
    HEALTH,

    /**
     * Sector de educación.
     */
    EDUCATION,

    /**
     * Sector de servicios.
     */
    SERVICES,

    /**
     * Otro sector no especificado.
     */
    OTHER;
}