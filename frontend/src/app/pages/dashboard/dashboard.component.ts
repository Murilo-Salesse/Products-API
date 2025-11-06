import { CommonModule, DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { InfosStoreCardComponent } from '../../components/cards/infos-store-card/infos-store-card.component';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';
import { TotalProductsResponse } from '../../interfaces/responses/total-products.model';
import { TotalStoresResponse } from '../../interfaces/responses/total-stores.model';
import { DashboardService } from '../../services/dashboard/dashboard.service';

@Component({
  selector: 'app-dashboard',
  imports: [SidebarComponent, InfosStoreCardComponent, CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  providers: [DatePipe],
})
export class DashboardComponent implements OnInit {
  currentDate: string;
  currentTime: string;

  isLoadingStore = true;
  isLoadingProduct = true;

  totalStores: number = 0;
  totalProducts: number = 0;
  totalReports: number = 0;

  constructor(
    private datePipe: DatePipe,
    private dashService: DashboardService
  ) {
    const currentDateObj = new Date();
    this.currentDate =
      this.datePipe.transform(currentDateObj, 'fullDate', undefined, 'pt') ||
      '';
    this.currentTime =
      this.datePipe.transform(currentDateObj, 'HH:mm', undefined, 'pt') || '';
  }

  ngOnInit(): void {
    this.getTotalStores();
    this.getTotalProduct();
  }

  getTotalStores() {
    this.isLoadingStore = true;

    this.dashService.totalStores().subscribe({
      next: (response: TotalStoresResponse) => {
        setTimeout(() => {
          this.totalStores = response?.total;
          this.isLoadingStore = false;
        }, 3000);
      },
      error: (error: any) => {
        console.log('Error:', error);
        this.isLoadingStore = false;
      },
    });
  }

  getTotalProduct() {
    this.isLoadingProduct = true;

    this.dashService.totalProducts().subscribe({
      next: (response: TotalProductsResponse) => {
        setTimeout(() => {
          this.totalProducts = response?.total;
          this.isLoadingProduct = false;
        }, 3000);
      },
      error: (error: any) => {
        console.log('Error:', error);
        this.isLoadingProduct = false;
      },
    });
  }
}
