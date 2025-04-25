package co.edu.unicauca.microservicestudent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
public class Student {
    @Id
    @NotNull(message = "El ID no puede estar vacío")
    @Column(name = "STUDENTID",nullable = false, unique = true)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Column(name = "STUDENTNAME",nullable = false)
    private String name;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Debe ser un correo electrónico válido")
    @Column(name = "STUDENTEMAIL",nullable = false)
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "La contraseña debe contener al menos una minúscula, una mayúscula y un número"
    )
    @Column(name = "STUDENTPASSWORD",nullable = false)
    private String password;

    // Atributos derivados de relaciones
    @ManyToMany(mappedBy = "postulated")
    private List<Project> postulated = new ArrayList<>();

    @ManyToMany(mappedBy = "approved")
    private List<Project> approved = new ArrayList<>();

    public Student(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
