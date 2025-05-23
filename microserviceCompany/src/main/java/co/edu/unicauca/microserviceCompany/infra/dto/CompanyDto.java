package co.edu.unicauca.microserviceCompany.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa el Data Transfer Object (DTO) para la entidad de empresa.
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    /**
     * El identificador del usuario que está asociado a la empresa.
     */
    private String userId;

    /**
     * El correo electrónico del usuario asociado a la empresa.
     */
    private String userEmail;

    /**
     * El nombre de la empresa.
     */
    private String companyName;

    /**
     * El nombre del contacto principal de la empresa.
     */
    private String contactName;

    /**
     * El apellido del contacto principal de la empresa.
     */
    private String contactLastName;

    /**
     * El número de teléfono del contacto principal de la empresa.
     */
    private String contactPhone;

    /**
     * El cargo o puesto del contacto principal dentro de la empresa.
     */
    private String contactPosition;

    /**
     * El sector al que pertenece la empresa, representado como una cadena.
     * Ejemplos: "Tecnología", "Salud", "Educación", etc.
     */
    private String companySector;
}
