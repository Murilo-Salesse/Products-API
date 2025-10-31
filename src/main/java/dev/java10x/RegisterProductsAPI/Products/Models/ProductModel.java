package dev.java10x.RegisterProductsAPI.Products.Models;

import dev.java10x.RegisterProductsAPI.Stores.Models.StoreModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// Transforma a classe em uma entidade do banco de dados
// JPA -> Java Persistence API
@Entity
@Table(name = "tb_produtos")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(name = "nome")
    private String Name;

    private String description;
    private int quantity;
    private double value;

    // Relação muitos-para-muitos
    @ManyToMany
    @JoinTable(
            name = "tb_produtos_lojas",                  // nome da tabela intermediária
            joinColumns = @JoinColumn(name = "produto_id"),  // chave que referencia o produto
            inverseJoinColumns = @JoinColumn(name = "loja_id") // chave que referencia a loja
    )
    private List<StoreModel> stores = new ArrayList<>();
}
