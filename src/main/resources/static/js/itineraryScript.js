function initItinerariesPagination(initialSize) {
    let currentPage = 0;
    let loadedPages = [0];
    let totalCardsVisible = initialSize;

    document.getElementById('btn-show-more').addEventListener('click', async function() {
        currentPage++;
        
        try {
            const response = await fetch(`/api/itineraries?page=${currentPage}`);
            const data = await response.json();
            
            if (data.itineraries && data.itineraries.length > 0) {
                const container = document.getElementById('itineraries-container');
                
                data.itineraries.forEach(itinerary => {
                    const card = createItineraryCard(itinerary);
                    container.insertAdjacentHTML('beforeend', card);
                });
                
                loadedPages.push(currentPage);
                totalCardsVisible += data.itineraries.length;
                
                if (!data.hasNext) {
                    document.getElementById('btn-show-more').style.display = 'none';
                }
                
                if (totalCardsVisible > 5) {
                    document.getElementById('btn-show-less').style.display = 'inline-block';
                }
            }
        } catch (error) {
            console.error('Error al cargar más itinerarios:', error);
        }
    });

    document.getElementById('btn-show-less').addEventListener('click', function() {
        const cards = document.querySelectorAll('.itinerary-card');
        const cardsToRemove = Math.min(5, cards.length - 5);
        
        for (let i = 0; i < cardsToRemove; i++) {
            cards[cards.length - 1 - i].remove();
        }
        
        totalCardsVisible -= cardsToRemove;
        
        if (loadedPages.length > 1) {
            loadedPages.pop();
            currentPage--;
        }
        
        document.getElementById('btn-show-more').style.display = 'inline-block';
        
        if (totalCardsVisible <= 5) {
            document.getElementById('btn-show-less').style.display = 'none';
        }
    });

    function createItineraryCard(itinerary) {
        return `
            <div class="col-md-6 col-lg-4 itinerary-card">
              <div class="card shadow-sm h-100">
                <img src="/itineraries/${itinerary.id}/image" 
                     class="card-img-top" 
                     alt="${itinerary.itineraryName}"
                     style="height: 250px; object-fit: cover; object-position: center;">
                <div class="card-body d-flex flex-column">
                  <h4 class="card-title fw-bold">${itinerary.itineraryName}</h4>
                  <p class="text-muted mb-1">${itinerary.destination}</p>
                  <p class="text-secondary small mb-3">${itinerary.startDate} - ${itinerary.finishDate}</p>
                  <div class="d-flex justify-content-end gap-2 mt-auto">
                    <a class="btn btn-info btn-sm" href="/itinerary">Ver</a>
                    <a class="btn btn-warning btn-sm" href="/itinerary/edit">Editar</a>
                    <button class="btn btn-outline-danger btn-sm">Eliminar</button>
                  </div>
                </div>
              </div>
            </div>
        `;
    }
}