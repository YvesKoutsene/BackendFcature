package glsi.example.backendfacture.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "LigneCommande")
public class LigneCommandeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ligne_commande_id")
    private int ligneCommandeId;
    private double quantity;

    @ManyToOne
    @JsonIgnore // Ignorer la sérialisation de la relation inverse
    @JoinColumn(name = "invoice_id")
    private InvoiceModel invoice;

    @ManyToOne
    @JsonIgnore // Ignorer la sérialisation de la relation inverse
    @JoinColumn(name = "product_id")
    private ProductModel product;

    @ManyToOne
    @JsonIgnore // Ignorer la sérialisation de la relation inverse
    @JoinColumn(name = "customer_id")
    private CostumerModel customer;

}
