package dev.java10x.RegisterProductsAPI.Products.Models;

import dev.java10x.RegisterProductsAPI.Stores.Models.StoreModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_produtos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String name;

    private String description;
    private int quantity;
    private double value;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "tb_produtos_lojas",
            joinColumns = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "loja_id")
    )
    private List<StoreModel> stores = new ArrayList<>();

    // Getter customizado para garantir que sempre retorne uma lista mutável
    public List<StoreModel> getStores() {
        if (stores == null) {
            stores = new ArrayList<>();
        }
        return stores;
    }

    // Setter customizado para garantir que sempre use uma lista mutável
    public void setStores(List<StoreModel> stores) {
        if (stores == null) {
            this.stores = new ArrayList<>();
        } else {
            this.stores = new ArrayList<>(stores);
        }
    }
}