package dev.java10x.RegisterProductsAPI.Stores.DTOS;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreWithoutProductsDTO {
    private Long id;
    private String name;
    private String address;
}
