package es.albavm.tfg.trifly.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.albavm.tfg.trifly.Model.Note;
import es.albavm.tfg.trifly.Model.User;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long>{
    Page<Note> findByUser(User user, Pageable pageable);
    Optional<Note> findById(Long id);
}
