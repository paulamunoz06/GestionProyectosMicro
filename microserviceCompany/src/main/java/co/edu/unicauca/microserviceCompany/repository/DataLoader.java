package co.edu.unicauca.microserviceCompany.repository;

import co.edu.unicauca.microserviceCompany.entity.EnumProjectState;
import co.edu.unicauca.microserviceCompany.entity.Project;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    IProjectRepository projectRepository;

    @Override
    @Transactional
    public void run(String... args){
        projectRepository.deleteAll();

    }
}
