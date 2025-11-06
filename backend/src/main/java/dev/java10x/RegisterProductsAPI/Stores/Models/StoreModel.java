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
@Data
public class StoreModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    // Essa relação já é mapeada pelo atributo lojas lá na classe ProdutoModel. Eu só quero enxergar o outro lado
    // O mappedBy informa qual campo é o dono da relação — ou seja, quem tem o @JoinTable
    @ManyToMany(mappedBy = "stores")
    private List<ProductModel> products = new ArrayList<>();


}