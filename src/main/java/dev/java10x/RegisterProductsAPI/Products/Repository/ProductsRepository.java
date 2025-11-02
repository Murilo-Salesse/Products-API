package dev.java10x.RegisterProductsAPI.Products.Repository;


import dev.java10x.RegisterProductsAPI.Products.Models.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductsRepository extends JpaRepository<ProductModel, Long> {

    List<ProductModel> findTop5ByOrderByValueDesc();

}
