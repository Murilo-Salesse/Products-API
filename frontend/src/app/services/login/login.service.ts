import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { RegisterRequest } from '../../interfaces/requests/register-user.request';
import { LoginRequest } from '../../interfaces/requests/user-login-response';
import { LoginResponse } from '../../interfaces/responses/user-login.model';
import { LoginJWTResponse } from '../../interfaces/responses/user-login.token.model';

@Injectable({
  providedIn: 'root',
})
export class LoginService {
  private url = environment.BASE_API;

  constructor(private http: HttpClient, private route: Router) {}

  login(body: LoginRequest) {
    return this.http
      .post<LoginJWTResponse>(`${this.url}${environment.AUTH.LOGIN}`, body)
      .pipe(catchError((error: HttpErrorResponse) => throwError(() => error)));
  }

  register(body: RegisterRequest) {
    return this.http
      .post<LoginResponse>(`${this.url}${environment.AUTH.REGISTER}`, body)
      .pipe(catchError((error: HttpErrorResponse) => throwError(() => error)));
  }
}
