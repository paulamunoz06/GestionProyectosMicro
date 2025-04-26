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

    private String id;
    private String name;
    private String email;
    private String password;
    private List<String> postulatedIds = new ArrayList<>();
    private List<String> approvedIds = new ArrayList<>();

    public StudentDto(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
