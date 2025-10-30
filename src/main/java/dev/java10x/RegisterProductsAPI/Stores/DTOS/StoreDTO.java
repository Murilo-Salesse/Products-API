package dev.java10x.RegisterProductsAPI.Stores.DTOS;

import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductIdDTO;
import lombok.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDTO {
    private Long id;
    private String name;
    private String address;

    private List<ProductIdDTO> products;
}
