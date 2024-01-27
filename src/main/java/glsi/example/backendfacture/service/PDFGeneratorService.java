package glsi.example.backendfacture.service;

import glsi.example.backendfacture.model.InvoiceModel;
import glsi.example.backendfacture.model.CostumerModel;
import glsi.example.backendfacture.model.LigneCommandeModel;
import glsi.example.backendfacture.pdf.PDFGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PDFGeneratorService {

    @Autowired
    private CostumerService costumerService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private EmailService emailService;

    /*public byte[] generateInvoicePDFAndSendEmail(int customerId) {
        try {
            // Récupérer le client
            LigneCommandeModel customer = .getClientById(customerId);

            // Générer le PDF
            byte[] pdfContent = generateInvoicePDF(customer);

            // Envoyer l'e-mail avec la pièce jointe
            String to = customer.getEmail();
            String subject = "AYELEVIE SHOP";
            String text = "Veuillez trouver votre facture en pièce jointe.";
            String attachmentFileName = "facture" + invoice.getInvoiceId() + ".pdf";
            emailService.sendInvoiceEmail(to, subject, text, pdfContent, attachmentFileName);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    private byte[] generateInvoicePDF(CostumerModel customer, InvoiceModel invoice) {
        PDFGenerator pdfGenerator = new PDFGenerator();
        return pdfGenerator.generateInvoicePDF(customer, invoice);
    }*/

}
