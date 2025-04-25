package co.edu.unicauca.mycompany.projects.access;

import co.edu.unicauca.mycompany.projects.domain.entities.Company;
import co.edu.unicauca.mycompany.projects.domain.entities.enumSector;

public class CompanyRepository implements  ICompanyRepository {
    @Override
    public boolean save(Company newCompany) {
        return false;
    }

    @Override
    public Company companyInfo(String nit) {
        return new Company("exampleCompany","contact","","","", enumSector.HEALTH,"","","");
    }

    @Override
    public String getSectorIdByName(String sectorName) {
        return "";
    }
}
