import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class StoresService {
  private url = environment.BASE_API;

  constructor(private http: HttpClient, private route: Router) {}
}
