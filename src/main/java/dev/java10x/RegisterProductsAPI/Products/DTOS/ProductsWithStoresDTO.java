package dev.java10x.RegisterProductsAPI.Products.DTOS;


import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreIdDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsWithStoresDTO {

    private Long id;
    private String name;
    private String description;
    private int quantity;
    private double value;

    private List<StoreIdDTO> stores;
}