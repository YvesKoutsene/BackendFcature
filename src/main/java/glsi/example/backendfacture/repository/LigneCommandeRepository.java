package glsi.example.backendfacture.repository;

import glsi.example.backendfacture.model.LigneCommandeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneCommandeRepository extends JpaRepository<LigneCommandeModel, Integer> {

    List<LigneCommandeModel> findByCustomerCustomerId(int customerId);

    @Query("SELECT COUNT(lc) FROM LigneCommandeModel lc WHERE lc.invoice.customer.id = :customerId")
    int countLigneCommandesByCustomerId(@Param("customerId") int customerId);

}
