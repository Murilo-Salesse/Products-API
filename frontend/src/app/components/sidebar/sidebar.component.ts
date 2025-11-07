import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { toast } from 'ngx-sonner';
import { ProductsService } from 'src/app/services/products/products.service';

@Component({
  selector: 'app-sidebar',
  imports: [],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
})
export class SidebarComponent {
  constructor(
    private router: Router,
    private productService: ProductsService
  ) {}

  clearStorage() {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
  }

  goToDash() {
    this.router.navigate(['/dashboard']);
  }

  goToStores() {
    this.router.navigate(['/stores']);
  }

  goToProducts() {
    this.router.navigate(['/products']);
  }

  sendReportWithFile() {
    this.productService.sendReportWithFile().subscribe({
      next: () => {
        toast.success('Relatório enviado com sucesso!');
      },
      error: (error: any) => {
        console.error('Erro ao enviar relatório:', error);
        toast.error('Falha ao enviar o relatório.');
      },
    });
  }
}
