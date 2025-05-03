package co.edu.unicauca.microserviceCompany.controller;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador REST para gestionar las operaciones relacionadas con las empresas en el sistema.
 *
 */
@RestController
@RequestMapping("/company")
public class CompanyController {

    /**
     * Servicio que maneja la lógica de negocio relacionada con las empresas.
     * Este servicio es responsable de registrar empresas, consultar por ID o email, y obtener estadísticas.
     */
    @Autowired
    private ICompanyService companyService;

    /**
     * Endpoint para registrar una nueva empresa en el sistema.
     *
     * Este método recibe un objeto {@link CompanyDto} con los datos de la empresa, registra la empresa
     * utilizando el servicio {@link ICompanyService}, y devuelve una respuesta con el estado de la operación.
     * En caso de éxito, devuelve un código HTTP 201 con la información de la empresa registrada. En caso de error,
     * retorna un código de error adecuado.
     *
     * @param companyDto Objeto que contiene los datos de la empresa a registrar.
     * @return ResponseEntity con el estado de la operación y los datos de la empresa registrada.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerCompany(@RequestBody CompanyDto companyDto) {
        try {
            Company registeredCompany = companyService.registerCompany(companyDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(companyService.companyToDto(registeredCompany));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al registrar la empresa: " + e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener los detalles de una empresa por su ID.
     *
     * Este método recibe el ID de la empresa como un parámetro de la URL, consulta la empresa correspondiente
     * usando el servicio {@link ICompanyService} y devuelve una respuesta con los detalles de la empresa.
     * Si no se encuentra la empresa, se devuelve una respuesta con código HTTP 404 (Not Found).
     *
     * @param companyId ID de la empresa a consultar.
     * @return ResponseEntity con los detalles de la empresa o una respuesta 404 si no se encuentra.
     */
    @GetMapping("/{companyId}")
    public ResponseEntity<?> getCompanyById(@PathVariable String companyId) {
        try {
            Optional<Company> company = companyService.findById(companyId);
            if (company.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(companyService.companyToDto(company.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener los detalles de una empresa por su correo electrónico.
     *
     * Este método recibe el correo electrónico de la empresa como un parámetro de la URL, consulta la empresa correspondiente
     * utilizando el servicio {@link ICompanyService} y devuelve una respuesta con los detalles de la empresa.
     * Si no se encuentra la empresa, se devuelve una respuesta con código HTTP 404 (Not Found).
     *
     * @param email Correo electrónico de la empresa a consultar.
     * @return ResponseEntity con los detalles de la empresa o una respuesta 404 si no se encuentra.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getCompanyByEmail(@PathVariable String email) {
        try {
            Optional<Company> company = companyService.findByEmail(email);
            if (company.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(companyService.companyToDto(company.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener una lista de todas las empresas registradas en el sistema.
     *
     * Este método consulta todas las empresas utilizando el servicio {@link ICompanyService} y devuelve
     * una lista con los datos de todas las empresas en formato {@link CompanyDto}.
     *
     * @return ResponseEntity con la lista de todas las empresas.
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllCompanies() {
        try {
            List<Company> companies = companyService.findAllCompanies();
            List<CompanyDto> companyDtos = companies.stream()
                    .map(company -> companyService.companyToDto(company))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(companyDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener el ID de un sector por su nombre.
     *
     * Este método recibe el nombre de un sector como parámetro, consulta el servicio {@link ICompanyService}
     * para obtener el ID correspondiente al sector, y devuelve una respuesta con el ID del sector.
     *
     * @param sectorName Nombre del sector a consultar.
     * @return ResponseEntity con el ID del sector.
     */
    @GetMapping("/sector/{sectorName}")
    public ResponseEntity<?> getSectorIdByName(@PathVariable String sectorName) {
        try {
            String sectorId = companyService.getSectorIdByName(sectorName);
            return ResponseEntity.ok(Map.of("sectorId", sectorId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para obtener la cantidad total de empresas registradas en el sistema.
     *
     * Este método consulta el servicio {@link ICompanyService} para obtener el número total de empresas
     * registradas y devuelve esta cantidad en una respuesta con formato JSON.
     *
     * @return ResponseEntity con la cantidad total de empresas.
     */
    @GetMapping("/count")
    public ResponseEntity<?> getCompanyCount() {
        try {
            int count = companyService.countAllCompanies();
            return ResponseEntity.ok(Map.of("count", count));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}