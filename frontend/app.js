import { createVehicleApi } from './services/apiClient.js';
import { renderVehicleCard } from './ui/VehicleCard.js';
import { renderFilters } from './ui/Filters.js';

const api = createVehicleApi('http://localhost:8080');

const filtersSlot = document.getElementById('filters');
const resultsSlot = document.getElementById('results');
const resultCount = document.getElementById('resultCount');

const meta = await api.getMeta();
const filtersEl = renderFilters(meta, handleSearch);
filtersSlot.append(filtersEl);

handleSearch({});

async function handleSearch(params) {
  const { total, results } = await api.searchVehicles(params);

  resultCount.textContent = `${total} result${total === 1 ? '' : 's'}`;
  resultsSlot.innerHTML = '';
  results.forEach(vehicle => resultsSlot.append(renderVehicleCard(vehicle)));
}