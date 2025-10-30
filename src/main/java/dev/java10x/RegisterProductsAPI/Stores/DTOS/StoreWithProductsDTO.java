package dev.java10x.RegisterProductsAPI.Stores.DTOS;


import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreWithProductsDTO {

    private Long id;
    private String name;
    private String address;

    private List<ProductsDTO> products;


}
