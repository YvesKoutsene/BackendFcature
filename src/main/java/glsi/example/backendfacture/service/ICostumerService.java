package glsi.example.backendfacture.service;

import glsi.example.backendfacture.model.CostumerModel;
//import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ICostumerService {

    CostumerModel registerCostumer(CostumerModel costumer);
    List<CostumerModel> getAllClients();
    CostumerModel getClientById(int clientId);
    CostumerModel updateClient(int clientId, CostumerModel updatedClient);
    void deleteClient(int clientId);
    CostumerModel authenticate(String email, String password);

}
