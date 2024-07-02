package br.com.rosa.domain.companyData;

import br.com.rosa.domain.companyData.dto.DataCompany;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class CompanyData {

    @Id
    private Long id;
    private String urlImg;
    private String reason;
    private String fantasyName;
    private String cnpj;
    private String street;
    private String number;
    private String district;
    private String cep;
    private String city;
    private String uf;
    private String phone1;
    private String phone2;
    private String clauses;
    private String observation;

    public CompanyData(DataCompany data, String urlimg) {
        this.id = 1l;
        this.urlImg = urlimg;
        this.reason = data.reason();
        this.fantasyName = data.fantasyName();
        this.cnpj = data.cnpj();
        this.street = data.street();
        this.number = data.number();
        this.district = data.district();
        this.cep = data.cep();
        this.city = data.city();
        this.uf = data.uf();
        this.phone1 = data.phone1();
        this.phone2 = data.phone2();
    }
}
