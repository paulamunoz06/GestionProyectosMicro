package co.edu.unicauca.microservicestudent.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

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
