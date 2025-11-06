import { Injectable } from '@angular/core';

import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { catchError, throwError } from 'rxjs';
import { StoreResponse } from 'src/app/interfaces/responses/total-stores-with-product';

@Injectable({
  providedIn: 'root',
})
export class StoresService {
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

  getAllStores() {
    const headers = this.getAuthHeaders();

    return this.http
      .get<StoreResponse>(
        `${this.url}${environment.STORES.GET_ALL}?withProducts=true`,
        { headers, withCredentials: true }
      )
      .pipe(catchError((error: HttpErrorResponse) => throwError(() => error)));
  }

  createStore(data: any) {
    const headers = this.getAuthHeaders();

    return this.http
      .post(`${this.url}${environment.STORES.CREATE_STORE}`, data, {
        headers,
        withCredentials: true,
      })
      .pipe(catchError((error: HttpErrorResponse) => throwError(() => error)));
  }

  updateStore(storeId: number, data: any) {
    const headers = this.getAuthHeaders();

    return this.http
      .put(`${this.url}${environment.STORES.UPDATE_STORE}/${storeId}`, data, {
        headers,
        withCredentials: true,
      })
      .pipe(catchError((error: HttpErrorResponse) => throwError(() => error)));
  }

  deleteStore(storeId: number) {
    const headers = this.getAuthHeaders();

    return this.http
      .delete(`${this.url}${environment.STORES.DELETE_STORE}/${storeId}`, {
        headers,
        withCredentials: true,
      })
      .pipe(catchError((error: HttpErrorResponse) => throwError(() => error)));
  }
}
