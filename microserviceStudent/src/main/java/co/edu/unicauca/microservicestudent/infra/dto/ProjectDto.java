package co.edu.unicauca.microservicestudent.infra.dto;

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
public class ProjectDto {
    private Long proid;
    private String protitle;
    private String prodescription;
    private String proAbstract;
    private String proGoals;
    private LocalDate proDate;
    private int proDeadline;
    private Double proBudget;
    private String proState;
    private List<Long> postulatedIds = new ArrayList<>();
    private List<Long> approvedIds = new ArrayList<>();

    public ProjectDto(Long proid, String protitle, String prodescription, String proAbstract, String proGoals, LocalDate proDate, int proDeadline, Double proBudget, String proState) {
        this.proid = proid;
        this.protitle = protitle;
        this.prodescription = prodescription;
        this.proAbstract = proAbstract;
        this.proGoals = proGoals;
        this.proDate = proDate;
        this.proDeadline = proDeadline;
        this.proBudget = proBudget;
        this.proState = proState;
    }

}
