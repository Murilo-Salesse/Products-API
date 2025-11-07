package dev.java10x.RegisterProductsAPI.Products.DTOS;


import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreIdDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsWithStoresDTO {

    private Long id;

    @NotBlank(message = "Nome não pode ser vazio.")
    private String name;

    @NotBlank(message = "Descrição não pode ser vazia.")
    private String description;

    @NotNull(message = "Quantidade não pode ser nula")
    @Min(value = 1, message = "Quantidade minima deve ser uma.")
    private int quantity;

    @NotNull(message = "Valor não ser nulo.")
    @Min(value = 1, message = "Valor deve ser maior que zero.")
    private double value;

    @NotNull(message = "Lojas não pode ser nula.")
    @Valid
    private List<StoreIdDTO> stores = new ArrayList<>();
}