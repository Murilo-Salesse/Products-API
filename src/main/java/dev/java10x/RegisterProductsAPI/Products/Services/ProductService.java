package dev.java10x.RegisterProductsAPI.Products.Services;

import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreIdDTO;
import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreWithoutProductsDTO;
import dev.java10x.RegisterProductsAPI.Stores.Models.StoreModel;
import dev.java10x.RegisterProductsAPI.Stores.Repository.StoresRepository;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductWithAllStoresDTO;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsWithStoresDTO;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsDTO;
import dev.java10x.RegisterProductsAPI.Products.Models.ProductModel;
import dev.java10x.RegisterProductsAPI.Products.Repository.ProductsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductsRepository productsRepository;
    private final StoresRepository storesRepository;

    public ProductService(ProductsRepository productsRepository, StoresRepository storesRepository) {
        this.productsRepository = productsRepository;
        this.storesRepository = storesRepository;
    }

    @Transactional
    public ProductsWithStoresDTO createProductWithStore(ProductsWithStoresDTO dto) {
      ProductModel product = new ProductModel();

      product.setName(dto.getName());
      product.setValue(dto.getValue());
      product.setDescription(dto.getDescription());
      product.setQuantity(dto.getQuantity());


      List<StoreModel> stores = dto.getStores() != null
              ? new ArrayList<>(dto.getStores().stream()
              .map(l -> storesRepository.findById(l.getId())
                      .orElseThrow(() -> new RuntimeException("Loja não encontrada")))
              .toList())
              : new ArrayList<>();

      product.setStores(stores);
      stores.forEach(loja -> loja.getProducts().add(product));

      ProductModel saveProduct = productsRepository.save(product);

      List<StoreIdDTO> storesDTO = saveProduct.getStores().stream()
              .map(l -> new StoreIdDTO(l.getId()))
              .toList();

      return new ProductsWithStoresDTO(saveProduct.getId(), saveProduct.getName(), saveProduct.getDescription(),
              saveProduct.getQuantity(), saveProduct.getValue(), storesDTO);
    }

    public List<ProductsDTO> listStoresWithoutStores(){
        return productsRepository.findAll().stream()
                .map(p -> new ProductsDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getQuantity(),
                        p.getValue()))
                .toList();
    }

    public List<ProductWithAllStoresDTO> listProductsWithStores() {
        return productsRepository.findAll().stream()
                .map(p -> {
                    List<StoreWithoutProductsDTO> storesDTO = p.getStores().stream()
                            .map(l -> new StoreWithoutProductsDTO(
                                    l.getId(),
                                    l.getName(),
                                    l.getAddress()))
                            .toList();
                    return new ProductWithAllStoresDTO(
                            p.getId(),
                            p.getName(),
                            p.getDescription(),
                            p.getQuantity(),
                            p.getValue(), storesDTO);
                })
                .toList();
    }

    public Optional<ProductWithAllStoresDTO> searchProductById(Long id) {
        return productsRepository.findById(id)
                .map(p -> {
                    List<StoreWithoutProductsDTO> storesDTO = p.getStores().stream()
                            .map(l -> new StoreWithoutProductsDTO(
                                    p.getId(),
                                    p.getName(),
                                    p.getDescription()))
                            .toList();
                    return new ProductWithAllStoresDTO(
                            p.getId(),
                            p.getName(),
                            p.getDescription(),
                            p.getQuantity(),
                            p.getValue(),
                            storesDTO);
                });
    }

    @Transactional
    public Optional<ProductsWithStoresDTO> updateProduct(Long id, ProductsWithStoresDTO dto) {
        Optional<ProductModel> productOpt = productsRepository.findById(id);
        if (productOpt.isEmpty()) return Optional.empty();

        ProductModel product = productOpt.get();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setQuantity(dto.getQuantity());
        product.setValue(dto.getValue());

        List<StoreModel> newStore = dto.getStores() != null
                ? new ArrayList<>(dto.getStores().stream()
                .map(l -> storesRepository.findById(l.getId())
                        .orElseThrow(() -> new RuntimeException("Loja não encontrada: " + l.getId())))
                .toList())
                : new ArrayList<>();

        product.getStores().forEach(loj -> loj.getProducts().remove(product));
        newStore.forEach(loj -> loj.getProducts().add(product));

        product.setStores(newStore);

        ProductModel attProduct = productsRepository.save(product);

        List<StoreIdDTO> storesDTO = attProduct.getStores().stream()
                .map(s -> new StoreIdDTO(s.getId()))
                .toList();

        return Optional.of(new ProductsWithStoresDTO(
                attProduct.getId(),
                attProduct.getName(),
                attProduct.getDescription(),
                attProduct.getQuantity(),
                attProduct.getValue(), storesDTO));
    }

    @Transactional
    public boolean deleteProduct(Long id) {
        Optional<ProductModel> productOpt = productsRepository.findById(id);
        if (productOpt.isEmpty()) return false;

        ProductModel product = productOpt.get();

        product.getStores().forEach(prod -> prod.getProducts().remove(product));
        product.getStores().clear();

        productsRepository.delete(product);
        return true;
    }
}
