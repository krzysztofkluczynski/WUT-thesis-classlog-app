export const environment = {
  production: true,
  backend_url: (window as any).env?.API_URL || 'http://localhost:8080',
};
