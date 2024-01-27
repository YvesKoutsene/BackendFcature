package glsi.example.backendfacture.service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import glsi.example.backendfacture.config.TwilioConfig;
import glsi.example.backendfacture.model.CostumerModel;
import glsi.example.backendfacture.model.InvoiceModel;
import glsi.example.backendfacture.model.LigneCommandeModel;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class WhatsAppService {

    static {
        TwilioConfig.init(TwilioConfig.ACCOUNT_SID, TwilioConfig.AUTH_TOKEN);
    }

    public static void sendWhatsAppMessageWithInfo(String toPhoneNumber, CostumerModel customer, List<LigneCommandeModel> lignesDeCommande, InvoiceModel invoice) {
        PhoneNumber to = new PhoneNumber("whatsapp:" + toPhoneNumber);
        PhoneNumber from = new PhoneNumber("whatsapp:" + TwilioConfig.TWILIO_NUMBER);

        StringBuilder messageBody = new StringBuilder();
        messageBody.append("Informations du client\n");
        messageBody.append("Nom du client: ").append(customer.getFullName()).append("\n");
        messageBody.append("Email du client: ").append(customer.getEmail()).append("\n");
        messageBody.append("Numéro du client: ").append(customer.getNumber()).append("\n");
        messageBody.append("------------------------------\n");

        // Informations de chaque ligne de commande
        int commandNumber = 1; // Initialiser le numéro de commande

        for (LigneCommandeModel ligneDeCommande : lignesDeCommande) {
            messageBody.append("Commande numéro: ").append(commandNumber).append("\n");
            messageBody.append("Quantité: ").append(ligneDeCommande.getQuantity()).append("\n");
            messageBody.append("Détails du produit\n");
            messageBody.append("Nom du produit: ").append(ligneDeCommande.getProduct().getNameProduct()).append("\n");
            messageBody.append("Prix du produit: ").append(ligneDeCommande.getProduct().getPriceUnit()).append("$\n");
            // Ajoutez d'autres détails du produit selon vos besoins
            messageBody.append("------------------------------\n");

            commandNumber++; // Incrémenter le numéro de commande
        }

        // Informations de la facture
        if (invoice != null) {
            messageBody.append("Informations de la facture:\n");
            messageBody.append("Prix total de la facture: ").append(invoice.getTotalAmount()).append("\n");
            messageBody.append("Date de la facture: ").append(formatDate(invoice.getDateInvoice())).append("\n");
            messageBody.append("------------------------------\n");
        } else {
            messageBody.append("Aucune facture associée à ces lignes de commande.\n");
        }

        // Conditions de paiement et Coordonnées bancaires
        messageBody.append("Conditions de paiement: \n");
        messageBody.append("1-Paiement intégral dans les 30 jours suivant la réception de la facture.\n");
        messageBody.append("2-En cas de retard de paiement, des frais d'intérêt de 2% par mois seront appliqués.\n");
        messageBody.append("Coordonnées bancaires\n");
        messageBody.append("Nom de la banque : ORABANK\n");
        messageBody.append("Numéro de compte : 0231-2467-6442-9875\n");
        messageBody.append("IBAN : AYELEVIE-SHOP-TOGO\n");
        messageBody.append("------------------------------\n");
        messageBody.append("Pour toute question concernant cette facture, veuillez contacter notre service clientèle. ayelevieshop@gmail.com\n");

        // Envoyer le message WhatsApp avec les informations
        Message.creator(to, from, messageBody.toString()).create();

        System.out.println("Message sent with information");
    }

    private static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(date);
    }

}
