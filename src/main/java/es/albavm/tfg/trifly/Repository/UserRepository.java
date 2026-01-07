package es.albavm.tfg.trifly.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import es.albavm.tfg.trifly.Model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findById(Long id);
    Page<User> findAll(Pageable pageable);

    @Query("SELECT MONTH(u.createdDate), COUNT(u) FROM User u WHERE YEAR(u.createdDate) = :year GROUP BY MONTH(u.createdDate) ORDER BY MONTH(u.createdDate)")
    List<Object[]> countUsersByMonth(@Param("year") int year);
    
    @Query("SELECT COUNT(i) * 1.0 / COUNT(DISTINCT u) FROM User u LEFT JOIN u.itineraries i")
    double avgItinerariesPerUser();

    @Query("SELECT COUNT(u) FROM User u WHERE u.lastLoginDate >= :since")
    long countActiveUsers(@Param("since") LocalDateTime since);

    @Query("SELECT COUNT(u) FROM User u")
    long countTotalUsers();
}
