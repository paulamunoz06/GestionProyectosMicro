package co.edu.unicauca.microserviceCompany.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRegistrationRequestDto {

    // Datos para crear el usuario en Keycloak
    private String userId; // El username/ID que el usuario desea para Keycloak
    private String userEmail;    // El email para la cuenta de Keycloak
    private String userPassword; // La contraseña deseada para la cuenta de Keycloak

    // Datos para el perfil de la empresa que se guardará en tu base de datos
    private String companyName;
    private String contactName;
    private String contactLastName;
    private String contactPhone;
    private String contactPosition;
    private String companySector;
}