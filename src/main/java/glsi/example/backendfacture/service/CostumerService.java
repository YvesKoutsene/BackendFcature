package glsi.example.backendfacture.service;

import glsi.example.backendfacture.model.CostumerModel;
import glsi.example.backendfacture.model.LigneCommandeModel;
import glsi.example.backendfacture.model.ProductModel;
import glsi.example.backendfacture.repository.CostumerRepository;
import glsi.example.backendfacture.repository.LigneCommandeRepository;
import glsi.example.backendfacture.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
/*import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;*/
import org.springframework.stereotype.Service;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;


@Service
public class CostumerService implements ICostumerService {

    private final CostumerRepository costumerRepository;
    //private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public CostumerService(CostumerRepository costumerRepository/*, BCryptPasswordEncoder passwordEncoder*/) {
        this.costumerRepository = costumerRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    //Enregistrer un Costumer
    @Override
    public CostumerModel registerCostumer(CostumerModel costumer) {
        // Vérifier si tous les champs requis sont fournis
        if (costumer.getFullName() == null  || costumer.getEmail() == null || costumer.getNumber() == null ||
                costumer.getPassword() == null || costumer.getRePassword() == null) {
            // Gérer l'erreur de champs requis non fournis
            throw new IllegalArgumentException("Tous les champs sont requis.");
        }

        // Vérifier si les mots de passe correspondent
        if (!costumer.getPassword().equals(costumer.getRePassword())) {
            // Gérer l'erreur de mots de passe non correspondants
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas.");
        }

        // Vérifier la longueur minimale des mots de passe
        if (costumer.getPassword().length() < 8 || costumer.getRePassword().length() < 8) {
            // Gérer l'erreur de longueur minimale non respectée
            throw new IllegalArgumentException("Les mots de passe doivent contenir au moins 8 caracteres.");
        }

        // Crypter les mots de passe avec BCrypt
        /*BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(costumer.getPassword());
        costumer.setPassword(encryptedPassword);
        costumer.setRePassword(encryptedPassword);*/

        // Vérifier si l'email est unique
        if (costumerRepository.existsByEmail(costumer.getEmail())) {
            // Gérer l'erreur d'email non unique
            throw new IllegalArgumentException("L'email est deja utilise par un autre utilisateur.");
        }

        // Si toutes les validations réussissent, enregistrez le client
        return costumerRepository.save(costumer);
    }

    //Afficher tous les clients
    @Override
    public List<CostumerModel> getAllClients() {
        return costumerRepository.findAll();
    }

    //Afficher un client
    @Override
    public CostumerModel getClientById(int clientId) {
        Optional<CostumerModel> optionalCostumer = costumerRepository.findById(clientId);
        return optionalCostumer.orElse(null);
    }

    //Mettre à jour un client
    @Override
    public CostumerModel updateClient(int clientId, CostumerModel updatedClient) {
        // Vérifier si le client existe
        CostumerModel existingClient = getClientById(clientId);
        if (existingClient == null) {
            // Gérer l'erreur de client non trouvé
            throw new IllegalArgumentException("Client non trouve avec l'identifiant : " + clientId);
        }

        // Mettre à jour les informations du client
        existingClient.setFullName(updatedClient.getFullName());
        existingClient.setEmail(updatedClient.getEmail());
        existingClient.setNumber(updatedClient.getNumber());
        existingClient.setPassword(updatedClient.getPassword());
        existingClient.setRePassword(updatedClient.getRePassword());

        // Vérifier si le mot de passe a été modifié
        /*if (!passwordEncoder.matches(updatedClient.getPassword(), existingClient.getPassword())) {
            // Crypter le nouveau mot de passe
            String encryptedPassword = passwordEncoder.encode(updatedClient.getPassword());

            // Mettre à jour les deux mots de passe dans le client existant
            existingClient.setPassword(encryptedPassword);
            existingClient.setRePassword(encryptedPassword);

        }*/

        // Enregistrer les modifications
        return costumerRepository.save(existingClient);
    }

    //Supprimer un client
    @Override
    public void deleteClient(int clientId) {
        // Vérifier si le client existe
        CostumerModel existingClient = getClientById(clientId);
        if (existingClient == null) {
            // Gérer l'erreur de client non trouvé
            throw new IllegalArgumentException("Client non trouvé avec l'identifiant : " + clientId);
        }
        // Supprimer le client
        costumerRepository.deleteById(clientId);
    }

    //S'authenfier
    public CostumerModel authenticate(String email, String password) {
        CostumerModel costumer = costumerRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouve avec cet email : " + email));

        if (costumer != null && password.equals(costumer.getPassword())){
            // Authentication successful, generate a JWT token here if necessary
            return costumer;
        } else {
            throw new IllegalArgumentException("Email ou mot de passe incorrect.");
        }

        //BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        /*if (passwordEncoder.matches(password, costumer.getPassword())) {
            // Authentification réussie, générez un token JWT ici si nécessaire
            return "AUTH_TOKEN";
        } else {
            throw new IllegalArgumentException("Email ou mot de passe incorrect.");
        }*/


    }


}
