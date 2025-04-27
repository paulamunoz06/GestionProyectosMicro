package co.edu.unicauca.microservicecoordinator.infra.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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


    public ProjectDto(String proid, String protitle, String prodescription, String proAbstract, String proGoals, LocalDate proDate, int proDeadline, Double proBudget, String proState) {
        this.proId = proid;
        this.proTitle = protitle;
        this.proDescription = prodescription;
        this.proAbstract = proAbstract;
        this.proGoals = proGoals;
        this.proDate = proDate;
        this.proDeadLine = proDeadline;
        this.proBudget = proBudget;
        this.proState = proState;
    }

}
