package glsi.example.backendfacture.repository;

import glsi.example.backendfacture.model.InvoiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceModel, Integer> {

    @Query("SELECT i FROM InvoiceModel i WHERE i.customer.id = :customerId")
    Optional<InvoiceModel> findByCustomerId(int customerId);

}
