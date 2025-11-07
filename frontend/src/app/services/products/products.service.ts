import { Injectable } from '@angular/core';

import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ProductsService {
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

  getAllProducts() {
    const headers = this.getAuthHeaders();

    return this.http
      .get<any>(`${this.url}${environment.PRODUCTS.GET_ALL}?withStores=true`, {
        headers,
        withCredentials: true,
      })
      .pipe(catchError((error: HttpErrorResponse) => throwError(() => error)));
  }

  createProduct(data: any) {
    const headers = this.getAuthHeaders();

    return this.http
      .post(`${this.url}${environment.PRODUCTS.CREATE_PRODUCT}`, data, {
        headers,
        withCredentials: true,
      })
      .pipe(catchError((error: HttpErrorResponse) => throwError(() => error)));
  }

  updateProduct(productId: number, data: any) {
    const headers = this.getAuthHeaders();

    return this.http
      .put(
        `${this.url}${environment.PRODUCTS.UPDATE_PRODUCT}/${productId}`,
        data,
        {
          headers,
          withCredentials: true,
        }
      )
      .pipe(catchError((error: HttpErrorResponse) => throwError(() => error)));
  }

  deleteProduct(productId: number) {
    const headers = this.getAuthHeaders();

    return this.http
      .delete(
        `${this.url}${environment.PRODUCTS.DELETE_PRODUCT}/${productId}`,
        {
          headers,
          withCredentials: true,
        }
      )
      .pipe(catchError((error: HttpErrorResponse) => throwError(() => error)));
  }

  sendReportWithFile() {
    const headers = this.getAuthHeaders();

    return this.http
      .get<any>(`${this.url}${environment.PRODUCTS.REPORT_FILE}`, {
        headers,
        withCredentials: true,
      })
      .pipe(catchError((error: HttpErrorResponse) => throwError(() => error)));
  }
}
