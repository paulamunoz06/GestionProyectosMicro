package co.edu.unicauca.microserviceCompany.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    private String userId;
    private String userEmail;
    private String userPassword;
    private String companyName;
    private String contactName;
    private String contactLastName;
    private String contactPhone;
    private String contactPosition;
    private String companySector;
}
