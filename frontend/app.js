import { createVehicleApi } from './services/apiClient.js';
import { renderVehicleCard } from './ui/VehicleCard.js';
import { renderFilters } from './ui/Filters.js';
import { renderPager } from './ui/Pager.js';

const api = createVehicleApi('http://localhost:8080');

let currentParams = {};
let currentPage = 1;
const pageSize = 12;

const filtersSlot = document.getElementById('filters');
const resultsSlot = document.getElementById('results');
const resultCount = document.getElementById('resultCount');

init();

async function init() {
  showLoading('Loading filters…');
  try {
    const meta = await api.getMeta();
    filtersSlot.innerHTML = '';
    filtersSlot.append(renderFilters(meta, (params) => {
      currentParams = params;
      currentPage = 1;
      handleSearch();
    }));
    await handleSearch();
  } catch (e) {
    showError(`Failed to load meta: ${e.message}`);
  }
}

async function handleSearch() {
  showLoading('Searching…');
  try {
    const data = await api.searchVehicles({ ...currentParams, page: currentPage, pageSize });
    const { total = 0, results = [] } = data;

    resultCount.textContent = `${total} result${total === 1 ? '' : 's'}`;
    resultsSlot.innerHTML = '';

    const grid = document.createElement('div');
    grid.style.display = 'grid';
    grid.style.gridTemplateColumns = 'repeat(auto-fit, minmax(260px, 1fr))';
    grid.style.gap = '1rem';
    results.forEach(vehicle => grid.append(renderVehicleCard(vehicle)));
    resultsSlot.append(grid);

    resultsSlot.append(renderPager(total, currentPage, pageSize, (newPage) => {
      currentPage = newPage;
      handleSearch();
    }));

    if (results.length === 0) resultsSlot.textContent = 'No vehicles match your filters.';
  } catch (e) {
    showError(`Search failed: ${e.message}`);
  }
}

function showLoading(msg) { 
  resultsSlot.textContent = msg; 
}

function showError(msg) { 
  resultsSlot.textContent = msg; 
}