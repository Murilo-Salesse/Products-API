import { registerLocaleData } from '@angular/common';
import {
  provideHttpClient,
  withInterceptorsFromDi,
} from '@angular/common/http';
import localePt from '@angular/common/locales/pt';
import {
  ApplicationConfig,
  importProvidersFrom,
  provideZoneChangeDetection,
} from '@angular/core';
import { FormsModule } from '@angular/forms'; // ✅ importe aqui
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';

registerLocaleData(localePt, 'pt');

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi()),
    importProvidersFrom(FormsModule),
  ],
};
