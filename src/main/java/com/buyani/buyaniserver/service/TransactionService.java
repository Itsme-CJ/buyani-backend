package com.buyani.buyaniserver.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.buyani.buyaniserver.controller.WebsocketController;
import com.buyani.buyaniserver.dto.ProductItemDTO;
import com.buyani.buyaniserver.dto.ReservationDTO;
import com.buyani.buyaniserver.dto.TransactionDTO;
import com.buyani.buyaniserver.dto.UserDTO;
import com.buyani.buyaniserver.dto.WebsocketDTO;
import com.buyani.buyaniserver.entity.ProductItem;
import com.buyani.buyaniserver.entity.Reservation;
import com.buyani.buyaniserver.entity.Store;
import com.buyani.buyaniserver.entity.Transaction;
import com.buyani.buyaniserver.entity.TransactionItem;
import com.buyani.buyaniserver.entity.User;
import com.buyani.buyaniserver.repository.ProductItemRepo;
import com.buyani.buyaniserver.repository.ReservationRepo;
import com.buyani.buyaniserver.repository.StoreRepo;
import com.buyani.buyaniserver.repository.TransactionItemRepo;
import com.buyani.buyaniserver.repository.TransactionRepo;
import com.buyani.buyaniserver.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionService {
  @Autowired
  ProductItemRepo productItemRepo;

  @Autowired
  StoreRepo storeRepo;

  @Autowired
  UserRepo userRepo;

  @Autowired
  TransactionRepo transactionRepo;

  @Autowired
  TransactionItemRepo transactionItemRepo;

  @Autowired
  ReservationRepo reservationRepo;

  @Autowired
  WebsocketController websocketController;

  private Integer getUserId(){
    String username = SecurityContextHolder.getContext().getAuthentication().getName();

    Optional<User> userOptional = userRepo.findByEmail(username);
    log.info("User {}", username);
    if (userOptional.isEmpty()) {
      log.error("Invalid user info");
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    User user = userOptional.get();

    return user.getUserId();
  } 

  public static String generateReferenceNumber(Integer primaryKey) {
      // Get the current date in the format yyMMdd
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
      String currentDate = dateFormat.format(new Date());
      
      // Combine the date with the primary key
      String transactionKey = currentDate + String.format("%06d", primaryKey);

      // Ensure the transaction key is exactly 12 characters
      if (transactionKey.length() > 12) {
          return transactionKey.substring(0, 12); // Truncate if necessary
      } else if (transactionKey.length() < 12) {
          return String.format("%-12s", transactionKey).replace(' ', '0'); // Pad with zeros if necessary
      }
      return transactionKey;
  }


  public Transaction createTransaction(TransactionDTO request) {
    List<ProductItemDTO> productDtos = request.getProductNumbers();
    List<String> productNumbers = new ArrayList<>();
    for (ProductItemDTO productDto: productDtos) {
      productNumbers.add(productDto.getProductNumber());
    }
    
    List<ProductItem> productItems = productItemRepo.findAllByProductNumber(productNumbers);
    for (ProductItem productItem : productItems) {
      log.info("productItem {}", productItem.getName());
    }
    Transaction transaction = new Transaction();
    transaction.setCash(request.getCash());
    transaction.setChange(request.getChange());
    transaction.setDiscount(request.getDiscount());
    transaction.setStatus(request.getStatus());
    transaction.setCustomerId(request.getCustomerId());
    transaction.setCustomerName(request.getCustomerName());
    transaction.setWhoAdded(getUserId());

    transaction.setTotalPrice(request.getTotalPrice());

    Optional<Store> sOptional = storeRepo.findByStoreId(request.getStoreId());
    if (!sOptional.isPresent()) {
      log.error("Store not found {}", request.getStoreId());
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    
    Store store = sOptional.get();
    transaction.setStore(store);
    
    User user = new User();
    if (request.getUserId() != null) {
      Optional<User> userOptional = userRepo.findByUserId(request.getUserId());
      if (!userOptional.isPresent()) {
        log.error("User not found {}", request.getUserId());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
      user = userOptional.get();
      transaction.setUser(user);
    }

    Transaction newTransaction = transactionRepo.save(transaction);
    Integer transactionId = newTransaction.getTransactionId();

    log.info("transactionId {}", transactionId);

    transaction.setTransactionNum(generateReferenceNumber(transactionId));
    transactionRepo.save(transaction);

    for (ProductItem productItem : productItems) {
      TransactionItem transactionItem = new TransactionItem();
      Integer stock = productItem.getStock();

      transactionItem.setProductItemId(productItem.getProductItemId());
      transactionItem.setTransaction(newTransaction);

      Optional<ProductItemDTO> optionalProductItem = productDtos.stream().filter(prdItem -> prdItem.getProductNumber().equals(productItem.getProductNumber())).findFirst();
      if (optionalProductItem.isEmpty()) {
        continue;
      }
      Integer quantity = optionalProductItem.get().getStock();
      transactionItem.setQuantity(quantity);
      transactionItemRepo.save(transactionItem);
      if (request.getStatus() == 0) {
        continue;
      }
      log.info("productItem {}", stock);
      Integer newStock = stock - quantity;
      productItem.setStock(newStock);
      productItemRepo.save(productItem);
      
    }
    log.info("request.getStatus() {}", request.getStatus());
    if (request.getStatus() == 3) {
      //Send notif
      WebsocketDTO websocketDTO = new WebsocketDTO();
      websocketDTO.setName(user.getLastName() + ", " + user.getFirstName());
      websocketDTO.setStoreId(store.getStoreId());
      websocketController.sendNotification(websocketDTO);
    }

    return transaction;
  }

  public Transaction updateTransaction(TransactionDTO request) { 
    Integer transactionId = request.getTrasactionId();
    Optional<Transaction> transacOptional = transactionRepo.findByTransactionId(transactionId);
    if (transacOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    Transaction transaction = transacOptional.get();
    transaction.setCash(request.getCash());
    transaction.setChange(request.getChange());
    transaction.setDiscount(request.getDiscount());
    transaction.setStatus(request.getStatus());
    transaction.setTotalPrice(request.getTotalPrice());
    transaction.setCustomerId(request.getCustomerId());
    transaction.setCustomerName(request.getCustomerName());
    transaction.setWhoAdded(getUserId());

    List<ProductItemDTO> productDtos = request.getProductNumbers();
    List<TransactionItem> transactionItems = transaction.getTransactionItems();

    for (ProductItemDTO productItemDTO : productDtos) {
      Optional<ProductItem> productItemOptional = productItemRepo.findByProductItemId(productItemDTO.getProductItemId());
      if (productItemOptional.isEmpty()) {
        continue;
      }

      ProductItem productItem = productItemOptional.get();
      Integer stock = productItem.getStock();
      
      Optional<TransactionItem> optionalProductItem = transactionItems.stream().filter(tItem -> tItem.getProductItemId().equals(productItemDTO.getProductItemId())).findFirst();
      // if new item in the transaction list, minus inventory
      if (optionalProductItem.isEmpty()) {
        Integer newStock = stock - productItemDTO.getStock();
        productItem.setStock(newStock);
        productItemRepo.save(productItem);

        TransactionItem transactionItem = new TransactionItem();
        transactionItem.setProductItemId(productItem.getProductItemId());
        transactionItem.setTransaction(transaction);
        transactionItem.setQuantity(productItemDTO.getStock());
        TransactionItem newTrans = transactionItemRepo.save(transactionItem);
        transactionItems.add(newTrans);
        continue;
      }

      TransactionItem transactionItem = optionalProductItem.get();
      Integer transactionItemQuantity = transactionItem.getQuantity();
      Integer requestQuantity = productItemDTO.getStock();
      if (transactionItemQuantity > requestQuantity) {
        Integer itemStock = transactionItem.getQuantity() - productItemDTO.getStock();
        Integer newStock = stock + itemStock;
        productItem.setStock(newStock);
        productItemRepo.save(productItem);
      } else if (transactionItemQuantity < requestQuantity) {
        Integer itemStock = productItemDTO.getStock() - transactionItem.getQuantity();
        Integer newStock =  stock - itemStock;
        productItem.setStock(newStock);
        productItemRepo.save(productItem);
      }

      transactionItem.setQuantity(requestQuantity);
      transactionItemRepo.save(transactionItem);
    }
    transactionRepo.save(transaction);
    for (TransactionItem transactionItem : transactionItems) {
      Optional<ProductItemDTO> optionalProductItem = productDtos.stream().filter(pItem -> pItem.getProductItemId().equals(transactionItem.getProductItemId())).findFirst();
      if (!optionalProductItem.isEmpty()) {
        continue;
      }
      Integer prevQuantity = transactionItem.getQuantity();
      Optional<ProductItem> productItemOptional = productItemRepo.findByProductItemId(transactionItem.getProductItemId());
      if (productItemOptional.isEmpty()) {
        continue;
      }

      ProductItem productItem = productItemOptional.get();
      Integer currentStock = productItem.getStock();
      Integer newStock = currentStock + prevQuantity;
      productItem.setStock(newStock);
      productItemRepo.save(productItem);
      transactionItem.setTransaction(null);
      TransactionItem nTransactionItem = transactionItemRepo.save(transactionItem);
      transactionItemRepo.delete(nTransactionItem);
    }

    Optional<Reservation> resOptional = reservationRepo.findByTransactionId(transactionId);
    if (resOptional.isPresent()) {
      log.info("Reservation is present");
      Reservation reservation = resOptional.get();
      reservation.setStatus(1);
      reservation.setTotalPrice(request.getTotalPrice());
      reservation.setDateClaimed(new Date());
      reservationRepo.save(reservation);
    }

    return new Transaction();
  }

  public TransactionDTO getTransaction(Integer id) {
    TransactionDTO transactionDTO = new TransactionDTO();
    Optional<Transaction> transacOptional = transactionRepo.findByTransactionId(id);
    if (transacOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    Transaction transaction = transacOptional.get();
    List<TransactionItem> transactionItems = transaction.getTransactionItems();
    List<ProductItemDTO> productItemDTOs = new ArrayList<>();

    for (TransactionItem transactionItem : transactionItems) {
      Optional<ProductItem> productItemOptional = productItemRepo.findByProductItemId(transactionItem.getProductItemId());
      if (productItemOptional.isEmpty()) {
        continue;
      }
    
      ProductItem productItem = productItemOptional.get();
      ProductItemDTO productItemDTO = new ProductItemDTO();
      productItemDTO.setCategory(productItem.getProductCategory().getName());
      productItemDTO.setName(productItem.getName());
      productItemDTO.setPrice(productItem.getPrice());
      productItemDTO.setProductItemId(productItem.getProductItemId());
      productItemDTO.setProductNumber(productItem.getProductNumber());
      productItemDTO.setStock(productItem.getStock());
      productItemDTO.setQuantity(transactionItem.getQuantity());
      productItemDTO.setTempStock(productItem.getStock());
      productItemDTOs.add(productItemDTO);
    }
    transactionDTO.setCash(transaction.getCash());
    transactionDTO.setChange(transaction.getChange());
    transactionDTO.setDiscount(transaction.getDiscount());
    transactionDTO.setStatus(transaction.getStatus());
    transactionDTO.setProductNumbers(productItemDTOs);
    transactionDTO.setTotalPrice(transaction.getTotalPrice());
    transactionDTO.setCustomerId(transaction.getCustomerId());
    transactionDTO.setCustomerName(transaction.getCustomerName());
    transactionDTO.setTransactionNum(transaction.getTransactionNum());

    Optional<Reservation> reservatiOptional = reservationRepo.findByTransactionId(id);
    if (reservatiOptional.isEmpty()) {
      log.info("No reservation");
      return transactionDTO;
    }

    Reservation reservation = reservatiOptional.get();
    ReservationDTO reservationDTO = new ReservationDTO();
    reservationDTO.setDateClaimed(reservation.getDateClaimed());
    reservationDTO.setNote(reservation.getNote());
    reservationDTO.setStatus(reservation.getStatus());
    reservationDTO.setTotalPrice(reservation.getTotalPrice());
    reservationDTO.setTransactionId(reservation.getTransactionId());
    reservationDTO.setReference(reservation.getReference());

    UserDTO userDTO = new UserDTO();
    User user = reservation.getUser();

    userDTO.setLastName(user.getLastName());
    userDTO.setFirstName(user.getFirstName());
    userDTO.setUserId(user.getUserId());

    reservationDTO.setUser(userDTO);
    
    transactionDTO.setReservation(reservationDTO);

    return transactionDTO;
  }

  public Transaction voidTransaction(TransactionDTO request, Integer transactionId) { 
    Optional<Transaction> transacOptional = transactionRepo.findByTransactionId(transactionId);
    if (transacOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    Transaction transaction = transacOptional.get();
    List<TransactionItem> transactionItems = transaction.getTransactionItems();
    for (TransactionItem transactionItem: transactionItems) {
      Integer prodId = transactionItem.getProductItemId();
      Optional<ProductItem> productItemOptional = productItemRepo.findByProductItemId(prodId);
      if (productItemOptional.isEmpty()) {
        continue;
      }

      ProductItem productItem = productItemOptional.get();
      Integer currentStock = productItem.getStock();
      Integer newStock = currentStock + transactionItem.getQuantity();
      productItem.setStock(newStock);
      productItemRepo.save(productItem);
    }

    
    transaction.setStatus(2);
    transaction.setAuthorizedBy(request.getAuthorizedBy());


    return transactionRepo.save(transaction);
  }

}
