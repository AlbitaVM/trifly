package es.albavm.tfg.trifly.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.albavm.tfg.trifly.Model.Expenditure;

@Repository
public interface ExpenditureRepository extends JpaRepository<Expenditure, Long>{
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expenditure e WHERE e.category.budget.id = :budgetId")
    double sumAmountByBudgetId(@Param("budgetId") Long budgetId);
}
