package co.edu.unicauca.microservicestudent.infra.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private List<Long> postulatedIds = new ArrayList<>();
    private List<Long> approvedIds = new ArrayList<>();

    public StudentDto(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
