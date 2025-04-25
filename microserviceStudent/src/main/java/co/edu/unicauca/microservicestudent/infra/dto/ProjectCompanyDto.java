package co.edu.unicauca.microservicestudent.infra.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCompanyDto {
    private Long proid;
    private String protitle;
    private String prodescription;
    private String proAbstract;
    private String proGoals;
    private LocalDate proDate;
    private int proDeadline;
    private Double proBudget;
    private String proState;

    private String id;
    private String email;
    private String password;
    private String companyName;
    private String contactName;
    private String contactLastName;
    private String contactPhone;
    private String contactPosition;
    private String companySector;
}
