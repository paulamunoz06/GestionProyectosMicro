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
public class CompanyRepositoryH2 implements ICompanyRepository {

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
            // Definir la URL de la API REST
            String apiUrl = "http://localhost:8080/company/register";

            // Crear una solicitud POST
            HttpPost request = new HttpPost(apiUrl);

            // Configurar las cabeceras para enviar JSON
            request.setHeader("Content-Type", "application/json");

            // Convertir el objeto Company a JSON
            String companyJson = mapper.writeValueAsString(mapToCompanyDto(newCompany));

            // Adjuntar el cuerpo JSON a la solicitud
            HttpEntity entity = new StringEntity(companyJson);
            request.setEntity(entity);

            // Ejecutar la solicitud y obtener la respuesta
            HttpResponse response = httpClient.execute(request);

            // Verificar el código de estado de la respuesta
            int statusCode = response.getStatusLine().getStatusCode();

            // Considerar exitoso si es 201 (Created) o 200 (OK)
            return statusCode == 201 || statusCode == 200;

        } catch (IOException ex) {
            Logger.getLogger(CompanyRepositoryH2.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    /**
     * Obtiene la información completa de una empresa según su NIT (Número de Identificación Tributaria),
     * consultando un servicio externo a través de una solicitud HTTP GET.
     *
     * @param nit el NIT de la empresa cuya información se desea recuperar.
     * @return un objeto {@code Company} con los datos de la empresa si la respuesta fue exitosa;
     *         en caso contrario, se retorna una instancia por defecto con datos ficticios.
     */
    @Override
    public Company companyInfo(String nit) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        Company companyReturn = null;

        try {
            // Definir la URL de la API REST
            String apiUrl = "http://localhost:8080/company/" + nit;
            // Crear una solicitud GET
            HttpGet request = new HttpGet(apiUrl);

            // Ejecutar la solicitud y obtener la respuesta
            HttpResponse response = httpClient.execute(request);

            // Verificar el código de estado de la respuesta
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // La solicitud fue exitosa, procesar la respuesta
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // Mapear la respuesta JSON a objeto Company
                companyReturn = mapFromCompanyDto(mapper.readValue(jsonResponse, CompanyDto.class));
            } else {
                // La solicitud falló, mostrar el código de estado
                Logger.getLogger(CompanyRepositoryH2.class.getName()).log(Level.SEVERE, null,
                        "Error al obtener información de la empresa. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(CompanyRepositoryH2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return companyReturn != null ? companyReturn :
                new Company("exampleCompany", "contact", "", "", "", enumSector.HEALTH, "", "", "");
    }

    /**
     * Obtiene el identificador único de un sector a partir de su nombre, mediante
     * una solicitud HTTP GET al servicio REST.
     *
     * @param sectorName el nombre del sector (por ejemplo, "TECHNOLOGY", "HEALTH", etc.).
     * @return una cadena con el identificador del sector si la operación fue exitosa;
     *         una cadena vacía en caso de error o si el sector no existe.
     */
    @Override
    public String getSectorIdByName(String sectorName) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        String sectorId = "";

        try {
            // Definir la URL de la API REST
            String apiUrl = "http://localhost:8080/company/sector/" + sectorName;
            // Crear una solicitud GET
            HttpGet request = new HttpGet(apiUrl);

            // Ejecutar la solicitud y obtener la respuesta
            HttpResponse response = httpClient.execute(request);

            // Verificar el código de estado de la respuesta
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // La solicitud fue exitosa, procesar la respuesta
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // Extraer el sectorId del JSON de respuesta
                SectorResponse sectorResponse = mapper.readValue(jsonResponse, SectorResponse.class);
                sectorId = sectorResponse.getSectorId();
            } else {
                // La solicitud falló, mostrar el código de estado
                Logger.getLogger(CompanyRepositoryH2.class.getName()).log(Level.SEVERE, null,
                        "Error al obtener el sector. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(CompanyRepositoryH2.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sectorId;
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
