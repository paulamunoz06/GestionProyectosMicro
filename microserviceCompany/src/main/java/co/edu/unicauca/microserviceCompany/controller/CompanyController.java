package co.edu.unicauca.microserviceCompany.controller;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Importar
import org.springframework.security.oauth2.jwt.Jwt;
/**
 * Controlador REST para gestionar las operaciones relacionadas con las empresas en el sistema.
 *
 */
@RestController
@RequestMapping("/company")
public class CompanyController {

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

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
    @PreAuthorize("hasRole('company')")
    public ResponseEntity<?> registerCompany(@RequestBody CompanyDto companyDto, @AuthenticationPrincipal Jwt jwt) {
        try {
            String userId = jwt.getSubject();
            String userEmail = jwt.getClaimAsString("email");

            if (userId == null || userId.isEmpty() || userEmail == null || userEmail.isEmpty()) {
                logger.warn("Token JWT incompleto: falta subject o email.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Información de usuario (ID o email) faltante en el token."));
            }

            companyDto.setUserId(userId);
            companyDto.setUserEmail(userEmail);
            // companyDto.setUserPassword(null); // Ya no existe este campo

            logger.info("Usuario {} intentando registrar empresa: {}", userId, companyDto.getCompanyName());
            Company registeredCompany = companyService.registerCompany(companyDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(companyService.companyToDto(registeredCompany));
        } catch (IllegalArgumentException e) {
            logger.warn("Error de argumento al registrar empresa: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error interno al registrar la empresa: ", e); // Log completo
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno al registrar la empresa."));
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
    @PreAuthorize("hasRole('company') or hasRole('coordinator')")
    public ResponseEntity<?> getCompanyById(@PathVariable String companyId,
                                                @AuthenticationPrincipal Jwt jwt) { // jwt para logging/info
        try {
            logger.info("Usuario {} solicitando empresa con ID: {}", jwt.getSubject(), companyId);
            Optional<Company> company = companyService.findById(companyId);
            if (company.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(companyService.companyToDto(company.get()));
        } catch (Exception e) {
            logger.error("Error al obtener empresa por ID {}: ", companyId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // Considerar INTERNAL_SERVER_ERROR para errores inesperados
                    .body(Map.of("error", "Error al obtener la empresa."));
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
            logger.error("Error al obtener empresa por email {}: ", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener la empresa por email."));
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
    @PreAuthorize("hasRole('coordinator')")
    public ResponseEntity<?> getAllCompanies() {
        try {
            List<Company> companies = companyService.findAllCompanies();
            List<CompanyDto> companyDtos = companies.stream()
                    .map(companyService::companyToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(companyDtos);
        } catch (Exception e) {
            logger.error("Error al obtener todas las empresas: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener todas las empresas."));
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
    public ResponseEntity<?> getSectorIdByName(@PathVariable String sectorName) {
        try {
            String sectorId = companyService.getSectorIdByName(sectorName);
            return ResponseEntity.ok(Map.of("sectorId", sectorId));
        } catch (Exception e) { // Ser más específico con la excepción si es posible
            logger.warn("Error al obtener ID del sector para '{}': {}", sectorName, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al procesar el nombre del sector."));
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
            logger.error("Error al obtener el conteo de empresas: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener el conteo de empresas."));
        }
    }
}