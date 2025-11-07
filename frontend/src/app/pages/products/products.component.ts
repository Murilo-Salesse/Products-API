import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { SidebarComponent } from 'src/app/components/sidebar/sidebar.component';
import { ProductsService } from 'src/app/services/products/products.service';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule, SidebarComponent, ReactiveFormsModule, FormsModule],
  templateUrl: './products.component.html',
})
export class ProductsComponent implements OnInit {
  products: any[] = [];
  isLoading = true;
  errorMessage = '';
  showModal = false;
  productForm: FormGroup;
  selectedStores: number[] = [];
  isEditMode = false;
  editingProductId: number | null = null;

  constructor(
    private productsService: ProductsService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      quantity: [0, [Validators.required, Validators.min(0)]],
      value: [0, [Validators.required, Validators.min(0)]],
    });
  }

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.isLoading = true;
    this.productsService.getAllProducts().subscribe({
      next: (res: any) => {
        this.products = res;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erro ao buscar produtos:', err);
        this.errorMessage = 'Não foi possível carregar os produtos.';
        this.isLoading = false;
      },
    });
  }

  openModal(): void {
    this.showModal = true;
    this.isEditMode = false;
    this.editingProductId = null;
    this.productForm.reset({
      quantity: 0,
      value: 0,
    });
    this.selectedStores = [];
  }

  openEditModal(product: any): void {
    this.showModal = true;
    this.isEditMode = true;
    this.editingProductId = product.id;

    this.productForm.patchValue({
      name: product.name,
      description: product.description,
      quantity: product.quantity,
      value: product.value,
    });

    this.selectedStores = product.stores?.map((s: any) => s.id) || [];
  }

  closeModal(): void {
    this.showModal = false;
    this.isEditMode = false;
    this.editingProductId = null;
    this.productForm.reset({
      quantity: 0,
      value: 0,
    });
    this.selectedStores = [];
  }

  addStore(): void {
    this.selectedStores.push(0);
  }

  removeStore(index: number): void {
    this.selectedStores.splice(index, 1);
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      const productData = {
        name: this.productForm.get('name')?.value,
        description: this.productForm.get('description')?.value,
        quantity: this.productForm.get('quantity')?.value,
        value: this.productForm.get('value')?.value,
        stores: this.selectedStores
          .filter((id) => id > 0)
          .map((id) => ({ id })),
      };

      if (this.isEditMode && this.editingProductId) {
        this.productsService
          .updateProduct(this.editingProductId, productData)
          .subscribe({
            next: (res) => {
              console.log('Produto atualizado com sucesso:', res);
              this.closeModal();
              this.loadProducts();
            },
            error: (err) => {
              console.error('Erro ao atualizar produto:', err);
              this.errorMessage = 'Não foi possível atualizar o produto.';
            },
          });
      } else {
        this.productsService.createProduct(productData).subscribe({
          next: (res) => {
            console.log('Produto criado com sucesso:', res);
            this.closeModal();
            this.loadProducts();
          },
          error: (err) => {
            console.error('Erro ao criar produto:', err);
            this.errorMessage = 'Não foi possível criar o produto.';
          },
        });
      }
    }
  }

  deleteProduct(productId: number) {
    const confirmation = confirm('Deseja realmente deletar o produto?');

    if (confirmation) {
      this.productsService.deleteProduct(productId).subscribe({
        next: () => {
          this.products = this.products.filter(
            (product) => product.id !== productId
          );
          this.loadProducts();
        },
        error: (err) => {
          console.error('Erro ao deletar produto:', err);
          this.errorMessage = 'Não foi possível deletar o produto.';
        },
      });
    }
  }
}
