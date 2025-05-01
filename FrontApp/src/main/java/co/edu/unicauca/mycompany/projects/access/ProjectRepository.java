package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.domain.entities.Project;
import co.edu.unicauca.mycompany.projects.domain.entities.Company;
import co.edu.unicauca.mycompany.projects.domain.entities.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ProjectRepository implements IProjectRepository {
    @Override
    public boolean save(Project newProject) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new com.fasterxml.jackson.databind.util.StdDateFormat());

        try {
            Long companyIdLong;
            try {
                companyIdLong = Long.parseLong(newProject.getIdcompany());
            } catch (NumberFormatException e) {
                Logger.getLogger(ProjectRepository.class.getName()).log(Level.SEVERE, "Invalid company ID format", e);
                return false;
            }

            String apiUrl = "http://localhost:8080/project/register?companyId=" + companyIdLong;

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
            projectDto.put("companyId", companyIdLong.toString());

            String projectJson = mapper.writeValueAsString(projectDto);

            System.out.println("Sending project data: " + projectJson);
            System.out.println("To URL: " + apiUrl);

            StringEntity entity = new StringEntity(projectJson, "UTF-8");
            request.setEntity(entity);

            HttpResponse response = httpClient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();

            String responseBody = EntityUtils.toString(response.getEntity());
            System.out.println("Server response code: " + statusCode);
            System.out.println("Server response: " + responseBody);

            return statusCode == 201 || statusCode == 200;

        } catch (IOException ex) {
            Logger.getLogger(ProjectRepository.class.getName()).log(Level.SEVERE, "Error saving project", ex);
            return false;
        }
    }

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
            Logger.getLogger(ProjectRepository.class.getName()).log(Level.SEVERE, "Error checking if project exists", ex);
            return false;
        }
    }

    @Override
    public List<Project> listAll() {
        return List.of();
    }

    @Override
    public List<Project> listProjectsAvailable(String studentId) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        List<Project> listReturn = null;
        try {
            // Definir la URL de la API REST
            String apiUrl = "http://localhost:8083/student/" + studentId +"/projectsAvailable" ;
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
                listReturn = mapper.readValue(jsonResponse, new TypeReference<List<Project>>() {});
            } else {
                // La solicitud falló, mostrar el código de estado
                Logger.getLogger(ProjectRepository.class.getName()).log(Level.SEVERE, null, "Error al obtener la lista de proyectos disponibles. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listReturn;
    }

    @Override
    public Project getProject(String projectId) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        Project project = null;
        try {
            // Definir la URL de la API REST
            String apiUrl = "http://localhost:8083/student/project/" + projectId ;
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
                Logger.getLogger(ProjectRepository.class.getName()).log(Level.SEVERE, null, "Error al obtener un proyecto. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return project;
    }

    @Override
    public boolean apply(String studentId, String projectId) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Definir la URL de la API REST
            String apiUrl = "http://localhost:8083/student/"+studentId+"/project/" + projectId;
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
                Logger.getLogger(ProjectRepository.class.getName()).log(Level.SEVERE, null, "Error al aplicar a un proyecto. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    @Override
    public List<Integer> countProjectsStudent(String studentId) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        List<Integer> listReturn = null;
        try {
            // Definir la URL de la API REST
            String apiUrl = "http://localhost:8083/student/" + studentId +"/projects" ;
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
                listReturn = mapper.readValue(jsonResponse, new TypeReference<List<Integer>>() {});

            } else {
                // La solicitud falló, mostrar el código de estado
                Logger.getLogger(ProjectRepository.class.getName()).log(Level.SEVERE, null, "Error al obtener la cantidad de proyectos. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listReturn;
    }

    @Override
    public int countByStatus(String status) {
        return 0;
    }

    @Override
    public int countTotalProjects() {
        return 0;
    }

    @Override
    public boolean updateProjectStatus(String projectId, String newStatus) {
        return false;
    }
    
}
