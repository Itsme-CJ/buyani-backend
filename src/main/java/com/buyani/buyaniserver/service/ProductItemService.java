package com.buyani.buyaniserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.buyani.buyaniserver.dto.ProductCategoryDTO;
import com.buyani.buyaniserver.dto.ProductItemDTO;
import com.buyani.buyaniserver.entity.ProductCategory;
import com.buyani.buyaniserver.entity.ProductItem;
import com.buyani.buyaniserver.entity.Store;
import com.buyani.buyaniserver.entity.User;
import com.buyani.buyaniserver.repository.ProductCategoryRepo;
import com.buyani.buyaniserver.repository.ProductItemRepo;
import com.buyani.buyaniserver.repository.StoreRepo;
import com.buyani.buyaniserver.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class ProductItemService {
  @Autowired
  ProductItemRepo productItemRepo;

  @Autowired
  ProductCategoryRepo productCategoryRepo;

  @Autowired
  UserRepo userRepo;

  @Autowired
  StoreRepo storeRepo;
 
  public List<ProductItem> getAllProducts() {
    return productItemRepo.findAll();
  }

  public List<ProductItem> getActiveProducts() {
    return productItemRepo.findByStatus("ACTIVE");
  }

  public List<ProductItem> getProductsByStoreId(Integer storeId) {
    return productItemRepo.findByStoreId(storeId);
  }

  public Map<String, Object> getProductById(Integer id) {
    ProductItem item = productItemRepo.findByProductItemId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    Map<String, Object> result = new HashMap<>();
    result.put("productItemId", item.getProductItemId());
    result.put("name", item.getName());
    result.put("genericName", item.getGenericName());
    result.put("description", item.getDescription());
    result.put("productNumber", item.getProductNumber());
    result.put("price", item.getPrice());
    result.put("stock", item.getStock());
    result.put("criticalLevel", item.getCriticalLevel());
    result.put("withPrescription", item.getWithPrescription());
    result.put("storeId", item.getStoreId());
    result.put("status", item.getStatus());
    String catName = item.getProductCategory() != null ? item.getProductCategory().getName() : null;
    result.put("categoryName", catName);    result.put("image", item.getImage());    return result;
  }

  public ProductItem createProductItem(ProductItemDTO productItemDTO) {
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

    String number = productItemDTO.getProductNumber();
    Optional<ProductItem> prodOptional = productItemRepo.findByProductNumber(number);
    if (prodOptional.isPresent()) {
      Integer prodStoreId = prodOptional.get().getStoreId();

      if (prodStoreId != null && prodStoreId == Integer.parseInt(storeId)) {
        log.info("Product Item already exist");
        throw new ResponseStatusException(HttpStatus.CONFLICT);
      }
    }

    ProductItem productItem = new ProductItem();
    productItem.setDescription(productItemDTO.getDescription());
    productItem.setName(productItemDTO.getName());
    productItem.setPrice(productItemDTO.getPrice());
    productItem.setProductNumber(productItemDTO.getProductNumber());
    productItem.setStoreId(Integer.parseInt(storeId));
    productItem.setStock(productItemDTO.getStock());
    productItem.setCriticalLevel(productItemDTO.getCriticalLevel());
    productItem.setGenericName(productItemDTO.getGenericName());
    productItem.setWithPrescription(productItemDTO.getWithPrescription());
    if (productItemDTO.getImage() != null) {
      productItem.setImage(productItemDTO.getImage());
    }

    String productCategoryName = productItemDTO.getCategory();
    Optional<ProductCategory> prodCategoryOptional = productCategoryRepo.findByNameAndStoreStoreId(productCategoryName, Integer.parseInt(storeId));

    ProductCategory productCategory;
    if (prodCategoryOptional.isEmpty()) {
      // Auto-create the category for this store so sellers don't need to pre-create it
      // Name is made unique per store to avoid DB unique-constraint collisions
      String uniqueCategoryName = productCategoryName + "_store_" + storeId;
      Optional<ProductCategory> existingByUniqueName = productCategoryRepo.findByNameAndStoreStoreId(uniqueCategoryName, Integer.parseInt(storeId));
      ProductCategory newCategory;
      if (existingByUniqueName.isPresent()) {
        newCategory = existingByUniqueName.get();
      } else {
        newCategory = new ProductCategory();
        newCategory.setName(uniqueCategoryName);
        newCategory.setDescription(productCategoryName);
        newCategory.setType("SELLER");
        newCategory.setItemNumber(0);
        newCategory.setStore(storeOptional.get());
        productCategoryRepo.save(newCategory);
      }
      productCategory = newCategory;
    } else {
      productCategory = prodCategoryOptional.get();
    }

    productItem.setProductCategory(productCategory);
    productItem.setStatus(productItemDTO.getStatus() != null ? productItemDTO.getStatus() : "INACTIVE");

    Integer newItemNumber = productCategory.getItemNumber() + 1;
    productCategory.setItemNumber(newItemNumber);
    productCategoryRepo.save(productCategory);

    productItemRepo.save(productItem);

    return productItem;
  }

  public void updateProductItemStatus(Integer productItemId, String status) {
    productItemRepo.updateStatus(productItemId, status);
  }

  public void decrementStock(Integer productItemId, Integer qty) {
    productItemRepo.decrementStock(productItemId, qty);
  } 
  
  public ProductItem updateProductItem(ProductItemDTO productItemDTO) {
    Integer productItemId = productItemDTO.getProductItemId();
    Optional<ProductItem> prodOptional = productItemRepo.findByProductItemId(productItemId);

    if (prodOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    ProductItem productItem = prodOptional.get();
    productItem.setDescription(productItemDTO.getDescription());
    productItem.setName(productItemDTO.getName());
    productItem.setProductNumber(productItemDTO.getProductNumber());
    productItem.setPrice(productItemDTO.getPrice());
    productItem.setStock(productItemDTO.getStock());
    productItem.setCriticalLevel(productItemDTO.getCriticalLevel());
    productItem.setGenericName(productItemDTO.getGenericName());
    productItem.setWithPrescription(productItemDTO.getWithPrescription());
    if (productItemDTO.getImage() != null) {
      productItem.setImage(productItemDTO.getImage());
    }

    String productCategoryName = productItemDTO.getCategory();
    Optional<ProductCategory> prodCategoryOptional = productCategoryRepo.findByNameAndStoreStoreId(productCategoryName, prodOptional.get().getStoreId());
    if (prodCategoryOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    ProductCategory productCategory = prodCategoryOptional.get();
    ProductCategory productCategory2 = productItem.getProductCategory();
    Integer categoryId = productCategory.getProductCategoryId();
    Integer categoryId2 = productCategory2.getProductCategoryId();

    if (categoryId != categoryId2) {
      productItem.setProductCategory(productCategory);
  
      Integer newItemNumber = productCategory.getItemNumber() + 1;
      Integer newItemNumber2 = productCategory2.getItemNumber() - 1;

      productCategory.setItemNumber(newItemNumber);
      productCategory2.setItemNumber(newItemNumber2);
      productCategoryRepo.save(productCategory);
      productCategoryRepo.save(productCategory2);
    }

    productItemRepo.save(productItem);
    return productItem;
  } 

  public void deleteProductItem(Integer productItemId) {
    Optional<ProductItem> prodOptional = productItemRepo.findByProductItemId(productItemId);
    if (prodOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    ProductItem productItem = prodOptional.get();
    ProductCategory productCategory = productItem.getProductCategory();
    if (productCategory != null) {
      Integer newItemNumber = productCategory.getItemNumber() - 1;
      productCategory.setItemNumber(Math.max(0, newItemNumber));
      productCategoryRepo.save(productCategory);
    }

    productItemRepo.delete(productItem);
  }
}
