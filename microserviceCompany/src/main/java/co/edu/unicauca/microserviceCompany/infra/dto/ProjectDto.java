package co.edu.unicauca.microserviceCompany.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private String proId;
    private String proTitle;
    private String proDescription;
    private String proAbstract;
    private String proGoals;
    private LocalDate proDate;
    private int proDeadLine;
    private Double proBudget;
    private String proState;
}
