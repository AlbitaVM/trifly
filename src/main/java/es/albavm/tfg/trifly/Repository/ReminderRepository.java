package es.albavm.tfg.trifly.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.albavm.tfg.trifly.Model.Reminder;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long>{
    Optional<Reminder> findById(Long id);
}
