package glsi.example.backendfacture.controller;

import glsi.example.backendfacture.dto.LigneCommandeDetailsDTO;
import glsi.example.backendfacture.model.LigneCommandeModel;
import glsi.example.backendfacture.service.LigneCommandeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ligne-commande")
public class LigneCommandeController {

    @Autowired
    private LigneCommandeService ligneCommandeService;

    @PostMapping("/create")
    public LigneCommandeModel createLigneCommande(
            @RequestParam("costumerId") int costumerId,
            @RequestParam("productId") int productId,
            @RequestParam("quantity") double quantity) {
        return ligneCommandeService.createLigneCommande(costumerId, productId, quantity);
    }

    @GetMapping("/{customerId}")
    public void afficherLignesDeCommandePourClient(@PathVariable int customerId) {
        ligneCommandeService.afficherDetailsCommandesEtFacture(customerId);
    }

    @GetMapping("/client/{customerId}")
    public List<LigneCommandeDetailsDTO> getLignesDeCommandeAvecDetailsPourClient(@PathVariable int customerId) {
        return ligneCommandeService.getLignesDeCommandeAvecDetailsPourClient(customerId);
    }

    // Supprimer une ligne de commande
    @DeleteMapping("/{ligneCommandeId}")
    public ResponseEntity<String> deleteLigneCommande(@PathVariable int ligneCommandeId) {
        try {
            ligneCommandeService.deleteLigneCommandeById(ligneCommandeId);
            return ResponseEntity.ok("Ligne de commande supprimée avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de la suppression de la ligne de commande.");
        }
    }

    //Afficher le nombre de ligne de commande d'un costumer
    @GetMapping("/countByCustomer/{customerId}")
    public int countLigneCommandesByCustomer(@PathVariable int customerId) {
        return ligneCommandeService.countLigneCommandesByCustomerId(customerId);
    }


}