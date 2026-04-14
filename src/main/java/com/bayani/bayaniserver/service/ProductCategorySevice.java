package com.bayani.bayaniserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import com.bayani.bayaniserver.dto.ProductCategoryDTO;
import com.bayani.bayaniserver.entity.ProductCategory;
import com.bayani.bayaniserver.entity.ProductItem;
import com.bayani.bayaniserver.entity.Store;
import com.bayani.bayaniserver.entity.User;
import com.bayani.bayaniserver.repository.ProductCategoryRepo;
import com.bayani.bayaniserver.repository.StoreRepo;
import com.bayani.bayaniserver.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ProductCategorySevice {
  @Autowired
  ProductCategoryRepo productCategoryRepo;

  @Autowired
  UserRepo userRepo;

  @Autowired
  StoreRepo storeRepo;
 
  public ProductCategory createProductCategory(ProductCategoryDTO productCategoryDTO) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    Optional<User> userOptional = userRepo.findByEmail(username);
    if (userOptional.isEmpty()) {
      log.error("Invalid user info");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    User user = userOptional.get();

    String storeId = user.getStoreId();

    Optional<Store> storeOptional = storeRepo.findByStoreId(Integer.parseInt(storeId));
    if (storeOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    String name = productCategoryDTO.getName();
    Optional<ProductCategory> prodOptional = productCategoryRepo.findByNameAndStoreStoreId(name, Integer.parseInt(storeId));
    if (prodOptional.isPresent()) {
      Store prodStore = prodOptional.get().getStore();

      if (prodStore != null && prodStore.getStoreId() == Integer.parseInt(storeId)) {
        log.info("Product Category name already exist");
        throw new ResponseStatusException(HttpStatus.CONFLICT);
      }
    }

    ProductCategory productCategory = new ProductCategory();
    productCategory.setDescription(productCategoryDTO.getDescription());
    productCategory.setName(productCategoryDTO.getName());
    productCategory.setType(productCategoryDTO.getType());
    productCategory.setItemNumber(0);

    productCategory.setStore(storeOptional.get());
    productCategoryRepo.save(productCategory);

    return productCategory;
  } 
  
  public ProductCategory updateProductCategory(ProductCategoryDTO productCategoryDTO) {
    Integer productCategoryId = productCategoryDTO.getProductCategoryId();
    Optional<ProductCategory> prodOptional = productCategoryRepo.findByProductCategoryId(productCategoryId);

    if (prodOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    ProductCategory productCategory = prodOptional.get();
    productCategory.setDescription(productCategoryDTO.getDescription());
    productCategory.setName(productCategoryDTO.getName());
    productCategory.setType(productCategoryDTO.getType());
    productCategoryRepo.save(productCategory);

    return productCategory;
  } 

  public ProductCategory deleteProductCategory(Integer id) {
    Optional<ProductCategory> prodOptional = productCategoryRepo.findByProductCategoryId(id);
    if (prodOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }


    List<ProductItem> productItemsList = prodOptional.get().getProductItems();
    if (!productItemsList.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The product category is currently associated with product items.");
    }
    productCategoryRepo.delete(prodOptional.get());
    
    return prodOptional.get();
  } 
}
