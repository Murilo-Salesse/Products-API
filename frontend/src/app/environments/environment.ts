import { ROUTES } from './routes';

export const environment = {
  production: true,
  appName: 'Nexzy',
  BASE_API: 'http://localhost:8080/api/',

  ...ROUTES,
};
