package glsi.example.backendfacture.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import glsi.example.backendfacture.model.CostumerModel;
import glsi.example.backendfacture.model.InvoiceModel;
import glsi.example.backendfacture.model.LigneCommandeModel;
import glsi.example.backendfacture.service.EmailService;
import glsi.example.backendfacture.service.LigneCommandeService;
import glsi.example.backendfacture.service.WhatsAppService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PDFGenerator {

    @Autowired
    private LigneCommandeService ligneCommandeService;
    @Autowired
    private EmailService emailService;

    public byte[] generateInvoicePDF(int customerId) {
        // Utilisation de ByteArrayOutputStream pour stocker le contenu du PDF en mémoire
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Création d'un document PDF
        Document document = new Document();

        try {
            // Utilisation de PdfWriter.getInstance avec ByteArrayOutputStream
            PdfWriter.getInstance(document, byteArrayOutputStream);

            // Ouverture du document
            document.open();

            // En-tête de la facture (Logo et informations de l'entreprise)
            addInvoiceHeader(document);

            // Récupération des informations du client une seule fois
            CostumerModel customer = ligneCommandeService.getCustomerById(customerId);

            // Informations du client
            addSectionTitle(document, "Informations du client");
            addCustomerInfo(document, customer);
            addSeparatorLine(document);

            // Récupération de toutes les lignes de commande pour le client
            List<LigneCommandeModel> lignesDeCommande = ligneCommandeService.getLignesDeCommandePourClient(customerId);

            // Détails de chaque ligne de commande
            int commandNumber = 1; // Initialiser le numéro de commande
            addSectionTitle(document, "Détails de la commande");
            for (LigneCommandeModel ligneDeCommande : lignesDeCommande) {
                addOrderDetails(document, ligneDeCommande, commandNumber);

                commandNumber++; // Incrémenter le numéro de commande

            }
            addSeparatorLine(document);

            // Détails de la facture
            InvoiceModel invoice = ligneCommandeService.getInvoiceForCustomer(customerId);
            if (invoice != null) {
                addInvoiceDetails(document, invoice);
                addSeparatorLine(document);
            } else {
                document.add(new Paragraph("Aucune facture associée à ces lignes de commande."));
                addSeparatorLine(document);
            }

            // Pied de page de la facture
            addInvoiceFooter(document);

            // Fermeture du document
            document.close();

            // Récupération du contenu du PDF sous forme de tableau de bytes
            byte[] pdfContent = byteArrayOutputStream.toByteArray();

            // Envoi de l'email avec le PDF en pièce jointe
            sendInvoiceEmail(customer.getEmail(), "AYELEVIE SHOP", "Veuillez trouver votre facture en pièce jointe.", pdfContent);

            return pdfContent;

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return null; // En cas d'erreur
    }

    // Fonction pour ajouter l'en-tête de la facture (Logo et informations de l'entreprise)
    private void addInvoiceHeader(Document document) {
        // Ajoutez ici votre logo et les informations de l'entreprise
        // Utilisez des polices et des styles appropriés
    }

    // Fonction pour ajouter un titre de section dans le document
    @SneakyThrows
    private void addSectionTitle(Document document, String title) {
        Paragraph sectionTitle = new Paragraph(title);
        sectionTitle.setFont(FontFactory.getFont(FontFactory.HELVETICA_BOLD));
        sectionTitle.setSpacingAfter(10f); // Espacement après le titre
        document.add(sectionTitle);
    }

    // Fonction pour ajouter les informations du client dans le document
    @SneakyThrows
    private void addCustomerInfo(Document document, CostumerModel customer) {
        document.add(new Paragraph("Nom du client: " + customer.getFullName()));
        document.add(new Paragraph("Email du client: " + customer.getEmail()));
        document.add(new Paragraph("Numéro du client: " + customer.getNumber()));
    }

    // Fonction pour ajouter une ligne de séparation dans le document
    @SneakyThrows
    private void addSeparatorLine(Document document) {
        LineSeparator lineSeparator = new LineSeparator();
        document.add(Chunk.NEWLINE); // Ajout d'un espace avant la ligne de séparation
        document.add(lineSeparator);
        document.add(Chunk.NEWLINE); // Ajout d'un espace après la ligne de séparation
    }

    // Fonction pour ajouter les détails d'une commande dans le document
    @SneakyThrows
    private void addOrderDetails(Document document, LigneCommandeModel ligneDeCommande, int commandNumber) {
        document.add(new Paragraph("Commande numéro: " + commandNumber));
        document.add(new Paragraph("Quantité: " + ligneDeCommande.getQuantity()));
        document.add(new Paragraph("Détails du produit"));
        document.add(new Paragraph("Nom du produit: " + ligneDeCommande.getProduct().getNameProduct()));
        document.add(new Paragraph("Prix du produit: " + ligneDeCommande.getProduct().getPriceUnit() + "$"));
        // Ajoutez d'autres détails du produit selon vos besoins
    }

    // Fonction pour ajouter les détails de la facture dans le document
    @SneakyThrows
    private void addInvoiceDetails(Document document, InvoiceModel invoice) {
        addSectionTitle(document, "Informations de la facture");
        document.add(new Paragraph("Prix total de la facture: " + invoice.getTotalAmount()));
        document.add(new Paragraph("Date de la facture: " + formatDate(invoice.getDateInvoice())));
        // Ajoutez d'autres détails de la facture selon vos besoins
    }

    // Fonction pour ajouter le pied de page de la facture
    @SneakyThrows
    private void addInvoiceFooter(Document document) {
        // Conditions de paiement
        document.add(new Paragraph("Conditions de paiement: "));
        document.add(new Paragraph("1-Paiement intégral dans les 30 jours suivant la réception de la facture."));
        document.add(new Paragraph("2-En cas de retard de paiement, des frais d'intérêt de 2% par mois seront appliqués."));

        // Coordonnées bancaires
        document.add(new Paragraph("Coordonnées bancaires "));
        document.add(new Paragraph("Nom de la banque : ORABANK"));
        document.add(new Paragraph("Numéro de compte : 0231-2467-6442-9875"));
        document.add(new Paragraph("IBAN : AYELEVIE-SHOP-TOGO"));

        //addSectionTitle

        // Autres informations
        document.add(new Paragraph("Pour toute question concernant cette facture, veuillez contacter notre service clientèle. ayelevieshop@gmail.com"));
    }

    // Fonction utilitaire pour envoyer l'email
    private void sendInvoiceEmail(String to, String subject, String body, byte[] pdfAttachment) {
        emailService.sendInvoiceEmail(to, subject, body, pdfAttachment, "Facture.pdf");
    }


    // Fonction utilitaire pour formater la date
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }


}

/*package glsi.example.backendfacture.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import glsi.example.backendfacture.model.CostumerModel;
import glsi.example.backendfacture.model.InvoiceModel;
import glsi.example.backendfacture.model.LigneCommandeModel;
import glsi.example.backendfacture.service.EmailService;
import glsi.example.backendfacture.service.LigneCommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PDFGenerator {

    @Autowired
    private LigneCommandeService ligneCommandeService;
    @Autowired
    private EmailService emailService;

    public byte[] generateInvoicePDF(int customerId) {
        // Utilisez ByteArrayOutputStream pour stocker le contenu du PDF en mémoire
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            // Utilisez PdfWriter.getInstance avec ByteArrayOutputStream
            PdfWriter.getInstance(document, byteArrayOutputStream);

            document.open();

            // Récupérez les informations du client une seule fois
            CostumerModel customer = ligneCommandeService.getCustomerById(customerId);

            // Ajoutez les informations du client au document PDF
            document.add(new Paragraph("Informations du client"));
            document.add(new Paragraph("Nom du client: " + customer.getFullName()));
            document.add(new Paragraph("Email du client: " + customer.getEmail()));
            document.add(new Paragraph("Numéro du client: " + customer.getNumber()));
            document.add(new Paragraph("------------------------------"));

            // Récupérez toutes les lignes de commande pour le client
            List<LigneCommandeModel> lignesDeCommande = ligneCommandeService.getLignesDeCommandePourClient(customerId);

            // Afficher les détails de chaque ligne de commande
            for (LigneCommandeModel ligneDeCommande : lignesDeCommande) {
                document.add(new Paragraph("Commande numero: " + ligneDeCommande.getLigneCommandeId()));
                document.add(new Paragraph("Quantité: " + ligneDeCommande.getQuantity()));

                // Afficher les détails du produit associé
                document.add(new Paragraph("Détails du produit"));
                document.add(new Paragraph("Nom du produit: " + ligneDeCommande.getProduct().getNameProduct()));
                document.add(new Paragraph("Prix du produit: " + ligneDeCommande.getProduct().getPriceUnit() + "$"));
                // Ajoutez d'autres détails du produit selon vos besoins

                document.add(new Paragraph("------------------------------"));
            }

            // Récupérez la facture associée à ces lignes de commande
            InvoiceModel invoice = ligneCommandeService.getInvoiceForCustomer(customerId);

            // Afficher les détails de la facture
            if (invoice != null) {
                document.add(new Paragraph("Informations de la facture:"));
                document.add(new Paragraph("Prix total de la facture: " + invoice.getTotalAmount()));
                document.add(new Paragraph("Date de la facture: " + formatDate(invoice.getDateInvoice())));
                document.add(new Paragraph("------------------------------"));
            } else {
                document.add(new Paragraph("Aucune facture associée à ces lignes de commande."));
            }

            // Fermez le document
            document.close();

            // Récupérez le contenu du PDF sous forme de tableau de bytes
            byte[] pdfContent = byteArrayOutputStream.toByteArray();

            // Envoyez l'email avec le PDF en pièce jointe
            sendInvoiceEmail(customer.getEmail(), "AYELEVIE SHOP", "Veuillez trouver votre facture en pièce jointe.", pdfContent);

            return pdfContent;

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return null; // En cas d'erreur
    }

    // Fonction utilitaire pour envoyer l'email
    private void sendInvoiceEmail(String to, String subject, String body, byte[] pdfAttachment) {
        emailService.sendInvoiceEmail(to, subject, body, pdfAttachment, "Facture.pdf");
    }

    // Fonction utilitaire pour formater la date
    private String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }

}
*/
