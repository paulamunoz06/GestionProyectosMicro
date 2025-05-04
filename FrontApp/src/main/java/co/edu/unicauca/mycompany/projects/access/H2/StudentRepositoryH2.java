
package co.edu.unicauca.mycompany.projects.access.H2;

import co.edu.unicauca.mycompany.projects.access.IStudentRepository;
import co.edu.unicauca.mycompany.projects.domain.entities.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class StudentRepositoryH2 implements IStudentRepository{

    @Override
    public Student getStudent(String id) {
        HttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        Student student = null;
        try {
            // Definir la URL de la API REST
            String apiUrl = "http://localhost:8083/student/" + id ;
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
                student = mapper.readValue(jsonResponse, Student.class);
            } else {
                // La solicitud falló, mostrar el código de estado
                Logger.getLogger(StudentRepositoryH2.class.getName()).log(Level.SEVERE, null, "Error al obtener un proyecto. Código de estado: " + statusCode);
            }
        } catch (IOException ex) {
            Logger.getLogger(StudentRepositoryH2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return student;
    }
    
}
