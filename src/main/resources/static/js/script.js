mapboxgl.accessToken = 'pk.eyJ1IjoiZGFuaWVsc2hlY2h0ZXIiLCJhIjoiY2xwYXVzMXNmMHlncDJqcGN5ZGRuYnB5byJ9.w29J9B9a-QpBqbE6n-332Q';

const map = new mapboxgl.Map({
  container: 'map',
  style: 'mapbox://styles/mapbox/streets-v12',
  center: [2.3522, 48.8566], // París
  zoom: 12
});

const locations = [
  { name: 'Aeropuerto Charles de Gaulle', lngLat: [2.55, 49.0097] },
  { name: 'Hotel du Louvre', lngLat: [2.3348, 48.8638] },
  { name: 'Torre Eiffel', lngLat: [2.2945, 48.8584] },
  { name: 'Museo del Louvre', lngLat: [2.3364, 48.8609] },
  { name: 'Río Sena', lngLat: [2.313, 48.855] },
  { name: 'Montmartre', lngLat: [2.3429, 48.8867] },
  { name: 'Basílica del Sagrado Corazón', lngLat: [2.3431, 48.8867] }
];

locations.forEach(loc => {
  new mapboxgl.Marker({ color: '#13a4ec' })
    .setLngLat(loc.lngLat)
    .setPopup(new mapboxgl.Popup().setText(loc.name))
    .addTo(map);
});

document.querySelectorAll('.add-activity').forEach(btn => {
  btn.addEventListener('click', () => {
    const dayCard = btn.closest('.day-card');
    const activities = dayCard.querySelector('.activities');
    const newActivity = activities.firstElementChild.cloneNode(true);
    newActivity.querySelectorAll('input, textarea').forEach(input => input.value = '');
    activities.appendChild(newActivity);
  });
});

document.querySelectorAll('.delete-activity').forEach(btn => {
  btn.addEventListener('click', () => {
    const activity = btn.closest('.activity');
    activity.remove();
  });
});

document.querySelectorAll('.add-day').forEach(btn => {
  btn.addEventListener('click', () => {
    const planner = document.querySelector('.day-planner');
    const newDay = planner.querySelector('.day-card').cloneNode(true);
    newDay.querySelectorAll('input, textarea').forEach(input => input.value = '');
    newDay.querySelector('h3').textContent = `Día ${planner.querySelectorAll('.day-card').length + 1}: Nueva Fecha`;
    planner.insertBefore(newDay, btn);
  });
});
