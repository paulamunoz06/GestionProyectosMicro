package co.edu.unicauca.microserviceCompany.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Company {

    @Id
    private String id;

    @Email(message = "Debe proporcionar un correo electrónico válido")
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String email;



    @NotBlank(message = "El nombre de la empresa no puede estar vacío")
    @Column(nullable = false)
    private String companyName;

    @NotBlank(message = "El nombre del contacto no puede estar vacío")
    @Column(nullable = false)
    private String contactName;

    @NotBlank(message = "El apellido del contacto no puede estar vacío")
    @Column(nullable = false)
    private String contactLastName;

    @NotBlank(message = "El teléfono de contacto no puede estar vacío")
    @Column(nullable = false)
    private String contactPhone;

    @NotBlank(message = "El cargo del contacto no puede estar vacío")
    @Column(nullable = false)
    private String contactPosition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumSector companySector;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "company_id")
    private List<Project> companyProjects = new ArrayList<>();

    public Company(String id,String companyName, String contactName, String contactLastName, String contactPhone,
                   String contactPosition, EnumSector companySector, String email) {
        this.id = id;
        this.companyName = companyName;
        this.contactName = contactName;
        this.contactLastName = contactLastName;
        this.contactPhone = contactPhone;
        this.contactPosition = contactPosition;
        this.companySector = companySector;
        this.email = email;
    }

    public void addProject(Project project) {
        companyProjects.add(project);
    }
}
