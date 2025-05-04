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
public class ProjectRepositoryH2 implements IProjectRepository{
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
        Logger logger = Logger.getLogger(ProjectRepositoryH2.class.getName());

        try {
            // Obtener el ID de la compañía - primero verificamos si ya tenemos el ID real
            String companyId = newProject.getIdcompany();
            logger.info("Initial companyId: " + companyId);

            if (companyId == null || companyId.trim().isEmpty()) {
                logger.severe("Company ID is null or empty");
                return false;
            }

            // Si parece ser un email (contiene @), debemos primero obtener el ID real
            if (companyId.contains("@")) {
                // Necesitamos obtener el ID real de la compañía usando el email
                String companyEmail = companyId; // Guardamos el email
                logger.info("Detected email in companyId field: " + companyEmail);

                try {
                    // Crear una petición para obtener la compañía por email
                    String companyUrl = "http://localhost:8080/company/email/" + companyEmail;
                    logger.info("Making request to: " + companyUrl);

                    HttpGet getCompanyRequest = new HttpGet(companyUrl);
                    getCompanyRequest.setHeader("Accept", "application/json");

                    HttpResponse companyResponse = httpClient.execute(getCompanyRequest);
                    int companyStatusCode = companyResponse.getStatusLine().getStatusCode();
                    logger.info("Company API response status code: " + companyStatusCode);

                    if (companyStatusCode == 200) {
                        String companyResponseBody = EntityUtils.toString(companyResponse.getEntity());
                        logger.info("Company API response body: " + companyResponseBody);

                        // Intentar analizar la respuesta JSON
                        try {
                            JsonNode companyNode = mapper.readTree(companyResponseBody);
                            logger.info("JSON parsed successfully. Root node type: " + companyNode.getNodeType());

                            // Depurar todos los campos en el nivel raíz
                            Iterator<String> fieldNames = companyNode.fieldNames();
                            logger.info("Fields in JSON response:");
                            while (fieldNames.hasNext()) {
                                String fieldName = fieldNames.next();
                                logger.info("Field: " + fieldName + ", Value: " + companyNode.get(fieldName));
                            }

                            // Primero verificar si el ID está en la raíz
                            if (companyNode.has("id") && !companyNode.get("id").isNull()) {
                                companyId = companyNode.get("id").asText();
                                logger.info("Found ID in root: " + companyId);
                            }
                            // Comprobar si está en 'userId' (basado en el mapeo de CompanyDto)
                            else if (companyNode.has("userId") && !companyNode.get("userId").isNull()) {
                                companyId = companyNode.get("userId").asText();
                                logger.info("Found ID in userId field: " + companyId);
                            }
                            // Buscar en cualquier nodo anidado que pueda contener el ID
                            else {
                                logger.info("ID not found in root level, searching nested nodes");
                                // Esta es una búsqueda más extensiva que puede encontrar el ID en cualquier nivel
                                companyId = findIdInJsonNode(companyNode);

                                if (companyId != null) {
                                    logger.info("Found ID in nested structure: " + companyId);
                                } else {
                                    logger.severe("No se pudo encontrar el ID en ninguna parte de la respuesta JSON");
                                    return false;
                                }
                            }

                            // Actualizar el ID en el objeto Project
                            newProject.setIdcompany(companyId);
                            logger.info("Updated project with company ID: " + companyId);

                        } catch (Exception e) {
                            logger.severe("Error parsing JSON response: " + e.getMessage());
                            e.printStackTrace();
                            return false;
                        }
                    } else {
                        logger.severe("Error al obtener la compañía por email. Código: " + companyStatusCode);
                        return false;
                    }
                } catch (Exception e) {
                    logger.severe("Error procesando la respuesta del servicio de compañía: " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }
            }

            // A este punto, companyId debería ser el ID real, ya sea porque era válido desde el principio
            // o porque lo hemos obtenido a partir del email
            logger.info("Final companyId for API call: " + companyId);

            String apiUrl = "http://localhost:8080/project/register?companyId=" + companyId;
            logger.info("Project register URL: " + apiUrl);

            HttpPost request = new HttpPost(apiUrl);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Accept", "application/json");

            Map<String, Object> projectDto = new HashMap<>();

            // Map project fields to DTO
            projectDto.put("proId", newProject.getProId());
            projectDto.put("proTitle", newProject.getProTitle());
            projectDto.put("proDescription", newProject.getProDescription());
            projectDto.put("proAbstract", newProject.getProAbstract());
            projectDto.put("proGoals", newProject.getProGoals());

            // Formatear la fecha correctamente
            if (newProject.getProDate() != null) {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                projectDto.put("proDate", sdf.format(newProject.getProDate()));
            } else {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                projectDto.put("proDate", sdf.format(new Date()));
            }

            projectDto.put("proDeadLine", newProject.getProDeadLine());
            projectDto.put("proBudget", newProject.getProBudget());
            projectDto.put("proState", "RECIBIDO");
            projectDto.put("companyId", companyId); // Usar el ID correcto

            String projectJson = mapper.writeValueAsString(projectDto);
            logger.info("Sending project data: " + projectJson);

            StringEntity entity = new StringEntity(projectJson, "UTF-8");
            request.setEntity(entity);

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());

            logger.info("Project API response code: " + statusCode);
            logger.info("Project API response: " + responseBody);

            return statusCode == 201 || statusCode == 200;

        } catch (IOException ex) {
            logger.severe("Error saving project: " + ex.getMessage());
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
        Logger logger = Logger.getLogger(ProjectRepositoryH2.class.getName());

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
        try {
            String apiUrl = "http://localhost:8080/project/exists/" + projectId;

            HttpGet request = new HttpGet(apiUrl);

            HttpResponse response = httpClient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity());
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> responseMap = mapper.readValue(responseBody, Map.class);

                return (Boolean) responseMap.get("exists");
            }

            return false;
        } catch (IOException ex) {
            Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE, "Error checking if project exists", ex);
            return false;
        }
    }

    /**
     * Obtiene una lista de todos los proyectos registrados consultando un servicio REST.
     *
     * @return una lista de objetos {@code Project} que representa todos los proyectos.
     *         Si ocurre un error al obtener la lista, se retorna una lista vacía.
     */
    @Override
    public List<Project> listAll() {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        List<Project> projectList = List.of(); // Lista vacía por defecto

        try {
            // Definir la URL de la API REST
            String apiUrl = "http://localhost:8081/coordinator/projects";

            // Crear la solicitud GET
            HttpGet request = new HttpGet(apiUrl);

            // Ejecutar la solicitud
            HttpResponse response = httpClient.execute(request);

            // Verificar el código de estado
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Leer el contenido JSON de la respuesta
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // Mapear el JSON a una lista de Project
                Project[] projectsArray = mapper.readValue(jsonResponse, Project[].class);
                projectList = List.of(projectsArray); // Convertir a lista inmutable
            } else {
                Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE,
                        "Error al obtener la lista de proyectos. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE,
                    "Error de IO al obtener la lista de proyectos", ex);
        }

        return projectList;
    }

    /**
     * Obtiene la lista de proyectos disponibles para un estudiante específico.
     *
     * @param studentId el ID del estudiante para el cual se obtienen los proyectos disponibles.
     * @return una lista de objetos {@code Project} que representan los proyectos disponibles para el estudiante.
     *         Si ocurre un error al obtener la lista, se retorna {@code null}.
     */
    @Override
    public List<Project> listProjectsAvailable(String studentId) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        List<Project> listReturn = null;
        try {
            // Definir la URL de la API REST
            String apiUrl = "http://localhost:8083/student/" + studentId + "/projectsAvailable";
            // Crear una solicitud GET
            HttpGet request = new HttpGet(apiUrl);

            // Ejecutar la solicitud y obtener la respuesta
            HttpResponse response = httpClient.execute(request);

            // Verificar el código de estado de la respuesta
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // La solicitud fue exitosa, procesar la respuesta
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // Mapear la respuesta JSON a objetos Product
                listReturn = mapper.readValue(jsonResponse, new TypeReference<List<Project>>() {
                });
            } else {
                // La solicitud falló, mostrar el código de estado
                Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE, null, "Error al obtener la lista de proyectos disponibles. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listReturn;
    }

    /**
     * Obtiene un proyecto específico mediante su ID.
     *
     * @param projectId el ID del proyecto que se desea obtener.
     * @return el objeto {@code Project} correspondiente al proyecto con el ID proporcionado.
     *         Si ocurre un error al obtener el proyecto, se retorna {@code null}.
     */
    @Override
    public Project getProject(String projectId) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        Project project = null;
        try {
            // Definir la URL de la API REST
            String apiUrl = "http://localhost:8081/coordinator/project/" + projectId;
            // Crear una solicitud GET

            HttpGet request = new HttpGet(apiUrl);

            // Ejecutar la solicitud y obtener la respuesta
            HttpResponse response = httpClient.execute(request);

            // Verificar el código de estado de la respuesta
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // La solicitud fue exitosa, procesar la respuesta
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // Mapear la respuesta JSON a objetos Product
                project = mapper.readValue(jsonResponse, Project.class);
            } else {
                // La solicitud falló, mostrar el código de estado
                Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE, null, "Error al obtener un proyecto. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return project;
    }

    /**
     * Permite a un estudiante aplicar a un proyecto específico.
     *
     * @param studentId el ID del estudiante que aplica al proyecto.
     * @param projectId el ID del proyecto al cual el estudiante desea aplicar.
     * @return {@code true} si la solicitud fue exitosa (código HTTP 200),
     *         {@code false} en caso contrario o si ocurre un error.
     */
    @Override
    public boolean apply(String studentId, String projectId) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Definir la URL de la API REST

            String apiUrl = "http://localhost:8083/student/" + studentId + "/project/" + projectId;
            // Crear una solicitud GET
            HttpPost request = new HttpPost(apiUrl);

            // Ejecutar la solicitud y obtener la respuesta
            HttpResponse response = httpClient.execute(request);

            // Verificar el código de estado de la respuesta
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return true;
            } else {
                // La solicitud falló, mostrar el código de estado
                Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE, null, "Error al aplicar a un proyecto. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    /**
     * Obtiene la cantidad de proyectos en los cuales un estudiante está involucrado.
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
        List<Integer> listReturn = null;
        try {
            // Definir la URL de la API REST

            String apiUrl = "http://localhost:8083/student/" + studentId + "/projects";
            // Crear una solicitud GET

            HttpGet request = new HttpGet(apiUrl);

            // Ejecutar la solicitud y obtener la respuesta
            HttpResponse response = httpClient.execute(request);

            // Verificar el código de estado de la respuesta
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // La solicitud fue exitosa, procesar la respuesta
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // Mapear la respuesta JSON a objetos Product
                listReturn = mapper.readValue(jsonResponse, new TypeReference<List<Integer>>() {
                });

            } else {
                // La solicitud falló, mostrar el código de estado
                Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE, null, "Error al obtener la cantidad de proyectos. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listReturn;
    }

    /**
     * Obtiene la cantidad de proyectos que están en un estado específico.
     *
     * @param status el estado de los proyectos para contar.
     * @return el número de proyectos que están en el estado especificado.
     *         Si ocurre un error al obtener el número de proyectos, se retorna 0.
     */
    @Override
    public int countByStatus(String status) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        int projectCount = 0; // Variable para almacenar el número de proyectos

        try {
            // Definir la URL de la API REST
            String apiUrl = "http://localhost:8081/coordinator/projects/count-by-status/" + status;

            // Crear una solicitud GET
            HttpGet request = new HttpGet(apiUrl);

            // Ejecutar la solicitud y obtener la respuesta
            HttpResponse response = httpClient.execute(request);

            // Verificar el código de estado de la respuesta
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // La solicitud fue exitosa, procesar la respuesta
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // Mapear la respuesta JSON a un valor int
                projectCount = mapper.readValue(jsonResponse, Integer.class);
            } else {
                // La solicitud falló, mostrar el código de estado
                Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE, "Error al obtener el número de proyectos. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE, "Error de IO al obtener el número de proyectos", ex);
        }

        return projectCount; // Retornar el número de proyectos o 0 en caso de error
    }

    /**
     * Obtiene el número total de proyectos registrados en el sistema.
     *
     * @return el número total de proyectos. Si ocurre un error al obtener el total, se retorna 0.
     */
    @Override
    public int countTotalProjects() {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        int totalProjects = 0; // Variable para almacenar el número total de proyectos

        try {
            // Definir la URL de la API REST
            String apiUrl = "http://localhost:8081/coordinator/projects/count-total";

            // Crear una solicitud GET
            HttpGet request = new HttpGet(apiUrl);

            // Ejecutar la solicitud y obtener la respuesta
            HttpResponse response = httpClient.execute(request);

            // Verificar el código de estado de la respuesta
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // La solicitud fue exitosa, procesar la respuesta
                String jsonResponse = EntityUtils.toString(response.getEntity());

                // Mapear la respuesta JSON a un valor int
                totalProjects = mapper.readValue(jsonResponse, Integer.class);
            } else {
                // La solicitud falló, mostrar el código de estado
                Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE, "Error al obtener el número total de proyectos. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE, "Error de IO al obtener el número total de proyectos", ex);
        }

        return totalProjects; // Retornar el número total de proyectos o 0 en caso de error
    }

    /**
     * Actualiza el estado de un proyecto específico.
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

        try {
            // Definir la URL del endpoint
            String apiUrl = "http://localhost:8081/coordinator/projects/update-status";

            // Crear el objeto JSON con proId y proState
            ObjectNode requestBody = mapper.createObjectNode();
            requestBody.put("proId", projectId);
            requestBody.put("proState", newStatus);

            // Crear la solicitud PUT
            HttpPut request = new HttpPut(apiUrl);
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(requestBody.toString(), "UTF-8"));

            // Ejecutar la solicitud
            HttpResponse response = httpClient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                return true;
            } else {
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                // Aquí puedes mostrarlo en un JOptionPane o consola
                Messages.mensajeVario("Error: " + responseBody);
                return false;
            }

        } catch (IOException ex) {
            Logger.getLogger(ProjectRepositoryH2.class.getName()).log(Level.SEVERE,
                    "Error de IO al actualizar el estado del proyecto", ex);
            JOptionPane.showMessageDialog(null, "Error de conexión con el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }
}
