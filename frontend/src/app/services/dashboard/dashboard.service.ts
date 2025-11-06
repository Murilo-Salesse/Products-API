import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { TotalProductsResponse } from '../../interfaces/responses/total-products.model';
import { TotalStoresResponse } from '../../interfaces/responses/total-stores.model';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  private url = environment.BASE_API;

  constructor(private http: HttpClient, private route: Router) {}

  getAuthHeaders() {
    const token = localStorage.getItem('token');

    if (token) {
      return new HttpHeaders({
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      });
    } else {
      return new HttpHeaders({
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      });
    }
  }

  totalStores() {
    const headers = this.getAuthHeaders();

    return this.http
      .get<TotalStoresResponse>(
        `${this.url}${environment.DASHBOARD.GET_STORES}`,
        { headers, withCredentials: true }
      )
      .pipe(
        catchError((error: HttpErrorResponse) => {
          return throwError(() => error);
        })
      );
  }

  totalProducts() {
    const headers = this.getAuthHeaders();

    return this.http
      .get<TotalProductsResponse>(
        `${this.url}${environment.DASHBOARD.GET_PRODUCTS}`,
        { headers, withCredentials: true }
      )
      .pipe(
        catchError((error: HttpErrorResponse) => {
          return throwError(() => error);
        })
      );
  }
}
