export function createVehicleApi(baseUrl) {
    return {
      async getMeta() {
          const response = await fetch(`${baseUrl}/api/meta`);
          if (!response.ok) {
              throw new Error(`meta error status: ${response.status}`);
          }
          return response.json();
      },
  
      async searchVehicles(params = {}) {
        const querySearch = toQueryString(params);
        const response = await fetch(`${baseUrl}/api/vehicles?${querySearch}`);
        if (!response.ok) {
          throw new Error(`vehicles error status: ${response.status}`);
        }
        return response.json();
      },
    };
  }
  
  function toQueryString(obj) {
    const querySearch = new URLSearchParams();
    for (const [key, value] of Object.entries(obj)) {
      if (value === undefined || value === null || value === '') continue;
      querySearch.append(key, value);
    }
    return querySearch.toString();
  }