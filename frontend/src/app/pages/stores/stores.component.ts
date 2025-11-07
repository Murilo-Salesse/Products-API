import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
  FormsModule,
} from '@angular/forms';
import { StoreResponse } from 'src/app/interfaces/responses/total-stores-with-product';
import { StoresService } from 'src/app/services/stores/stores.service';
import { SidebarComponent } from 'src/app/components/sidebar/sidebar.component';

@Component({
  selector: 'app-stores',
  standalone: true,
  imports: [CommonModule, SidebarComponent, ReactiveFormsModule, FormsModule],
  templateUrl: './stores.component.html',
})
export class StoresComponent implements OnInit {
  stores: StoreResponse = [];
  isLoading = true;
  errorMessage = '';
  showModal = false;
  storeForm: FormGroup;
  selectedProducts: number[] = [];
  isEditMode = false;
  editingStoreId: number | null = null;

  constructor(
    private storesService: StoresService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.storeForm = this.fb.group({
      name: ['', Validators.required],
      address: ['', Validators.required],
    });
  }

  ngOnInit() {
    this.loadStores();
  }

  loadStores() {
    this.isLoading = true;
    this.storesService.getAllStores().subscribe({
      next: (res: StoreResponse) => {
        this.stores = res;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erro ao buscar lojas:', err);
        this.errorMessage = 'Não foi possível carregar as lojas.';
        this.isLoading = false;
      },
    });
  }

  openModal(): void {
    this.showModal = true;
    this.isEditMode = false;
    this.editingStoreId = null;
    this.storeForm.reset();
    this.selectedProducts = [];
  }

  openEditModal(store: any): void {
    this.showModal = true;
    this.isEditMode = true;
    this.editingStoreId = store.id;

    this.storeForm.patchValue({
      name: store.name,
      address: store.address,
    });

    this.selectedProducts = store.products.map((p: any) => p.id);
  }

  closeModal(): void {
    this.showModal = false;
    this.isEditMode = false;
    this.editingStoreId = null;
    this.storeForm.reset();
    this.selectedProducts = [];
  }

  addProduct(): void {
    this.selectedProducts.push(0);
  }

  removeProduct(index: number): void {
    this.selectedProducts.splice(index, 1);
  }

  onSubmit(): void {
    if (this.storeForm.valid) {
      const storeData = {
        name: this.storeForm.get('name')?.value,
        address: this.storeForm.get('address')?.value,
        products: this.selectedProducts
          .filter((id) => id > 0)
          .map((id) => ({ id })),
      };

      if (this.isEditMode && this.editingStoreId) {
        this.storesService
          .updateStore(this.editingStoreId, storeData)
          .subscribe({
            next: (res) => {
              console.log('Loja atualizada com sucesso:', res);
              this.closeModal();
              this.loadStores();
            },
            error: (err) => {
              console.error('Erro ao atualizar loja:', err);
              this.errorMessage = 'Não foi possível atualizar a loja.';
            },
          });
      } else {
        this.storesService.createStore(storeData).subscribe({
          next: (res) => {
            console.log('Loja criada com sucesso:', res);
            this.closeModal();
            this.loadStores();
          },
          error: (err) => {
            console.error('Erro ao criar loja:', err);
            this.errorMessage = 'Não foi possível criar a loja.';
          },
        });
      }
    }
  }

  deleteStore(storeId: number) {
    const confirmation = confirm('Deseja realmente deletar a loja?');

    if (confirmation) {
      this.storesService.deleteStore(storeId).subscribe({
        next: () => {
          this.stores = this.stores.filter((store) => store.id !== storeId);
          this.loadStores();
        },
        error: (err) => {
          console.error('Erro ao deletar loja:', err);
          this.errorMessage = 'Não foi possível deletar a loja.';
        },
      });
    }
  }
}
