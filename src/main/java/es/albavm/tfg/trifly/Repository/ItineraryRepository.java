package es.albavm.tfg.trifly.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import es.albavm.tfg.trifly.Model.Itinerary;
import es.albavm.tfg.trifly.Model.User;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary,Long> {
    
    Optional<Itinerary> findById(Long id);
    boolean existsByItineraryName(String itineraryName);
    Page<Itinerary> findAll(Pageable pageable);
    List<Itinerary> findByUser(User user);

    
}
