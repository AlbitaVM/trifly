package es.albavm.tfg.trifly.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.albavm.tfg.trifly.Model.Note;

@Repository
public interface NoteRepository extends JpaRepository<Note,Long>{
    
}
