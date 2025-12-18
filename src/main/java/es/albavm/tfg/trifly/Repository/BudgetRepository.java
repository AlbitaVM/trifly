package es.albavm.tfg.trifly.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.albavm.tfg.trifly.Model.Budget;
import es.albavm.tfg.trifly.Model.User;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long>{
    Page<Budget> findByUser(User user, Pageable pageable);
    Optional<Budget> findById(Long id);
}
