package dev.java10x.RegisterProductsAPI.Products.DTOS;


import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreIdDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsWithStoresDTO {

    @NotNull(message = "ID não pode ser nulo.")
    private Long id;

    @NotNull(message = "Nome pode ser nulo.")
    @NotBlank(message = "Nome não pode ser vazio")
    private String name;
    private String description;
    private int quantity;
    private double value;

    private List<StoreIdDTO> stores;
}