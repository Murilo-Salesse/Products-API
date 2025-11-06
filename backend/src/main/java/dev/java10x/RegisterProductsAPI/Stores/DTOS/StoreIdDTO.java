package dev.java10x.RegisterProductsAPI.Stores.DTOS;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreIdDTO {

    @NotNull(message = "O ID não pode ser nulo.")
    private Long id;
}
