package glsi.example.backendfacture.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Customer")
public class CostumerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    private String fullName;
    private String email;
    private String number;
    private String password;
    private String rePassword;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<InvoiceModel> invoices;

    public List<InvoiceModel> getInvoices() {
        return invoices;
    }

    @JsonIgnore // Ignorer la sérialisation de la relation inverse
    public void setInvoices(List<InvoiceModel> invoices) {
        this.invoices = invoices;
    }
}

