export interface Product {
  id: number;
  name: string;
  description: string;
  quantity: number;
  value: number;
}

export interface Store {
  id: number;
  name: string;
  address: string;
  products: Product[];
}

export type StoreResponse = Store[];
