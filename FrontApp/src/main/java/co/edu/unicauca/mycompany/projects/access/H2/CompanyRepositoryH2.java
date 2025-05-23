package co.edu.unicauca.mycompany.projects.access.H2;

import co.edu.unicauca.mycompany.projects.access.ICompanyRepository;
import co.edu.unicauca.mycompany.projects.domain.entities.Company;
import co.edu.unicauca.mycompany.projects.domain.entities.enumSector;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Implementación del repositorio de empresas que se comunica con un servicio REST
 * mediante peticiones HTTP para realizar operaciones de persistencia y consulta
 * sobre entidades de tipo Company
 */
public class CompanyRepositoryH2 extends Token implements ICompanyRepository {
    private static final Logger LOGGER = Logger.getLogger(CompanyRepositoryH2.class.getName());
    /**
     * Registra una nueva empresa enviando una solicitud HTTP POST al servicio REST.
     *
     * @param newCompany la entidad {@code Company} que se desea guardar.
     * @return {@code true} si la operación fue exitosa (código HTTP 200 o 201),
     *         o {@code false} en caso contrario o si ocurre una excepción.
     */
    @Override
    public boolean save(Company newCompany) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        try {
            String apiUrl = IUrl.ApiGatewayUrl + "/public/register-new-company";
            LOGGER.info("Attempting to save company via API Gateway: " + apiUrl);

            HttpPost request = new HttpPost(apiUrl);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json"); // Es buena práctica incluir Accept

            // Añadir token de autorización si está disponible
            if (this.token != null && !this.token.isEmpty()) {
                request.setHeader("Authorization", "Bearer " + this.token);
                LOGGER.info("Authorization header added for /company/register");
            } else {
                LOGGER.warning("Token is null or empty. Request to /company/register will be made without Authorization header.");
                // Dependiendo de la configuración del gateway, esto podría fallar si el token es estrictamente necesario.
            }

            String companyJson = mapper.writeValueAsString(mapToCompanyDto(newCompany));
            LOGGER.fine("Request body for save: " + companyJson);

            HttpEntity entity = new StringEntity(companyJson, "UTF-8"); // Especificar UTF-8 es buena práctica
            request.setEntity(entity);

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity()); // Leer el cuerpo para logging

            LOGGER.info("Response status code for save: " + statusCode);
            LOGGER.fine("Response body for save: " + responseBody);

            return statusCode == 201 || statusCode == 200;

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "IOException during save operation: " + ex.getMessage(), ex);
            return false;
        }
    }

    /**
     * Obtiene la información completa de una empresa según su NIT (o ID),
     * consultando el API Gateway.
     * URL: /company/{companyId} (requiere autenticación)
     *
     * @param nit el NIT (o ID) de la empresa cuya información se desea recuperar.
     * @return un objeto {@code Company} con los datos de la empresa si la respuesta fue exitosa;
     * en caso contrario, se retorna una instancia por defecto o null.
     */
    @Override
    public Company companyInfo(String nit) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        Company companyReturn = null;

        try {
            String apiUrl = IUrl.ApiGatewayUrl + "/company/" + nit;
            LOGGER.info("Attempting to get company info via API Gateway: " + apiUrl);
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");

            // Añadir token de autorización si está disponible
            if (this.token != null && !this.token.isEmpty()) {
                request.setHeader("Authorization", "Bearer " + this.token);
                LOGGER.info("Authorization header added for /company/" + nit);
            } else {
                LOGGER.warning("Token is null or empty. Request to /company/" + nit + " will be made without Authorization header.");
                // Esta ruta requiere autenticación, por lo que sin token probablemente falle.
            }

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String jsonResponse = EntityUtils.toString(response.getEntity()); // Leer siempre para liberar conexión y log

            LOGGER.info("Response status code for companyInfo: " + statusCode);
            LOGGER.fine("Response body for companyInfo: " + jsonResponse);

            if (statusCode == 200) {
                companyReturn = mapFromCompanyDto(mapper.readValue(jsonResponse, CompanyDto.class));
            } else {
                LOGGER.log(Level.SEVERE, "Error al obtener información de la empresa. Código de estado: " + statusCode + ", Respuesta: " + jsonResponse);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "IOException during companyInfo operation: " + ex.getMessage(), ex);
        }

        // Considera si realmente quieres devolver una instancia por defecto en caso de error total.
        // Podría ser mejor devolver null y manejarlo en el código que llama.
        return companyReturn != null ? companyReturn :
                new Company("exampleCompany", "contact", "", "", "", enumSector.OTHER, "", "", "");
    }

    /**
     * Obtiene el identificador único de un sector a partir de su nombre, mediante
     * una solicitud HTTP GET al servicio REST.
     *
     * @param sectorName el nombre del sector (por ejemplo, "TECHNOLOGY", "HEALTH", etc.).
     * @return una cadena con el identificador del sector si la operación fue exitosa;
     *         una cadena vacía en caso de error o si el sector no existe.
     */
 /**
     * Obtiene el identificador único de un sector a partir de su nombre, mediante
     * una solicitud HTTP GET al API Gateway.
     * URL: /company/sector/{sectorName} (permitAll)
     *
     * @param sectorName el nombre del sector (por ejemplo, "TECHNOLOGY", "HEALTH", etc.).
     * @return una cadena con el identificador del sector si la operación fue exitosa;
     * una cadena vacía en caso de error o si el sector no existe.
     */
    @Override
    public String getSectorIdByName(String sectorName) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        String sectorId = "";

        try {
            // La URL debe ser codificada si sectorName puede contener caracteres especiales.
            // Por simplicidad, aquí no se hace, pero considéralo para producción.
            String apiUrl = IUrl.ApiGatewayUrl + "/company/sector/" + sectorName;
            LOGGER.info("Attempting to get sector ID via API Gateway: " + apiUrl);
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");

            // Aunque es permitAll, enviar el token si está disponible no suele ser un problema
            // y puede ser útil si la política de seguridad cambia o para auditoría.
            if (this.token != null && !this.token.isEmpty()) {
                request.setHeader("Authorization", "Bearer " + this.token);
                LOGGER.info("Authorization header (optional for permitAll) added for /company/sector/" + sectorName);
            }

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String jsonResponse = EntityUtils.toString(response.getEntity());

            LOGGER.info("Response status code for getSectorIdByName: " + statusCode);
            LOGGER.fine("Response body for getSectorIdByName: " + jsonResponse);

            if (statusCode == 200) {
                SectorResponse sectorResponse = mapper.readValue(jsonResponse, SectorResponse.class);
                sectorId = sectorResponse.getSectorId();
            } else {
                LOGGER.log(Level.SEVERE, "Error al obtener el sector. Código de estado: " + statusCode + ", Respuesta: " + jsonResponse);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "IOException during getSectorIdByName operation: " + ex.getMessage(), ex);
        }

        return sectorId;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    /**
    * Clase auxiliar utilizada para la serialización y deserialización de objetos {@code Company}
    * en formato JSON al comunicarse con servicios REST.
    *
    * Representa la estructura esperada en las solicitudes y respuestas del servicio.
    */
    private static class CompanyDto {
        /** Identificador único del usuario. */
        private String userId;

        /** Correo electrónico asociado al usuario. */
        private String userEmail;

        /** Contraseña del usuario. */
        private String userPassword;

        /** Nombre de la empresa. */
        private String companyName;

        /** Nombre del contacto principal. */
        private String contactName;

        /** Apellido del contacto principal. */
        private String contactLastName;

        /** Número telefónico del contacto principal. */
        private String contactPhone;

        /** Cargo del contacto principal dentro de la empresa. */
        private String contactPosition;

        /** Sector al que pertenece la empresa. */
        private String companySector;

        // Getters y setters
        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public String getUserEmail() { return userEmail; }
        public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

        public String getUserPassword() { return userPassword; }
        public void setUserPassword(String userPassword) { this.userPassword = userPassword; }

        public String getCompanyName() { return companyName; }
        public void setCompanyName(String companyName) { this.companyName = companyName; }

        public String getContactName() { return contactName; }
        public void setContactName(String contactName) { this.contactName = contactName; }

        public String getContactLastName() { return contactLastName; }
        public void setContactLastName(String contactLastName) { this.contactLastName = contactLastName; }

        public String getContactPhone() { return contactPhone; }
        public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

        public String getContactPosition() { return contactPosition; }
        public void setContactPosition(String contactPosition) { this.contactPosition = contactPosition; }

        public String getCompanySector() { return companySector; }
        public void setCompanySector(String companySector) { this.companySector = companySector; }
    }

    /**
    * Clase auxiliar utilizada para deserializar la respuesta JSON
    * que contiene el identificador de un sector.
    *
    * Se emplea en la obtención de información específica del sector desde el servicio REST.
    */
    private static class SectorResponse {
        /** Identificador único del sector. */
        private String sectorId;

        public String getSectorId() { return sectorId; }
        public void setSectorId(String sectorId) { this.sectorId = sectorId; }
    }

    /**
    * Convierte un objeto {@code Company} a su representación {@code CompanyDto},
    * adecuada para ser serializada en formato JSON al comunicarse con el servicio REST.
    *
    * @param company la entidad {@code Company} que se desea convertir.
    * @return una instancia de {@code CompanyDto} con los campos correspondientes mapeados.
    */
    private CompanyDto mapToCompanyDto(Company company) {
        CompanyDto dto = new CompanyDto();
        dto.setUserId(company.getId());
        dto.setUserEmail(company.getEmail());
        dto.setUserPassword(company.getPassword());
        dto.setCompanyName(company.getCompanyName());
        dto.setContactName(company.getContactName());
        dto.setContactLastName(company.getContactLastName());
        dto.setContactPhone(company.getContactPhone());
        dto.setContactPosition(company.getContactPosition());
        dto.setCompanySector(company.getCompanySector().toString());
        return dto;
    }

    /**
    * Convierte un objeto {@code CompanyDto}, recibido de una respuesta JSON del servicio REST,
    * en una instancia de {@code Company}.
    *
    * Si el campo {@code companySector} no corresponde con ningún valor válido del enum {@code enumSector},
    * se asigna por defecto el sector {@code OTHER}.
    *
    * @param dto el objeto {@code CompanyDto} que se desea convertir.
    * @return una nueva instancia de {@code Company} con los datos mapeados desde el DTO.
    */
    private Company mapFromCompanyDto(CompanyDto dto) {
        enumSector sector;
        try {
            sector = enumSector.valueOf(dto.getCompanySector());
        } catch (IllegalArgumentException e) {
            sector = enumSector.OTHER;
        }

        return new Company(
                                dto.getCompanyName(),
                dto.getContactName(),
                dto.getContactLastName(),
                dto.getContactPhone(),
                dto.getContactPosition(),
                sector,
                dto.getUserId(),
                dto.getUserEmail(),
                dto.getUserPassword()
  
      );
    }
}