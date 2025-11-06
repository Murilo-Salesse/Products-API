import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { toast } from 'ngx-sonner';
import { LoginService } from '../../services/login/login.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  email = '';
  password = '';
  name = '';
  isLoading = false;

  constructor(private router: Router, private loginService: LoginService) {}

  register() {
    if (this.email === '' || this.name === '' || this.password === '') {
      this.showToast('Preencha todos os campos para se cadastrar.');
      return;
    }

    if (this.isLoading) return;

    this.isLoading = true;

    const body = {
      email: this.email,
      name: this.name,
      password: this.password,
    };

    this.loginService.register(body).subscribe({
      next: () => {
        setTimeout(() => {
          this.isLoading = false;
          this.router.navigate(['/login']);
        }, 1200);
      },
      error: (error: any) => {
        console.log('error: ', error);

        setTimeout(() => {
          const backendMsg =
            error?.error?.message || 'Erro inesperado ao fazer login.';
          this.showToast(backendMsg);
        });

        this.isLoading = false;
      },
    });
  }

  showToast(message: string) {
    toast.error('Erro', {
      description: `${message}`,
    });
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
