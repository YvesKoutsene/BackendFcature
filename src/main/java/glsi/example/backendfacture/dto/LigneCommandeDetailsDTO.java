package glsi.example.backendfacture.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LigneCommandeDetailsDTO {

    private int ligneCommandeId;
    private double quantity;
    private String nomProduit;
    private double prixProduit;

    private Date dateInvoice;
    private Double totalAmount;
    private Boolean statusPaid;

    private String fullName;
    private String email;
    private String number;

    // Ajoutez d'autres champs au besoin
}
