package dev.java10x.RegisterProductsAPI.Stores.Models;

import dev.java10x.RegisterProductsAPI.Products.Models.ProductModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_lojas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StoreModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @ManyToMany(mappedBy = "stores")
    private List<ProductModel> products = new ArrayList<>();

    // Getter customizado para garantir que sempre retorne uma lista mutável
    public List<ProductModel> getProducts() {
        if (products == null) {
            products = new ArrayList<>();
        }
        return products;
    }

    // Setter customizado para garantir que sempre use uma lista mutável
    public void setProducts(List<ProductModel> products) {
        if (products == null) {
            this.products = new ArrayList<>();
        } else {
            this.products = new ArrayList<>(products);
        }
    }
}