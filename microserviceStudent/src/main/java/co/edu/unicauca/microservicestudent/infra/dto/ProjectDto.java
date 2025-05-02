package co.edu.unicauca.microservicestudent.infra.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
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
    private String proId;
    private String proTitle;
    private String proDescription;
    private String proAbstract;
    private String proGoals;
    private LocalDate proDate;
    private int proDeadLine;
    private Double proBudget;
    private String proState;
    private String idcompany;
    private String proCoordinator;
    @JsonIgnore
    private List<String> postulated = new ArrayList<>();
    @JsonIgnore
    private List<String> approved = new ArrayList<>();

    public ProjectDto(String proid, String protitle, String prodescription, String proAbstract, String proGoals, LocalDate proDate, int proDeadline, Double proBudget, String proState, String idcompany, String proCoordinator) {
        this.proId = proid;
        this.proTitle = protitle;
        this.proDescription = prodescription;
        this.proAbstract = proAbstract;
        this.proGoals = proGoals;
        this.proDate = proDate;
        this.proDeadLine = proDeadline;
        this.proBudget = proBudget;
        this.proState = proState;
        this.idcompany = idcompany;
        this.proCoordinator = proCoordinator;
    }

}
