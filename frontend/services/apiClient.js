export function createVehicleApi(baseUrl) {

    return {
      async getMeta() {
          const response = await fetch(`${baseUrl}/api/meta`)
          return response.json();
      },
  
      async searchVehicles(params = {}) {
        const querySearch = new URLSearchParams(params).toString();
        const response = await fetch(`${baseUrl}/api/vehicles?${querySearch}`)
        const results = await response.json()
  
        return { total: response.length, results };
      },
    };
}