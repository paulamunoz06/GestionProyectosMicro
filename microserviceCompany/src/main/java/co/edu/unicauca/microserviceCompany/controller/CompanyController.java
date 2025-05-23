package co.edu.unicauca.microserviceCompany.controller;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto; // Para respuestas y perfil de empresa
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyRegistrationRequestDto; // Para la petición de registro inicial
import co.edu.unicauca.microserviceCompany.service.ICompanyService;
import co.edu.unicauca.microserviceCompany.service.KeycloakAdminService; // Importar el nuevo servicio
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/") // Ruta base para los endpoints de empresa
public class CompanyController {

    private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private ICompanyService companyService;

    @Autowired
    private KeycloakAdminService keycloakAdminService; // Inyectar el servicio de admin de Keycloak

    /**
     * Endpoint PÚBLICO para registrar una nueva empresa Y crear su usuario correspondiente en Keycloak.
     * Este es el flujo para un usuario completamente nuevo que se registra como empresa.
     *
     * @param registrationRequest DTO que contiene el username, email, password deseados para Keycloak,
     *                            y los detalles del perfil de la empresa.
     * @return ResponseEntity con el estado de la operación y los datos de la empresa registrada.
     */
    @PostMapping("/public/register-new-company") // Endpoint público, permitido en SecurityConfig
    public ResponseEntity<?> registerNewCompanyWithUserCreation(@RequestBody CompanyRegistrationRequestDto registrationRequest) {
        try {
            // Validar que los datos necesarios para Keycloak y la empresa estén presentes
            if (registrationRequest.getUserId() == null || registrationRequest.getUserId().isEmpty() ||
                    registrationRequest.getUserEmail() == null || registrationRequest.getUserEmail().isEmpty() ||
                    registrationRequest.getUserPassword() == null || registrationRequest.getUserPassword().isEmpty() ||
                    registrationRequest.getCompanyName() == null || registrationRequest.getCompanyName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Username, email, password y nombre de la empresa son requeridos."));
            }

            logger.info("Iniciando registro de nuevo usuario '{}' en Keycloak y perfil de empresa.", registrationRequest.getUserId());

            // Paso 1: Crear el usuario en Keycloak
            String keycloakUserId = keycloakAdminService.createUserInKeycloak(
                    registrationRequest.getUserId(),
                    registrationRequest.getUserEmail(),
                    registrationRequest.getUserPassword(),
                    "company", // Rol a asignar en Keycloak
                    registrationRequest.getContactName(),
                    registrationRequest.getContactLastName()
            );
            logger.info("Usuario '{}' creado en Keycloak con ID: {}", registrationRequest.getUserId(), keycloakUserId);

            // Paso 2: Crear el perfil de la empresa en la base de datos local
            CompanyDto companyProfileDto = new CompanyDto();
            companyProfileDto.setUserId(keycloakUserId); // Usar el ID devuelto por Keycloak
            companyProfileDto.setUserEmail(registrationRequest.getUserEmail()); // Usar el email proporcionado
            companyProfileDto.setCompanyName(registrationRequest.getCompanyName());
            companyProfileDto.setContactName(registrationRequest.getContactName());
            companyProfileDto.setContactLastName(registrationRequest.getContactLastName());
            companyProfileDto.setContactPhone(registrationRequest.getContactPhone());
            companyProfileDto.setContactPosition(registrationRequest.getContactPosition());
            companyProfileDto.setCompanySector(registrationRequest.getCompanySector());
            // userPassword no se guarda en nuestra BD, ni en este DTO

            Company registeredCompanyEntity = companyService.registerCompany(companyProfileDto);
            logger.info("Perfil de empresa registrado en la BD local para Keycloak User ID: {}", keycloakUserId);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(companyService.companyToDto(registeredCompanyEntity)); // Devolver el DTO de la empresa

        } catch (IllegalArgumentException e) {
            logger.warn("Argumento inválido durante el registro de nueva empresa/usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) { // Captura errores de KeycloakAdminService o validaciones
            logger.error("Error de ejecución durante el registro de nueva empresa/usuario: {}", e.getMessage(), e);
            // Podrías querer dar un mensaje más genérico al cliente en algunos casos
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error durante el proceso de registro: " + e.getMessage()));
        } catch (Exception e) { // Captura general para otros errores inesperados
            logger.error("Error interno inesperado durante el registro de nueva empresa/usuario: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno en el servidor."));
        }
    }

    /**
     * Endpoint PROTEGIDO para que una empresa YA AUTENTICADA (con token JWT)
     * cree o actualice su perfil de empresa en la base de datos.
     * El RequestBody solo debe contener los detalles de la empresa (companyName, etc.).
     *
     * @param companyDetailsDto DTO con los detalles del perfil de la empresa.
     * @param jwt Token JWT del usuario autenticado (con rol 'COMPANY').
     * @return ResponseEntity con el estado de la operación.
     */
    @PostMapping("/register") // Este es el endpoint original, ahora protegido
    @PreAuthorize("hasRole('company')") // El rol debe ser COMPANY (Spring Security añade ROLE_ internamente)
    public ResponseEntity<?> createOrUpdateCompanyProfile(@RequestBody CompanyDto companyDetailsDto, @AuthenticationPrincipal Jwt jwt) {
        try {
            String userIdFromToken = jwt.getSubject();
            String userEmailFromToken = jwt.getClaimAsString("email");

            if (userIdFromToken == null || userEmailFromToken == null) {
                logger.warn("Token JWT incompleto: falta subject o email.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Información de usuario (ID o email) faltante en el token."));
            }

            // Usar un nuevo DTO para el servicio, asegurando que userId y userEmail vienen del token
            CompanyDto dtoForService = new CompanyDto();
            dtoForService.setUserId(userIdFromToken);
            dtoForService.setUserEmail(userEmailFromToken); // O el email de contacto de la empresa si es diferente
            dtoForService.setCompanyName(companyDetailsDto.getCompanyName());
            dtoForService.setContactName(companyDetailsDto.getContactName());
            dtoForService.setContactLastName(companyDetailsDto.getContactLastName());
            dtoForService.setContactPhone(companyDetailsDto.getContactPhone());
            dtoForService.setContactPosition(companyDetailsDto.getContactPosition());
            dtoForService.setCompanySector(companyDetailsDto.getCompanySector());

            // Aquí, companyService.registerCompany podría necesitar una lógica de "buscar o crear".
            // Si la empresa con userIdFromToken ya existe, se podría actualizar.
            // Si no, se crea. Esto depende de la implementación de tu ICompanyService.
            // Asumamos por ahora que registerCompany puede manejar esto o que tienes un método separado para update.
            // Por simplicidad, y dado que el endpoint original era POST /register, lo mantenemos así.
            // Una mejor API tendría POST para crear y PUT para actualizar.

            Optional<Company> existingCompanyOpt = companyService.findById(userIdFromToken);
            Company resultingCompany;
            if (existingCompanyOpt.isPresent()) {
                // Actualizar perfil existente (necesitarías un método update en ICompanyService)
                // Asumamos que tienes updateCompany(String id, CompanyDto dto)
                logger.info("Usuario {} actualizando perfil de empresa.", userIdFromToken);
                resultingCompany = companyService.updateCompany(userIdFromToken, dtoForService); // Necesitas este método en tu servicio
                return ResponseEntity.ok(companyService.companyToDto(resultingCompany));
            } else {
                // Crear nuevo perfil de empresa
                logger.info("Usuario {} creando perfil de empresa.", userIdFromToken);
                resultingCompany = companyService.registerCompany(dtoForService);
                return ResponseEntity.status(HttpStatus.CREATED).body(companyService.companyToDto(resultingCompany));
            }

        } catch (IllegalArgumentException e) {
            logger.warn("Argumento ilegal al registrar/actualizar perfil de empresa: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error interno al registrar/actualizar perfil de empresa: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor."));
        }
    }

    @GetMapping("/{companyId}")
    @PreAuthorize("hasRole('coordinator') or (hasRole('company') and #companyId == authentication.principal.subject)")
    public ResponseEntity<?> getCompanyById(@PathVariable String companyId, @AuthenticationPrincipal Jwt jwt) {
        try {
            logger.info("Usuario {} solicitando empresa con ID: {}", jwt.getSubject(), companyId);
            Optional<Company> company = companyService.findById(companyId);
            if (company.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(companyService.companyToDto(company.get()));
        } catch (Exception e) {
            logger.error("Error al obtener empresa por ID {}: ", companyId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
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
    // Si SecurityConfig ya tiene .hasRole("COORDINATOR"), este @PreAuthorize es opcional.
    // @PreAuthorize("hasRole('COORDINATOR')")
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
    // @PreAuthorize("hasRole('COORDINATOR')") // Opcional si SecurityConfig lo cubre
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