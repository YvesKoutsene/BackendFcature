package glsi.example.backendfacture.controller;

import glsi.example.backendfacture.model.InvoiceModel;
import glsi.example.backendfacture.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/create")
    public ResponseEntity<InvoiceModel> createFacture(
            @RequestParam int costumerId,
            @RequestBody List<InvoiceService.ProductQuantity> productQuantities) {
        try {
            InvoiceModel newFacture = invoiceService.createFacture(costumerId, productQuantities);
            return new ResponseEntity<>(newFacture, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Envoyer facture par whatsapp
    @GetMapping("/sendWhatsapp/{customerId}")
    public String sendWhatsAppMessage(@PathVariable int customerId) {
        try {
            invoiceService.sendWhatsAppMessage(customerId);
            return "WhatsApp message envoyé avec succès";
        } catch (Exception e) {
            e.printStackTrace();
            return "Echec d'envoie de WhatsApp message";
        }
    }

}
