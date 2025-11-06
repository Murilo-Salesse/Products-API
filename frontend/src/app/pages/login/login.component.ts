import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { toast } from 'ngx-sonner';
import { LoginJWTResponse } from '../../interfaces/responses/user-login.token.model';
import { LoginService } from '../../services/login/login.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  isLoading: boolean = false;

  constructor(private router: Router, private loginService: LoginService) {}

  login() {
    if (this.email === '' || this.password === '') {
      this.showToast();
      return;
    }

    if (this.isLoading) return;
    this.isLoading = true;

    const body = {
      email: this.email,
      password: this.password,
    };

    this.loginService.login(body).subscribe({
      next: (response: LoginJWTResponse) => {
        localStorage.setItem('token', response.token);
        localStorage.setItem(
          'user',
          JSON.stringify({
            id: response.id,
            email: response.email,
            name: response.name,
          })
        );

        setTimeout(() => {
          this.isLoading = false;
          this.router.navigate(['/dashboard']);
        }, 1200);
      },
      error: (error: any) => {
        setTimeout(() => {
          const backendMsg =
            error?.error?.message || 'Erro inesperado ao fazer login.';
          this.toastErrorRequest(backendMsg);
        });

        this.isLoading = false;
      },
    });
  }

  showToast() {
    toast.error('Erro', {
      description: 'Preencha o email e senha.',
    });
  }

  toastErrorRequest(message: string) {
    toast.error('Erro', {
      description: `${message}`,
    });
  }
  goToRegister() {
    this.router.navigate(['/register']);
  }
}
