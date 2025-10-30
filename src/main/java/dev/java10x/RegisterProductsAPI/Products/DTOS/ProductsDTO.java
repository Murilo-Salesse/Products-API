package dev.java10x.RegisterProductsAPI.Products.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsDTO {


    private Long id;
    private String name;
    private String description;
    private int quantity;
    private double value;
}
