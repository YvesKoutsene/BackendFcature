package glsi.example.backendfacture.controller;

import glsi.example.backendfacture.pdf.PDFGenerator;
import glsi.example.backendfacture.service.PDFGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pdf")
public class PDFController {

    @Autowired
    private PDFGenerator pdfGenerator;

    @GetMapping("/{customerId}")
    public void generateInvoicePDF(@PathVariable int customerId) {
        byte[] pdfContent = pdfGenerator.generateInvoicePDF(customerId);
    }
}
