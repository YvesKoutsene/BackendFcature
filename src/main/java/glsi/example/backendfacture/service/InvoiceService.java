package glsi.example.backendfacture.service;

import glsi.example.backendfacture.model.CostumerModel;
import glsi.example.backendfacture.model.InvoiceModel;
import glsi.example.backendfacture.model.LigneCommandeModel;
import glsi.example.backendfacture.model.ProductModel;
import glsi.example.backendfacture.repository.CostumerRepository;
import glsi.example.backendfacture.repository.InvoiceRepository;
import glsi.example.backendfacture.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class InvoiceService {

    private final CostumerRepository costumerRepository;
    private final ProductRepository productRepository;
    private final InvoiceRepository invoiceRepository;
    private final LigneCommandeService ligneCommandeService;
    private final WhatsAppService whatsAppService;

    @Autowired
    public InvoiceService(
            CostumerRepository costumerRepository,
            ProductRepository productRepository,
            InvoiceRepository invoiceRepository,
            LigneCommandeService ligneCommandeService,
            WhatsAppService whatsAppService
    ) {
        this.costumerRepository = costumerRepository;
        this.productRepository = productRepository;
        this.invoiceRepository = invoiceRepository;
        this.ligneCommandeService = ligneCommandeService;
        this.whatsAppService = whatsAppService;
    }

    @Transactional
    public InvoiceModel createFacture(int costumerId, List<ProductQuantity> productQuantities) {
        CostumerModel costumer = costumerRepository.findById(costumerId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID : " + costumerId));

        InvoiceModel invoice = new InvoiceModel();
        invoice.setCustomer(costumer);
        invoice.setDateInvoice(new Date());
        invoice.setStatusPaid(false);

        double totalAmount = 0.0;

        for (ProductQuantity productQuantity : productQuantities) {
            int productId = productQuantity.getProductId();
            int quantity = productQuantity.getQuantity();

            ProductModel product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID : " + productId));

            double productTotal = product.getPriceUnit() * quantity;
            totalAmount += productTotal;

            invoice.getProducts().add(product);
        }

        invoice.setTotalAmount(totalAmount);

        return invoiceRepository.save(invoice);
    }

    public InvoiceModel getInvoiceById(int invoiceId) {
        return invoiceRepository.findById(invoiceId).orElse(null);
    }


    //Envoyer facture par whatsapp
    public void sendWhatsAppMessage(int customerId) {
        CostumerModel customer = ligneCommandeService.getCustomerById(customerId);
        List<LigneCommandeModel> lignesDeCommande = ligneCommandeService.getLignesDeCommandePourClient(customerId);
        InvoiceModel invoice = ligneCommandeService.getInvoiceForCustomer(customerId);

        // Appeler la fonction WhatsAppService avec les informations
        whatsAppService.sendWhatsAppMessageWithInfo(customer.getNumber(), customer, lignesDeCommande, invoice);
    }

    // Classe auxiliaire pour stocker l'ID du produit et la quantité dans la commande
    public static class ProductQuantity {
        private int productId;
        private int quantity;

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}
