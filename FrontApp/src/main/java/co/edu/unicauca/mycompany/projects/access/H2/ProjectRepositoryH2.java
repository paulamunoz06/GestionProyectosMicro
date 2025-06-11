package co.edu.unicauca.mycompany.projects.access.H2;

import co.edu.unicauca.mycompany.projects.access.IProjectRepository;
import co.edu.unicauca.mycompany.projects.domain.entities.Project;
import co.edu.unicauca.mycompany.projects.infra.Messages;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Implementación del repositorio de proyectos que interactúa con un servicio REST
 * mediante peticiones HTTP para realizar operaciones de persistencia y consulta
 * sobre entidades de tipo Project.
 *
 */
public class ProjectRepositoryH2 extends Token implements IProjectRepository{
    private Logger logger = Logger.getLogger(ProjectRepositoryH2.class.getName());
    /**
     * Guarda un nuevo proyecto enviando una solicitud HTTP POST al servicio REST.
     *
     * @param newProject el objeto {@code Project} que se desea guardar.
     * @return {@code true} si la operación fue exitosa (código HTTP 200 o 201),
     *         {@code false} en caso contrario o si ocurre una excepción.
     */
    /**
     * Guarda un nuevo proyecto enviando una solicitud HTTP POST al servicio REST.
     * Se ha modificado para manejar correctamente el ID de la compañía.
     *
     * @param newProject el objeto {@code Project} que se desea guardar.
     * @return {@code true} si la operación fue exitosa (código HTTP 200 o 201),
     *         {@code false} en caso contrario o si ocurre una excepción.
     */
    @Override
    public boolean save(Project newProject) { 
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new com.fasterxml.jackson.databind.util.StdDateFormat());

        try {
            String companyId = newProject.getIdcompany();
            logger.info("Initial companyId: " + companyId);

            if (companyId == null || companyId.trim().isEmpty()) {
                logger.severe("Company ID is null or empty");
                return false;
            }

            if (companyId.contains("@")) {
                String companyEmail = companyId;
                logger.log(Level.INFO, "Detected email in companyId field: {0}", companyEmail);

                try {
                    // URL a través del API Gateway
                    String companyUrl = IUrl.ApiGatewayUrl + "/company/email/" + companyEmail;
                    logger.log(Level.INFO, "Making request to (via Gateway): {0}", companyUrl);

                    HttpGet getCompanyRequest = new HttpGet(companyUrl);
                    getCompanyRequest.setHeader("Accept", "application/json");
                    // Añadir el token de autorización
                    getCompanyRequest.setHeader("Authorization", "Bearer " + token);
                    logger.info("Authorization header added for company request.");


                    HttpResponse companyResponse = httpClient.execute(getCompanyRequest);
                    int companyStatusCode = companyResponse.getStatusLine().getStatusCode();
                    logger.log(Level.INFO, "Company API response status code: {0}", companyStatusCode);

                    if (companyStatusCode == 200) {
                        String companyResponseBody = EntityUtils.toString(companyResponse.getEntity());
                        logger.log(Level.INFO, "Company API response body: {0}", companyResponseBody);

                        try {
                            JsonNode companyNode = mapper.readTree(companyResponseBody);
                            logger.info("JSON parsed successfully. Root node type: " + companyNode.getNodeType());

                            Iterator<String> fieldNames = companyNode.fieldNames();
                            logger.info("Fields in JSON response:");
                            while (fieldNames.hasNext()) {
                                String fieldName = fieldNames.next();
                                logger.info("Field: " + fieldName + ", Value: " + companyNode.get(fieldName));
                            }

                            String resolvedCompanyId = null;
                            if (companyNode.has("id") && !companyNode.get("id").isNull()) {
                                resolvedCompanyId = companyNode.get("id").asText();
                                logger.log(Level.INFO, "Found ID in root: {0}", resolvedCompanyId);
                            } else if (companyNode.has("userId") && !companyNode.get("userId").isNull()) {
                                resolvedCompanyId = companyNode.get("userId").asText();
                                logger.log(Level.INFO, "Found ID in userId field: {0}", resolvedCompanyId);
                            } else {
                                logger.info("ID not found in root level, searching nested nodes");
                                resolvedCompanyId = findIdInJsonNode(companyNode);

                                if (resolvedCompanyId != null) {
                                    logger.log(Level.INFO, "Found ID in nested structure: {0}", resolvedCompanyId);
                                } else {
                                    logger.severe("No se pudo encontrar el ID en ninguna parte de la respuesta JSON de compañía.");
                                    return false;
                                }
                            }
                            companyId = resolvedCompanyId; // Actualizar companyId con el ID real
                            newProject.setIdcompany(companyId);
                            logger.info("Updated project with company ID: " + companyId);

                        } catch (Exception e) {
                            logger.severe("Error parsing JSON response from company service: " + e.getMessage());
                            e.printStackTrace();
                            return false;
                        }
                    } else {
                        logger.severe("Error al obtener la compañía por email (via Gateway). Código: " + companyStatusCode + ". Body: " + EntityUtils.toString(companyResponse.getEntity()));
                        return false;
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error procesando la respuesta del servicio de compa\u00f1\u00eda (via Gateway): {0}", e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }

            logger.log(Level.INFO, "Final companyId for API call: {0}", companyId);

            // URL para registrar proyecto a través del API Gateway
            String apiUrl = IUrl.ApiGatewayUrl + "/company/project/register?companyId=" + companyId;
            logger.log(Level.INFO, "Project register URL (via Gateway): {0}", apiUrl);

            HttpPost request = new HttpPost(apiUrl);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");
            // Añadir el token de autorización
            request.setHeader("Authorization", "Bearer " + token);
            logger.info("Authorization header added for project registration request.");

            Map<String, Object> projectDto = new HashMap<>();
            projectDto.put("proId", newProject.getProId());
            projectDto.put("proTitle", newProject.getProTitle());
            projectDto.put("proDescription", newProject.getProDescription());
            projectDto.put("proAbstract", newProject.getProAbstract());
            projectDto.put("proGoals", newProject.getProGoals());

            if (newProject.getProDate() != null) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                projectDto.put("proDate", sdf.format(newProject.getProDate()));
            } else {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                projectDto.put("proDate", sdf.format(new Date())); // Fecha actual si es nula
            }

            projectDto.put("proDeadLine", newProject.getProDeadLine());
            projectDto.put("proBudget", newProject.getProBudget());
            projectDto.put("proState", "RECIBIDO");
            projectDto.put("companyId", companyId);

            String projectJson = mapper.writeValueAsString(projectDto);
            logger.info("Sending project data: " + projectJson);

            StringEntity entity = new StringEntity(projectJson, "UTF-8");
            request.setEntity(entity);

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());

            logger.info("Project API response code (via Gateway): " + statusCode);
            logger.info("Project API response (via Gateway): " + responseBody);

            return statusCode == 201 || statusCode == 200;

        } catch (IOException ex) {
            logger.severe("Error saving project (via Gateway): " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Método auxiliar para buscar recursivamente un campo de ID en un nodo JSON
     * @param node El nodo JSON a explorar
     * @return El valor del ID si se encuentra, o null si no se encuentra
     */
    private String findIdInJsonNode(JsonNode node) {

        // Verificar campos de ID comunes en este nodo
        String[] possibleIdFields = {"id", "userId", "user_id", "companyId", "company_id"};

        for (String field : possibleIdFields) {
            if (node.has(field) && !node.get(field).isNull() && !node.get(field).asText().isEmpty()) {
                return node.get(field).asText();
            }
        }

        // Si este es un objeto, buscar recursivamente en todos sus campos
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String fieldValue = findIdInJsonNode(entry.getValue());
                if (fieldValue != null) {
                    return fieldValue;
                }
            }
        }

        // Si este es un array, buscar recursivamente en todos sus elementos
        if (node.isArray()) {
            for (JsonNode element : node) {
                String fieldValue = findIdInJsonNode(element);
                if (fieldValue != null) {
                    return fieldValue;
                }
            }
        }

        // No se encontró ningún ID
        return null;
    }
    /**
     * Verifica si un proyecto con el ID especificado ya existe en el servicio REST.
     * Ruta Gateway: /company/project/exists/{projectId} (requiere "coordinator" o "company")
     * 
     * @param projectId el ID del proyecto a verificar.
     * @return {@code true} si el proyecto existe, {@code false} si no existe
     *         o si ocurre un error al realizar la solicitud.
     */

    @Override
    public boolean existProjectId(String projectId) {
        if (projectId == null || projectId.isEmpty()) {
            return false;
        }
        HttpClient httpClient = HttpClients.createDefault();
        // La ruta correcta según la config del gateway es "/company/project/exists/"
        String apiUrl = IUrl.ApiGatewayUrl + "/company/project/exists/" + projectId;
        logger.info("Checking if project exists (via Gateway): " + apiUrl);

        try {
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");
            if (this.token != null && !this.token.isEmpty()) {
                request.setHeader("Authorization", "Bearer " + this.token);
            } else {
                 logger.warning("Token is null or empty. Endpoint /company/project/exists/** requires 'coordinator' or 'company' role.");
            }

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());

            if (statusCode == 200) {
                ObjectMapper mapper = new ObjectMapper();
                // Asumiendo que la respuesta es un JSON como {"exists": true/false}
                JsonNode responseNode = mapper.readTree(responseBody);
                if (responseNode.has("exists")) {
                    return responseNode.get("exists").asBoolean();
                }
                logger.warning("Response for project existence check does not contain 'exists' field: " + responseBody);
                return false; // O manejar de otra forma si la respuesta no es la esperada
            }
            logger.severe("Error checking project existence. Status: " + statusCode + ", Body: " + responseBody);
            return false;
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IOException while checking if project exists: " + ex.getMessage(), ex);
            return false;
        }
    }


    /**
     * Obtiene una lista de todos los proyectos registrados consultando un servicio REST.
     * Ruta Gateway: /coordinator/projects (requiere rol "coordinator")
     *
     * @return una lista de objetos {@code Project} que representa todos los proyectos.
     *         Si ocurre un error al obtener la lista, se retorna una lista vacía.
     */
    @Override
    public List<Project> listAll() {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        String apiUrl = IUrl.ApiGatewayUrl + "/coordinator/projects";
        logger.info("Listing all projects (via Gateway): " + apiUrl);

        try {
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");
            if (this.token != null && !this.token.isEmpty()) {
                request.setHeader("Authorization", "Bearer " + this.token);
            } else {
                logger.warning("Token is null or empty. Endpoint /coordinator/projects requires 'coordinator' role.");
            }

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String jsonResponse = EntityUtils.toString(response.getEntity());

            if (statusCode == 200) {
                return mapper.readValue(jsonResponse, new TypeReference<List<Project>>() {});
            } else {
                logger.log(Level.SEVERE, "Error getting project list. Status: " + statusCode + ", Body: " + jsonResponse);
                return Collections.emptyList();
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IOException while listing all projects: " + ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    /**
     * Obtiene la lista de proyectos disponibles para un estudiante específico.
     * Ruta Gateway : /student/{studentId}/projectsAvailable (requiere token)
     *
     * @param studentId el ID del estudiante para el cual se obtienen los proyectos disponibles.
     * @return una lista de objetos {@code Project} que representan los proyectos disponibles para el estudiante.
     *         Si ocurre un error al obtener la lista, se retorna {@code null}.
     */
   @Override
    public List<Project> listProjectsAvailable(String studentId) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        // Suponiendo que la ruta en el Gateway es /student/...
        String apiUrl = IUrl.ApiGatewayUrl + "/student/" + studentId + "/projectsAvailable";
        logger.info("Listing available projects for student (via Gateway): " + apiUrl);

        try {
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");
            if (this.token != null && !this.token.isEmpty()) {
                request.setHeader("Authorization", "Bearer " + this.token);
            } else {
                logger.warning("Token is null or empty for listProjectsAvailable. Assuming endpoint requires authentication.");
            }

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String jsonResponse = EntityUtils.toString(response.getEntity());

            if (statusCode == 200) {
                return mapper.readValue(jsonResponse, new TypeReference<List<Project>>() {});
            } else {
                logger.log(Level.SEVERE, "Error listing available projects. Status: " + statusCode + ", Body: " + jsonResponse);
                return null; // O Collections.emptyList() según prefieras manejar el error
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IOException while listing available projects: " + ex.getMessage(), ex);
            return null;
        }
    }


    /**
     * Obtiene un proyecto específico mediante su ID.
     * Ruta Gateway: /coordinator/project/{projectId} (requiere rol "coordinator")
     *
     * @param projectId el ID del proyecto que se desea obtener.
     * @return el objeto {@code Project} correspondiente al proyecto con el ID proporcionado.
     *         Si ocurre un error al obtener el proyecto, se retorna {@code null}.
     */
    @Override
    public Project getProject(String projectId) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        String apiUrl = IUrl.ApiGatewayUrl + "/coordinator/project/" + projectId;
        logger.info("Getting project by ID (via Gateway): " + apiUrl);

        try {
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");
            if (this.token != null && !this.token.isEmpty()) {
                request.setHeader("Authorization", "Bearer " + this.token);
            } else {
                logger.warning("Token is null or empty. Endpoint /coordinator/project/** requires 'coordinator' role.");
            }

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String jsonResponse = EntityUtils.toString(response.getEntity());

            if (statusCode == 200) {
                return mapper.readValue(jsonResponse, Project.class);
            } else {
                logger.log(Level.SEVERE, "Error getting project. Status: " + statusCode + ", Body: " + jsonResponse);
                return null;
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IOException while getting project: " + ex.getMessage(), ex);
            return null;
        }
    }


    /**
     * Permite a un estudiante aplicar a un proyecto específico.
     * Ruta Gateway : /student/{studentId}/project/{projectId} (POST, requiere token)
     * 
     * @param studentId el ID del estudiante que aplica al proyecto.
     * @param projectId el ID del proyecto al cual el estudiante desea aplicar.
     * @return {@code true} si la solicitud fue exitosa (código HTTP 200),
     *         {@code false} en caso contrario o si ocurre un error.
     */
    @Override
    public boolean apply(String studentId, String projectId) {
        HttpClient httpClient = HttpClients.createDefault();
        String apiUrl = IUrl.ApiGatewayUrl + "/student/" + studentId + "/project/" + projectId;
        logger.info("Applying to project (via Gateway): " + apiUrl);

        try {
            HttpPost request = new HttpPost(apiUrl);
            // No se especifica Content-Type, pero para POST podría ser necesario
            // request.setHeader("Content-Type", "application/json"); // Si envía cuerpo
            if (this.token != null && !this.token.isEmpty()) {
                request.setHeader("Authorization", "Bearer " + this.token);
            } else {
                 logger.warning("Token is null or empty for apply. Assuming endpoint requires authentication.");
            }

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            EntityUtils.consumeQuietly(response.getEntity()); // Consumir entidad para liberar conexión

            if (statusCode == 200) {
                return true;
            } else {
                logger.log(Level.SEVERE, "Error applying to project. Status: " + statusCode);
                return false; // Devuelve false en lugar de true en caso de error
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IOException while applying to project: " + ex.getMessage(), ex);
            return false; // Devuelve false en caso de excepción
        }
    }

    /**
     * Obtiene la cantidad de proyectos en los cuales un estudiante está involucrado.
     * Ruta Gateway: /student/{studentId}/projects (requiere token)
     *
     * @param studentId el ID del estudiante para el cual se obtiene el número de proyectos.
     * @return una lista de enteros donde cada número representa la cantidad de proyectos
     *         del estudiante en diferentes estados.
     *         Si ocurre un error, se retorna {@code null}.
     */
    @Override
    public List<Integer> countProjectsStudent(String studentId) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        // Suponiendo que la ruta en el Gateway es /student/...
        String apiUrl = IUrl.ApiGatewayUrl + "/student/" + studentId + "/projects";
        logger.info("Counting projects for student (via Gateway): " + apiUrl);

        try {
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");
            if (this.token != null && !this.token.isEmpty()) {
                request.setHeader("Authorization", "Bearer " + this.token);
            } else {
                 logger.warning("Token is null or empty for countProjectsStudent. Assuming endpoint requires authentication.");
            }

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String jsonResponse = EntityUtils.toString(response.getEntity());

            if (statusCode == 200) {
                return mapper.readValue(jsonResponse, new TypeReference<List<Integer>>() {});
            } else {
                logger.log(Level.SEVERE, "Error counting student projects. Status: " + statusCode + ", Body: " + jsonResponse);
                return null;
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IOException while counting student projects: " + ex.getMessage(), ex);
            return null;
        }
    }

    /**
     * Obtiene la cantidad de proyectos que están en un estado específico.
     * Ruta Gateway: /coordinator/projects/count-by-status/{status} (requiere rol "coordinator")
     *
     * @param status el estado de los proyectos para contar.
     * @return el número de proyectos que están en el estado especificado.
     *         Si ocurre un error al obtener el número de proyectos, se retorna 0.
     */
    @Override
    public int countByStatus(String status) {
        //try {
        //    Thread.sleep(1000*3);
        //} catch (InterruptedException ex) {
        //    Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE, null, ex);
        //}
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        String apiUrl = IUrl.ApiGatewayUrl + "/coordinator/projects/count-by-status/" + status;
        logger.info("Counting projects by status (via Gateway): " + apiUrl);

        try {
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");
            if (this.token != null && !this.token.isEmpty()) {
                request.setHeader("Authorization", "Bearer " + this.token);
            } else {
                logger.warning("Token is null or empty. Endpoint /coordinator/projects/count-by-status/** requires 'coordinator' role.");
            }

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String jsonResponse = EntityUtils.toString(response.getEntity());

            if (statusCode == 200) {
                return mapper.readValue(jsonResponse, Integer.class);
            } else {
                logger.log(Level.SEVERE, "Error counting projects by status. Status: " + statusCode + ", Body: " + jsonResponse);
                return 0;
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IOException while counting projects by status: " + ex.getMessage(), ex);
            return 0;
        }
    }

    /**
     * Obtiene el número total de proyectos registrados en el sistema.
     * Ruta Gateway: /coordinator/projects/count-total (requiere rol "coordinator")
     *
     * @return el número total de proyectos. Si ocurre un error al obtener el total, se retorna 0.
     */
    @Override
    public int countTotalProjects() {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        String apiUrl = IUrl.ApiGatewayUrl + "/coordinator/projects/count-total";
        logger.info("Counting total projects (via Gateway): " + apiUrl);

        try {
            HttpGet request = new HttpGet(apiUrl);
            request.setHeader("Accept", "application/json");
            if (this.token != null && !this.token.isEmpty()) {
                request.setHeader("Authorization", "Bearer " + this.token);
            } else {
                 logger.warning("Token is null or empty. Endpoint /coordinator/projects/count-total requires 'coordinator' role.");
            }

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String jsonResponse = EntityUtils.toString(response.getEntity());

            if (statusCode == 200) {
                return mapper.readValue(jsonResponse, Integer.class);
            } else {
                logger.log(Level.SEVERE, "Error counting total projects. Status: " + statusCode + ", Body: " + jsonResponse);
                return 0;
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IOException while counting total projects: " + ex.getMessage(), ex);
            return 0;
        }
    }

    /**
     * Actualiza el estado de un proyecto específico.
     * Ruta Gateway: /coordinator/projects/update-status (PUT, requiere rol "coordinator")
     *
     * @param projectId el ID del proyecto cuyo estado se desea actualizar.
     * @param newStatus el nuevo estado que se asignará al proyecto.
     * @return {@code true} si el estado del proyecto fue actualizado exitosamente (código HTTP 200),
     *         {@code false} si hubo un error al intentar actualizar el estado o si el servidor retorna un código diferente a 200.
     */
     @Override
    public boolean updateProjectStatus(String projectId, String newStatus) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        String apiUrl = IUrl.ApiGatewayUrl + "/coordinator/projects/update-status";
        logger.info("Updating project status (via Gateway): " + apiUrl);

        try {
            ObjectNode requestBody = mapper.createObjectNode();
            requestBody.put("proId", projectId);
            requestBody.put("proState", newStatus);

            HttpPut request = new HttpPut(apiUrl);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json"); 
            request.setEntity(new StringEntity(requestBody.toString(), "UTF-8"));

            if (this.token != null && !this.token.isEmpty()) {
                request.setHeader("Authorization", "Bearer " + this.token);
            } else {
                logger.warning("Token is null or empty. Endpoint /coordinator/projects/update-status requires 'coordinator' role.");
            }

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity()); // Leer para log y liberar conexión

            if (statusCode == 200) {
                logger.info("Project status updated successfully. Response: " + responseBody);
                return true;
            } else {
                logger.severe("Error updating project status. Status: " + statusCode + ", Body: " + responseBody);
                Messages.mensajeVario("Error al actualizar estado: " + responseBody + " (Código: " + statusCode + ")"); // Mantengo JOptionPane vía Messages
                return false;
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "IOException while updating project status: " + ex.getMessage(), ex);
            JOptionPane.showMessageDialog(null, "Error de conexión con el servidor al actualizar estado.", "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }
}
