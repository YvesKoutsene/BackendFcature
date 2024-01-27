package glsi.example.backendfacture.repository;

import glsi.example.backendfacture.model.CostumerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CostumerRepository extends JpaRepository<CostumerModel, Integer> {
    boolean existsByEmail(String email);
    Optional<CostumerModel> findByEmail(String email);


}
