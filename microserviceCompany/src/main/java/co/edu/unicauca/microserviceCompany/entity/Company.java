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

/**
 * Representa una empresa en el sistema.
 *
 * La clase {@link Company} contiene los atributos necesarios para registrar y gestionar una empresa,
 * como el nombre de la empresa, los datos de contacto y la relación con los proyectos asociados a la empresa.
 *
 * La clase incluye métodos para agregar proyectos a la empresa y realizar la persistencia de los datos en la base de datos.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "company")
public class Company {

    /**
     * Identificador único de la empresa.
     * Este atributo se utiliza como clave primaria de la entidad {@link Company}.
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * Correo electrónico de la empresa.
     * Este campo debe ser único y no puede estar vacío. Se valida que el correo sea válido a través de anotaciones de validación.
     */
    @Email(message = "Debe proporcionar un correo electrónico válido")
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Column(name = "email",nullable = false, unique = true)
    private String email;

    /**
     * Nombre de la empresa.
     * Este campo no puede estar vacío y se valida a través de una anotación de validación.
     */
    @NotBlank(message = "El nombre de la empresa no puede estar vacío")
    @Column(name = "company_name",nullable = false)
    private String companyName;

    /**
     * Nombre del contacto de la empresa.
     * Este campo no puede estar vacío y se valida a través de una anotación de validación.
     */
    @NotBlank(message = "El nombre del contacto no puede estar vacío")
    @Column(name = "contact_name",nullable = false)
    private String contactName;

    /**
     * Apellido del contacto de la empresa.
     * Este campo no puede estar vacío y se valida a través de una anotación de validación.
     */
    @NotBlank(message = "El apellido del contacto no puede estar vacío")
    @Column(name = "contact_last_name",nullable = false)
    private String contactLastName;

    /**
     * Teléfono del contacto de la empresa.
     * Este campo no puede estar vacío y se valida a través de una anotación de validación.
     */
    @NotBlank(message = "El teléfono de contacto no puede estar vacío")
    @Column(name = "contact_phone",nullable = false)
    private String contactPhone;

    /**
     * Cargo del contacto de la empresa.
     * Este campo no puede estar vacío y se valida a través de una anotación de validación.
     */
    @NotBlank(message = "El cargo del contacto no puede estar vacío")
    @Column(name = "contact_position",nullable = false)
    private String contactPosition;

    /**
     * Sector al que pertenece la empresa.
     * Este campo es un valor enumerado basado en la clase {@link EnumSector}.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "company_sector",nullable = false)
    private EnumSector companySector;

    /**
     * Lista de proyectos asociados a la empresa.
     * Esta relación es de tipo `OneToMany`, donde una empresa puede tener varios proyectos.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "idcompany")
    private List<Project> companyProjects = new ArrayList<>();

    /**
     * Constructor que inicializa los atributos de la empresa.
     *
     * @param id Identificador único de la empresa.
     * @param companyName Nombre de la empresa.
     * @param contactName Nombre del contacto de la empresa.
     * @param contactLastName Apellido del contacto de la empresa.
     * @param contactPhone Teléfono del contacto de la empresa.
     * @param contactPosition Cargo del contacto de la empresa.
     * @param companySector Sector al que pertenece la empresa.
     * @param email Correo electrónico de la empresa.
     */
    public Company(String id, String companyName, String contactName, String contactLastName, String contactPhone,
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

    /**
     * Método para agregar un proyecto a la empresa.
     *
     * @param project Proyecto que se desea agregar a la lista de proyectos de la empresa.
     */
    public void addProject(Project project) {
        companyProjects.add(project);
    }
}