package co.edu.unicauca.microserviceCompany.controller;

import co.edu.unicauca.microserviceCompany.entity.Company;
import co.edu.unicauca.microserviceCompany.infra.dto.CompanyDto;
import co.edu.unicauca.microserviceCompany.service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private ICompanyService companyService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCompany(@RequestBody CompanyDto companyDto) {
        try {
            Company registeredCompany = companyService.registerCompany(companyDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(companyService.companyToDto(registeredCompany));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al registrar la empresa: " + e.getMessage()));
        }
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long companyId) {
        try {
            Optional<Company> company = companyService.findById(companyId);
            if (company.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(companyService.companyToDto(company.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getCompanyByEmail(@PathVariable String email) {
        try {
            Optional<Company> company = companyService.findByEmail(email);
            if (company.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(companyService.companyToDto(company.get()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/all")
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