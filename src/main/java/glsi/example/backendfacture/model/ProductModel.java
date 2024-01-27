package glsi.example.backendfacture.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "Product")
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;

    private String nameProduct;
    private double priceUnit;

    @ManyToMany(mappedBy = "products", cascade = CascadeType.ALL)
    private List<InvoiceModel> invoices;

    public ProductModel() {}


}
