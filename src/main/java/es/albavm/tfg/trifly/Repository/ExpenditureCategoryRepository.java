package es.albavm.tfg.trifly.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.albavm.tfg.trifly.Model.ExpenditureCategory;

@Repository
public interface ExpenditureCategoryRepository extends JpaRepository<ExpenditureCategory, Long> {

    List<ExpenditureCategory> findByBudgetId(Long budgetId);
}
