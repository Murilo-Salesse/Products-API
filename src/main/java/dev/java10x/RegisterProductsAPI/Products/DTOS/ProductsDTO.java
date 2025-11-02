package dev.java10x.RegisterProductsAPI.Products.DTOS;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsDTO {

    @NotNull(message = "ID Não pode ser nulo.")
    private Long id;

    private String name;
    private String description;
    private int quantity;
    private double value;
}
