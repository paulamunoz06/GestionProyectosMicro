package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.domain.entities.Company;
import co.edu.unicauca.mycompany.projects.domain.entities.enumSector;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CompanyRepository implements ICompanyRepository {
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
            Logger.getLogger(CompanyRepository.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

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
                Logger.getLogger(CompanyRepository.class.getName()).log(Level.SEVERE, null,
                        "Error al obtener información de la empresa. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(CompanyRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return companyReturn != null ? companyReturn :
                new Company("exampleCompany", "contact", "", "", "", enumSector.HEALTH, "", "", "");
    }

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
                Logger.getLogger(CompanyRepository.class.getName()).log(Level.SEVERE, null,
                        "Error al obtener el sector. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(CompanyRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sectorId;
    }

    // Clases auxiliares para la serialización/deserialización de JSON
    private static class CompanyDto {
        private String userId;
        private String userEmail;
        private String userPassword;
        private String companyName;
        private String contactName;
        private String contactLastName;
        private String contactPhone;
        private String contactPosition;
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

    private static class SectorResponse {
        private String sectorId;

        public String getSectorId() { return sectorId; }
        public void setSectorId(String sectorId) { this.sectorId = sectorId; }
    }

    // Métodos de mapeo entre entidad y DTO
    private CompanyDto mapToCompanyDto(Company company) {
        CompanyDto dto = new CompanyDto();
        dto.setUserId(company.getUserId());
        dto.setUserEmail(company.getUserEmail());
        dto.setUserPassword(company.getUserPassword());
        dto.setCompanyName(company.getCompanyName());
        dto.setContactName(company.getContactName());
        dto.setContactLastName(company.getContactLastName());
        dto.setContactPhone(company.getContactPhone());
        dto.setContactPosition(company.getContactPosition());
        dto.setCompanySector(company.getCompanySector().toString());
        return dto;
    }

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
                dto.getUserEmail(),
                dto.getUserPassword(),
                dto.getUserId()
        );
    }
}