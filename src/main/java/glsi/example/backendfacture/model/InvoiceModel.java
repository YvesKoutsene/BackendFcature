package glsi.example.backendfacture.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "Invoice")
public class InvoiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private int invoiceId;

    private Date dateInvoice;
    private Double totalAmount;
    private Boolean statusPaid;

    @ManyToOne
    @JsonIgnore // Ignorer la sérialisation de la relation inverse
    @JoinColumn(name = "customer_id")
    private CostumerModel customer;

    @ManyToMany
    @JoinTable(
            name = "invoice_product",
            joinColumns = @JoinColumn(name = "invoice_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @JsonIgnore // Ignorer la sérialisation de la relation inverse
    private List<ProductModel> products;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<LigneCommandeModel> lignesDeCommande;

    public CostumerModel getCustomer() {
        return customer;
    }

    public void setCustomer(CostumerModel customer) {
        this.customer = customer;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }

    public List<LigneCommandeModel> getLignesDeCommande() {
        return lignesDeCommande;
    }

    public void setLignesDeCommande(List<LigneCommandeModel> lignesDeCommande) {
        this.lignesDeCommande = lignesDeCommande;
    }


}
