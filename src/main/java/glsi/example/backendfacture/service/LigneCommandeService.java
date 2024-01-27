package glsi.example.backendfacture.service;

import glsi.example.backendfacture.dto.LigneCommandeDetailsDTO;
import glsi.example.backendfacture.model.CostumerModel;
import glsi.example.backendfacture.model.InvoiceModel;
import glsi.example.backendfacture.model.LigneCommandeModel;
import glsi.example.backendfacture.model.ProductModel;
import glsi.example.backendfacture.repository.CostumerRepository;
import glsi.example.backendfacture.repository.InvoiceRepository;
import glsi.example.backendfacture.repository.LigneCommandeRepository;
import glsi.example.backendfacture.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class LigneCommandeService {

    @Autowired
    private LigneCommandeRepository ligneCommandeRepository;

    @Autowired
    private CostumerRepository costumerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public LigneCommandeModel createLigneCommande(int costumerId, int productId, double quantity) {
        // Vérifier si le costumer existe
        CostumerModel costumer = costumerRepository.findById(costumerId)
                .orElseThrow(() -> new RuntimeException("Costumer not found with id: " + costumerId));

        // Vérifier si le produit existe
        ProductModel product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        // Créer une nouvelle ligne de commande
        LigneCommandeModel ligneCommande = new LigneCommandeModel();
        ligneCommande.setQuantity(quantity);
        ligneCommande.setCustomer(costumer);
        ligneCommande.setProduct(product);

        // Enregistrer la ligne de commande
        ligneCommande = ligneCommandeRepository.save(ligneCommande);

        // Mettre à jour la facture du costumer avec la nouvelle ligne de commande
        InvoiceModel invoice = costumer.getInvoices().stream()
                .filter(inv -> inv.getStatusPaid() == null || !inv.getStatusPaid())
                .findFirst()
                .orElseGet(() -> {
                    InvoiceModel newInvoice = new InvoiceModel();
                    newInvoice.setCustomer(costumer);
                    costumer.getInvoices().add(newInvoice);
                    return newInvoice;
                });

        // Actualiser la date de la facture à la date actuelle
        invoice.setDateInvoice(new Date());

        // Ajouter l'id de la facture à la ligne de commande
        ligneCommande.setInvoice(invoice);

        // Calcul du montant total de la facture
        double productTotalAmount = product.getPriceUnit() * quantity;
        double invoiceTotalAmount = (invoice.getTotalAmount() != null ? invoice.getTotalAmount() : 0) + productTotalAmount;
        invoice.setTotalAmount(invoiceTotalAmount);

        // Vérifier si la liste des produits est nulle, l'initialiser
        if (invoice.getProducts() == null) {
            invoice.setProducts(new ArrayList<>());
        }

        // Ajouter le produit à la liste des produits de la facture
        invoice.getProducts().add(product);

        // Enregistrer la facture mise à jour
        invoiceRepository.save(invoice);

        return ligneCommande;
    }

    public List<LigneCommandeModel> getLignesDeCommandePourClient(int customerId) {
        return ligneCommandeRepository.findByCustomerCustomerId(customerId);
    }

    //Afficher les informations de la facture d'un costumer
    public void afficherDetailsCommandesEtFacture(int customerId) {
        // Récupérer les informations du client une seule fois, car elles sont communes à toutes les lignes de commande
        CostumerModel customer = getCustomerById(customerId);
        System.out.println("Informations du client");
        System.out.println("Nom du client: " + customer.getFullName());
        System.out.println("Email du client: " + customer.getEmail());
        System.out.println("Numéro du client: " + customer.getNumber());
        System.out.println("------------------------------");

        // Récupérer toutes les lignes de commande pour le client
        List<LigneCommandeModel> lignesDeCommande = getLignesDeCommandePourClient(customerId);

        // Afficher les détails de chaque ligne de commande
        for (LigneCommandeModel ligneDeCommande : lignesDeCommande) {
            System.out.println("Ligne de commande ID: " + ligneDeCommande.getLigneCommandeId());
            System.out.println("Quantité: " + ligneDeCommande.getQuantity());

            // Afficher les détails du produit associé
            System.out.println("Détails du produit");
            System.out.println("Nom du produit: " + ligneDeCommande.getProduct().getNameProduct());
            System.out.println("Prix du produit: " + ligneDeCommande.getProduct().getPriceUnit());
            // Ajoutez d'autres détails du produit selon vos besoins

            System.out.println("------------------------------");
        }

        // Récupérer la facture associée à ces lignes de commande
        InvoiceModel invoice = getInvoiceForCustomer(customerId);

        // Afficher les détails de la facture
        if (invoice != null) {
            System.out.println("Informations de la facture:");
            System.out.println("Prix total de la facture: " + invoice.getTotalAmount());
            System.out.println("Date de la facture: " + invoice.getDateInvoice());
            System.out.println("Statut de la facture: " + invoice.getStatusPaid());
            System.out.println("------------------------------");
        } else {
            System.out.println("Aucune facture associée à ces lignes de commande.");
        }
    }

    public CostumerModel getCustomerById(int customerId) {
        return costumerRepository.findById(customerId).orElse(null);
    }

    public InvoiceModel getInvoiceForCustomer(int customerId) {
        return invoiceRepository.findByCustomerId(customerId).orElse(null);
    }

    //Afficher les lignes de commandes d'un costumer
    public List<LigneCommandeDetailsDTO> getLignesDeCommandeAvecDetailsPourClient(int customerId) {
        List<LigneCommandeModel> lignesDeCommande = ligneCommandeRepository.findByCustomerCustomerId(customerId);
        List<LigneCommandeDetailsDTO> lignesDeCommandeDetails = new ArrayList<>();

        for (LigneCommandeModel ligneDeCommande : lignesDeCommande) {
            LigneCommandeDetailsDTO detailsDTO = new LigneCommandeDetailsDTO();
            detailsDTO.setLigneCommandeId(ligneDeCommande.getLigneCommandeId());
            detailsDTO.setQuantity(ligneDeCommande.getQuantity());

            // Détails du produit associé
            detailsDTO.setNomProduit(ligneDeCommande.getProduct().getNameProduct());
            detailsDTO.setPrixProduit(ligneDeCommande.getProduct().getPriceUnit());
            // Ajoutez d'autres détails du produit selon vos besoins

            lignesDeCommandeDetails.add(detailsDTO);
        }

        return lignesDeCommandeDetails;
    }

    @Transactional
    public void deleteLigneCommandeById(int ligneCommandeId) {
        // Récupérer la ligne de commande avant de la supprimer
        Optional<LigneCommandeModel> optionalLigneCommande = ligneCommandeRepository.findById(ligneCommandeId);

        // Supprimer la ligne de commande de la base de données
        optionalLigneCommande.ifPresent(ligneCommande -> {
            // Récupérer la facture associée à la ligne de commande supprimée
            InvoiceModel invoice = ligneCommande.getInvoice();

            // Mettre à jour le montant total de la facture
            updateInvoiceAfterDelete(invoice, ligneCommande.getLigneCommandeId());

            // Supprimer la ligne de commande de la base de données
            ligneCommandeRepository.deleteById(ligneCommandeId);
        });
    }

    public void updateInvoiceAfterDelete(InvoiceModel invoice, int deletedLigneCommandeId) {
        if (invoice != null) {
            // Calculer le montant total de la facture
            double totalAmount = calculateInvoiceTotalAmount(invoice, deletedLigneCommandeId);
            invoice.setTotalAmount(totalAmount);

            // Enregistrer la facture mise à jour
            invoiceRepository.save(invoice);
        }
    }

    private double calculateInvoiceTotalAmount(InvoiceModel invoice, int deletedLigneCommandeId) {
        // Obtenir le montant total actuel de la facture
        double totalAmount = invoice.getTotalAmount();

        // Soustraire le montant de la ligne de commande supprimée si elle existe
        Optional<LigneCommandeModel> optionalDeletedLigneCommande = ligneCommandeRepository.findById(deletedLigneCommandeId);
        if (optionalDeletedLigneCommande.isPresent()) {
            LigneCommandeModel deletedLigneCommande = optionalDeletedLigneCommande.get();
            totalAmount -= deletedLigneCommande.getProduct().getPriceUnit() * deletedLigneCommande.getQuantity();
        }

        // Assurer que le montant total est au moins 0.0
        return Math.max(totalAmount, 0.0);
    }


    //Afficher le nombre de ligne de commande d'un costumer
    public int countLigneCommandesByCustomerId(int customerId) {
        return ligneCommandeRepository.countLigneCommandesByCustomerId(customerId);
    }



}
