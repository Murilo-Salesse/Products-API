package dev.java10x.RegisterProductsAPI.Stores.Repository;

import dev.java10x.RegisterProductsAPI.Stores.Models.StoreModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoresRepository extends JpaRepository<StoreModel, Long> {

    long count();
}
