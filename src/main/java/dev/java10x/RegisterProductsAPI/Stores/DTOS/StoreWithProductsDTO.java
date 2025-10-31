package dev.java10x.RegisterProductsAPI.Stores.DTOS;


import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreWithProductsDTO {

    private Long id;

    @NotBlank(message = "Nome da loja não pode ser vazio.")
    private String name;

    @NotBlank(message = "Endereço da loja não pode ser vazio.")
    private String address;

    @NotNull(message = "ID do produto não pode ser nulo.")
    @Valid
    private List<ProductsDTO> products;


}
