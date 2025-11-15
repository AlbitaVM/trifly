package es.albavm.tfg.trifly.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import es.albavm.tfg.trifly.Model.Itinerary;
import es.albavm.tfg.trifly.Repository.ItineraryRepository;
import es.albavm.tfg.trifly.dto.Itinerary.SummaryItineraryDto;

@Service
public class ItineraryService {
    
    @Autowired
    private ItineraryRepository itineraryRepository;

    public Page<SummaryItineraryDto> getAllItinerariesPaged(Pageable pageable){
        return itineraryRepository.findAll(pageable).map(it -> new SummaryItineraryDto(
            it.getId(), 
            it.getItineraryName(),
            it.getDestination(),
            it.getStartDate(),
            it.getFinishDate(),
            it.getImageBoolean()
            )
        );
    }

    public Optional<Itinerary> findById(Long id){
        return itineraryRepository.findById(id);
    }
}
