package glsi.example.backendfacture.controller;

import glsi.example.backendfacture.model.CostumerModel;
import glsi.example.backendfacture.model.LoginRequest;
import glsi.example.backendfacture.model.ProductModel;
import glsi.example.backendfacture.service.CostumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/costumers")
public class CostumerController {

    @Autowired
    private CostumerService costumerService;


    //Enregistrer un client
    @PostMapping("/register")
    public ResponseEntity<CostumerModel> registerCostumer(@RequestBody CostumerModel costumer) {
        CostumerModel registeredCostumer = costumerService.registerCostumer(costumer);
        return new ResponseEntity<>(registeredCostumer, HttpStatus.CREATED);
    }

    //Afficher la liste des clients
    @GetMapping
    public ResponseEntity<List<CostumerModel>> getAllClients() {
        List<CostumerModel> clients = costumerService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    //Afficher un client
    @GetMapping("/{clientId}")
    public ResponseEntity<CostumerModel> getClientById(@PathVariable int clientId) {
        CostumerModel client = costumerService.getClientById(clientId);
        if (client != null) {
            return new ResponseEntity<>(client, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Mettre à jour un client
    @PutMapping("/{clientId}")
    public ResponseEntity<CostumerModel> updateClient(@PathVariable int clientId, @RequestBody CostumerModel updatedClient) {
        CostumerModel client = costumerService.updateClient(clientId, updatedClient);
        return new ResponseEntity<>(client, HttpStatus.OK);
    }

    //Supprimer un client
    @DeleteMapping("/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable int clientId) {
        costumerService.deleteClient(clientId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //S'authentifier
    /*@PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody String email, @RequestBody String password) {
        String token = costumerService.authenticate(email, password);
        return ResponseEntity.ok(token);
    }*/

    @PostMapping("/login02")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

            CostumerModel costumerModel = costumerService.authenticate(email, password);

        if (costumerModel != null) {
            // Connexion réussie, retournez l'utilisateur connecté
            return ResponseEntity.ok(costumerModel);
        } else {
            // Connexion échouée, retournez une erreur ou une réponse appropriée
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Echec de l'authentification");
        }
    }


}
