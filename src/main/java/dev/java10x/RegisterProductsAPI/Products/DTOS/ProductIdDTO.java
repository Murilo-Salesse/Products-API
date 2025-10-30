package dev.java10x.RegisterProductsAPI.Products.DTOS;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductIdDTO {

    private Long id;
}