export function renderVehicleCard(vehicle) {
    const element = document.createElement('div');
    element.style.border = '1px solid #ddd';
    element.style.borderRadius = '8px';
    element.style.padding = '0.75rem';
    element.innerHTML = `
      <h3 style="margin:0 0 .25rem 0">${vehicle.year} ${vehicle.make} ${vehicle.model}</h3>
      <div style="font-size:14px;color:#444">
        <div><strong>Trim:</strong> ${vehicle.trim ?? '-'}</div>
        <div><strong>Body:</strong> ${vehicle.body ?? '-'}</div>
        <div><strong>Seats:</strong> ${vehicle.seats ?? '-'}</div>
        <div><strong>MSRP:</strong> ${vehicle.msrp != null ? '£' + vehicle.msrp.toString() : '-'}</div>
      </div>
    `;
    return element;
  }